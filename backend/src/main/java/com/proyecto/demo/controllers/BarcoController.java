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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/barco")
@Tag(name = "Barcos", description = "Operaciones relacionadas con los barcos")
public class BarcoController {

    private static final Logger log = LoggerFactory.getLogger(BarcoController.class);

    @Autowired
    private BarcoService barcoService;

    @Operation(summary = "Listar todos los barcos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de barcos obtenida correctamente")
    })
    @GetMapping("/list")
    public ResponseEntity<List<BarcoDTO>> listarBarcos() {
        log.info("Recibi peticion de listar barcos");
        List<BarcoDTO> barcos = barcoService.listarBarcos();
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    @Operation(summary = "Listar barcos paginados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de barcos obtenida correctamente")
    })
    @GetMapping("/list/{page}")
    public ResponseEntity<List<BarcoDTO>> listarBarcos(
            @Parameter(description = "Número de página a consultar") @PathVariable Integer page) {
        log.info("Recibi peticion de listar barcos paginados");
        List<BarcoDTO> barcos = barcoService.listarBarcos(PageRequest.of(page, 20));
        return ResponseEntity.status(HttpStatus.OK).body(barcos);
    }

    @Operation(summary = "Recuperar un barco por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco encontrado"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    @GetMapping("{idBarco}")
    public ResponseEntity<BarcoDTO> recuperarBarco(
            @Parameter(description = "ID del barco a recuperar") @PathVariable Long idBarco) {
        log.info("Recibi peticion de buscar un barco");
        BarcoDTO barco = barcoService.recuperarBarco(idBarco);
        return ResponseEntity.status(HttpStatus.OK).body(barco);
    }

    @Operation(summary = "Crear un nuevo barco")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Barco creado correctamente")
    })
    @PostMapping
    public ResponseEntity<BarcoDTO> create(
            @Parameter(description = "Datos del nuevo barco") @RequestBody BarcoDTO barcoDTO) {
        BarcoDTO created = barcoService.crear(barcoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar un barco existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    @PutMapping
    public ResponseEntity<BarcoDTO> update(
            @Parameter(description = "Datos del barco actualizado") @RequestBody BarcoDTO barcoDTO) {
        BarcoDTO updated = barcoService.actualizarBarco(barcoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @Operation(summary = "Eliminar un barco por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Barco eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    @DeleteMapping("{idBarco}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del barco a eliminar") @PathVariable Long idBarco) {
        barcoService.borrarBarco(idBarco);
        return ResponseEntity.noContent().build();
    }
}
