package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private Double velocidadX;
    private Double velocidadY;
    private Long posicionId;
    private Long modeloId;
    private Long jugadorId;
    private Long tableroId;

    public BarcoDTO(){
        // Velocidad inicial detenida
        this.velocidadX = 0.0;
        this.velocidadY = 0.0;
    }
    
    public BarcoDTO(Double velocidadX, Double velocidadY){
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }
    
    // Constructor de compatibilidad
    public BarcoDTO(Double velocidad){
        this.velocidadX = 0.0;
        this.velocidadY = velocidad;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    
    public Double getVelocidadX(){
        return velocidadX;
    }
    public void setVelocidadX(Double velocidadX){
        this.velocidadX = velocidadX;
    }
    
    public Double getVelocidadY(){
        return velocidadY;
    }
    public void setVelocidadY(Double velocidadY){
        this.velocidadY = velocidadY;
    }
    
    // Método de compatibilidad para velocidad lineal
    public Double getVelocidad(){
        return Math.max(Math.abs(velocidadX != null ? velocidadX : 0), 
                       Math.abs(velocidadY != null ? velocidadY : 0));
    }
    public void setVelocidad(Double velocidad){
        this.velocidadY = velocidad;
        this.velocidadX = 0.0;
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

