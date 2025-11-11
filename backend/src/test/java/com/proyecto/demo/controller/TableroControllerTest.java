package com.proyecto.demo.controller;

import com.proyecto.demo.models.*;
import com.proyecto.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class TableroControllerTest {

    @Autowired private BarcoRepository barcoRepository;
    @Autowired private CeldaRepository celdaRepository;
    @Autowired private JugadorRepository jugadorRepository;
    @Autowired private ModeloRepository modeloRepository;
    @Autowired private PosicionRepository posicionRepository;
    @Autowired private TableroRepository tableroRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WebTestClient webTestClient;

    // ============================================================
    // 🔹 INICIALIZACIÓN DE ROLES Y USUARIOS PARA AUTENTICACIÓN
    // ============================================================
    @BeforeEach
    void init() {
        barcoRepository.deleteAll();
        posicionRepository.deleteAll();
        celdaRepository.deleteAll();
        tableroRepository.deleteAll();
        jugadorRepository.deleteAll();
        modeloRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear roles
        Role roleAdmin = new Role();
        roleAdmin.setName(Role.RoleName.ROLE_ADMIN);
        roleRepository.save(roleAdmin);

        Role roleUser = new Role();
        roleUser.setName(Role.RoleName.ROLE_USER);
        roleRepository.save(roleUser);

        // Crear usuarios con roles
        User admin = new User("admin", "admin@regata.com", "admin123");
        admin.setRoles(Set.of(roleAdmin));
        userRepository.save(admin);

        User jugador = new User("jugador", "jugador@regata.com", "jugador123");
        jugador.setRoles(Set.of(roleUser));
        userRepository.save(jugador);

        // Crear entorno del mini-juego
        Modelo modelo = new Modelo("Mini Velero", "#123abc");
        modeloRepository.save(modelo);

        Jugador j1 = new Jugador("Jugador Test 1");
        Jugador j2 = new Jugador("Jugador Test 2");
        jugadorRepository.save(j1);
        jugadorRepository.save(j2);

        Tablero tablero = new Tablero();
        tableroRepository.save(tablero);

        // Crear mapa pequeño 5x5
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                char tipo = determinarCeldaReducida(x, y);
                Celda celda = new Celda(tipo, x, y);
                celda.setTablero(tablero);
                celdaRepository.save(celda);
            }
        }

        // Posiciones iniciales
        Posicion p1 = new Posicion(1, 1);
        Posicion p2 = new Posicion(3, 3);
        Posicion p3 = new Posicion(2, 2);
        posicionRepository.save(p1);
        posicionRepository.save(p2);
        posicionRepository.save(p3);

        // Barcos de prueba
        Barco b1 = new Barco(0.0, 0.0);
        b1.setModelo(modelo);
        b1.setJugador(j1);
        b1.setTablero(tablero);
        b1.setPosicion(p1);
        barcoRepository.save(b1);

        Barco b2 = new Barco(1.0, 0.5);
        b2.setModelo(modelo);
        b2.setJugador(j2);
        b2.setTablero(tablero);
        b2.setPosicion(p2);
        barcoRepository.save(b2);
    }

    private char determinarCeldaReducida(int x, int y) {
        if (x == 0 || y == 0 || x == 4 || y == 4) return 'X';
        if (x == 1 && y == 1) return 'P';
        if (x == 3 && y == 3) return 'M';
        return 'A';
    }

    // ============================================================
    // TEST 1: Cambiar velocidad (jugador autenticado)
    // ============================================================
    @Test
    void testCambiarVelocidadBarco() {
        String token = loginAndGetToken("jugador", "jugador123");

        webTestClient.post()
                .uri("http://localhost:8081/api/tablero/1/barco/1/cambiar-velocidad")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"deltaVx\": 1.0, \"deltaVy\": 0.5}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.velocidadX").isEqualTo(1.0)
                .jsonPath("$.velocidadY").isEqualTo(0.5);
    }

    // ============================================================
    // TEST 2: Obtener posición futura (jugador autenticado)
    // ============================================================
    @Test
    void testObtenerPosicionFutura() {
        String token = loginAndGetToken("jugador", "jugador123");

        webTestClient.get()
                .uri("http://localhost:8081/api/tablero/1/barco/2/posicion-futura")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.x").isEqualTo(4.0)
                .jsonPath("$.y").isEqualTo(3.5);
    }

    // ============================================================
    // TEST 3: Actualizar barco (admin autenticado)
    // ============================================================
    @Test
    void testActualizarJugadorYPosicionDeBarco() {
        String token = loginAndGetToken("admin", "admin123");

        webTestClient.put()
                .uri("http://localhost:8081/api/barco")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "id": 1,
                        "velocidadX": 0.0,
                        "velocidadY": 0.0,
                        "posicionId": 1,
                        "modeloId": 1,
                        "jugadorId": 2,
                        "tableroId": 1
                    }
                """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.jugadorId").isEqualTo(2);
    }

    // ============================================================
    // TEST 4: Eliminar barco (admin autenticado)
    // ============================================================
    @Test
    void testEliminarBarcoPorId() {
        String token = loginAndGetToken("admin", "admin123");

        webTestClient.delete()
                .uri("http://localhost:8081/api/barco/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("http://localhost:8081/api/barco/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus().isNotFound();
    }

    
    // MÉTODO AUXILIAR PARA LOGIN Y TOKEN

    private String loginAndGetToken(String username, String password) {
      String token = webTestClient.post()
        .uri("http://localhost:8081/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.token").value(token -> {})
        .returnResult()
        .getResponseBody()
        .toString();
    return token;
    }
}
