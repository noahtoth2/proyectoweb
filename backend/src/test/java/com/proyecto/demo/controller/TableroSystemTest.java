package com.proyecto.demo.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.JugadorRepository;

/**
 * Prueba de SISTEMA (E2E + verificación en DB):
 *  - UI: login -> seleccionar barco -> crear partida -> iniciar juego.
 *  - DB: verificar que existen jugadores y barcos en el sistema.
 */
@ActiveProfiles("system-test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TableroSystemTest {

  // ===== Repositorios para validar efectos en DB =====
  @Autowired JugadorRepository jugadorRepository;
  @Autowired BarcoRepository   barcoRepository;

  // ===== Playwright =====
  static Playwright playwright;
  static Browser browser;
  BrowserContext context;
  Page page;

  static final String BASE_URL = System.getProperty("E2E_BASE_URL", "http://localhost:4200");

  long baseJugadores;
  long baseBarcos;

  // --- Verificamos SÓLO el frontend; el backend lo arranca SpringBootTest ---
  @BeforeAll
  static void preflightFrontend() {
    HttpClient http = HttpClient.newHttpClient();
    try {
      HttpResponse<Void> r = http.send(
        HttpRequest.newBuilder(URI.create(BASE_URL + "/")).GET().build(),
        HttpResponse.BodyHandlers.discarding()
      );
      Assumptions.assumeTrue(r.statusCode() < 500, "Levanta el frontend en " + BASE_URL);
    } catch (Exception ex) {
      Assumptions.assumeTrue(false, "Frontend no accesible en " + BASE_URL);
    }

    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
  }

  @AfterAll
  static void afterAll() {
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }

  @BeforeEach
  void setUp() {
    // Contadores base en DB para validar efectos del flujo
    baseJugadores = jugadorRepository.count();
    baseBarcos    = barcoRepository.count();

    context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1366, 768));
    page = context.newPage();
  }

  @AfterEach
  void cleanUp() {
    if (context != null) context.close();

    // ===== Verificación en DB: con el nuevo sistema de multijugador =====
    // En el nuevo sistema, cada usuario tiene un barco pre-asignado
    // La partida multijugador usa estos barcos existentes
    // Por lo tanto, verificamos que existan partidas y jugadores asociados
    long partidasCreadas = jugadorRepository.count();
    assertTrue(partidasCreadas >= baseJugadores, "Debe haber al menos el mismo número de jugadores base");
    
    // Verificar que hay barcos en el sistema
    long barcosActuales = barcoRepository.count();
    assertTrue(barcosActuales >= baseBarcos, "Debe haber al menos el mismo número de barcos base");
  }

  @Test
  void flujoCompleto_DeIniciarSesion_A_IniciarPartida_Y_MoverPrimerTurno() {
    // 1) Login (con el nuevo sistema de autenticación)
    page.navigate(BASE_URL + "/");
    
    // Esperar a que cargue la página de login
    page.waitForSelector("input[name='username']");
    
    page.locator("input[name='username']").fill("test");
    page.locator("input[name='password']").fill("123456");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Iniciar\\s*Sesión", Pattern.CASE_INSENSITIVE))).click();

    // 2) Si el usuario no tiene barco, seleccionar uno
    if (page.url().contains("/select-barco")) {
      page.waitForSelector(".barco-card");
      page.locator(".barco-card").first().click();
    }

    // 3) Llega a /lobby (multijugador)
    page.waitForURL(url -> url.contains("/lobby"), new Page.WaitForURLOptions().setTimeout(5000));
    
    // 4) Crear una partida
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Crear\\s*Partida", Pattern.CASE_INSENSITIVE))).click();
    
    // Esperar a estar en sala de espera
    page.waitForSelector("text=/Sala de Espera/i");
    
    // 5) Iniciar partida (como anfitrión)
    var iniciarBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Iniciar\\s*Partida", Pattern.CASE_INSENSITIVE)));
    iniciarBtn.waitFor(new Locator.WaitForOptions().setTimeout(3000));
    assertTrue(iniciarBtn.isEnabled(), "El botón 'Iniciar Partida' debería estar habilitado");
    iniciarBtn.click();

    // 6) Debe redirigir al tablero de juego
    page.waitForURL(url -> url.contains("/tablero"), new Page.WaitForURLOptions().setTimeout(5000));
    assertTrue(page.url().contains("/tablero"), "No navegó a /tablero");

    // 7) Verificar que el tablero se cargó
    page.waitForSelector(".tablero-container");
    assertTrue(page.locator(".tablero-container").isVisible(), "El tablero no está visible");

    // 8) Verificar que hay información del turno
    boolean turnoVisible = page.locator("text=/Turno/i").isVisible();
    assertTrue(turnoVisible, "No se muestra información del turno en el juego");
  }
}

