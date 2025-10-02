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

import com.proyecto.demo.dto.CeldaDTO;
import com.proyecto.demo.services.CeldaService;

@Controller
@RequestMapping("/celda")
public class CeldaController {

    @Autowired
    private CeldaService celdaService;

    // Listar celdas
    @GetMapping("/list")
    public ModelAndView listarCeldas() {
        ModelAndView modelAndView = new ModelAndView("celda-list");
        List<CeldaDTO> celdas = celdaService.listarCeldas();
        modelAndView.addObject("listadoCeldas", celdas);
        return modelAndView;
    }

    // Ver una celda
    @GetMapping("/view/{idCelda}")
    public ModelAndView recuperarCelda(@PathVariable Long idCelda) {
        ModelAndView modelAndView = new ModelAndView("celda-view");
        CeldaDTO celda = celdaService.recuperarCelda(idCelda);
        modelAndView.addObject("celda", celda);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("celda-create");
        modelAndView.addObject("celda", new CeldaDTO());
        return modelAndView;
    }

    // Crear celda y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute CeldaDTO celdaDTO) {
        celdaService.crear(celdaDTO);
        return new RedirectView("/celda/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idCelda}")
    public ModelAndView editForm(@PathVariable Long idCelda) {
        ModelAndView modelAndView = new ModelAndView("celda-edit");
        CeldaDTO celda = celdaService.recuperarCelda(idCelda);
        modelAndView.addObject("celda", celda);
        return modelAndView;
    }

    // Actualizar celda y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute CeldaDTO celdaDTO) {
        celdaService.actualizarCelda(celdaDTO);
        return new RedirectView("/celda/list");
    }

    // Eliminar celda y redireccionar
    @GetMapping("/delete/{idCelda}")
    public RedirectView delete(@PathVariable Long idCelda) {
        celdaService.borrarBarco(idCelda);
        return new RedirectView("/celda/list");}
    }
