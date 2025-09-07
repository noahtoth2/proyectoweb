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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.proyecto.demo.dto.ModeloDTO;
import com.proyecto.demo.services.ModeloService;

@Controller
@RequestMapping("/modelo")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    // Listar modelos
    @GetMapping("/list")
    public ModelAndView listarModelos() {
        ModelAndView modelAndView = new ModelAndView("modelo-list");
        List<ModeloDTO> modelos = modeloService.listarModelos();
        modelAndView.addObject("listadoModelos", modelos);
        return modelAndView;
    }

    // Ver un modelo
    @GetMapping("/view/{idModelo}")
    public ModelAndView recuperarModelo(@PathVariable Long idModelo) {
        ModelAndView modelAndView = new ModelAndView("modelo-view");
        ModeloDTO modelo = modeloService.recuperarJugador(idModelo);
        modelAndView.addObject("modelo", modelo);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("modelo-create");
        modelAndView.addObject("modelo", new ModeloDTO());
        return modelAndView;
    }

    // Crear modelo y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute ModeloDTO modeloDTO) {
        modeloService.crear(modeloDTO);
        return new RedirectView("/modelo/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idModelo}")
    public ModelAndView editForm(@PathVariable Long idModelo) {
        ModelAndView modelAndView = new ModelAndView("modelo-edit");
        ModeloDTO modelo = modeloService.recuperarJugador(idModelo);
        modelAndView.addObject("modelo", modelo);
        return modelAndView;
    }

    // Actualizar modelo y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute ModeloDTO modeloDTO) {
        modeloService.actualizarModelo(modeloDTO);
        return new RedirectView("/modelo/list");
    }

    // Eliminar modelo y redireccionar
    @GetMapping("/delete/{idModelo}")
    public RedirectView delete(@PathVariable Long idModelo, RedirectAttributes redirectAttributes) {
        try {
            modeloService.borrarModelo(idModelo);
            redirectAttributes.addFlashAttribute("successMessage", "Modelo eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return new RedirectView("/modelo/list");
    }
}