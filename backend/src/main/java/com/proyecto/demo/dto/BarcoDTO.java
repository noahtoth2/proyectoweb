package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private Double velocidad;
    private Long posicionId;
    private Long modeloId;
    private Long jugadorId;
    private Long tableroId;

    public BarcoDTO(){

    }
    public BarcoDTO(Double velocidad){
        this.velocidad=velocidad;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Double getVelocidad(){
        return velocidad;
    }
    public void setVelocidad(Double velocidad){
        this.velocidad=velocidad;
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

