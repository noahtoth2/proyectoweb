import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.Barco;

@Controller
public class BarcoController {
    @Autowired
    private BarcoService barcoService;

   // http://localhost:8080/person/list
    // @RequestMapping(value="/person/list", method=RequestMethod.GET)
    @GetMapping("/listbarcos")
    public ModelAndView listarBarcos() {
        log.info("Recibi peticion de listar barco);
        ModelAndView modelAndView = new ModelAndView("barco-list");
        List<BarcoDTO> barcos = BarcoService.listarBarcos();
        modelAndView.addObject("listadoBarcos", barcos);
        return modelAndView;
    }

}