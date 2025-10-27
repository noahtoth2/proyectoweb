package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.CeldaRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

public class TableroSystemTest {
    @Autowired
    private BarcoRepository barcoRepository;

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private PosicionRepository posicionRepository;

    @Autowired
    private TableroRepository tableroRepository;
    
    @Autowired
    private WebTestClient webTestClient;


    
}
