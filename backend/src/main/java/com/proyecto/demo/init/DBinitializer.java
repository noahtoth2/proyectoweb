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

        List<Modelo> modelos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Modelo modelo = new Modelo("Modelo " + i, generarColorAleatorio());
            modeloRepository.save(modelo);
            modelos.add(modelo);
        }

        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Jugador jugador = new Jugador("Jugador " + i);
            jugadorRepository.save(jugador);
            jugadores.add(jugador);
        }

        int barcoCount = 0;
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < 10; i++) {
                double velocidad = 5 + (15 - 5) * random.nextDouble();

                Barco barco = new Barco(velocidad);

                barco.setJugador(jugador);

                Modelo modeloRandom = modelos.get(random.nextInt(modelos.size()));
                barco.setModelo(modeloRandom);

                Posicion posicion = new Posicion(barcoCount, barcoCount);
                posicionRepository.save(posicion);
                barco.setPosicion(posicion);

                barcoRepository.save(barco);

                jugador.getBarcos().add(barco);
                barcoCount++;
            }
            jugadorRepository.save(jugador);
        }
    }

    private String generarColorAleatorio() {
        String[] colores = {
            "Rojo", "Verde", "Azul", "Blanco", "Negro",
            "Amarillo", "Gris", "Naranja", "Rosa", "Marrón"
        };
        return colores[new Random().nextInt(colores.length)];
    }
}
