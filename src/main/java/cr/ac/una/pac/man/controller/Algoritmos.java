
package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.Posicion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Algoritmos {

    public Algoritmos() {
    }

    public List<Posicion> Dijsktra(String[][] matriz, Posicion inicio, Posicion objetivo) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        String ObstaculoCaja = "#";
        int[] FilasAlrededor = {-1, 0, 1, 0};
        int[] ColumbasAlrededor = {0, 1, 0, -1};

        int[][] distancias = new int[filas][columnas];
        for (int[] row : distancias) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distancias[inicio.fila][inicio.columna] = 0;

        Posicion[][] PosicionesAnteriores = new Posicion[filas][columnas];
        PriorityQueue<Posicion> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(p -> p.distancia));
        colaPrioridad.offer(new Posicion(inicio.fila, inicio.columna, 0));

        while (!colaPrioridad.isEmpty()) {
            Posicion actual = colaPrioridad.poll();

            if (actual.fila == objetivo.fila && actual.columna == objetivo.columna) {
                return construirRuta(PosicionesAnteriores, inicio, objetivo);
            }
            for (int j = 0; j < 4; j++) {
                int nuevaFila = actual.fila + FilasAlrededor[j];
                int nuevaColumna = actual.columna + ColumbasAlrededor[j];
                if (esPosicionValida(nuevaFila, nuevaColumna, filas, columnas)
                        && !matriz[nuevaFila][nuevaColumna].equals(ObstaculoCaja)
                        && distancias[nuevaFila][nuevaColumna] > actual.distancia + 1) {
                    colaPrioridad.offer(new Posicion(nuevaFila, nuevaColumna, actual.distancia + 1));
                    distancias[nuevaFila][nuevaColumna] = actual.distancia + 1;
                    PosicionesAnteriores[nuevaFila][nuevaColumna] = actual;
                }
            }
        }
        return new ArrayList<>();
    }

    /*public List<Posicion> DijsktraContrario(String[][] matriz, Posicion inicio, Posicion objetivo) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        String ObstaculoCaja = "#";
        int[] FilasAlrededor = {-1, 0, 1, 0};
        int[] ColumbasAlrededor = {0, 1, 0, -1};

        int[][] distancias = new int[filas][columnas];
        for (int[] row : distancias) {
            Arrays.fill(row, Integer.MIN_VALUE);
        }
        distancias[inicio.fila][inicio.columna] = 0;

        Posicion[][] PosicionesAnteriores = new Posicion[filas][columnas];
        PriorityQueue<Posicion> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(p -> -p.distancia));
        colaPrioridad.offer(new Posicion(inicio.fila, inicio.columna, 0));

        while (!colaPrioridad.isEmpty()) {
            Posicion actual = colaPrioridad.poll();

            if (actual.fila == objetivo.fila && actual.columna == objetivo.columna) {
                return construirRuta(PosicionesAnteriores, inicio, objetivo);
            }
            for (int j = 0; j < 4; j++) {
                int nuevaFila = actual.fila + FilasAlrededor[j];
                int nuevaColumna = actual.columna + ColumbasAlrededor[j];
                if (esPosicionValida(nuevaFila, nuevaColumna, filas, columnas)
                        && !matriz[nuevaFila][nuevaColumna].equals(ObstaculoCaja)
                        && distancias[nuevaFila][nuevaColumna] < actual.distancia + 1) {
                    colaPrioridad.offer(new Posicion(nuevaFila, nuevaColumna, actual.distancia + 1));
                    distancias[nuevaFila][nuevaColumna] = actual.distancia + 1;
                    PosicionesAnteriores[nuevaFila][nuevaColumna] = actual;
                }
            }
        }
        return new ArrayList<>();
    }*/
    
    private List<Posicion> construirRuta(Posicion[][] PosicionRealizada, Posicion inicio, Posicion objetivo) {
        List<Posicion> ruta = new ArrayList<>();
        Posicion actual = objetivo;
        while (actual != null) {
            ruta.add(0, actual);
            if (actual.equals(inicio)) {
                break;
            }
            actual = PosicionRealizada[actual.fila][actual.columna];
        }
        return ruta;
    }
    
    
    public List<Posicion> BFS(String[][] matriz, Posicion inicio, Posicion objetivo) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        String ObstaculoCaja = "#";
        int[] FilasAlrededor = {-1, 0, 1, 0};
        int[] ColumbasAlrededor = {0, 1, 0, -1};

        boolean[][] PaseoMiedo = new boolean[filas][columnas];
        Posicion[][] PosicionesAnteriores = new Posicion[filas][columnas];
        Queue<Posicion> cola = new LinkedList<>();
        cola.offer(inicio);

        while (!cola.isEmpty()) {
            Posicion actual = cola.poll();

            if (actual.fila == objetivo.fila && actual.columna == objetivo.columna) {
                return construirRuta(PosicionesAnteriores, inicio, objetivo);
            }
            for (int j = 0; j < 4; j++) {
                int nuevaFila = actual.fila + FilasAlrededor[j];
                int nuevaColumna = actual.columna + ColumbasAlrededor[j];
                if (esPosicionValida(nuevaFila, nuevaColumna, filas, columnas) && !PaseoMiedo[nuevaFila][nuevaColumna]
                        && !matriz[nuevaFila][nuevaColumna].equals(ObstaculoCaja)) {
                    cola.offer(new Posicion(nuevaFila, nuevaColumna));
                    PaseoMiedo[nuevaFila][nuevaColumna] = true;
                    PosicionesAnteriores[nuevaFila][nuevaColumna] = actual;

                }
            }
        }
        return new ArrayList<>();
    }
    
    public List<Posicion> Floyd(String[][] matriz, Posicion inicio, Posicion objetivo) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        int n = filas * columnas;
        String ObstaculoCaja = "#";

        int[][] distancias = new int[n][n];
        int[][] predecesor = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(distancias[i], Integer.MAX_VALUE);
            Arrays.fill(predecesor[i], -1);  // -1 indica que no hay predecesor
            distancias[i][i] = 0;
        }

        int[] FilasAlrededor = {-1, 0, 1, 0};
        int[] ColumbasAlrededor = {0, 1, 0, -1};

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!matriz[i][j].equals(ObstaculoCaja)) {
                    for (int k = 0; k < 4; k++) {
                        int nuevaFila = i + FilasAlrededor[k];
                        int nuevaColumna = j + ColumbasAlrededor[k];
                        if (esPosicionValida(nuevaFila, nuevaColumna, filas, columnas)
                                && !matriz[nuevaFila][nuevaColumna].equals(ObstaculoCaja)) {
                            distancias[i * columnas + j][nuevaFila * columnas + nuevaColumna] = 1;
                            predecesor[i * columnas + j][nuevaFila * columnas + nuevaColumna] = i * columnas + j;
                        }
                    }
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] != Integer.MAX_VALUE && distancias[k][j] != Integer.MAX_VALUE
                            && distancias[i][j] > distancias[i][k] + distancias[k][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        predecesor[i][j] = predecesor[k][j];
                    }
                }
            }
        }

        int from = inicio.fila * columnas + inicio.columna;
        int to = objetivo.fila * columnas + objetivo.columna;

        if (distancias[from][to] == Integer.MAX_VALUE) {
            return new ArrayList<>(); // No hay camino
        }

        List<Posicion> ruta = new ArrayList<>();
        while (to != from) {
            ruta.add(new Posicion(to / columnas, to % columnas));
            to = predecesor[from][to];
        }
        ruta.add(inicio);
        Collections.reverse(ruta);

        return ruta;
    }
    
        private boolean esPosicionValida(int fila, int columna, int filas, int columnas) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
    
}
