package com.proyecto.demo.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.CeldaSimpleDTO;
import com.proyecto.demo.dto.TableroDTO;
import com.proyecto.demo.mappers.TableroMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Service
public class TableroService {
    @Autowired
    private TableroRepository tableroRepository;
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private PosicionRepository posicionRepository;

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

    // MÉTODOS DE LÓGICA DEL JUEGO

    /**
     * Cambia la velocidad de un barco específico
     */
    public boolean cambiarVelocidadBarco(Long tableroId, Long barcoId, double deltaVx, double deltaVy) {
        Tablero tablero = tableroRepository.findById(tableroId).orElseThrow();
        Barco barco = barcoRepository.findById(barcoId).orElseThrow();
        
        // Verificar que el barco pertenece al tablero
        if (!barco.getTablero().getId().equals(tableroId)) {
            throw new IllegalArgumentException("El barco no pertenece al tablero especificado");
        }
        
        boolean resultado = tablero.cambiarVelocidad(barco, deltaVx, deltaVy);
        if (resultado) {
            barcoRepository.save(barco); // Guardar los cambios en la base de datos
        }
        return resultado;
    }

    /**
     * Mueve un barco aplicando las reglas del juego
     */
    public Tablero.ResultadoMovimiento moverBarco(Long tableroId, Long barcoId) {
        Tablero tablero = tableroRepository.findById(tableroId).orElseThrow();
        Barco barco = barcoRepository.findById(barcoId).orElseThrow();
        
        // Verificar que el barco pertenece al tablero
        if (!barco.getTablero().getId().equals(tableroId)) {
            throw new IllegalArgumentException("El barco no pertenece al tablero especificado");
        }
        
        Tablero.ResultadoMovimiento resultado = tablero.moverBarco(barco);
        
        if (resultado.isExitoso()) {
            // Guardar la nueva posición en la base de datos
            posicionRepository.save(barco.getPosicion());
            barcoRepository.save(barco);
        }
        
        return resultado;
    }

    /**
     * Calcula la posición futura de un barco sin moverlo
     */
    public Posicion calcularPosicionFutura(Long tableroId, Long barcoId) {
        Tablero tablero = tableroRepository.findById(tableroId).orElseThrow();
        Barco barco = barcoRepository.findById(barcoId).orElseThrow();
        
        // Verificar que el barco pertenece al tablero
        if (!barco.getTablero().getId().equals(tableroId)) {
            throw new IllegalArgumentException("El barco no pertenece al tablero especificado");
        }
        
        return tablero.calcularNuevaPosicion(barco);
    }

    /**
     * Obtiene todas las celdas de un tablero
     */
    public List<CeldaSimpleDTO> getCeldas(Long tableroId) {
        Tablero tablero = tableroRepository.findById(tableroId).orElseThrow();
        return tablero.getCeldas().stream()
            .map(celda -> new CeldaSimpleDTO(
                celda.getId(),
                celda.getTipoCelda(),
                celda.getY(), // fila
                celda.getX()  // columna
            ))
            .collect(java.util.stream.Collectors.toList());
    }
}
