package com.proyecto.demo.models;



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


    private Double velocidadX;
    private Double velocidadY;

    @OneToOne
    private Posicion posicion;

    @ManyToOne
    private Modelo modelo;

    @ManyToOne
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "tablero_id")
    private Tablero tablero;

    public Barco(){
        
    }

    public Barco(Double velocidadX, Double velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(Double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public Double getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(Double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
}
