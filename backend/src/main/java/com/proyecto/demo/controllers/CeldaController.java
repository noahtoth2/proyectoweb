package com.proyecto.demo.controllers;

import java.util.List;

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

import com.proyecto.demo.dto.CeldaDTO;
import com.proyecto.demo.services.CeldaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/celda")
@Tag(name = "Controlador de Celdas", description = "Gestiona todas las operaciones CRUD para las celdas del tablero")
public class CeldaController {

    @Autowired
    private CeldaService celdaService;

    @Operation(summary = "Listar todas las celdas", description = "Obtiene una lista completa de todas las celdas del tablero")
    @GetMapping("/list")
    public ResponseEntity<List<CeldaDTO>> listarCeldas() {
        List<CeldaDTO> celdas = celdaService.listarCeldas();
        return ResponseEntity.ok(celdas);
    }

    @Operation(summary = "Obtener celda por ID", description = "Recupera la información completa de una celda específica mediante su ID")
    @GetMapping("/{idCelda}")
    public ResponseEntity<CeldaDTO> recuperarCelda(
            @Parameter(description = "Identificador único de la celda", required = true, example = "1") 
            @PathVariable Long idCelda) {
        CeldaDTO celda = celdaService.recuperarCelda(idCelda);
        return ResponseEntity.ok(celda);
    }

    @Operation(summary = "Crear nueva celda", description = "Crea una nueva celda en el tablero")
    @PostMapping
    public ResponseEntity<CeldaDTO> create(
            @Parameter(description = "Datos de la nueva celda", required = true) 
            @RequestBody CeldaDTO celdaDTO) {
        celdaService.crear(celdaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(celdaDTO);
    }

    @Operation(summary = "Actualizar celda existente", description = "Modifica los datos de una celda existente")
    @PutMapping
    public ResponseEntity<CeldaDTO> update(
            @Parameter(description = "Datos actualizados de la celda", required = true) 
            @RequestBody CeldaDTO celdaDTO) {
        celdaService.actualizarCelda(celdaDTO);
        return ResponseEntity.ok(celdaDTO);
    }

    @Operation(summary = "Eliminar celda", description = "Elimina una celda del sistema")
    @DeleteMapping("/{idCelda}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único de la celda a eliminar", required = true, example = "1") 
            @PathVariable Long idCelda) {
        celdaService.borrarCelda(idCelda);
        return ResponseEntity.noContent().build();
    }
}
