package com.proyecto.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.demo.dto.TableroDTO;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.services.TableroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tablero")
@Tag(name = "API REST de Tableros", description = "Gestiona todas las operaciones CRUD para los tableros de juego via API REST")
public class TableroController {

    @Autowired
    private TableroService tableroService;

    @Operation(summary = "Listar todos los tableros", description = "Obtiene una lista completa de todos los tableros del juego")
    @GetMapping("/list")
    public ResponseEntity<List<TableroDTO>> getAllTableros() {
        try {
            List<TableroDTO> tableros = tableroService.listarTableros();
            return ResponseEntity.ok(tableros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener tablero por ID", description = "Obtiene un tablero específico mediante su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<TableroDTO> getTableroById(@Parameter(description = "ID único del tablero") @PathVariable Long id) {
        try {
            TableroDTO tablero = tableroService.recuperarTablero(id);
            return ResponseEntity.ok(tablero);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo tablero", description = "Crea un nuevo tablero en el sistema")
    @PostMapping
    public ResponseEntity<TableroDTO> createTablero(@Parameter(description = "Datos del tablero a crear") @RequestBody TableroDTO tableroDTO) {
        try {
            tableroService.crear(tableroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tableroDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Actualizar tablero", description = "Actualiza un tablero existente")
    @PutMapping("/{id}")
    public ResponseEntity<TableroDTO> updateTablero(
            @Parameter(description = "ID del tablero a actualizar") @PathVariable Long id,
            @Parameter(description = "Nuevos datos del tablero") @RequestBody TableroDTO tableroDTO) {
        try {
            tableroDTO.setId(id);
            tableroService.actualizarTablero(tableroDTO);
            return ResponseEntity.ok(tableroDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Eliminar tablero", description = "Elimina un tablero del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTablero(@Parameter(description = "ID del tablero a eliminar") @PathVariable Long id) {
        try {
            tableroService.borrarTablero(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Inicializar tablero de juego", description = "Crea un nuevo tablero con la configuración inicial del juego")
    @PostMapping("/initialize")
    public ResponseEntity<TableroDTO> initializeTablero() {
        try {
            // Crear un tablero básico
            TableroDTO tablero = new TableroDTO();
            tableroService.crear(tablero);
            return ResponseEntity.status(HttpStatus.CREATED).body(tablero);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @Operation(summary = "Cambiar velocidad de barco", description = "Aplica un cambio de velocidad a un barco específico")
    @PostMapping("/{tableroId}/barco/{barcoId}/cambiar-velocidad")
    public ResponseEntity<?> cambiarVelocidadBarco(
            @Parameter(description = "ID del tablero") @PathVariable Long tableroId,
            @Parameter(description = "ID del barco") @PathVariable Long barcoId,
            @Parameter(description = "Cambios de velocidad") @RequestBody CambioVelocidadRequest request) {
        try {
            boolean resultado = tableroService.cambiarVelocidadBarco(tableroId, barcoId, request.getDeltaVx(), request.getDeltaVy());
            if (resultado) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "Velocidad cambiada exitosamente"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cambio de velocidad inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error interno del servidor"));
        }
    }

    @Operation(summary = "Mover barco", description = "Ejecuta el movimiento de un barco aplicando las reglas del juego")
    @PostMapping("/{tableroId}/barco/{barcoId}/mover")
    public ResponseEntity<?> moverBarco(
            @Parameter(description = "ID del tablero") @PathVariable Long tableroId,
            @Parameter(description = "ID del barco") @PathVariable Long barcoId) {
        try {
            Tablero.ResultadoMovimiento resultado = tableroService.moverBarco(tableroId, barcoId);
            return ResponseEntity.ok(Map.of(
                "exitoso", resultado.isExitoso(),
                "mensaje", resultado.getMensaje(),
                "tipo", resultado.getTipo().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al mover el barco"));
        }
    }

    @Operation(summary = "Obtener posición futura", description = "Calcula la posición futura de un barco sin moverlo")
    @GetMapping("/{tableroId}/barco/{barcoId}/posicion-futura")
    public ResponseEntity<?> obtenerPosicionFutura(
            @Parameter(description = "ID del tablero") @PathVariable Long tableroId,
            @Parameter(description = "ID del barco") @PathVariable Long barcoId) {
        try {
            com.proyecto.demo.models.Posicion posicion = tableroService.calcularPosicionFutura(tableroId, barcoId);
            return ResponseEntity.ok(Map.of(
                "x", posicion.getX(),
                "y", posicion.getY()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al calcular posición futura"));
        }
    }

    @Operation(summary = "Obtener celdas del tablero", description = "Obtiene todas las celdas de un tablero específico")
    @GetMapping("/{tableroId}/celdas")
    public ResponseEntity<?> getCeldas(@Parameter(description = "ID del tablero") @PathVariable Long tableroId) {
        try {
            List<com.proyecto.demo.dto.CeldaSimpleDTO> celdas = tableroService.getCeldas(tableroId);
            return ResponseEntity.ok(celdas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener celdas del tablero"));
        }
    }

    // Clases DTO simples para las requests del API
    public static class CambioVelocidadRequest {
        private double deltaVx;
        private double deltaVy;

        public double getDeltaVx() { return deltaVx; }
        public void setDeltaVx(double deltaVx) { this.deltaVx = deltaVx; }
        public double getDeltaVy() { return deltaVy; }
        public void setDeltaVy(double deltaVy) { this.deltaVy = deltaVy; }
    }
}