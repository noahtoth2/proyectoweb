import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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