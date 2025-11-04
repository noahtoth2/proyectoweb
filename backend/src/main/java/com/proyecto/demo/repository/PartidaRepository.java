package com.proyecto.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    Optional<Partida> findByCodigo(String codigo);
    List<Partida> findByFinalizadaFalseOrderByFechaCreacionDesc();
}
