import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class JugadorService {
    @Autowired
    private JugadorRepository jugadorRepository;

    // Crear
    public Jugador crearJugador(String nombre) {
        Jugador jugador = new Jugador(nombre, 0, "");
        jugador.setId(nextId++);
        jugadores.add(jugador);
        return jugador;
    }

    // Leer (obtener todos)
    public List<Jugador> obtenerJugadores() {
        return jugadores;
    }

    // Leer (obtener por id)
    public Jugador obtenerJugadorPorId(int id) {
        Optional<Jugador> jugador = jugadores.stream()
            .filter(j -> j.getId() == id)
            .findFirst();
        return jugador.orElse(null);
    }

    // Actualizar
    public boolean actualizarJugador(int id, String nuevoNombre) {
        Jugador jugador = obtenerJugadorPorId(id);
        if (jugador != null) {
            jugador.setNombre(nuevoNombre);
            return true;
        }
        return false;
    }

    // Eliminar
    public boolean eliminarJugador(int id) {
        return jugadores.removeIf(j -> j.getId() == id);
    }
}