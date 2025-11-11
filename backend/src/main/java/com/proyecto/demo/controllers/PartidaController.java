package com.proyecto.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.demo.dto.CrearPartidaRequest;
import com.proyecto.demo.dto.PartidaDTO;
import com.proyecto.demo.dto.SeleccionarBarcoRequest;
import com.proyecto.demo.dto.UnirsePartidaRequest;
import com.proyecto.demo.services.PartidaService;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "*")
public class PartidaController {
    
    private final PartidaService partidaService;
    
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }
    
    @PostMapping
    public ResponseEntity<PartidaDTO> crearPartida(@RequestBody CrearPartidaRequest request) {
        try {
            PartidaDTO partida = partidaService.crearPartida(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(partida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/unirse")
    public ResponseEntity<PartidaDTO> unirsePartida(@RequestBody UnirsePartidaRequest request) {
        try {
            PartidaDTO partida = partidaService.unirsePartida(request);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<PartidaDTO> iniciarPartida(@PathVariable Long id) {
        try {
            PartidaDTO partida = partidaService.iniciarPartida(id);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/{id}/seleccionar-barco")
    public ResponseEntity<PartidaDTO> seleccionarBarco(@PathVariable Long id, @RequestBody SeleccionarBarcoRequest request) {
        try {
            PartidaDTO partida = partidaService.seleccionarBarco(id, request);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/{id}/siguiente-turno")
    public ResponseEntity<PartidaDTO> siguienteTurno(@PathVariable Long id) {
        try {
            PartidaDTO partida = partidaService.siguienteTurno(id);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<PartidaDTO> finalizarPartida(@PathVariable Long id, @RequestParam Long ganadorId) {
        try {
            PartidaDTO partida = partidaService.finalizarPartida(id, ganadorId);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> obtenerPartida(@PathVariable Long id) {
        try {
            PartidaDTO partida = partidaService.obtenerPartida(id);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PartidaDTO> obtenerPartidaPorCodigo(@PathVariable String codigo) {
        try {
            PartidaDTO partida = partidaService.obtenerPartidaPorCodigo(codigo);
            return ResponseEntity.ok(partida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<PartidaDTO>> listarPartidasActivas() {
        List<PartidaDTO> partidas = partidaService.listarPartidasActivas();
        return ResponseEntity.ok(partidas);
    }
}
