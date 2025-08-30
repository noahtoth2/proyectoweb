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

@Controller
@RequestMapping("/barco")
public class BarcoController {

    @Autowired
    private BarcoService barcoService;

    // Listar barcos
    @GetMapping("/list")
    public ModelAndView listarBarcos() {
        ModelAndView modelAndView = new ModelAndView("barco-list");
        List<BarcoDTO> barcos = barcoService.listarBarcos();
        modelAndView.addObject("listadoBarcos", barcos);
        return modelAndView;
    }

    // Ver un barco
    @GetMapping("/view/{idBarco}")
    public ModelAndView recuperarBarco(@PathVariable Long idBarco) {
        ModelAndView modelAndView = new ModelAndView("barco-view");
        BarcoDTO barco = barcoService.recuperarBarco(idBarco);
        modelAndView.addObject("barco", barco);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("barco-create");
        modelAndView.addObject("barco", new BarcoDTO());
        return modelAndView;
    }

    // Crear barco y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute BarcoDTO barcoDTO) {
        barcoService.crear(barcoDTO);
        return new RedirectView("/barco/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idBarco}")
    public ModelAndView editForm(@PathVariable Long idBarco) {
        ModelAndView modelAndView = new ModelAndView("barco-edit");
        BarcoDTO barco = barcoService.recuperarBarco(idBarco);
        modelAndView.addObject("barco", barco);
        return modelAndView;
    }

    // Actualizar barco y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute BarcoDTO barcoDTO) {
        barcoService.actualizarBarco(barcoDTO);
        return new RedirectView("/barco/list");
    }

    // Eliminar barco y redireccionar
    @GetMapping("/delete/{idBarco}")
    public RedirectView delete(@PathVariable Long idBarco) {
        barcoService.borrarBarco(idBarco);
        return new RedirectView("/barco/list");}
    }