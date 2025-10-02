package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.CeldaDTO;
import com.proyecto.demo.mappers.CeldaMapper;
import com.proyecto.demo.models.Celda;
import com.proyecto.demo.repository.CeldaRepository;

@Service
public class CeldaService {
    @Autowired
    private CeldaRepository celdaRepository;

    public List<CeldaDTO> listarCeldas() {
        // TODO Encapsular esto en el PersonMapper
        List<CeldaDTO> celdaDTOs = new ArrayList<>();
        for (Celda  celda: celdaRepository.findAll()) {
            celdaDTOs.add(CeldaMapper.toDTO(celda));
        }
        return celdaDTOs;
    }

    public CeldaDTO recuperarCelda(Long id) {
        return CeldaMapper.toDTO(celdaRepository.findById(id).orElseThrow());
    }

    public void crear(CeldaDTO celdaDTO) {
        Celda entity = CeldaMapper.toEntity(celdaDTO);
        entity.setId(null);
        celdaRepository.save(entity);
    }

    public void actualizarCelda(CeldaDTO celdaDTO) {
        Celda entity = CeldaMapper.toEntity(celdaDTO);
        // TODO Chequear que el id sea != null
        celdaRepository.save(entity);
    }

    public void borrarBarco(Long celdaId) {
        celdaRepository.deleteById(celdaId);
    }
}