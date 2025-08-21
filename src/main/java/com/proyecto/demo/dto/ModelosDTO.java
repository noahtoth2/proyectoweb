package com.edu.proyecto.demo.dto;

public class ModelosDTO{
    private Long id;
    private String nombre;
    private String color;


    public ModelosDTO(){

    }
    public ModelosDTO(String nombre,String color){
        this.nombre=nombre;
        this.color=color;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color=color;
    }
    
}