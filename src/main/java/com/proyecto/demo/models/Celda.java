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
    private int x;
    private int y;

    @ManyToOne 
    private Tablero tablero;



    public Celda() {
    }

    public Celda(Character tipocelda, int x, int y){
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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}