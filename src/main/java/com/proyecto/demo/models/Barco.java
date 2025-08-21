



@Entity
public class Barco {

    @id
    @GeneratedValue(strategy=GenerationType.Auto)
    private long id;

    private double velocidad;
    private Posicion posicion;
    private Jugador jugador;
    private Modelo modelo;

    public Barco(double velocidad, Posicion posicion, Jugador jugador, Modelo modelo) {
        this.velocidad = velocidad;
        this.posicion = posicion;
        this.jugador = jugador;
        this.modelo = modelo;
    }

    public Barco(double velocidad, Posicion posicion, Jugador jugador, Modelo modelo) {
        this.velocidad = velocidad;
        this.posicion = posicion;
        this.jugador = jugador;
        this.modelo = modelo;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public double getPosicion() {
        return posicion;
    }

    public void setPosicion(double posicion) {
        this.posicion = posicion;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public  getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
}