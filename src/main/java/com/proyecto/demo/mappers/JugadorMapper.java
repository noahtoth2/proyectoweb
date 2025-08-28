package com.proyecto.demo.mappers;

import com.proyecto.demo.dto.JugadorDTO;
import com.proyecto.demo.models.Jugador;

public class JugadorMapper {
    public static JugadorDTO toDTO(Jugador jugador) {
        JugadorDTO jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(jugador.getId());
        jugadorDTO.setNombre(jugador.getNombre());
        return jugadorDTO;
    }

    public static Jugador toEntity(JugadorDTO jugadorDTO) {
        Jugador jugador = new Jugador();
        jugador.setId(jugadorDTO.getId());
        jugador.setNombre(jugadorDTO.getNombre());
        return jugador;
    }