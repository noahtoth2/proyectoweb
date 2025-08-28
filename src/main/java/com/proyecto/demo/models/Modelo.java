import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Modelo {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;
    

    private String nombre;
    private String color;

    @OneToMany(mappedBy = "modelo")
    private List<Barco> barcos= new ArrayList<>();


    public Modelo(){
    }

    public Modelo(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Barco> getBarcos(){
        return barcos;
    }
}