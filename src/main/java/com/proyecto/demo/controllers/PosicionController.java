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

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.services.PosicionService;

@Controller
@RequestMapping("/posicion")
public class PosicionController {

    @Autowired
    private PosicionService posicionService;

    // Listar posiciones
    @GetMapping("/list")
    public ResponseEntity<List<PosicionDTO>> listarPosiciones() {
        List<PosicionDTO> posiciones = posicionService.listarBarcos();
        return ResponseEntity.status(HttpStatus.OK).body(posiciones);
    }

    // Ver una posición
    @GetMapping("{idPosicion}")
    public PosicionDTO recuperarPosicion(@PathVariable Long idPosicion) {
        return posicionService.recuperarBarco(idPosicion);
    }
/*
    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("posicion-create");
        modelAndView.addObject("posicion", new PosicionDTO());
        return modelAndView;
    }
*/
    // Crear posición y redireccionar
    @PostMapping("/create")
    public PosicionDTO create(@RequestBody PosicionDTO posicionDTO) {
        return  posicionService.crear(posicionDTO);
    }
/*
    // Formulario para editar
    @GetMapping("/edit-form/{idPosicion}")
    public ModelAndView editForm(@PathVariable Long idPosicion) {
        ModelAndView modelAndView = new ModelAndView("posicion-edit");
        PosicionDTO posicion = posicionService.recuperarBarco(idPosicion);
        modelAndView.addObject("posicion", posicion);
        return modelAndView;
    }
*/
    // Actualizar posición y redireccionar
    @PutMapping("/update")
    public PosicionDTO update(@ModelAttribute PosicionDTO posicionDTO) {
        return posicionService.actualizarPosicion(posicionDTO);
    }

    // Eliminar posición y redireccionar
    @DeleteMapping("{idPosicion}")
    public void delete(@PathVariable Long idPosicion) {
        posicionService.borrarBarco(idPosicion);
    }
}