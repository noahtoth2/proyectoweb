package com.proyecto.demo.dto;

import java.util.List;

public class JugadorDTO{
    private Long id;
    private String nombre;
    private List<BarcoDTO> barcos;
    private List<Long> barcosIds; 
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
    public List<BarcoDTO> getBarcos() { 
        return barcos; 
    }
    public void setBarcos(List<BarcoDTO> barcos) { 
        this.barcos = barcos; 
    }
    public List<Long> getBarcosIds() { 
        return barcosIds; 
    }
    public void setBarcosIds(List<Long> barcosIds) { 
        this.barcosIds = barcosIds; 
    }
}