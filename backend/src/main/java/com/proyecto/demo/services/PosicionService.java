package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.mappers.PosicionMapper;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.repository.PosicionRepository;

@Service
public class PosicionService {
    @Autowired
    private PosicionRepository posicionRepository;

    public List<PosicionDTO> listarPosiciones() {
        List<PosicionDTO> posicionDTOs = new ArrayList<>();
        for (Posicion posicion: posicionRepository.findAll()) {
            posicionDTOs.add(PosicionMapper.toDTO(posicion));
        }
        return posicionDTOs;
    }

    public List<PosicionDTO> listarPosiciones(PageRequest pageRequest) {
        List<PosicionDTO> posicionDTOs = new ArrayList<>();
        for (Posicion posicion : posicionRepository.findAll(pageRequest).getContent()) {
            posicionDTOs.add(PosicionMapper.toDTO(posicion));
        }
        return posicionDTOs;
    }

    public PosicionDTO recuperarPosicion(Long id) {
        return PosicionMapper.toDTO(posicionRepository.findById(id).orElseThrow());
    }

    public PosicionDTO crear(PosicionDTO posicionDTO) {
        Posicion entity = PosicionMapper.toEntity(posicionDTO);
        entity.setId(null);
        Posicion saved = posicionRepository.save(entity);
        return PosicionMapper.toDTO(saved);
    }

    public PosicionDTO actualizarPosicion(PosicionDTO posicionDTO) {
        Posicion entity = PosicionMapper.toEntity(posicionDTO);
        Posicion saved = posicionRepository.save(entity);
        return PosicionMapper.toDTO(saved);
    }

    public void borrarPosicion(Long posicionId) {
        posicionRepository.deleteById(posicionId);
    }
}