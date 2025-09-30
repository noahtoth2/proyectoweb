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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.demo.dto.ModeloDTO;
import com.proyecto.demo.services.ModeloService;

@RestController
@RequestMapping("/modelo")
public class ModeloController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModeloService modeloService;


    // Listar modelos
    @GetMapping("/list")
    public ResponseEntity<List<ModeloDTO>> listarModelos() {
        List<ModeloDTO> modelos = modeloService.listarModelos();
        return ResponseEntity.status(HttpStatus.OK).body(modelos);
    }

    // Ver un modelo
    @GetMapping("{idModelo}")
    public ResponseEntity<ModeloDTO> recuperarModelo(@PathVariable Long idModelo) {
        ModeloDTO modelo = modeloService.recuperarModelo(idModelo);
        return ResponseEntity.status(HttpStatus.OK).body(modelo);
    }


    // Crear modelo
    @PostMapping
    public ResponseEntity<ModeloDTO> create(@RequestBody ModeloDTO modeloDTO) {
        ModeloDTO created = modeloService.crear(modeloDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar modelo
    @PutMapping
    public ResponseEntity<MdeoloDTO> update(@RequestBody ModeloDTO modeloDTO) {
        ModeloDTO updated = modeloService.actualizarModelo(modeloDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // Eliminar modelo
    @DeleteMapping("{idModelo}")
    public ResponseEntity<Void> delete(@PathVariable Long idModelo) {
        modeloService.borrarModelo(idModelo);
        return ResponseEntity.noContent().build();
    }


}