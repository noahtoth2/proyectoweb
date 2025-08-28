package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Modelo;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

}