public class Jugador {
    private int id;
    private String nombre;
    

    public Jugador(String nombre, int edad, String posicion) {
        this.nombre = nombre;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}