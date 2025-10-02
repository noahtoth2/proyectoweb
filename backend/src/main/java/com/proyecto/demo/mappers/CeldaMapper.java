package com.proyecto.demo.mappers;


import com.proyecto.demo.dto.CeldaDTO;
import com.proyecto.demo.models.Celda; 


public class CeldaMapper {
    public static CeldaDTO toDTO(Celda celda) {
        CeldaDTO celdaDTO = new CeldaDTO();
        celdaDTO.setId(celda.getId());
        celdaDTO.setTipoCelda(celda.getTipoCelda()); 
        celdaDTO.setX(celda.getX());
        celdaDTO.setY(celda.getY());

        return celdaDTO;

    }

    public static Celda toEntity(CeldaDTO celdaDTO) {
        Celda celda = new Celda();
        celda.setId(celdaDTO.getId());
        celda.setTipoCelda(celdaDTO.getTipoCelda());
        celda.setX(celdaDTO.getX());
        celda.setY(celdaDTO.getY());
        
        return celda;
    }
}