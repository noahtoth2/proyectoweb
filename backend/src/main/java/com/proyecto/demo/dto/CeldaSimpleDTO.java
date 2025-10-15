package com.proyecto.demo.dto;

public class CeldaSimpleDTO {
    private Long id;
    private Character tipo;
    private Integer fila;
    private Integer columna;

    public CeldaSimpleDTO() {}

    public CeldaSimpleDTO(Long id, Character tipo, Integer fila, Integer columna) {
        this.id = id;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getColumna() {
        return columna;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }
}