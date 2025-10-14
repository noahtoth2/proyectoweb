package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.TableroDTO;
import com.proyecto.demo.mappers.TableroMapper;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.TableroRepository;

@Service
public class TableroService {
    @Autowired
    private TableroRepository tableroRepository;

    public List<TableroDTO> listarTableros() {
        List<TableroDTO> tableroDTOs = new ArrayList<>();
        for (Tablero  tablero: tableroRepository.findAll()) {
            tableroDTOs.add(TableroMapper.toDTO(tablero));
        }
        return tableroDTOs;
    }

    public TableroDTO recuperarTablero(Long id) {
        return TableroMapper.toDTO(tableroRepository.findById(id).orElseThrow());
    }

    public void crear(TableroDTO barcoDTO) {
        Tablero entity = TableroMapper.toEntity(barcoDTO);
        entity.setId(null);
        tableroRepository.save(entity);
    }

    public void actualizarTablero(TableroDTO tableroDTO) {
        Tablero entity = TableroMapper.toEntity(tableroDTO);
        tableroRepository.save(entity);
    }

    public void borrarTablero(Long tableroId) {
        tableroRepository.deleteById(tableroId);
    }
}
