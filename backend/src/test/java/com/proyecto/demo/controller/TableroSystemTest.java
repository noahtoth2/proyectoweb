package com.proyecto.demo.controller;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assumptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.JugadorRepository;
// Puedes inyectar más si deseas: TableroRepository, CeldaRepository, etc.
import com.proyecto.demo.repository.TableroRepository;
import com.proyecto.demo.repository.CeldaRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de SISTEMA (E2E + verificación en DB):
 *  - UI: login -> crear 2 jugadores -> crear 2 barcos -> colocarlos (P) -> iniciar juego -> subir vx -> mover.
 *  - DB: assert que se crearon al menos +2 jugadores y +2 barcos vs el estado inicial.
 */
@ActiveProfiles("system-test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // Levanta el backend (8080)
public class TableroSystemTest {

  // ===== Repositorios para validar efectos en DB =====
  @Autowired JugadorRepository jugadorRepository;
  @Autowired BarcoRepository   barcoRepository;
  // @Autowired TableroRepository tableroRepository;
  // @Autowired CeldaRepository   celdaRepository;

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
  static void preflightFrontend() throws Exception {
    var http = HttpClient.newHttpClient();
    try {
      var r = http.send(HttpRequest.newBuilder(URI.create(BASE_URL + "/")).GET().build(),
                        HttpResponse.BodyHandlers.discarding());
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

    // ===== Verificación en DB: deben haberse creado al menos 2 jugadores y 2 barcos =====
    long j = jugadorRepository.count();
    long b = barcoRepository.count();
    assertTrue(j >= baseJugadores + 2, "No se crearon 2 jugadores en DB");
    assertTrue(b >= baseBarcos + 2,    "No se crearon 2 barcos en DB");
  }

  @Test
  void flujoCompleto_DeIniciarSesion_A_IniciarPartida_Y_MoverPrimerTurno() {
    // 1) Login (client-side)
    page.navigate(BASE_URL + "/");
    page.getByLabel(Pattern.compile("Email", Pattern.CASE_INSENSITIVE)).fill("e2e@regata.com");
    page.getByLabel(Pattern.compile("Contraseña|Password", Pattern.CASE_INSENSITIVE)).fill("Playwright1!");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Iniciar\\s*Sesión", Pattern.CASE_INSENSITIVE))).click();

    // 2) Llega a /tablero
    page.waitForURL(url -> url.contains("/tablero"));
    assertTrue(page.url().contains("/tablero"), "No navegó a /tablero");
    assertTrue(page.getByText(Pattern.compile("Regata Online", Pattern.CASE_INSENSITIVE)).isVisible());

    // Helpers para abrir dropdowns
    final Runnable openPlayersDropdown = () ->
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Jugadores", Pattern.CASE_INSENSITIVE))).click();
    final Runnable openBoatsDropdown   = () ->
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Barcos", Pattern.CASE_INSENSITIVE))).click();

    // 3) Crear 2 jugadores
    openPlayersDropdown.run();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Crear\\s*Jugador", Pattern.CASE_INSENSITIVE))).click();
    page.locator("#playerName").fill("Alice");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("^\\s*Crear\\s*$", Pattern.CASE_INSENSITIVE))).click();

    openPlayersDropdown.run();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Crear\\s*Jugador", Pattern.CASE_INSENSITIVE))).click();
    page.locator("#playerName").fill("Bob");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("^\\s*Crear\\s*$", Pattern.CASE_INSENSITIVE))).click();

    // 4) Crear 1 barco para cada jugador
    // Alice
    openBoatsDropdown.run();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Crear\\s*Barco", Pattern.CASE_INSENSITIVE))).click();
    page.locator("#jugadorId").selectOption(new SelectOption().setLabel("Alice"));
    page.locator("#velocidadX").fill("1");
    page.locator("#velocidadY").fill("0");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("^\\s*Crear\\s*$", Pattern.CASE_INSENSITIVE))).click();

    // Bob
    openBoatsDropdown.run();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Crear\\s*Barco", Pattern.CASE_INSENSITIVE))).click();
    page.locator("#jugadorId").selectOption(new SelectOption().setLabel("Bob"));
    page.locator("#velocidadX").fill("1");
    page.locator("#velocidadY").fill("0");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("^\\s*Crear\\s*$", Pattern.CASE_INSENSITIVE))).click();

    // 5) Colocar barcos en casillas P (Partida)
    openBoatsDropdown.run();
    page.getByText(Pattern.compile("\\bAlice\\b.*Barco #", Pattern.CASE_INSENSITIVE)).click();
    page.locator("[title$=' - P']").first().click();

    openBoatsDropdown.run();
    page.getByText(Pattern.compile("\\bBob\\b.*Barco #", Pattern.CASE_INSENSITIVE)).click();
    var partidaCells = page.locator("[title$=' - P']");
    if (partidaCells.count() >= 2) partidaCells.nth(1).click();
    else partidaCells.first().click();

    // 6) Iniciar juego
    var iniciarBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Iniciar\\s*Juego", Pattern.CASE_INSENSITIVE)));
    assertTrue(iniciarBtn.isEnabled(), "El botón 'Iniciar Juego' debería estar habilitado con 2 jugadores y barcos colocados");
    iniciarBtn.click();

    // 7) Turno 1: seleccionar barco del jugador en turno, subir vx y mover a un movimiento válido
    String turnoActual = page.getByText(Pattern.compile("Turno:\\s+(\\w+)", Pattern.CASE_INSENSITIVE)).textContent();
    Matcher m = Pattern.compile("Turno:\\s+(\\w+)", Pattern.CASE_INSENSITIVE).matcher(turnoActual);
    assertTrue(m.find(), "No se detectó el nombre del turno actual");
    String jugadorEnTurno = m.group(1);

    // Seleccionar barco del jugador en turno
    openBoatsDropdown.run();
    page.getByText(Pattern.compile("\\b" + Pattern.quote(jugadorEnTurno) + "\\b.*Barco #", Pattern.CASE_INSENSITIVE)).click();

    // Aumentar velocidad X (+1) si está habilitado
    var vxIncrease = page.locator(".velocity-controls .velocity-control-group").first().locator("button.increase");
    if (vxIncrease.isEnabled()) {
      vxIncrease.click();
    }

    // Mover a la primera opción de "Movimientos posibles: → (x, y)"
    var firstMove = page.locator(".moves-list .move-option").first();
    String moveText = firstMove.textContent();           // "→ (12, 7)"
    Matcher mv = Pattern.compile("\\((\\-?\\d+)\\s*,\\s*(\\-?\\d+)\\)").matcher(moveText);
    assertTrue(mv.find(), "No se pudieron extraer coordenadas de 'Movimientos posibles'");
    String x = mv.group(1), y = mv.group(2);

    // Click sobre la celda destino por title="(x, y) - A" o " - M"
    var targetA = page.locator(String.format("[title='(%s, %s) - A']", x, y)).first();
    if (targetA.isVisible()) targetA.click();
    else page.locator(String.format("[title='(%s, %s) - M']", x, y)).first().click();

    // 8) Verificamos cambio de turno o mensaje de movimiento
    boolean turnoCambio = page.getByText(Pattern.compile("Turno:\\s+(?!"+Pattern.quote(jugadorEnTurno)+")\\w+", Pattern.CASE_INSENSITIVE)).isVisible();
    boolean mensajeMovimiento = page.getByText(Pattern.compile("Movimiento|META|colocado|velocidad", Pattern.CASE_INSENSITIVE)).isVisible();
    assertTrue(turnoCambio || mensajeMovimiento, "Se esperaba cambio de turno o mensaje tras mover el barco");
  }
}

