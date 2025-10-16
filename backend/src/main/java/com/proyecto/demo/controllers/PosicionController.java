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

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.services.PosicionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/posicion")
@Tag(name = "Controlador de Posiciones", description = "Gestiona todas las operaciones CRUD para las posiciones de los barcos en el tablero, incluyendo coordenadas X e Y")
public class PosicionController {

    @Autowired
    private PosicionService posicionService;

    @Operation(summary = "Listar todas las posiciones", description = "Obtiene una lista completa de todas las posiciones registradas en el sistema")
    @GetMapping("/list")
    public ResponseEntity<List<PosicionDTO>> listarPosiciones() {
        List<PosicionDTO> posiciones = posicionService.listarPosiciones();
        return ResponseEntity.status(HttpStatus.OK).body(posiciones);
    }

    @Operation(summary = "Listar posiciones con paginación", description = "Obtiene una lista paginada de posiciones, mostrando 20 posiciones por página")
    @GetMapping("/list/{page}")
    public ResponseEntity<List<PosicionDTO>> listarPosiciones(
            @Parameter(description = "Número de página a consultar (empezando desde 0)", required = true, example = "0") 
            @PathVariable Integer page) {
        List<PosicionDTO> posiciones = posicionService.listarPosiciones(PageRequest.of(page, 20));
        return ResponseEntity.status(HttpStatus.OK).body(posiciones);
    }

    @Operation(summary = "Obtener posición por ID", description = "Recupera la información completa de una posición específica mediante su identificador único")
    @GetMapping("{idPosicion}")
    public ResponseEntity<PosicionDTO> recuperarPosicion(
            @Parameter(description = "Identificador único de la posición a buscar", required = true, example = "1") 
            @PathVariable Long idPosicion) {
        PosicionDTO posicion = posicionService.recuperarPosicion(idPosicion);
        return ResponseEntity.status(HttpStatus.OK).body(posicion);
    }

    @Operation(summary = "Crear nueva posición", description = "Registra una nueva posición en el sistema con coordenadas X e Y")
    @PostMapping
    public ResponseEntity<PosicionDTO> create(
            @Parameter(description = "Objeto PosicionDTO con los datos de la nueva posición (coordenadas X e Y)", required = true) 
            @RequestBody PosicionDTO posicionDTO) {
        PosicionDTO created = posicionService.crear(posicionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar posición existente", description = "Modifica las coordenadas de una posición ya registrada en el sistema")
    @PutMapping
    public ResponseEntity<PosicionDTO> update(
            @Parameter(description = "Objeto PosicionDTO con los datos actualizados de la posición (debe incluir el ID)", required = true) 
            @RequestBody PosicionDTO posicionDTO) {
        PosicionDTO updated = posicionService.actualizarPosicion(posicionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @Operation(summary = "Eliminar posición", description = "Elimina permanentemente una posición del sistema mediante su ID")
    @DeleteMapping("{idPosicion}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único de la posición a eliminar", required = true, example = "1") 
            @PathVariable Long idPosicion) {
        posicionService.borrarPosicion(idPosicion);
        return ResponseEntity.noContent().build();
    }
}