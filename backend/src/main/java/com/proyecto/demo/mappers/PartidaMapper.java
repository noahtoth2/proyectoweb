package com.proyecto.demo.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.proyecto.demo.dto.PartidaDTO;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Partida;

@Component
public class PartidaMapper {
    
    private final JugadorMapper jugadorMapper;
    
    public PartidaMapper(JugadorMapper jugadorMapper) {
        this.jugadorMapper = jugadorMapper;
    }
    
    public PartidaDTO toDTO(Partida partida) {
        if (partida == null) {
            return null;
        }
        
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setCodigo(partida.getCodigo());
        dto.setNombre(partida.getNombre());
        dto.setMaxJugadores(partida.getMaxJugadores());
        dto.setIniciada(partida.getIniciada());
        dto.setFinalizada(partida.getFinalizada());
        dto.setFechaCreacion(partida.getFechaCreacion());
        dto.setFechaInicio(partida.getFechaInicio());
        dto.setFechaFin(partida.getFechaFin());
        
        if (partida.getCreador() != null) {
            dto.setCreadorId(partida.getCreador().getId());
            dto.setCreadorNombre(partida.getCreador().getNombre());
        }
        
        if (partida.getTurnoActual() != null) {
            dto.setTurnoActualId(partida.getTurnoActual().getId());
            dto.setTurnoActualNombre(partida.getTurnoActual().getNombre());
        }
        
        if (partida.getGanador() != null) {
            dto.setGanadorId(partida.getGanador().getId());
            dto.setGanadorNombre(partida.getGanador().getNombre());
        }
        
        if (partida.getJugadores() != null) {
            dto.setJugadores(partida.getJugadores().stream()
                .map(jugadorMapper::toDTO)
                .collect(Collectors.toList()));
            dto.setCantidadJugadores(partida.getJugadores().size());
            
            Map<Long, Long> selecciones = new HashMap<>();
            for (Jugador jugador : partida.getJugadores()) {
                if (jugador.getBarcoSeleccionadoId() != null) {
                    selecciones.put(jugador.getId(), jugador.getBarcoSeleccionadoId());
                }
            }
            dto.setJugadorBarcoSelecciones(selecciones);
        }
        
        return dto;
    }
}
