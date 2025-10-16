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

import com.proyecto.demo.dto.ModeloDTO;
import com.proyecto.demo.services.ModeloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/modelo")
@Tag(name = "Controlador de Modelos", description = "Gestiona todas las operaciones CRUD para los modelos de barcos, incluyendo tipos, colores y características de cada modelo")
public class ModeloController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModeloService modeloService;

    @Operation(summary = "Listar todos los modelos", description = "Obtiene una lista completa de todos los modelos de barcos disponibles en el sistema")
    @GetMapping("/list")
    public ResponseEntity<List<ModeloDTO>> listarModelos() {
        List<ModeloDTO> modelos = modeloService.listarModelos();
        return ResponseEntity.status(HttpStatus.OK).body(modelos);
    }

    @Operation(summary = "Obtener modelo por ID", description = "Recupera la información completa de un modelo específico mediante su identificador único")
    @GetMapping("{idModelo}")
    public ResponseEntity<ModeloDTO> recuperarModelo(
            @Parameter(description = "Identificador único del modelo a buscar", required = true, example = "1") 
            @PathVariable Long idModelo) {
        ModeloDTO modelo = modeloService.recuperarJugador(idModelo);
        return ResponseEntity.status(HttpStatus.OK).body(modelo);
    }

    @Operation(summary = "Crear nuevo modelo", description = "Registra un nuevo modelo de barco en el sistema con nombre y color")
    @PostMapping
    public ResponseEntity<ModeloDTO> create(
            @Parameter(description = "Objeto ModeloDTO con los datos del nuevo modelo (nombre y color requeridos)", required = true) 
            @RequestBody ModeloDTO modeloDTO) {
        modeloService.crear(modeloDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(modeloDTO);
    }

    @Operation(summary = "Actualizar modelo existente", description = "Modifica los datos de un modelo ya registrado en el sistema")
    @PutMapping
    public ResponseEntity<ModeloDTO> update(
            @Parameter(description = "Objeto ModeloDTO con los datos actualizados del modelo (debe incluir el ID)", required = true) 
            @RequestBody ModeloDTO modeloDTO) {
        modeloService.actualizarModelo(modeloDTO);
        return ResponseEntity.status(HttpStatus.OK).body(modeloDTO);
    }

    @Operation(summary = "Eliminar modelo", description = "Elimina permanentemente un modelo del sistema mediante su ID")
    @DeleteMapping("{idModelo}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único del modelo a eliminar", required = true, example = "1") 
            @PathVariable Long idModelo) {
        modeloService.borrarModelo(idModelo);
        return ResponseEntity.noContent().build();
    }
}