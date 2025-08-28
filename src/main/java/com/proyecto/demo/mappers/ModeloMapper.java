package com.proyecto.demo.mappers;

import com.proyecto.demo.dto.ModeloDTO;
import com.proyecto.demo.models.Modelo;

public class ModeloMapper{

    public static ModeloDTO toDTO(Modelo modelo){
        ModeloDTO modeloDTO = new ModeloDTO();
        modeloDTO.setId(modelo.getId());
        modeloDTO.setColor(modelo.getColor());
        modeloDTO.setNombre(modelo.getNombre());

        return modeloDTO;
    }

    public static Modelo toEntity(ModeloDTO modeloDTO){
        Modelo modelo = new Modelo();
        modelo.setId(modeloDTO.getId());
        modelo.setColor(modeloDTO.getColor());
        modelo.setNombre(modeloDTO.getNombre());

        return modelo;
    }

    

}