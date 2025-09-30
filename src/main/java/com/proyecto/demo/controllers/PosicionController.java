package com.proyecto.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.services.PosicionService;

@RestController
@RequestMapping("/posicion")
public class PosicionController {

    private static final Logger log = LoggerFactory.getLogger(PosicionController.class);

    @Autowired
    private PosicionService posicionService;

    // Listar posiciones
    @GetMapping("/list")
    public ResponseEntity<List<PosicionDTO>> listarPosiciones() {
        log.info("Recibi peticion de listar posiciones");
        List<PosicionDTO> posiciones = posicionService.listarPosiciones();
        return ResponseEntity.status(HttpStatus.OK).body(posiciones);
    }

    @GetMapping("/list/{page}")
    public ResponseEntity<List<PosicionDTO>> listarPosiciones(@PathVariable Integer page) {
        log.info("Recibi peticion de listar posiciones");
        List<PosicionDTO> posiciones = posicionService.listarPosiciones(PageRequest.of(page, 20));
        return ResponseEntity.status(HttpStatus.OK).body(posiciones);
    }

    // Ver una posicion
    @GetMapping("{idPosicion}")
    public ResponseEntity<PosicionDTO> recuperarPosicion(@PathVariable Long idPosicion) {
        log.info("Recibi peticion de buscar una posicion");
        PosicionDTO posicion = posicionService.recuperarPosicion(idPosicion);
        return ResponseEntity.status(HttpStatus.OK).body(posicion);
    }

    // Crear posicion
    @PostMapping
    public ResponseEntity<PosicionDTO> create(@RequestBody PosicionDTO posicionDTO) {
        PosicionDTO created = posicionService.crear(posicionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar posicion
    @PutMapping
    public ResponseEntity<PosicionDTO> update(@RequestBody PosicionDTO posicionDTO) {
        PosicionDTO updated = posicionService.actualizarPosicion(posicionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // Eliminar posicion
    @DeleteMapping("{idPosicion}")
    public ResponseEntity<Void> delete(@PathVariable Long idPosicion) {
        posicionService.borrarPosicion(idPosicion);
        return ResponseEntity.noContent().build();
    }
}