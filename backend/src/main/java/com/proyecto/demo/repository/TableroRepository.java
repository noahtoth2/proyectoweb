package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Tablero;

@Repository
public interface TableroRepository extends JpaRepository<Tablero, Long> {
}
