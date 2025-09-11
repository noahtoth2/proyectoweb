package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.mappers.PosicionMapper;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.repository.PosicionRepository;

@Service
public class PosicionService {
    @Autowired
    private PosicionRepository posicionRepository;

    public List<PosicionDTO> listarBarcos() {
        // TODO Encapsular esto en el PersonMapper
        List<PosicionDTO> posicionDTOs = new ArrayList<>();
        for (Posicion  posicion: posicionRepository.findAll()) {
            posicionDTOs.add(PosicionMapper.toDTO(posicion));
        }
        return posicionDTOs;
    }

    public PosicionDTO recuperarBarco(Long id) {
        return PosicionMapper.toDTO(posicionRepository.findById(id).orElseThrow());
    }

    public void crear(PosicionDTO posicionDTO) {
        Posicion entity = PosicionMapper.toEntity(posicionDTO);
        entity.setId(null);
        posicionRepository.save(entity);
    }

    public void actualizarPosicion(PosicionDTO posicionDTO) {
        Posicion entity = PosicionMapper.toEntity(posicionDTO);
        // TODO Chequear que el id sea != null
        posicionRepository.save(entity);
    }

    public void borrarBarco(Long posicionId) {
        posicionRepository.deleteById(posicionId);
    }
}