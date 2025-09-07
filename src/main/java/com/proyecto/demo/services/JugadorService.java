package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.mappers.JugadorMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.JugadorRepository;

@Service
public class JugadorService {
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private BarcoRepository barcoRepository;

    public List<JugadorDTO> listarJugadores() {
        List<JugadorDTO> jugadorDTOs = new ArrayList<>();
        for (Jugador jugador : jugadorRepository.findAll()) {
            jugadorDTOs.add(JugadorMapper.toDTO(jugador));
        }
        return jugadorDTOs;
    }

    public JugadorDTO recuperarJugador(Long id) {
        return JugadorMapper.toDTO(jugadorRepository.findById(id).orElseThrow());
    }

    public void crear(JugadorDTO jugadorDTO) {
        
        
        Jugador entity = JugadorMapper.toEntity(jugadorDTO);
        entity.setId(null);
        entity = jugadorRepository.save(entity);
          if (jugadorDTO.getBarcosIds() != null) {
            for (Long barcoId : jugadorDTO.getBarcosIds()) {
                Barco barco = barcoRepository.findById(barcoId).orElse(null);
                if (barco != null) {
                    barco.setJugador(entity); // Asigna el jugador al barco
                    barcoRepository.save(barco); // Guarda el barco actualizado
                }
            }
        }
    }
    

    public void actualizarJugador(JugadorDTO jugadorDTO) {
        Jugador entity = JugadorMapper.toEntity(jugadorDTO);
        // TODO: Chequear que el id sea != null
        jugadorRepository.save(entity);
    }

    public void borrarJugador(Long jugadorId) {
        jugadorRepository.deleteById(jugadorId);
    }
}