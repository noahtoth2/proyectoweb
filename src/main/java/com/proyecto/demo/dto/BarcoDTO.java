package com.proyecto.demo.dto;

public class BarcoDTO{

    private Long id;
    private Double velocidad;
    

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
   
}

