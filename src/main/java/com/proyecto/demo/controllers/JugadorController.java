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

import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.services.JugadorService;

@Controller
@RequestMapping("/jugador")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    // Listar jugadores
    @GetMapping("/list")
    public ModelAndView listarJugadores() {
        ModelAndView modelAndView = new ModelAndView("jugador-list");
        List<JugadorDTO> jugadores = jugadorService.listarJugadores();
        modelAndView.addObject("listadoJugadores", jugadores);
        return modelAndView;
    }

    // Ver un jugador
    @GetMapping("/view/{idJugador}")
    public ModelAndView recuperarJugador(@PathVariable Long idJugador) {
        ModelAndView modelAndView = new ModelAndView("jugador-view");
        JugadorDTO jugador = jugadorService.recuperarJugador(idJugador);
        modelAndView.addObject("jugador", jugador);
        return modelAndView;
    }

    // Formulario para crear
    @GetMapping("/create-form")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("jugador-create");
        modelAndView.addObject("jugador", new JugadorDTO());
        return modelAndView;
    }

    // Crear jugador y redireccionar
    @PostMapping("/create")
    public RedirectView create(@ModelAttribute JugadorDTO jugadorDTO) {
        jugadorService.crear(jugadorDTO);
        return new RedirectView("/jugador/list");
    }

    // Formulario para editar
    @GetMapping("/edit-form/{idJugador}")
    public ModelAndView editForm(@PathVariable Long idJugador) {
        ModelAndView modelAndView = new ModelAndView("jugador-edit");
        JugadorDTO jugador = jugadorService.recuperarJugador(idJugador);
        modelAndView.addObject("jugador", jugador);
        return modelAndView;
    }

    // Actualizar jugador y redireccionar
    @PostMapping("/update")
    public RedirectView update(@ModelAttribute JugadorDTO jugadorDTO) {
        jugadorService.actualizarJugador(jugadorDTO);
        return new RedirectView("/jugador/list");
    }

    // Eliminar jugador y redireccionar
    @GetMapping("/delete/{idJugador}")
    public RedirectView delete(@PathVariable Long idJugador) {
        jugadorService.borrarJugador(idJugador);
        return new RedirectView("/jugador/list");}
    }