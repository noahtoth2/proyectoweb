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

        
        Barco barco1 = new Barco(0.0, 0.0);
        barco1.setModelo(modeloPrueba);
        barco1.setJugador(jugador1);
        barco1.setTablero(tablero);
        barco1.setPosicion(pos1);
        barcoRepository.save(barco1);

        Barco barco2 = new Barco(1.0, 0.5);
        barco2.setModelo(modeloPrueba);
        barco2.setJugador(jugador2);
        barco2.setTablero(tablero);
        barco2.setPosicion(pos2);
        barcoRepository.save(barco2);

        Barco barco3 = new Barco(0.0, 0.0);
        barco3.setModelo(modeloPrueba);
        barco3.setJugador(jugador1);
        barco3.setTablero(tablero);
        barco3.setPosicion(pos1);
        barcoRepository.save(barco3);

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

    // INICIO DE LAS PRUEBAS POR CADA METODO//

    //POST
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

    
    //GET
    @Test
    void testObtenerPosicionFutura() {

        //Barco 2 tiene velocidad (1.0,0.5) y posicion (3,3)
        // Posicion futura esperada es (4.0,3.5)

    webTestClient.get()
        .uri("http://localhost:8081/api/tablero/1/barco/2/posicion-futura")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.x").isEqualTo(4.0)
        .jsonPath("$.y").isEqualTo(3.5);
    }


    //PUT
    @Test
    void testActualizarJugadorYPosicionDeBarco() {
    // vamos a cambiar al jugador y posicion del barco1
    // velocidadX/Y se dejan igual solo para completar el DTO.

    webTestClient.put()
        .uri("http://localhost:8081/api/barco")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("""
            {
                "id": 1,
                "velocidadX": 0.0,
                "velocidadY": 0.0,
                "posicionId": 2,
                "modeloId": 1,
                "jugadorId": 2,
                "tableroId": 1
            }
        """)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.jugadorId").isEqualTo(2)
        .jsonPath("$.posicionId").isEqualTo(2);
    }


    //DELETE
    @Test
    void testEliminarBarcoPorId() {
    // eliminar el barco con id=3
    webTestClient.delete()
        .uri("http://localhost:8081/api/barco/3")
        .exchange()
        .expectStatus().isOk(); 

    // intentar obtenerlo y verificar que ya no existe
    webTestClient.get()
        .uri("http://localhost:8081/api/barco/3")
        .exchange()
        .expectStatus().isNotFound();
    }

    
}