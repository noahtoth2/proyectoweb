package com.proyecto.demo.dto;

public class SeleccionarBarcoRequest {
    private Long jugadorId;
    private Long barcoId;

    public SeleccionarBarcoRequest() {
    }

    public SeleccionarBarcoRequest(Long jugadorId, Long barcoId) {
        this.jugadorId = jugadorId;
        this.barcoId = barcoId;
    }

    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Long getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }
}
