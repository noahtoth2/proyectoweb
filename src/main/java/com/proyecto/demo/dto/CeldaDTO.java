package com.proyecto.demo.dto;

public class CeldaDTO{

    private Long id;
    private char tipocelda;
    private int x;
    private int y;

    public CeldaDTO(){
    }

    public CeldaDTO(char tipocelda, int x, int y){
        this.tipocelda = tipocelda;
        this.x = x;
        this.y = y;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long Id){
        this.id = id;
    }

    public char getTipoCelda(){
        return tipocelda;
    }

    public void SetTipoCelda(char tipocelda){
        this.tipocelda = tipocelda;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }
    
}