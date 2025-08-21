import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OnetoOne;
import jakarta.persistence.OnetoMany;

@Entity
public class Jugador{

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;


    private String nombre;
    
    @OnetoMany(mappedBy = "jugador")
    private List<Barco> barcos = new ArrayList<>();
    
    public Jugador(){
    }


    public Jugador(String nombre) {
        this.nombre = nombre;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Barco> getBarcos(){
        return barcos;
    }

    
}