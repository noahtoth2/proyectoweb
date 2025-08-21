import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OnetoOne;


@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private long id;


    private String nombre;
    
    @OnetoOne
    Barco barco = new Barco;
    
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

   public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Barco getBarco(){
        return barco;
    }

    public void serBarco(Barco barco){
        this.barco = barco;
    }
}