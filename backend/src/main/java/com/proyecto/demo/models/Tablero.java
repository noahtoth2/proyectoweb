package com.proyecto.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tablero {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "tablero")
    private List<Barco> barcos= new ArrayList<>();

    @OneToMany(mappedBy = "tablero")
    private List<Celda> celdas= new ArrayList<>();


    public Tablero() {
    }

    public Tablero(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Barco> getBarcos() {
        return barcos;
    }

    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }

    public List<Celda> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }

    // MÉTODOS DE LÓGICA DEL JUEGO

    /**
     * Calcula la nueva posición de un barco basada en su velocidad actual
     */
    public Posicion calcularNuevaPosicion(Barco barco) {
        if (barco.getPosicion() == null) {
            throw new IllegalStateException("El barco debe tener una posición inicial");
        }
        
        Posicion posicionActual = barco.getPosicion();
        int nuevaX = posicionActual.getX() + barco.getVelocidadX().intValue();
        int nuevaY = posicionActual.getY() + barco.getVelocidadY().intValue();
        
        return new Posicion(nuevaX, nuevaY);
    }

    /**
     * Valida si un barco puede moverse a una posición específica
     */
    public ResultadoMovimiento validarMovimiento(Barco barco, Posicion nuevaPosicion) {
        // Verificar límites del tablero (21x21)
        if (nuevaPosicion.getX() < 0 || nuevaPosicion.getX() >= 21 || 
            nuevaPosicion.getY() < 0 || nuevaPosicion.getY() >= 21) {
            return new ResultadoMovimiento(false, "Fuera de los límites del tablero", TipoResultado.DESTRUIDO);
        }

        // Buscar la celda en la nueva posición
        Celda celdaDestino = obtenerCelda(nuevaPosicion.getX(), nuevaPosicion.getY());
        if (celdaDestino == null) {
            return new ResultadoMovimiento(false, "Celda no encontrada", TipoResultado.DESTRUIDO);
        }

        // Aplicar reglas según el tipo de celda
        switch (celdaDestino.getTipoCelda()) {
            case 'A': // Agua - puede navegar sin problema
                return new ResultadoMovimiento(true, "Movimiento válido en agua", TipoResultado.CONTINUA);
            
            case 'P': // Partida - puede navegar sin problema (son casillas de inicio)
                return new ResultadoMovimiento(true, "Movimiento válido en zona de partida", TipoResultado.CONTINUA);
            
            case 'X': // Pared/Obstáculo - barco destruido
                return new ResultadoMovimiento(false, "¡Barco destruido por colisión con pared!", TipoResultado.DESTRUIDO);
            
            case 'M': // Meta - termina el trayecto
                return new ResultadoMovimiento(true, "¡Meta alcanzada! Carrera terminada", TipoResultado.META_ALCANZADA);
            
            default:
                return new ResultadoMovimiento(false, "Tipo de celda desconocido", TipoResultado.DESTRUIDO);
        }
    }

    /**
     * Ejecuta el movimiento de un barco aplicando las reglas del juego
     */
    public ResultadoMovimiento moverBarco(Barco barco) {
        Posicion nuevaPosicion = calcularNuevaPosicion(barco);
        ResultadoMovimiento resultado = validarMovimiento(barco, nuevaPosicion);
        
        if (resultado.isExitoso()) {
            // Actualizar la posición del barco
            barco.getPosicion().setX(nuevaPosicion.getX());
            barco.getPosicion().setY(nuevaPosicion.getY());
        }
        
        return resultado;
    }

    /**
     * Valida si un cambio de velocidad es permitido
     * - Solo +1, 0, -1 en cada componente
     * - Solo se puede cambiar X o Y, no ambos en el mismo turno
     * - Permitir (0,0) para no cambiar velocidad
     */
    public boolean validarCambioVelocidad(double deltaVx, double deltaVy) {
        // Validar que los cambios sean solo -1, 0 o +1
        if (Math.abs(deltaVx) > 1 || Math.abs(deltaVy) > 1) {
            return false;
        }
        
        // Validar que solo se cambie X o Y, no ambos (excepto cuando ambos son 0)
        return deltaVx == 0 || deltaVy == 0;
    }

    /**
     * Aplica un cambio de velocidad a un barco
     */
    public boolean cambiarVelocidad(Barco barco, double deltaVx, double deltaVy) {
        if (!validarCambioVelocidad(deltaVx, deltaVy)) {
            return false;
        }
        
        double nuevaVx = barco.getVelocidadX() + deltaVx;
        double nuevaVy = barco.getVelocidadY() + deltaVy;
        
        barco.setVelocidadX(nuevaVx);
        barco.setVelocidadY(nuevaVy);
        
        return true;
    }

    /**
     * Obtiene una celda específica por coordenadas
     */
    private Celda obtenerCelda(int x, int y) {
        return celdas.stream()
                .filter(celda -> celda.getX().equals(x) && celda.getY().equals(y))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica si hay otros barcos en una posición específica
     */
    public boolean hayBarcoEnPosicion(int x, int y) {
        return barcos.stream()
                .anyMatch(barco -> barco.getPosicion() != null && 
                         barco.getPosicion().getX() == x && 
                         barco.getPosicion().getY() == y);
    }

    // Clases auxiliares para el resultado del movimiento
    public static class ResultadoMovimiento {
        private final boolean exitoso;
        private final String mensaje;
        private final TipoResultado tipo;

        public ResultadoMovimiento(boolean exitoso, String mensaje, TipoResultado tipo) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.tipo = tipo;
        }

        public boolean isExitoso() { return exitoso; }
        public String getMensaje() { return mensaje; }
        public TipoResultado getTipo() { return tipo; }
    }

    public enum TipoResultado {
        CONTINUA,      // El juego continúa normalmente
        META_ALCANZADA, // El barco alcanzó la meta
        DESTRUIDO      // El barco fue destruido
    }
}