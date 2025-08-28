package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

}