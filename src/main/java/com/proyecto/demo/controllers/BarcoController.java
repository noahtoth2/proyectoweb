package com.proyecto.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.proyecto.demo.services.BarcoService;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequestMapping("/barcos")
public class BarcoController {
    @Autowired
    private BarcoService barcoService;

    @GetMapping("list")
    public List<BarcoDTO> listarBarcos() {
        return barcoService.listarBarcos();
    }
        barcoService.listarBarcos();

}

    