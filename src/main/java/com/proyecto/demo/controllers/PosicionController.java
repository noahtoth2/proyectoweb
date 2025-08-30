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
    public ModelAndView listarPosiciones() {
        ModelAndView modelAndView = new ModelAndView("posicion-list");
        List<PosicionDTO> posiciones = posicionService.listarBarcos();
        modelAndView.addObject("listadoPosiciones", posiciones);
        return modelAndView;
    }

    // Ver una posición
    @GetMapping("/view/{idPosicion}")
    public ModelAndView recuperarPosicion(@PathVariable Long idPosicion) {
        ModelAndView modelAndView = new ModelAndView("posicion-view");
        PosicionDTO posicion = posicionService.recuperarBarco(idPosicion);
        modelAndView.addObject("posicion", posicion);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("posicion-create");
        modelAndView.addObject("posicion", new PosicionDTO());
        return modelAndView;
    }

    // Crear posición y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute PosicionDTO posicionDTO) {
        posicionService.crear(posicionDTO);
        return new RedirectView("/posicion/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idPosicion}")
    public ModelAndView editForm(@PathVariable Long idPosicion) {
        ModelAndView modelAndView = new ModelAndView("posicion-edit");
        PosicionDTO posicion = posicionService.recuperarBarco(idPosicion);
        modelAndView.addObject("posicion", posicion);
        return modelAndView;
    }

    // Actualizar posición y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute PosicionDTO posicionDTO) {
        posicionService.actualizarBarco(posicionDTO);
        return new RedirectView("/posicion/list");
    }

    // Eliminar posición y redireccionar
    @GetMapping("/delete/{idPosicion}")
    public RedirectView delete(@PathVariable Long idPosicion) {
        posicionService.borrarBarco(idPosicion);
        return new RedirectView("/posicion/list");
    }
}