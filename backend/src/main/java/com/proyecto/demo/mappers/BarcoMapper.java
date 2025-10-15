package com.proyecto.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Component
public class BarcoMapper {
    
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private PosicionRepository posicionRepository;
    @Autowired
    private TableroRepository tableroRepository;
    
    public BarcoDTO toDTO(Barco barco) {
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setVelocidadX(barco.getVelocidadX());
        barcoDTO.setVelocidadY(barco.getVelocidadY());
        barcoDTO.setPosicionId(barco.getPosicion() != null ? barco.getPosicion().getId() : null);
        barcoDTO.setModeloId(barco.getModelo() != null ? barco.getModelo().getId() : null);
        barcoDTO.setJugadorId(barco.getJugador() != null ? barco.getJugador().getId() : null);
        barcoDTO.setTableroId(barco.getTablero() != null ? barco.getTablero().getId() : null);
        return barcoDTO;
    }

    public Barco toEntity(BarcoDTO barcoDTO) {
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        
        // Mapear velocidades con valores por defecto seguros
        Double velocidadXDto = barcoDTO.getVelocidadX();
        Double velocidadYDto = barcoDTO.getVelocidadY();
        double velocidadX = (velocidadXDto != null) ? velocidadXDto : 0.0;
        double velocidadY = (velocidadYDto != null) ? velocidadYDto : 0.0;
        barco.setVelocidadX(velocidadX);
        barco.setVelocidadY(velocidadY);
        
        // Mapear relaciones si están presentes
        if (barcoDTO.getJugadorId() != null) {
            Jugador jugador = jugadorRepository.findById(barcoDTO.getJugadorId()).orElse(null);
            barco.setJugador(jugador);
            System.out.println("DEBUG: Asignando jugador " + (jugador != null ? jugador.getNombre() : "NULL") + " al barco");
        }
        
        if (barcoDTO.getModeloId() != null) {
            Modelo modelo = modeloRepository.findById(barcoDTO.getModeloId()).orElse(null);
            barco.setModelo(modelo);
        }
        
        if (barcoDTO.getPosicionId() != null) {
            Posicion posicion = posicionRepository.findById(barcoDTO.getPosicionId()).orElse(null);
            barco.setPosicion(posicion);
        }
        
        if (barcoDTO.getTableroId() != null) {
            Tablero tablero = tableroRepository.findById(barcoDTO.getTableroId()).orElse(null);
            barco.setTablero(tablero);
        }
        
        return barco;
    }
}