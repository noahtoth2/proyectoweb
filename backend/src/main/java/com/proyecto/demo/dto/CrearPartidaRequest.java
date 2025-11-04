package com.proyecto.demo.dto;

public class CrearPartidaRequest {
    private String nombre;
    private String nombreJugador;
    private Integer maxJugadores;

    public CrearPartidaRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Integer getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }
}
