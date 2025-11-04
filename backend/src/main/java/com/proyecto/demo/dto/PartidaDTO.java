package com.proyecto.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PartidaDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Long creadorId;
    private String creadorNombre;
    private List<JugadorDTO> jugadores;
    private Integer maxJugadores;
    private Boolean iniciada;
    private Boolean finalizada;
    private Long turnoActualId;
    private String turnoActualNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long ganadorId;
    private String ganadorNombre;
    private Integer cantidadJugadores;

    // Constructors
    public PartidaDTO() {
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

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public String getCreadorNombre() {
        return creadorNombre;
    }

    public void setCreadorNombre(String creadorNombre) {
        this.creadorNombre = creadorNombre;
    }

    public List<JugadorDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<JugadorDTO> jugadores) {
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

    public Long getTurnoActualId() {
        return turnoActualId;
    }

    public void setTurnoActualId(Long turnoActualId) {
        this.turnoActualId = turnoActualId;
    }

    public String getTurnoActualNombre() {
        return turnoActualNombre;
    }

    public void setTurnoActualNombre(String turnoActualNombre) {
        this.turnoActualNombre = turnoActualNombre;
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

    public Long getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(Long ganadorId) {
        this.ganadorId = ganadorId;
    }

    public String getGanadorNombre() {
        return ganadorNombre;
    }

    public void setGanadorNombre(String ganadorNombre) {
        this.ganadorNombre = ganadorNombre;
    }

    public Integer getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(Integer cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }
}
