package com.proyecto.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/jugador")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Controlador de Jugadores", description = "Gestiona todas las operaciones CRUD para los jugadores del juego, incluyendo registro, consulta, actualización y eliminación de jugadores")
public class JugadorController {

    private static final Logger log = LoggerFactory.getLogger(JugadorController.class);

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private BarcoService barcoService;

    @Operation(summary = "Listar todos los jugadores", description = "Obtiene una lista completa de todos los jugadores registrados en el sistema")
    @GetMapping("/list")
    public ResponseEntity<List<JugadorDTO>> listarJugadores() {
        List<JugadorDTO> jugadores = jugadorService.listarJugadores();
        return ResponseEntity.status(HttpStatus.OK).body(jugadores);
    }

    @Operation(summary = "Obtener jugador por ID", description = "Recupera la información completa de un jugador específico mediante su identificador único")
    @GetMapping("{idJugador}")
    public ResponseEntity<JugadorDTO> recuperarJugador(
            @Parameter(description = "Identificador único del jugador a buscar", required = true, example = "1") 
            @PathVariable Long idJugador) {
        JugadorDTO jugador = jugadorService.recuperarJugador(idJugador);
        return ResponseEntity.status(HttpStatus.OK).body(jugador);
    }

    @Operation(summary = "Crear nuevo jugador", description = "Registra un nuevo jugador en el sistema con el nombre proporcionado")
    @PostMapping
    public ResponseEntity<JugadorDTO> create(
            @Parameter(description = "Objeto JugadorDTO con los datos del nuevo jugador (nombre requerido)", required = true) 
            @RequestBody JugadorDTO jugadorDTO) {
        JugadorDTO created = jugadorService.crear(jugadorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar jugador existente", description = "Modifica los datos de un jugador ya registrado en el sistema")
    @PutMapping
    public ResponseEntity<JugadorDTO> update(
            @Parameter(description = "Objeto JugadorDTO con los datos actualizados del jugador (debe incluir el ID)", required = true) 
            @RequestBody JugadorDTO jugadorDTO) {
        JugadorDTO updated = jugadorService.actualizarJugador(jugadorDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @Operation(summary = "Eliminar jugador", description = "Elimina permanentemente un jugador del sistema mediante su ID")
    @DeleteMapping("{idJugador}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único del jugador a eliminar", required = true, example = "1") 
            @PathVariable Long idJugador) {
        jugadorService.borrarJugador(idJugador);
        return ResponseEntity.noContent().build();
    }
}