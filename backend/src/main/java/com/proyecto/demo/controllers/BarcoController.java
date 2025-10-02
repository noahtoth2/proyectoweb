package com.proyecto.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.BarcoService;

@RestController
@RequestMapping("/barco")
public class BarcoController {

    private static final Logger log = LoggerFactory.getLogger(BarcoController.class);

    @Autowired
    private BarcoService barcoService;

    // Listar barcos
    @GetMapping("/list")
    public ResponseEntity<List<BarcoDTO>> listarBarcos() {
        log.info("Recibi peticion de listar barcos");
        List<BarcoDTO> barcos = barcoService.listarBarcos();
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    @GetMapping("/list/{page}")
    public ResponseEntity<List<BarcoDTO>> listarBarcos(@PathVariable Integer page) {
        log.info("Recibi peticion de listar barcos");
        List<BarcoDTO> barcos = barcoService.listarBarcos(PageRequest.of(page, 20));
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    // Ver un barco
    @GetMapping("{idBarco}")
    public ResponseEntity<BarcoDTO> recuperarBarco(@PathVariable Long idBarco) {
        log.info("Recibi peticion de buscar un barco");
        BarcoDTO barco = barcoService.recuperarBarco(idBarco);
        return ResponseEntity.status(HttpStatus.OK).body(barco);
    }

    // Crear barco y redireccionar
    @PostMapping
    public ResponseEntity<BarcoDTO> create(@RequestBody BarcoDTO barcoDTO) {
        BarcoDTO created = barcoService.crear(barcoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar barco y redireccionar
    @PutMapping
    public ResponseEntity<BarcoDTO> update(@RequestBody BarcoDTO barcoDTO) {
        BarcoDTO updated = barcoService.actualizarBarco(barcoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // Eliminar barco y redireccionar
    @DeleteMapping("{idBarco}")
    public ResponseEntity<Void> delete(@PathVariable Long idBarco) {
        barcoService.borrarBarco(idBarco);
        return ResponseEntity.noContent().build();
    }
}