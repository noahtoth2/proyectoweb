package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Posicion;

@Repository
public interface PosicionRepository extends JpaRepository<Posicion, Long> {
}
