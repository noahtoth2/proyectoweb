package com.proyecto.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tablero {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "tablero")
    private List<Barco> barcos= new ArrayList<>();

    @OneToMany(mappedBy = "tablero")
    private List<Celda> celdas= new ArrayList<>();


    public Tablero() {
    }

    public List<Barco> getBarcos() {
        return barcos;
    }

    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }

    public List<Celda> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }
}