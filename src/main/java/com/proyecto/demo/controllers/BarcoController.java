package com.proyecto.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.BarcoService;

@RestController
@RequestMapping("/barco")
public class BarcoController {

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
    public List<BarcoDTO> listarBarcos(@PathVariable Integer page) {
        log.info("Recibi peticion de listar barcos");
        return barcoService.listarBarcos(PageRequest.of(page, 20));
    }

    // Ver un barco
    @GetMapping("{idBarco}")
    public BarcoDTO recuperarBarco(@PathVariable Long idBarco) {
        log.info("Recibi peticion de buscar un barco");
        return barcoService.recuperarBarco(idBarco);
    }

    
    // Crear barco y redireccionar
    @PostMapping
    public BarcoDTO create(@RequestBody BarcoDTO barcoDTO) {
       
        return barcoService.crear(barcoDTO);;
    }

   

    // Actualizar barco y redireccionar
    @PutMapping
    public BarcoDTO update(@RequestBody BarcoDTO barcoDTO) {
        
        return barcoService.actualizarBarco(barcoDTO);
    }

    // Eliminar barco y redireccionar
    @DeleteMapping("{idBarco}")
    public void delete(@PathVariable Long idBarco) {
        
        return barcoService.borrarBarco(idBarco);
    }