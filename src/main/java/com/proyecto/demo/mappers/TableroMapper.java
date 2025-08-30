package com.proyecto.demo.mappers;


import com.proyecto.demo.dto.TableroDTO;
import com.proyecto.demo.models.Tablero; 


public class TableroMapper {
    public static TableroDTO toDTO(Tablero tablero) {
        TableroDTO tableroDTO = new TableroDTO();
        tableroTO.setId(tablero.getId());

        return tableroDTO;

    }

    public static Tablero toEntity(TableroDTO tableroDTO) {
        Tablero tablero = new Tablero();
        tablero.setId(tableroDTO.getId());
        
        return tablero;
    }
}