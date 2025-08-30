package com.proyecto.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Celda;
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.models.Posicion;
import com.proyecto.demo.models.Tablero;

import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.CeldaRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
import com.proyecto.demo.repository.PosicionRepository;
import com.proyecto.demo.repository.TableroRepository;

@Component
public class DbInitializer implements CommandLineRunne{

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
    public void run(String... args) throws Exception{

        Modelo modelo1 = modeloRepository.save(
                       new Modelo("buque", "verde"));

        Modelo modelo2 = modeloRepository.save(
                       new Modelo("crucero", "blanco"));



        Jugador jugador1 = jugadorRepository.save(
                         new Jugador("Juan"));

        Jugador jugador2 = jugadorRepository.save(
                         new Jugador("Martin"));

        Jugador jugador3 = jugadorRepository.save(
                         new Jugador("Laura"));

        Jugador jugador4 = jugadorRepository.save(
                         new Jugador("Julian"));

        Jugador jugador5 = jugadorRepository.save(
                         new Jugador("Danna"));

        Jugador jugador6 = jugadorRepository.save(
                         new Jugador("Camilo"));



        Posicion posicionini = posicionRepository.save(
                         new Posicion("0","0"));


        Barco barco1 = barcoRepository.save(
                       new Barco("10"));
        
        Barco barco2 = barcoRepository.save(
                       new Barco("3.5"));

        Barco barco3 = barcoRepository.save(
                       new Barco("4.8"));

        Barco barco4 = barcoRepository.save(
                       new Barco("8.7"));

        Barco barco5 = barcoRepository.save(
                       new Barco("6.6"));
        
    }

       //Barco1
       barco1.setJugador(jugador1);
       barco1.setPosicion(posicionini);
       barco1.setModelo(modelo1);

       



}


