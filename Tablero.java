public class Tablero {
    private int id;
    private Barco barco;
    private int[][] tablero;

    public Tablero(Barco barco, int filas, int columnas) {
        this.barco = barco;
        this.tablero = new int[filas][columnas];
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