package com.edu.proyecto.demo.mappers;
import co.edu.proyecto.demo.dto.BarcoDTO;
import co.edu.proyecto.demo.models.Barco;

public class BarcoMapper {
    public static BarcoDTO toDTO(Barco barco) {
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setCedula(barco.getCedula());
        barcoDTO.setFirstName(barco.getFirstName());
        barcoDTO.setLastName(barco.getLastName());
        return barcoDTO;

    }

    public static Barco toEntity(BarcoDTO barcoDTO) {
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        barco.setCedula(barcoDTO.getCedula());
        barco.setFirstName(barcoDTO.getFirstName());
        barco.setLastName(barcoDTO.getLastName());
        return barco;
    }
}