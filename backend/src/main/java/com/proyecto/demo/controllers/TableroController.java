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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/tablero")
@Tag(name = "Controlador de Tableros Web", description = "Gestiona las vistas web para las operaciones CRUD de tableros de juego, proporcionando formularios y páginas HTML para la gestión visual de tableros")
public class TableroController {

    @Autowired
    private TableroService tableroService;

    @Operation(summary = "Página de lista de tableros", description = "Muestra una página web con la lista completa de todos los tableros de juego")
    @GetMapping("/list")
    public ModelAndView listarTableros() {
        ModelAndView modelAndView = new ModelAndView("tablero-list");
        List<TableroDTO> tableros = tableroService.listarTableros();
        modelAndView.addObject("listadoTableros", tableros);
        return modelAndView;
    }

    @Operation(summary = "Página de detalle de tablero", description = "Muestra una página web con los detalles completos de un tablero específico")
    @GetMapping("/view/{idTablero}")
    public ModelAndView recuperarTablero(
            @Parameter(description = "Identificador único del tablero a visualizar", required = true, example = "1") 
            @PathVariable Long idTablero) {
        ModelAndView modelAndView = new ModelAndView("tablero-view");
        TableroDTO tablero = tableroService.recuperarTablero(idTablero);
        modelAndView.addObject("tablero", tablero);
        return modelAndView;
    }

    @Operation(summary = "Formulario de creación", description = "Muestra el formulario web para crear un nuevo tablero de juego")
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("tablero-create");
        modelAndView.addObject("tablero", new TableroDTO());
        return modelAndView;
    }

    @Operation(summary = "Procesar creación de tablero", description = "Procesa el formulario de creación y redirige a la lista de tableros")
    @PostMapping("/create")
    public RedirectView create(
            @Parameter(description = "Datos del nuevo tablero enviados desde el formulario", required = true) 
            @ModelAttribute TableroDTO tableroDTO) {
        tableroService.crear(tableroDTO);
        return new RedirectView("/tablero/list");
    }

    @Operation(summary = "Formulario de edición", description = "Muestra el formulario web para editar un tablero existente")
    @GetMapping("/edit-form/{idTablero}")
    public ModelAndView editForm(
            @Parameter(description = "Identificador único del tablero a editar", required = true, example = "1") 
            @PathVariable Long idTablero) {
        ModelAndView modelAndView = new ModelAndView("tablero-edit");
        TableroDTO tablero = tableroService.recuperarTablero(idTablero);
        modelAndView.addObject("tablero", tablero);
        return modelAndView;
    }

    @Operation(summary = "Procesar actualización de tablero", description = "Procesa el formulario de edición y redirige a la lista de tableros")
    @PostMapping("/update")
    public RedirectView update(
            @Parameter(description = "Datos actualizados del tablero enviados desde el formulario", required = true) 
            @ModelAttribute TableroDTO tableroDTO) {
        tableroService.actualizarTablero(tableroDTO);
        return new RedirectView("/tablero/list");
    }

    @Operation(summary = "Eliminar tablero", description = "Elimina un tablero del sistema y redirige a la lista actualizada")
    @GetMapping("/delete/{idTablero}")
    public RedirectView delete(
            @Parameter(description = "Identificador único del tablero a eliminar", required = true, example = "1") 
            @PathVariable Long idTablero) {
        tableroService.borrarTablero(idTablero);
        return new RedirectView("/tablero/list");
    }
}