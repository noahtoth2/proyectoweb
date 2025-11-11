package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private double velocidadX;
    private double velocidadY;
    private Long posicionId;
    private Long modeloId;
    private Long jugadorId;
    private Long tableroId;
    
    // Objetos anidados para el admin
    private ModeloDTO modelo;
    private PosicionDTO posicion;
    private String jugadorNombre;

    public BarcoDTO() {
    }

    public BarcoDTO(double velocidadX, double velocidadY, Long posicionId, Long modeloId, Long jugadorId, Long tableroId) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.posicionId = posicionId;
        this.modeloId = modeloId;
        this.jugadorId = jugadorId;
        this.tableroId = tableroId;
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

    public Long getPosicionId() {
        return posicionId;
    }

    public void setPosicionId(Long posicionId) {
        this.posicionId = posicionId;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Long getTableroId() {
        return tableroId;
    }

    public void setTableroId(Long tableroId) {
        this.tableroId = tableroId;
    }

    public ModeloDTO getModelo() {
        return modelo;
    }

    public void setModelo(ModeloDTO modelo) {
        this.modelo = modelo;
    }

    public PosicionDTO getPosicion() {
        return posicion;
    }

    public void setPosicion(PosicionDTO posicion) {
        this.posicion = posicion;
    }

    public String getJugadorNombre() {
        return jugadorNombre;
    }

    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
    }
}

