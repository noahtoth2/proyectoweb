package com.proyecto.demo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "partidas")
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codigo; // Código único para unirse a la partida
    
    @Column(nullable = false)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Jugador creador;
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    private List<Jugador> jugadores = new ArrayList<>();
    
    @Column(nullable = false)
    private Integer maxJugadores = 4;
    
    @Column(nullable = false)
    private Boolean iniciada = false;
    
    @Column(nullable = false)
    private Boolean finalizada = false;
    
    @ManyToOne
    @JoinColumn(name = "turno_jugador_id")
    private Jugador turnoActual;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column
    private LocalDateTime fechaInicio;
    
    @Column
    private LocalDateTime fechaFin;
    
    @ManyToOne
    @JoinColumn(name = "ganador_id")
    private Jugador ganador;

    // Constructors
    public Partida() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Jugador getCreador() {
        return creador;
    }

    public void setCreador(Jugador creador) {
        this.creador = creador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Integer getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public Boolean getIniciada() {
        return iniciada;
    }

    public void setIniciada(Boolean iniciada) {
        this.iniciada = iniciada;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Jugador turnoActual) {
        this.turnoActual = turnoActual;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }
}
