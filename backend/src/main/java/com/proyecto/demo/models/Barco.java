package com.proyecto.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Barco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Velocidad vectorial con componentes X e Y
    private double velocidadX;
    private double velocidadY;

    @OneToOne
    private Posicion posicion;

    @ManyToOne
    private Modelo modelo;

    @ManyToOne
    private Jugador jugador;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tablero_id")
    private Tablero tablero;

    public Barco() {
        // Velocidad inicial detenida como indica el documento
        this.velocidadX = 0.0;
        this.velocidadY = 0.0;
    }

    public Barco(double velocidadX, double velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    // Constructor de compatibilidad con velocidad lineal
    public Barco(double velocidad) {
        this.velocidadX = 0.0;
        this.velocidadY = velocidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    // Método de compatibilidad para obtener velocidad lineal
    public double getVelocidad() {
        return Math.max(Math.abs(velocidadX), Math.abs(velocidadY));
    }

    // Método de compatibilidad para establecer velocidad lineal
    public void setVelocidad(double velocidad) {
        this.velocidadY = velocidad;
        this.velocidadX = 0.0;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
}
