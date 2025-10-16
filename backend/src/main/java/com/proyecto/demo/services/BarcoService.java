package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.mappers.BarcoMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Service
public class BarcoService {
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private PosicionRepository posicionRepository;
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private TableroRepository tableroRepository;

    public List<BarcoDTO> listarBarcos() {
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco  barco: barcoRepository.findAll()) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }

    public List<BarcoDTO> listarBarcos(PageRequest pageRequest) {
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco  barco: barcoRepository.findAll(pageRequest)) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }

    public BarcoDTO recuperarBarco(Long id) {
        return BarcoMapper.toDTO(barcoRepository.findById(id).orElseThrow());
    }

    public BarcoDTO crear(BarcoDTO barcoDTO) {
        System.out.println("=== DEBUG CREAR BARCO ===");
        System.out.println("JugadorId: " + barcoDTO.getJugadorId());
        System.out.println("ModeloId: " + barcoDTO.getModeloId());
        System.out.println("TableroId: " + barcoDTO.getTableroId());
        System.out.println("PosicionId: " + barcoDTO.getPosicionId());
        System.out.println("========================");
        
        Barco entity = BarcoMapper.toEntity(barcoDTO);
        entity.setId(null);
        Barco saved = barcoRepository.save(entity);
        
        System.out.println("=== BARCO CREADO ===");
        System.out.println("Barco ID: " + saved.getId());
        System.out.println("Jugador asignado: " + (saved.getJugador() != null ? saved.getJugador().getNombre() : "NULL"));
        System.out.println("====================");
        
        return BarcoMapper.toDTO(saved);
    }

    public BarcoDTO actualizarBarco(BarcoDTO barcoDTO) {
        Barco entity = barcoRepository.findById(barcoDTO.getId()).orElseThrow();

        System.out.println("=== DEBUG ACTUALIZAR BARCO ===");
        System.out.println("ID: " + barcoDTO.getId());
        System.out.println("VelocidadX: " + barcoDTO.getVelocidadX());
        System.out.println("VelocidadY: " + barcoDTO.getVelocidadY());
        System.out.println("PosicionId: " + barcoDTO.getPosicionId());
        System.out.println("ModeloId: " + barcoDTO.getModeloId());
        System.out.println("JugadorId: " + barcoDTO.getJugadorId());
        System.out.println("TableroId: " + barcoDTO.getTableroId());
        System.out.println("===============================");

        entity.setVelocidadX(barcoDTO.getVelocidadX());
        entity.setVelocidadY(barcoDTO.getVelocidadY());

        if (barcoDTO.getPosicionId() != null) {
            Posicion posicion = posicionRepository.findById(barcoDTO.getPosicionId()).orElse(null);
            entity.setPosicion(posicion);
        }

        if (barcoDTO.getModeloId() != null) {
            Modelo modelo = modeloRepository.findById(barcoDTO.getModeloId()).orElse(null);
            entity.setModelo(modelo);
        }

        if (barcoDTO.getJugadorId() != null) {
            Jugador jugador = jugadorRepository.findById(barcoDTO.getJugadorId()).orElse(null);
            entity.setJugador(jugador);
        }

        if (barcoDTO.getTableroId() != null) {
            Tablero tablero = tableroRepository.findById(barcoDTO.getTableroId()).orElse(null);
            entity.setTablero(tablero);
        }

        Barco saved = barcoRepository.save(entity);
        return BarcoMapper.toDTO(saved);
    }

    public void borrarBarco(Long barcoId) {
        barcoRepository.deleteById(barcoId);
    }

    public List<BarcoDTO> listarBarcosDisponibles() {
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco barco : barcoRepository.findAll()) {
            if (barco.getJugador() == null) {
                barcoDTOs.add(BarcoMapper.toDTO(barco));
            }
        }
        return barcoDTOs;
    }
}