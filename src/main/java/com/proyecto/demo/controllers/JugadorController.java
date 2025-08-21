@Controller
public class BarcoController {
    @Autowired
    private BarcoService barcoService;

    @GetMapping("/barcos")
    public String listarBarcos(Model model) {
        List<Barco> barcos = barcoService.listarBarcos();
        model.addAttribute("barcos", barcos);
        return "lista_barcos"; // Nombre de la vista (HTML) para mostrar la lista de barcos
    }
}