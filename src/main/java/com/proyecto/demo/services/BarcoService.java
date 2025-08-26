import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarcoService {
    @Autowired
    private BarcoRepository barcoRepository;

    // Crear
    public Barco crearBarco(BarcoDTO barco){
        Barco barco = new Barco(velocidad, posicion, jugador, modelo);
        barco.setId(nextId++);
        barcos.add(barco);
        return barco;
    }

    // Leer (obtener todos)
    public List<Barco> obtenerBarcos() {
        return barcos;
    }

    // Leer (obtener por id)
    public Barco obtenerBarcoPorId(int id) {
        Optional<Barco> barco = barcos.stream()
            .filter(b -> b.getId() == id)
            .findFirst();
        return barco.orElse(null);
    }

    // Actualizar
    public boolean actualizarBarco(int id, double nuevaVelocidad, Posicion nuevaPosicion, Jugador nuevoJugador, Modelo nuevoModelo) {
        Barco barco = obtenerBarcoPorId(id);
        if (barco != null) {
            barco.setVelocidad(nuevaVelocidad);
            barco.setPosicion(nuevaPosicion);
            barco.setJugador(nuevoJugador);
            barco.setModelo(nuevoModelo);
            return true;
        }
        return false;
    }

    // Eliminar
    public boolean eliminarBarco(int id) {
        return barcos.removeIf(b -> b.getId() == id);
    }

    public List<BarcoDTO> listarBarcos() {
        // TODO Encapsular esto en el PersonMapper
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco  barco: BarcoRepository.findAll()) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }
}