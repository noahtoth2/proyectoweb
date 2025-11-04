package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.ModeloDTO;
import com.proyecto.demo.mappers.ModeloMapper;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.repository.ModeloRepository;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    public List<ModeloDTO> listarModelos() {
        List<ModeloDTO> modeloDTOs = new ArrayList<>();
        for (Modelo modelo : modeloRepository.findAll()) {
            modeloDTOs.add(ModeloMapper.toDTO(modelo));
        }
        return modeloDTOs;
    }

    public ModeloDTO recuperarModelo(Long id) {
        return ModeloMapper.toDTO(modeloRepository.findById(id).orElseThrow());
    }

    public void crear(ModeloDTO modeloDTO) {
        Modelo entity = ModeloMapper.toEntity(modeloDTO);
        entity.setId(null);
        modeloRepository.save(entity);
    }

    public void actualizarModelo(ModeloDTO modeloDTO) {
        Modelo entity = ModeloMapper.toEntity(modeloDTO);
        modeloRepository.save(entity);
    }

    public void borrarModelo(Long modeloId) {
        Modelo modelo = modeloRepository.findById(modeloId)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado con ID: " + modeloId));
        
        if (modelo.getBarcos() != null && !modelo.getBarcos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el modelo '" + modelo.getNombre() + 
                                     "' porque está relacionado con " + modelo.getBarcos().size() + 
                                     " barco(s)");
        }
        
        modeloRepository.deleteById(modeloId);
    }
}