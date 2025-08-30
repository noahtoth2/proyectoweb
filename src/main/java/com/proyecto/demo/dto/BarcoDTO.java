package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private double velocidad;
    

    public BarcoDTO(){

    }
    public BarcoDTO(double velocidad){
        this.velocidad=velocidad;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }
    public double getVelocidad(){
        return velocidad;
    }
    public void setVelocidad(double velocidad){
        this.velocidad=velocidad;
    }
   
}

