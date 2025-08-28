import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Celda {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;

    private char tipocelda;
    private int x;
    private int y;

    @OneToOne
    private Tablero tablero = new Tablero;



    public Celda() {
    }

    public Celda(char tipocelda, int x, int y){
        this.tipocelda = tipocelda;
        this.x = x;
        this.y = y;
    }

    



}