package com.proyecto.demo.models;



import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Jugador{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) 
    private Long id;


    private String nombre;
    
    @OneToMany(mappedBy = "jugador")
    private List<Barco> barcos = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;
    
    public Jugador(){
    }


    public Jugador(String nombre) {
        this.nombre = nombre;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Barco> getBarcos(){
        return barcos;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    
}