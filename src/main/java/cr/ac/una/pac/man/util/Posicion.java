package cr.ac.una.pac.man.util;

public class Posicion {
    public int fila;
    public int columna;
    public int distancia;

        public Posicion(int fila, int columna, int distancia) {
            this.fila = fila;
            this.columna = columna;
            this.distancia = distancia;
        }

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
    
}
