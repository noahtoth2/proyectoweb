package com.proyecto.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.demo.dto.CrearPartidaRequest;
import com.proyecto.demo.dto.PartidaDTO;
import com.proyecto.demo.dto.SeleccionarBarcoRequest;
import com.proyecto.demo.dto.UnirsePartidaRequest;
import com.proyecto.demo.mappers.PartidaMapper;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Partida;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.PartidaRepository;

@Service
public class PartidaService {
    
    private final PartidaRepository partidaRepository;
    private final JugadorRepository jugadorRepository;
    private final PartidaMapper partidaMapper;
    
    public PartidaService(PartidaRepository partidaRepository, 
                         JugadorRepository jugadorRepository,
                         PartidaMapper partidaMapper) {
        this.partidaRepository = partidaRepository;
        this.jugadorRepository = jugadorRepository;
        this.partidaMapper = partidaMapper;
    }
    
    @Transactional
    public PartidaDTO crearPartida(CrearPartidaRequest request) {
        // Crear jugador creador
        Jugador creador = new Jugador(request.getNombreJugador());
        
        // Crear partida
        Partida partida = new Partida();
        partida.setNombre(request.getNombre());
        partida.setCodigo(generarCodigo());
        partida.setMaxJugadores(request.getMaxJugadores() != null ? request.getMaxJugadores() : 4);
        partida.setCreador(creador);
        
        // Guardar partida primero
        partida = partidaRepository.save(partida);
        
        // Asignar partida al jugador y guardar
        creador.setPartida(partida);
        creador = jugadorRepository.save(creador);
        
        // Actualizar creador en partida
        partida.setCreador(creador);
        partida.getJugadores().add(creador);
        partida = partidaRepository.save(partida);
        
        return partidaMapper.toDTO(partida);
    }
    
    @Transactional
    public PartidaDTO unirsePartida(UnirsePartidaRequest request) {
        Partida partida = partidaRepository.findByCodigo(request.getCodigo())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (partida.getIniciada()) {
            throw new RuntimeException("La partida ya ha iniciado");
        }
        
        if (partida.getFinalizada()) {
            throw new RuntimeException("La partida ha finalizado");
        }
        
        if (partida.getJugadores().size() >= partida.getMaxJugadores()) {
            throw new RuntimeException("La partida está llena");
        }
        
        // Crear nuevo jugador
        Jugador jugador = new Jugador(request.getNombreJugador());
        jugador.setPartida(partida);
        jugador = jugadorRepository.save(jugador);
        
        partida.getJugadores().add(jugador);
        partida = partidaRepository.save(partida);
        
        return partidaMapper.toDTO(partida);
    }
    
    @Transactional
    public PartidaDTO iniciarPartida(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (partida.getIniciada()) {
            throw new RuntimeException("La partida ya ha iniciado");
        }
        
        if (partida.getJugadores().size() < 2) {
            throw new RuntimeException("Se necesitan al menos 2 jugadores");
        }
        
        // ⭐ Validar que todos los jugadores hayan seleccionado un barco
        for (Jugador jugador : partida.getJugadores()) {
            if (jugador.getBarcoSeleccionadoId() == null) {
                throw new RuntimeException("Todos los jugadores deben seleccionar un barco antes de iniciar");
            }
        }
        
        partida.setIniciada(true);
        partida.setFechaInicio(LocalDateTime.now());
        
        // Establecer el primer turno
        if (!partida.getJugadores().isEmpty()) {
            partida.setTurnoActual(partida.getJugadores().get(0));
        }
        
        partida = partidaRepository.save(partida);
        return partidaMapper.toDTO(partida);
    }
    
    @Transactional
    public PartidaDTO seleccionarBarco(Long partidaId, SeleccionarBarcoRequest request) {
        Partida partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (partida.getIniciada()) {
            throw new RuntimeException("No se puede seleccionar barco después de iniciar la partida");
        }
        
        Jugador jugador = jugadorRepository.findById(request.getJugadorId())
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        // Validar que el jugador pertenece a esta partida
        if (!partida.getJugadores().contains(jugador)) {
            throw new RuntimeException("El jugador no pertenece a esta partida");
        }
        
        // Validar que el barco no esté ocupado por otro jugador
        for (Jugador j : partida.getJugadores()) {
            if (j.getBarcoSeleccionadoId() != null && 
                j.getBarcoSeleccionadoId().equals(request.getBarcoId()) &&
                !j.getId().equals(jugador.getId())) {
                throw new RuntimeException("Este barco ya fue seleccionado por otro jugador");
            }
        }
        
        // Asignar el barco al jugador
        jugador.setBarcoSeleccionadoId(request.getBarcoId());
        jugadorRepository.save(jugador);
        
        // Refrescar partida y retornar
        partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        return partidaMapper.toDTO(partida);
    }
    
    @Transactional
    public PartidaDTO siguienteTurno(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (!partida.getIniciada() || partida.getFinalizada()) {
            throw new RuntimeException("La partida no está en curso");
        }
        
        List<Jugador> jugadores = partida.getJugadores();
        int indexActual = jugadores.indexOf(partida.getTurnoActual());
        int siguienteIndex = (indexActual + 1) % jugadores.size();
        
        partida.setTurnoActual(jugadores.get(siguienteIndex));
        partida = partidaRepository.save(partida);
        
        return partidaMapper.toDTO(partida);
    }
    
    @Transactional
    public PartidaDTO finalizarPartida(Long partidaId, Long ganadorId) {
        Partida partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        Jugador ganador = jugadorRepository.findById(ganadorId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        partida.setFinalizada(true);
        partida.setFechaFin(LocalDateTime.now());
        partida.setGanador(ganador);
        
        partida = partidaRepository.save(partida);
        return partidaMapper.toDTO(partida);
    }
    
    public PartidaDTO obtenerPartida(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        return partidaMapper.toDTO(partida);
    }
    
    public PartidaDTO obtenerPartidaPorCodigo(String codigo) {
        Partida partida = partidaRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        return partidaMapper.toDTO(partida);
    }
    
    public List<PartidaDTO> listarPartidasActivas() {
        return partidaRepository.findByFinalizadaFalseOrderByFechaCreacionDesc()
            .stream()
            .map(partidaMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    private String generarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        // Verificar que no exista
        if (partidaRepository.findByCodigo(codigo.toString()).isPresent()) {
            return generarCodigo(); // Recursivo si ya existe
        }
        
        return codigo.toString();
    }
}
