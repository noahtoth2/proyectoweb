import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.proyecto.demo.dto.ModeloDTO;
import com.edu.proyecto.demo.mappers.ModeloMapper;
import com.edu.proyecto.demo.models.Modelo;
import com.edu.proyecto.demo.repository.ModeloRepository;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    public List<ModeloDTO> listarModelos() {
        List<ModeloDTO> modeloDTOs = new ArrayList<>();
        for (Modelo modelo : modeloRepository.findAll()) {
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



