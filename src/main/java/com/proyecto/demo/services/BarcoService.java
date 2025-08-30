import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.mappers.BarcoMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.repository.BarcoRepository;

@Service
public class BarcoService {
    @Autowired
    private BarcoRepository barcoRepository;

    public List<BarcoDTO> listarBarcos() {
        // TODO Encapsular esto en el PersonMapper
        List<BarcoDTO> barcoDTOs = new ArrayList<>();
        for (Barco  barco: BarcoRepository.findAll()) {
            barcoDTOs.add(BarcoMapper.toDTO(barco));
        }
        return barcoDTOs;
    }

    public PersonDTO recuperarBarco(Long id) {
        return BarcoMapper.toDTO(barcoRepository.findById(id).orElseThrow());
    }

    public void crear(BarcoDTO barcoDTO) {
        Barco entity = BarcoMapper.toEntity(barcoDTO);
        entity.setId(null);
        BarcoRepository.save(entity);
    }

    public void actualizarBarco(BarcoDTO barcoDTO) {
        Barco entity = BarcoMapper.toEntity(barcoDTO);
        // TODO Chequear que el id sea != null
        BarcoRepository.save(entity);
    }

    public void borrarBarco(Long barcoId) {
        BarcoRepository.deleteById(barcoId);
    }
}