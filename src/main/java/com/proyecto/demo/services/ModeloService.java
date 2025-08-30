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
            modeloDTOs.add(ModeloMapper.toDTO(modelo));
        }
        return modeloDTOs;
    }

    public ModeloDTO recuperarJugador(Long id) {
        return ModeloMapper.toDTO(ModeloRepository.findById(id).orElseThrow());
    }

    public void crear(ModeloDTO modeloDTO) {
        Modelo entity = ModeloMapper.toEntity(ModeloDTO);
        entity.setId(null);
        modeloRepository.save(entity);
    }

    public void actualizarModelo(ModeloDTO modeloDTO) {
        Modelo entity = ModeloMapper.toEntity(modeloDTO);
        // TODO: Chequear que el id sea != null
        modeloRepository.save(entity);
    }

    public void borrarModelo(Long modeloId) {
        modeloRepository.deleteById(modeloId);
    }
}



