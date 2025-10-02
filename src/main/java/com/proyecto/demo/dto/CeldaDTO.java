package com.proyecto.demo.dto;

public class CeldaDTO{

    private Long id;
    private Character tipocelda;
    private Integer x;
    private Integer y;

    public CeldaDTO(){
    }hhh

    public CeldaDTO(Character tipocelda, Integer x, Integer y){
        this.tipocelda = tipocelda;
        this.x = x;
        this.y = y;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Character getTipoCelda(){
        return tipocelda;
    }

    public void setTipoCelda(Character tipocelda){
        this.tipocelda = tipocelda;
    }

    public Integer getX(){
        return x;
    }

    public void setX(Integer x){
        this.x = x;
    }

    public Integer getY(){
        return y;
    }

    public void setY(Integer y){
        this.y = y;
    }
    
}