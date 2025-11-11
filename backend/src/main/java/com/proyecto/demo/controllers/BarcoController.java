package com.proyecto.demo.controllers;

import java.util.List;

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

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.BarcoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/barco")
@Tag(name = "Controlador de Barcos", description = "Gestiona todas las operaciones CRUD para los barcos del juego, incluyendo listar, crear, actualizar, eliminar y buscar barcos por ID")
public class BarcoController {

    @Autowired
    private BarcoService barcoService;

    @Operation(summary = "Listar todos los barcos", description = "Obtiene una lista completa de todos los barcos registrados en el sistema")
    @GetMapping("/list")
    public ResponseEntity<List<BarcoDTO>> listarBarcos() {
        List<BarcoDTO> barcos = barcoService.listarBarcos();
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    @Operation(summary = "Listar barcos con paginación", description = "Obtiene una lista paginada de barcos, mostrando 20 barcos por página")
    @GetMapping("/list/{page}")
    public ResponseEntity<List<BarcoDTO>> listarBarcos(
            @Parameter(description = "Número de página a consultar (empezando desde 0)", required = true, example = "0") 
            @PathVariable Integer page) {
        List<BarcoDTO> barcos = barcoService.listarBarcos(PageRequest.of(page, 20));
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    @Operation(summary = "Obtener barco por ID", description = "Recupera la información completa de un barco específico mediante su identificador único")
    @GetMapping("{idBarco}")
    public ResponseEntity<BarcoDTO> recuperarBarco(
            @Parameter(description = "Identificador único del barco a buscar", required = true, example = "1") 
            @PathVariable Long idBarco) {
        BarcoDTO barco = barcoService.recuperarBarco(idBarco);
        return ResponseEntity.status(HttpStatus.OK).body(barco);
    }

    @Operation(summary = "Crear nuevo barco", description = "Registra un nuevo barco en el sistema con los datos proporcionados")
    @PostMapping
    public ResponseEntity<BarcoDTO> create(
            @Parameter(description = "Objeto BarcoDTO con los datos del nuevo barco (jugadorId, modeloId, velocidadX, velocidadY, posición)", required = true) 
            @RequestBody BarcoDTO barcoDTO) {

        BarcoDTO created = barcoService.crear(barcoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar barco existente", description = "Modifica los datos de un barco ya registrado en el sistema")
    @PutMapping
    public ResponseEntity<BarcoDTO> update(
            @Parameter(description = "Objeto BarcoDTO con los datos actualizados del barco (debe incluir el ID)", required = true) 
            @RequestBody BarcoDTO barcoDTO) {

        BarcoDTO updated = barcoService.actualizarBarco(barcoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @Operation(summary = "Eliminar barco", description = "Elimina permanentemente un barco del sistema mediante su ID")
    @DeleteMapping("{idBarco}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único del barco a eliminar", required = true, example = "1") 
            @PathVariable Long idBarco) {
        barcoService.borrarBarco(idBarco);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todos los barcos disponibles", description = "Devuelve todos los barcos del sistema para selección en partidas")
    @GetMapping("/disponibles")
    public ResponseEntity<List<BarcoDTO>> obtenerBarcosDisponibles() {
        List<BarcoDTO> barcos = barcoService.listarBarcos();
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }
}
