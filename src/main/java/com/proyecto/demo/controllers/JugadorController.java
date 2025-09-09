package com.proyecto.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.services.BarcoService;
import com.proyecto.demo.services.JugadorService;

@RestController
@RequestMapping("/jugador")
public class JugadorController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private BarcoService barcoService;

    // Listar jugadores
    @GetMapping("/list")
    public ResponseEntity<List<JugadorDTO>> listarJugadores() {
        List<JugadorDTO> jugadores = jugadorService.listarJugadores();
        return ResponseEntity.status(HttpStatus.OK).body(personas);
    }

    // Ver un jugador
    @GetMapping("{idJugador}")
    public JugadorDTO recuperarJugador(@PathVariable Long idJugador) {
        return jugadorService.recuperarJugador(idJugador);
    }

    // Formulario para crear
    //@GetMapping("/create-form")
    //public ModelAndView createForm() {
     //   ModelAndView modelAndView = new ModelAndView("jugador-create");
     //   modelAndView.addObject("jugador", new JugadorDTO());
     //   modelAndView.addObject("barcosDisponibles", barcoService.listarBarcosDisponibles());
     //   return modelAndView;
    //}

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
        modelAndView.addObject("barcosDisponibles", barcoService.listarBarcosDisponibles());
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