package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.mappers.BarcoMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.repository.BarcoRepository;

@Service
public class BarcoService {
    @Autowired
    private BarcoRepository barcoRepository;

    public List<BarcoDTO> listarBarcos() {
        // TODO Encapsular esto en el PersonMapper
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco  barco: barcoRepository.findAll()) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }

    public List<BarcoDTO> listarBarcos(PageRequest pageRequest) {
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco barco : barcoRepository.findAll(pageRequest).getContent()) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }

    public BarcoDTO recuperarBarco(Long id) {
        return BarcoMapper.toDTO(barcoRepository.findById(id).orElseThrow());
    }

    public BarcoDTO crear(BarcoDTO barcoDTO) {
        Barco entity = BarcoMapper.toEntity(barcoDTO);
        entity.setId(null);
        Barco saved = barcoRepository.save(entity);
        return BarcoMapper.toDTO(saved);
    }

    public BarcoDTO actualizarBarco(BarcoDTO barcoDTO) {
        Barco entity = BarcoMapper.toEntity(barcoDTO);
        // TODO Chequear que el id sea != null
        Barco saved = barcoRepository.save(entity);
        return BarcoMapper.toDTO(saved);
    }

    public void borrarBarco(Long barcoId) {
        barcoRepository.deleteById(barcoId);
    }

    public List<BarcoDTO> listarBarcosDisponibles() {
    List<BarcoDTO> barcoDTOs = new ArrayList<>();
    for (Barco barco : barcoRepository.findAll()) {
        if (barco.getJugador() == null) { // Solo barcos sin jugador
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
    }
    return barcoDTOs;
}



}