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
    public ModeloDTO recuperarModelo(@PathVariable Long idModelo) {
        return modeloService.recuperarJugador(idModelo);
    }
    
    // Crear modelo y redireccionar
    @PostMapping
    public ModeloDTO create(@RequestBody ModeloDTO modeloDTO) {
        return modeloService.crear(modeloDTO);
    }

    // Actualizar modelo y redireccionar
    @PutMapping
    public ModeloDTO update(@RequestBody ModeloDTO modeloDTO) {
        return modeloService.actualizarModelo(modeloDTO);
    }

    // Eliminar modelo y redireccionar
    @DeleteMapping("{idModelo}")
    public void delete(@PathVariable Long idModelo) {
            modeloService.borrarModelo(idModelo);
        }

}