package com.proyecto.demo.dto;

public class PosicionDTO{
    private Long id;
        private Integer x;
        private Integer y;



    public PosicionDTO(){

    }
    public PosicionDTO(Integer x,Integer y){
        this.x=x;
        this.y=y;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Integer getX(){
        return x;
    }
    public void setX(Integer x){
        this.x=x;
    }
    public Integer getY(){
        return y;
    }
    public void setY(Integer y){
        this.y=y;
    }
    
}