package com.proyecto.demo.mappers;


import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.models.Posicion; 


public class PosicionMapper {
    public static PosicionDTO toDTO(Posicion posicion) {
        PosicionDTO posicionDTO = new PosicionDTO();
        posicionTO.setId(posicion.getId());
        posicionDTO.setX(posicion.getX());
        posicionDTO.setY(posicion.getY());

        return posicionDTO;

    }

    public static Posicion toEntity(PosicionDTO posicionDTO) {
        Posicion posicion = new Posicion();
        posicion.setId(posicionDTO.getId());
        posicion.setX(posicionDTO.getX());
        posicion.setY(posicionTO.getY());
        
        return posicion;
    }
}