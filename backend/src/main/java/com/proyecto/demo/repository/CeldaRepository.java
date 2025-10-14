package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Celda;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, Long> {
}
