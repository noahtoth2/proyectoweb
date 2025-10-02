package com.proyecto.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.services.BarcoService;
import com.proyecto.demo.services.JugadorService;

@RestController
@RequestMapping("/jugador")
public class JugadorController {

    private static final Logger log = LoggerFactory.getLogger(JugadorController.class);

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private BarcoService barcoService;

    // Listar jugadores
    @GetMapping("/list")
    public ResponseEntity<List<JugadorDTO>> listarJugadores() {
        List<JugadorDTO> jugadores = jugadorService.listarJugadores();
        return ResponseEntity.status(HttpStatus.OK).body(jugadores);
    }

    // Ver un jugador
    @GetMapping("{idJugador}")
    public ResponseEntity<JugadorDTO> recuperarJugador(@PathVariable Long idJugador) {
        JugadorDTO jugador = jugadorService.recuperarJugador(idJugador);
        return ResponseEntity.status(HttpStatus.OK).body(jugador);
    }

    // Crear jugador
    @PostMapping
    public ResponseEntity<JugadorDTO> create(@RequestBody JugadorDTO jugadorDTO) {
        JugadorDTO created = jugadorService.crear(jugadorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar jugador
    @PutMapping
    public ResponseEntity<JugadorDTO> update(@RequestBody JugadorDTO jugadorDTO) {
        JugadorDTO updated = jugadorService.actualizarJugador(jugadorDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // Eliminar jugador
    @DeleteMapping("{idJugador}")
    public ResponseEntity<Void> delete(@PathVariable Long idJugador) {
        jugadorService.borrarJugador(idJugador);
        return ResponseEntity.noContent().build();
    }
}