package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.demo.dto.PosicionDTO;
import com.proyecto.demo.mappers.PosicionMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Service
public class PosicionService {
    @Autowired
    private PosicionRepository posicionRepository;
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private TableroRepository tableroRepository;

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
        
        if (posicionDTO.getBarcoId() != null) {
            Barco barco = barcoRepository.findById(posicionDTO.getBarcoId())
                .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
            
            entity.setBarco(barco);
            
            // Guardar primero la posición
            Posicion saved = posicionRepository.save(entity);
            
            // Actualizar el barco con la nueva posición
            barco.setPosicion(saved);
            
            if (posicionDTO.getTableroId() != null) {
                Tablero tablero = tableroRepository.findById(posicionDTO.getTableroId())
                    .orElseThrow(() -> new RuntimeException("Tablero no encontrado"));
                barco.setTablero(tablero);
            }
            
            barcoRepository.save(barco);
            
            return PosicionMapper.toDTO(saved);
        }
        
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