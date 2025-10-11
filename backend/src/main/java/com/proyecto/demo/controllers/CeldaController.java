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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/celda")
@Tag(name = "Controlador de Celdas Web", description = "Gestiona las vistas web para las operaciones CRUD de celdas del tablero, proporcionando formularios y páginas HTML para la gestión visual")
public class CeldaController {

    @Autowired
    private CeldaService celdaService;

    @Operation(summary = "Página de lista de celdas", description = "Muestra una página web con la lista completa de todas las celdas del tablero")
    @GetMapping("/list")
    public ModelAndView listarCeldas() {
        ModelAndView modelAndView = new ModelAndView("celda-list");
        List<CeldaDTO> celdas = celdaService.listarCeldas();
        modelAndView.addObject("listadoCeldas", celdas);
        return modelAndView;
    }

    @Operation(summary = "Página de detalle de celda", description = "Muestra una página web con los detalles completos de una celda específica")
    @GetMapping("/view/{idCelda}")
    public ModelAndView recuperarCelda(
            @Parameter(description = "Identificador único de la celda a visualizar", required = true, example = "1") 
            @PathVariable Long idCelda) {
        ModelAndView modelAndView = new ModelAndView("celda-view");
        CeldaDTO celda = celdaService.recuperarCelda(idCelda);
        modelAndView.addObject("celda", celda);
        return modelAndView;
    }

    @Operation(summary = "Formulario de creación", description = "Muestra el formulario web para crear una nueva celda")
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("celda-create");
        modelAndView.addObject("celda", new CeldaDTO());
        return modelAndView;
    }

    @Operation(summary = "Procesar creación de celda", description = "Procesa el formulario de creación y redirige a la lista de celdas")
    @PostMapping("/create")
    public RedirectView create(
            @Parameter(description = "Datos de la nueva celda enviados desde el formulario", required = true) 
            @ModelAttribute CeldaDTO celdaDTO) {
        celdaService.crear(celdaDTO);
        return new RedirectView("/celda/list");
    }

    @Operation(summary = "Formulario de edición", description = "Muestra el formulario web para editar una celda existente")
    @GetMapping("/edit-form/{idCelda}")
    public ModelAndView editForm(
            @Parameter(description = "Identificador único de la celda a editar", required = true, example = "1") 
            @PathVariable Long idCelda) {
        ModelAndView modelAndView = new ModelAndView("celda-edit");
        CeldaDTO celda = celdaService.recuperarCelda(idCelda);
        modelAndView.addObject("celda", celda);
        return modelAndView;
    }

    @Operation(summary = "Procesar actualización de celda", description = "Procesa el formulario de edición y redirige a la lista de celdas")
    @PostMapping("/update")
    public RedirectView update(
            @Parameter(description = "Datos actualizados de la celda enviados desde el formulario", required = true) 
            @ModelAttribute CeldaDTO celdaDTO) {
        celdaService.actualizarCelda(celdaDTO);
        return new RedirectView("/celda/list");
    }

    @Operation(summary = "Eliminar celda", description = "Elimina una celda del sistema y redirige a la lista actualizada")
    @GetMapping("/delete/{idCelda}")
    public RedirectView delete(
            @Parameter(description = "Identificador único de la celda a eliminar", required = true, example = "1") 
            @PathVariable Long idCelda) {
        celdaService.borrarBarco(idCelda);
        return new RedirectView("/celda/list");
    }
}
