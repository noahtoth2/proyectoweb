import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.proyecto.demo.dto.JugadorDTO;
import com.edu.proyecto.demo.mappers.JugadorMapper;
import com.edu.proyecto.demo.models.Jugador;
import com.edu.proyecto.demo.repository.JugadorRepository;

@Service
public class JugadorService {
    @Autowired
    private JugadorRepository jugadorRepository;

    public List<JugadorDTO> listarJugadores() {
        List<JugadorDTO> jugadorDTOs = new ArrayList<>();
        for (Jugador jugador : jugadorRepository.findAll()) {
            jugadorDTOs.add(JugadorMapper.toDTO(jugador));
        }
        return jugadorDTOs;
    }

    public JugadorDTO recuperarJugador(Long id) {
        return JugadorMapper.toDTO(jugadorRepository.findById(id).orElseThrow());
    }

    public void crear(JugadorDTO jugadorDTO) {
        Jugador entity = JugadorMapper.toEntity(jugadorDTO);
        entity.setId(null);
        jugadorRepository.save(entity);
    }

    public void actualizarJugador(JugadorDTO jugadorDTO) {
        Jugador entity = JugadorMapper.toEntity(jugadorDTO);
        // TODO: Chequear que el id sea != null
        jugadorRepository.save(entity);
    }

    public void borrarJugador(Long jugadorId) {
        jugadorRepository.deleteById(jugadorId);
    }
}