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
import com.proyecto.demo.models.Tablero;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.CeldaRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;
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
    private TableroRepository tableroRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // Limpiar base de datos si es necesario
        barcoRepository.deleteAll();
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

        // 4. Crear mapa según la imagen 
        createMapFromImage(tablero);

        // 5. Crear barcos SIN posición inicial (se colocan durante el juego)
        createInitialBoats(modelos);

        System.out.println("Base de datos inicializada con:");
        System.out.println("   - " + modelos.size() + " modelos de barcos");
        System.out.println("   - " + jugadores.size() + " jugadores");
        System.out.println("   - 1 tablero con mapa según especificación");
        System.out.println("   - " + barcoRepository.count() + " barcos disponibles (sin posición inicial)");
        System.out.println("   - Los barcos se colocarán cuando los jugadores los seleccionen");
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

    private void createInitialBoats(List<Modelo> modelos) {
        // Crear barcos disponibles SIN posición inicial ni jugador asignado
        // Los jugadores los seleccionarán y colocarán durante el juego
        
        // Crear varios barcos de cada modelo para que los jugadores puedan elegir
        for (Modelo modelo : modelos) {
            // Crear 3 barcos de cada modelo
            for (int i = 0; i < 3; i++) {
                Barco barco = new Barco(0.0, 0.0); // Velocidad inicial en 0
                barco.setModelo(modelo);
                // NO asignar jugador, posición ni tablero
                // Estos se asignarán cuando el jugador seleccione el barco
                barcoRepository.save(barco);
            }
        }
    }
}