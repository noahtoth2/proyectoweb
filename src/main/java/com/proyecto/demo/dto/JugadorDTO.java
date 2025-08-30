package com.proyecto.demo.dto;

public class JugadorDTO{
    private Long id;
    private String nombre;


    public JugadorDTO(){

    }
    public JugadorDTO(String nombre){
        this.nombre=nombre;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    
}