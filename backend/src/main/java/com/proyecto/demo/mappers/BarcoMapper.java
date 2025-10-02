package com.proyecto.demo.mappers;


import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.models.Barco; 


public class BarcoMapper {
    
    public static BarcoDTO toDTO(Barco barco) {
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setVelocidad(barco.getVelocidad());
        barcoDTO.setPosicionId(barco.getPosicion() != null ? barco.getPosicion().getId() : null);
        barcoDTO.setModeloId(barco.getModelo() != null ? barco.getModelo().getId() : null);
        barcoDTO.setJugadorId(barco.getJugador() != null ? barco.getJugador().getId() : null);
        barcoDTO.setTableroId(barco.getTablero() != null ? barco.getTablero().getId() : null);
        return barcoDTO;
    }

    public static Barco toEntity(BarcoDTO barcoDTO) {
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        barco.setVelocidad(barcoDTO.getVelocidad());
        return barco;
    }
}