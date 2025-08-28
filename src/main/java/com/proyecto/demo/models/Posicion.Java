import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Posicion {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;

    private int x;
    private int y;

    @OneToOne
    private Barco barco = new Barco;

    public Posicion(){
    }

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Barco getBarco(){
        return barco;
    }

    public void setBarco(Barco barco){
        this.barco = barco;
    }

}