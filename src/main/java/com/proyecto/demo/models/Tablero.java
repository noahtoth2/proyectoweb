import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tablero {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;

    @OneToMany(mappedBy = "tablero")
    private List<Barco> barcos= new ArrayList<>();

    @OneToMany(mappedBy = "tablero")
    private List<Celda> celdas= new ArrayList<>();


    public Tablero() {
    }

    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }
}