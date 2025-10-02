package com.proyecto.demo.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.CeldaRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Component
public class DBinitializer implements CommandLineRunner {

    @Autowired
    private BarcoRepository barcoRepository;

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private PosicionRepository posicionRepository;

    @Autowired
    private TableroRepository tableroRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        // Crear 10 modelos
        List<Modelo> modelos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Modelo modelo = new Modelo("Modelo " + i, generarColorAleatorio());
            modeloRepository.save(modelo);
            modelos.add(modelo);
        }

        // Crear 5 jugadores
        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Jugador jugador = new Jugador("Jugador " + i);
            jugadorRepository.save(jugador);
            jugadores.add(jugador);
        }

        // Crear 50 barcos (10 por jugador)
        int barcoCount = 0;
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < 10; i++) {
                double velocidad = 5 + (15 - 5) * random.nextDouble(); // Velocidad entre 5 y 15

                Barco barco = new Barco(velocidad);

                // Asignar jugador
                barco.setJugador(jugador);

                // Asignar modelo aleatorio
                Modelo modeloRandom = modelos.get(random.nextInt(modelos.size()));
                barco.setModelo(modeloRandom);

                // Asignar posición única (por ejemplo x=barcoCount, y=barcoCount)
                Posicion posicion = new Posicion(barcoCount, barcoCount);
                posicionRepository.save(posicion);
                barco.setPosicion(posicion);

                // Guardar barco
                barcoRepository.save(barco);

                // Añadir barco al jugador
                jugador.getBarcos().add(barco);
                barcoCount++;
            }
            jugadorRepository.save(jugador);
        }

        System.out.println("Base de datos inicializada con 5 jugadores, 10 modelos y 50 barcos.");
    }

    private String generarColorAleatorio() {
        String[] colores = {
            "Rojo", "Verde", "Azul", "Blanco", "Negro",
            "Amarillo", "Gris", "Naranja", "Rosa", "Marrón"
        };
        return colores[new Random().nextInt(colores.length)];
    }
}
