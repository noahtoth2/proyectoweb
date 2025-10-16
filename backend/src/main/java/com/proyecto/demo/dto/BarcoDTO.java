package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private double velocidadX;
    private double velocidadY;
    private Long posicionId;
    private Long modeloId;
    private Long jugadorId;
    private Long tableroId;

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
}

