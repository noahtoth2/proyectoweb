

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OnetoOne;

@Entity
public class Barco {

    @id
    @GeneratedValue(strategy=GenerationType.Auto)
    private long id;

    private double velocidad;
    private Posicion posicion;


    @OnetoOne
    private Modelo modelo = new Modelo;
    @OnetoOne
    private Jugador jugador = new Jugador;

    public Barco() {
    }

    public Barco(double velocidad, Posicion posicion) {
        this.velocidad = velocidad;
        this.posicion = posicion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPosicion(Posicion posicion) {
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