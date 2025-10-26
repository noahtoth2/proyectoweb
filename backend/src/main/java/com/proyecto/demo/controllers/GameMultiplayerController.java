package com.proyecto.demo.controllers;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Controlador todo-en-uno para un prototipo multijugador usando REST + SSE.
 *
 * Diseño: Un único archivo contiene clases internas que modelan una Game ligera,
 * Players y Actions. El estado se guarda en memoria (ConcurrentHashMap). Este
 * enfoque mantiene todo en un solo archivo según lo solicitado y es adecuado
 * para un prototipo o pruebas. Para producción, separar responsabilidades y
 * usar persistencia externa y pub/sub.
 */
@RestController
@RequestMapping("/api/game")
public class GameMultiplayerController {

    private final Logger log = LoggerFactory.getLogger(GameMultiplayerController.class);

    private final AtomicLong gameIdSeq = new AtomicLong(1);
    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // DTOs / modelos ligeros dentro del mismo archivo
    public static class CreateGameRequest {
        public String name;
        public Integer maxPlayers = 2;
    }

    public static class CreateGameResponse {
        public Long gameId;
        public String status;
        public long createdAt;
    }

    public static class JoinRequest {
        public String playerName;
    }

    public static class JoinResponse {
        public String playerId;
        public Long gameId;
        public String status;
    }

    public static class ActionDTO {
        public String playerId;
        public String type; // e.g., MOVE
        public Map<String, Object> payload;
        public Long clientTick;
    }

    public static class GameStateDTO {
        public Long gameId;
        public String status;
        public long version;
        public List<PlayerView> players = new ArrayList<>();
        public List<Map<String, Object>> boats = new ArrayList<>();
    }

    public static class PlayerView {
        public String playerId;
        public String name;
        public boolean ready;
    }

    // Entidad de juego en memoria
    private static class Game {
        public final Long id;
        public final String name;
        public volatile String status = "WAITING";
        public final int maxPlayers;
        public final Map<String, Player> players = new ConcurrentHashMap<>();
        public final AtomicLong version = new AtomicLong(0);

        // ejemplo simple de estado: lista de barcos representados como maps
        public final List<Map<String, Object>> boats = new CopyOnWriteArrayList<>();

        public Game(Long id, String name, int maxPlayers) {
            this.id = id;
            this.name = name;
            this.maxPlayers = maxPlayers;
        }
    }

    private static class Player {
        public final String id;
        public final String name;
        public volatile boolean ready = false;

        public Player(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // Create a new game
    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest req) {
        long id = gameIdSeq.getAndIncrement();
        String name = Optional.ofNullable(req.name).orElse("game-" + id);
        int maxP = Optional.ofNullable(req.maxPlayers).orElse(2);
        Game g = new Game(id, name, maxP);
        // initial simple boats state — in a real game use your Tablero/Celda models
        g.boats.add(Map.of("id", 1, "x", 3, "y", 1, "vx", 0.0, "vy", 0.0));
        g.boats.add(Map.of("id", 2, "x", 17, "y", 1, "vx", 0.0, "vy", 0.0));
        games.put(id, g);
        emitters.put(id, new CopyOnWriteArrayList<>());

        CreateGameResponse r = new CreateGameResponse();
        r.gameId = id;
        r.status = g.status;
        r.createdAt = Instant.now().toEpochMilli();
        log.info("Created game {} (maxPlayers={})", id, maxP);
        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    // Join an existing game
    @PostMapping("/{gameId}/join")
    public ResponseEntity<JoinResponse> joinGame(@PathVariable Long gameId, @RequestBody JoinRequest req) {
        Game g = games.get(gameId);
        if (g == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (g.players.size() >= g.maxPlayers) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        String pid = UUID.randomUUID().toString();
        Player p = new Player(pid, Optional.ofNullable(req.playerName).orElse("player-" + pid.substring(0, 6)));
        g.players.put(pid, p);
        if (g.players.size() >= g.maxPlayers) {
            g.status = "READY";
        }
        JoinResponse jr = new JoinResponse();
        jr.playerId = pid;
        jr.gameId = gameId;
        jr.status = g.status;
        notifyGameUpdate(g);
        log.info("Player {} joined game {} (players={}/{})", p.name, gameId, g.players.size(), g.maxPlayers);
        return ResponseEntity.ok(jr);
    }

    // Simple action endpoint: clients post actions here
    @PostMapping("/{gameId}/action")
    public ResponseEntity<?> action(@PathVariable Long gameId, @RequestBody ActionDTO action) {
        Game g = games.get(gameId);
        if (g == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        if (!g.players.containsKey(action.playerId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Player not in game");

        // Very small example: support MOVE action that updates a boat's vx/vy
        try {
            if ("MOVE".equalsIgnoreCase(action.type) && action.payload != null) {
                Object idObj = action.payload.get("boatId");
                Object vxObj = action.payload.get("vx");
                Object vyObj = action.payload.get("vy");
                if (idObj != null) {
                    long bid = ((Number) idObj).longValue();
                    for (Map<String, Object> boat : g.boats) {
                        Number bIdNum = (Number) boat.get("id");
                        if (bIdNum != null && bIdNum.longValue() == bid) {
                            if (vxObj instanceof Number) {
                                boat.put("vx", ((Number) vxObj).doubleValue());
                            }
                            if (vyObj instanceof Number) {
                                boat.put("vy", ((Number) vyObj).doubleValue());
                            }
                        }
                    }
                }
            }

            // bump version
            long ver = g.version.incrementAndGet();
            log.debug("Applied action to game {}: type={} by {} -> version={}", gameId, action.type, action.playerId, ver);
            notifyGameUpdate(g);
            return ResponseEntity.ok(Map.of("status", "OK", "version", ver));
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload types");
        }
    }

    // Get full state snapshot
    @GetMapping("/{gameId}/state")
    public ResponseEntity<GameStateDTO> state(@PathVariable Long gameId) {
        Game g = games.get(gameId);
        if (g == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        GameStateDTO s = toDTO(g);
        return ResponseEntity.ok(s);
    }

    // SSE stream for a game: clients open EventSource to receive updates
    @GetMapping("/{gameId}/stream")
    public SseEmitter stream(@PathVariable Long gameId, @RequestParam(required = false) Long lastVersion) {
        Game g = games.get(gameId);
        if (g == null) return new SseEmitter(0L); // empty emitter; client should handle

        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(gameId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(gameId, emitter));
        emitter.onTimeout(() -> removeEmitter(gameId, emitter));

        // send initial state immediately
        try {
            emitter.send(SseEmitter.event().name("init").data(toDTO(g)));
        } catch (IOException e) {
            removeEmitter(gameId, emitter);
        }

        return emitter;
    }

    // Helper: convert Game to GameStateDTO
    private GameStateDTO toDTO(Game g) {
        GameStateDTO s = new GameStateDTO();
        s.gameId = g.id;
        s.status = g.status;
        s.version = g.version.get();
        for (Player p : g.players.values()) {
            PlayerView pv = new PlayerView();
            pv.playerId = p.id;
            pv.name = p.name;
            pv.ready = p.ready;
            s.players.add(pv);
        }
        s.boats.addAll(g.boats);
        return s;
    }

    private void notifyGameUpdate(Game g) {
        List<SseEmitter> list = emitters.get(g.id);
        if (list == null) return;
        GameStateDTO payload = toDTO(g);
        List<SseEmitter> toRemove = new ArrayList<>();
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name("update").data(payload));
            } catch (IOException e) {
                toRemove.add(emitter);
            }
        }
        for (SseEmitter e : toRemove) removeEmitter(g.id, e);
    }

    private void removeEmitter(Long gameId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(gameId);
        if (list != null) list.remove(emitter);
    }

}
