package com.proyecto.demo.mappers;

import java.util.ArrayList;
import java.util.List;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Jugador;

public class JugadorMapper {
    public static JugadorDTO toDTO(Jugador jugador) {
        JugadorDTO jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(jugador.getId());
        jugadorDTO.setNombre(jugador.getNombre());
        List<BarcoDTO> barcosDTO = new ArrayList<>();
        if (jugador.getBarcos() != null) {
            for (Barco barco : jugador.getBarcos()) {
                barcosDTO.add(BarcoMapper.toDTO(barco));
            }
        }
        jugadorDTO.setBarcos(barcosDTO);
        return jugadorDTO;
    }


       

    public static Jugador toEntity(JugadorDTO jugadorDTO) {
        Jugador jugador = new Jugador();
        jugador.setId(jugadorDTO.getId());
        jugador.setNombre(jugadorDTO.getNombre());
        return jugador;
    }


}