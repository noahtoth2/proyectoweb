package com.proyecto.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Celda {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private char tipocelda;
    private int x;
    private int y;

    @OneToOne
    private Tablero tablero = new Tablero();



    public Celda() {
    }

    public Celda(char tipocelda, int x, int y){
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

    public char getTipoCelda(){
        return tipocelda;
    }

    public void setTipoCelda(char tipocelda){
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