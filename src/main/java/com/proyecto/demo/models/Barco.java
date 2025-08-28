package com.proyecto.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Barco {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;

    private double velocidad;

    @OneToOne
    private Posicion posicion;
    @OneToOne
    private Modelo modelo = new Modelo;
    @OneToOne
    private Jugador jugador = new Jugador;

    public Barco() {
    }

    public Barco(double velocidad) {
        this.velocidad = velocidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public double getPosicion() {
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

    public  getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
}