package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



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
        // TODO Encapsular esto en el PersonMapper
        List<TableroDTO> tableroDTOs = new ArrayList<>();
        for (Tablero  barco: tableroRepository.findAll()) {
            tableroDTOs.add(TableroMapper.toDTO(barco));
        }
        return tableroDTOs;
    }

    public TableroDTO recuperarBarco(Long id) {
        return TableroMapper.toDTO(tableroRepository.findById(id).orElseThrow());
    }

    public void crear(TableroDTO barcoDTO) {
        Tablero entity = TableroMapper.toEntity(barcoDTO);
        entity.setId(null);
        tableroRepository.save(entity);
    }

    public void actualizarBarco(TableroDTO tableroDTO) {
        Tablero entity = TableroMapper.toEntity(tableroDTO);
        // TODO Chequear que el id sea != null
        tableroRepository.save(entity);
    }

    public void borrarBarco(Long barcoId) {
        tableroRepository.deleteById(barcoId);
    }
}
