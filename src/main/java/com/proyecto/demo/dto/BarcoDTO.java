package com.edu.proyecto.demo.dto;

public class Barco{

    private int id;
    private double velocidad;
    private Posicion posicion;

    public BarcoDTO(){

    }
    public BarcoDTO(double velocidad,Posicion posicion){
        this.velocidad=velocidad;
        this.posicion=posicion;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id
    }
    public double getVelocidad(){
        return velocidad;
    }
    public void setVelocidad(double velocidad){
        this.velocidad=velocidad;
    }
    public Posicion getPosicion(){
        return posicion;

    }
    public void setPosicion(Posicion posicion){
        this.posicion=posicion;
    }
}

