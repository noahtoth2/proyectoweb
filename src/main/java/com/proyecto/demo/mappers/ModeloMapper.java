package com.edu.proyecto.demo.mappers;

import co.edu.proyecto.demo.dto.ModeloDTO;
import co.edu.proyecto.demo.models.Modelo;

public class ModeloMapper(){

    public static ModeloDTO toDTO(Modelo modelo){
        ModeloDTO modeloDTO = new ModeloDTO();
        modeloDTO.setId(modelo.getId());
        modeloDTO.setColor(modelo.getColor());
        modeloDTO.setNombre(modelo.getNombre());

        return modeloDTO;
    }

    public static Model toEntity(ModeloDTO modeloDTO){
        Modelo modelo = new Modelo();
        modelo.setId(modeloDTO.getId());
        modelo.setColor(modeloDTO.getColor());
        modelo.setNombre(modeloDTO.setNombre());

        return model;
    }

    

}