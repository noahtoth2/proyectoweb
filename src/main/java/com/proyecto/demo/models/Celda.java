package com.proyecto.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Celda {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Character tipocelda;
    private Integer x;
    private Integer y;

    @ManyToOne 
    private Tablero tablero;



    public Celda() {
    }

    public Celda(Character tipocelda, Integer x, Integer y){
        this.tipocelda = tipocelda;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getTipoCelda(){
        return tipocelda;
    }

    public void setTipoCelda(Character tipocelda){
        this.tipocelda = tipocelda;
    }

    public int getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }


}