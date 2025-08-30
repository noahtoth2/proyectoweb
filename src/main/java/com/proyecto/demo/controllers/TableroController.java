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

import com.proyecto.demo.dto.TableroDTO;
import com.proyecto.demo.services.TableroService;

@Controller
@RequestMapping("/tablero")
public class TableroController {

    @Autowired
    private TableroService tableroService;

    // Listar tableros
    @GetMapping("/list")
    public ModelAndView listarTableros() {
        ModelAndView modelAndView = new ModelAndView("tablero-list");
        List<TableroDTO> tableros = tableroService.listarTableros();
        modelAndView.addObject("listadoTableros", tableros);
        return modelAndView;
    }

    // Ver un tablero
    @GetMapping("/view/{idTablero}")
    public ModelAndView recuperarTablero(@PathVariable Long idTablero) {
        ModelAndView modelAndView = new ModelAndView("tablero-view");
        TableroDTO tablero = tableroService.recuperarTablero(idTablero);
        modelAndView.addObject("tablero", tablero);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("tablero-create");
        modelAndView.addObject("tablero", new TableroDTO());
        return modelAndView;
    }

    // Crear tablero y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute TableroDTO tableroDTO) {
        tableroService.crear(tableroDTO);
        return new RedirectView("/tablero/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idTablero}")
    public ModelAndView editForm(@PathVariable Long idTablero) {
        ModelAndView modelAndView = new ModelAndView("tablero-edit");
        TableroDTO tablero = tableroService.recuperarTablero(idTablero);
        modelAndView.addObject("tablero", tablero);
        return modelAndView;
    }

    // Actualizar tablero y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute TableroDTO tableroDTO) {
        tableroService.actualizarTablero(tableroDTO);
        return new RedirectView("/tablero/list");
    }

    // Eliminar tablero y redireccionar
    @GetMapping("/delete/{idTablero}")
    public RedirectView delete(@PathVariable Long idTablero) {
        tableroService.borrarTablero(idTablero);
        return new RedirectView("/tablero/list");}
    }