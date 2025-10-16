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