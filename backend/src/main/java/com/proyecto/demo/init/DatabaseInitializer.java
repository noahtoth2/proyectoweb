package com.proyecto.demo.init;

import java.util.ArrayList;
import java.util.List;

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
public class DatabaseInitializer implements CommandLineRunner {

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
        
        // Limpiar base de datos si es necesario
        barcoRepository.deleteAll();
        posicionRepository.deleteAll();
        celdaRepository.deleteAll();
        tableroRepository.deleteAll();
        jugadorRepository.deleteAll();
        modeloRepository.deleteAll();

        // 1. Crear modelos de barcos
        List<Modelo> modelos = new ArrayList<>();
        Modelo modeloVelero = new Modelo("Velero Clásico", "#3498db");
        Modelo modeloYate = new Modelo("Yate de Carreras", "#e74c3c");
        Modelo modeloCatamaran = new Modelo("Catamarán", "#2ecc71");
        
        modeloRepository.save(modeloVelero);
        modeloRepository.save(modeloYate);
        modeloRepository.save(modeloCatamaran);
        
        modelos.add(modeloVelero);
        modelos.add(modeloYate);
        modelos.add(modeloCatamaran);

        // 2. Crear jugadores
        List<Jugador> jugadores = new ArrayList<>();
        Jugador jugador1 = new Jugador("Capitán Navegante");
        Jugador jugador2 = new Jugador("Marinero Experto");
        Jugador jugador3 = new Jugador("Piloto Veterano");
        
        jugadorRepository.save(jugador1);
        jugadorRepository.save(jugador2);
        jugadorRepository.save(jugador3);
        
        jugadores.add(jugador1);
        jugadores.add(jugador2);
        jugadores.add(jugador3);

        // 3. Crear tablero principal
        Tablero tablero = new Tablero();
        tableroRepository.save(tablero);

        // 4. Crear mapa según la imagen (21x21 como aparece en el documento)
        createMapFromImage(tablero);

        // 5. Crear posiciones iniciales para barcos
        List<Posicion> posicionesIniciales = createInitialPositions();

        // 6. Crear barcos con velocidad vectorial
        createInitialBoats(modelos, jugadores, tablero, posicionesIniciales);

        System.out.println("✅ Base de datos inicializada con:");
        System.out.println("   - " + modelos.size() + " modelos de barcos");
        System.out.println("   - " + jugadores.size() + " jugadores");
        System.out.println("   - 1 tablero con mapa según especificación");
        System.out.println("   - " + barcoRepository.count() + " barcos de prueba");
        System.out.println("   - Sistema de velocidad vectorial implementado");
    }

    private void createMapFromImage(Tablero tablero) {
        // Crear mapa de 21x21 según la imagen del documento
        for (int y = 0; y < 21; y++) {
            for (int x = 0; x < 21; x++) {
                char tipoCelda = determineCellType(x, y);
                Celda celda = new Celda(tipoCelda, x, y);
                celda.setTablero(tablero);
                celdaRepository.save(celda);
            }
        }
    }

    private char determineCellType(int x, int y) {
        // Crear mapa en forma de H (21x21)
        // Reglas: A=Agua, X=Pared, P=Partida, M=Meta
        
        // Forma de H:
        // - Brazos verticales izquierdo y derecho (agua)
        // - Puente horizontal en el medio (agua)
        // - Resto son paredes
        
        // Brazo izquierdo de la H (columnas 0-7)
        if (x >= 0 && x <= 7) {
            // Posición de partida en la esquina superior izquierda
            if (y == 1 && x == 3) {
                return 'P';
            }
            return 'A'; // Agua navegable
        }
        
        // Brazo derecho de la H (columnas 13-20)
        if (x >= 13 && x <= 20) {
            // Posición de partida en la esquina superior derecha
            if (y == 1 && x == 17) {
                return 'P';
            }
            return 'A'; // Agua navegable
        }
        
        // Puente horizontal de la H (filas 9-11, centro)
        if (y >= 9 && y <= 11) {
            // Meta en el centro del puente
            if (y == 10 && x == 10) {
                return 'M';
            }
            return 'A'; // Agua navegable
        }
        
        // Todo lo demás son paredes
        return 'X';
    }

    private List<Posicion> createInitialPositions() {
        List<Posicion> posiciones = new ArrayList<>();
        
        // Posiciones de partida (P) en el mapa en forma de H
        Posicion pos1 = new Posicion(3, 1);   // Brazo izquierdo de la H
        Posicion pos2 = new Posicion(17, 1);  // Brazo derecho de la H
        Posicion pos3 = new Posicion(4, 2);   // Cerca del brazo izquierdo (agua)
        
        posicionRepository.save(pos1);
        posicionRepository.save(pos2);
        posicionRepository.save(pos3);
        
        posiciones.add(pos1);
        posiciones.add(pos2);
        posiciones.add(pos3);
        
        return posiciones;
    }

    private void createInitialBoats(List<Modelo> modelos, List<Jugador> jugadores, 
                                   Tablero tablero, List<Posicion> posiciones) {
        
        // Barco 1: En brazo izquierdo, velocidad hacia el centro y abajo
        Barco barco1 = new Barco(1.0, 1.0); // vx=1 (hacia derecha), vy=1 (hacia abajo)
        barco1.setModelo(modelos.get(0));
        barco1.setJugador(jugadores.get(0));
        barco1.setTablero(tablero);
        barco1.setPosicion(posiciones.get(0));
        barcoRepository.save(barco1);

        // Barco 2: En brazo derecho, velocidad hacia el centro y abajo
        Barco barco2 = new Barco(-1.0, 1.0); // vx=-1 (hacia izquierda), vy=1 (hacia abajo)
        barco2.setModelo(modelos.get(1));
        barco2.setJugador(jugadores.get(1));
        barco2.setTablero(tablero);
        barco2.setPosicion(posiciones.get(1));
        barcoRepository.save(barco2);

        // Barco 3: En brazo izquierdo, velocidad detenido
        Barco barco3 = new Barco(0.0, 0.0); // vx=0, vy=0 (detenido)
        barco3.setModelo(modelos.get(2));
        barco3.setJugador(jugadores.get(2));
        barco3.setTablero(tablero);
        barco3.setPosicion(posiciones.get(2));
        barcoRepository.save(barco3);
    }
}