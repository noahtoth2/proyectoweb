package com.proyecto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.demo.models.Barco;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {
    // Aquí puedes definir métodos personalizados si es necesario
}
