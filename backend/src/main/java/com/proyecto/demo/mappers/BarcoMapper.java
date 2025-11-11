package com.proyecto.demo.mappers;

import org.springframework.stereotype.Component;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.models.Barco;


@Component
public class BarcoMapper {
    
    public static BarcoDTO toDTO(Barco barco) {
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setVelocidadX(barco.getVelocidadX());
        barcoDTO.setVelocidadY(barco.getVelocidadY());
        
        // Mapear IDs de las relaciones
        if (barco.getPosicion() != null) {
            barcoDTO.setPosicionId(barco.getPosicion().getId());
            barcoDTO.setPosicion(PosicionMapper.toDTO(barco.getPosicion()));
        }
        if (barco.getModelo() != null) {
            barcoDTO.setModeloId(barco.getModelo().getId());
            barcoDTO.setModelo(ModeloMapper.toDTO(barco.getModelo()));
        }
        if (barco.getJugador() != null) {
            barcoDTO.setJugadorId(barco.getJugador().getId());
            barcoDTO.setJugadorNombre(barco.getJugador().getNombre());
        }
        if (barco.getTablero() != null) {
            barcoDTO.setTableroId(barco.getTablero().getId());
        }
        return barcoDTO;
    }

    public static Barco toEntity(BarcoDTO barcoDTO) {
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        barco.setVelocidadX(barcoDTO.getVelocidadX());
        barco.setVelocidadY(barcoDTO.getVelocidadY());
        return barco;
    }
       
}