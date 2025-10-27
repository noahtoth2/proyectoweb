package com.proyecto.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Celda;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;

import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.CeldaRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class TableroControllerTest {

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

   
    
    @BeforeEach
    void init() {
        
        Modelo modeloPrueba = new Modelo("Mini Velero", "#123abc");
        modeloRepository.save(modeloPrueba);

        
        Jugador jugador1 = new Jugador("Jugador Test 1");
        Jugador jugador2 = new Jugador("Jugador Test 2");
        jugadorRepository.save(jugador1);
        jugadorRepository.save(jugador2);

        
        Tablero tablero = new Tablero();
        tableroRepository.save(tablero);

        // Crear mapa (versión reducida 5x5) =====
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                char tipoCelda = determinarCeldaReducida(x, y);
                Celda celda = new Celda(tipoCelda, x, y);
                celda.setTablero(tablero);
                celdaRepository.save(celda);
            }
        }

        
        Posicion pos1 = new Posicion(1, 1); // punto de partida
        Posicion pos2 = new Posicion(3, 3); // otra posición 
        posicionRepository.save(pos1);
        posicionRepository.save(pos2);

        
        Barco barcoEstatico = new Barco(0.0, 0.0);
        barcoEstatico.setModelo(modeloPrueba);
        barcoEstatico.setJugador(jugador1);
        barcoEstatico.setTablero(tablero);
        barcoEstatico.setPosicion(pos1);
        barcoRepository.save(barcoEstatico);

    }

    private char determinarCeldaReducida(int x, int y) {
    // Borde del mapa: paredes
    if (x == 0 || y == 0 || x == 4 || y == 4) return 'X';

    // Punto de partida
    if (x == 1 && y == 1) return 'P';

    // Meta
    if (x == 3 && y == 3) return 'M';

    // Resto: agua
    return 'A';
    }

    @Test
    void testCambiarVelocidadBarco() {
    webTestClient.post()
        .uri("http://localhost:8081/api/tablero/1/barco/1/cambiar-velocidad")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"velocidadX\": 1.0, \"velocidadY\": 0.5}")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.velocidadX").isEqualTo(1.0)
        .jsonPath("$.velocidadY").isEqualTo(0.5);
        }
    
}