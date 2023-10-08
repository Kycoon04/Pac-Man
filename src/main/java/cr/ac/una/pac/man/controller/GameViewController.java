/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 *
 *
 * @author jomav
 */
public class GameViewController implements Initializable {

    @FXML
    private ImageView Background;
    @FXML
    private GridPane gridGame;
    private int numFila;
    private int numColumna;
    private int PJ_Columna;
    private int PJ_Fila;
    String[] numeros;
    private String[][] MatrizNumber = new String[13][13];
    private String[][] MatrizRespaldo = new String[13][13];

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Background.setImage(new Image("/cr/ac/una/pac/man/images/Nivel" + FlowController.getNivel() + ".jpg"));
        CargarNivel();
        Platform.runLater(() -> {
            Node node = Background; // Puedes usar cualquier nodo que esté en la escena
            Scene scene = node.getScene();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                MovimientoPersonaje(event);
            });
        });
    }

    public void CargarNivel() {
        if (!FlowController.getInstance().isImportar()) {
            int i = 0;
            ImageView imageView;
            StringBuilder builder = new StringBuilder();
            try {
                File file = new File("src/main/resources/cr/ac/una/pac/man/mapas/" + FlowController.getNivel() + ".txt");
                InputStream in = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line + "\n");
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String file = builder.toString();
            numeros = file.split("\\s+");
            CargarMatriz(numeros);

            numFila = gridGame.getRowCount();
            numColumna = gridGame.getColumnCount();
            Pintar(MatrizNumber);

        }
    }

    public void CargarMatriz(String[] numeros) {
        int index = 0;
        for (int i = 0; i < MatrizNumber.length; i++) {
            for (int j = 0; j < MatrizNumber.length; j++) {
                MatrizNumber[i][j] = numeros[index];
                MatrizRespaldo[i][j] = MatrizNumber[i][j];
                index++;
            }
        }
    }

    public void Pintar(String[][] matriz) {
        int i = 0;
        ImageView imageView = new ImageView();
        ImageView imageView2;
        for (int Fila = 0; Fila < 13; Fila++) {
            for (int columna = 0; columna < 13; columna++) {
                switch (matriz[Fila][columna]) {
                    case "#":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Borde.png"));
                        break;
                    case "2":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Coin.png"));
                        break;
                    case "4":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Blinky.png"));
                        break;
                    case "5":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Clyde.png"));
                        break;
                    case "6":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Inky.png"));
                        break;
                    case "7":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Pinky.png"));
                        break;
                    case "3":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/pacmanR.png"));
                        PJ_Columna = columna;
                        PJ_Fila = Fila;
                        break;
                }
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                gridGame.setColumnIndex(imageView, columna);
                gridGame.setRowIndex(imageView, Fila);
                gridGame.add(imageView, columna, Fila);
                i++;
                imageView = new ImageView();
            }
        }
    }

    public void MovimientoPersonaje(KeyEvent event) {
        ImageView PersonajeMove = new ImageView(new Image("/cr/ac/una/pac/man/images/pacmanR.png"));
        PersonajeMove.setFitHeight(50);
        PersonajeMove.setFitWidth(50);
        switch (event.getCode()) {
            case UP:
                PersonajeMove.setImage(new Image("/cr/ac/una/pac/man/images/pacmanU.png"));
                moverPersonaje(-1, 0, PersonajeMove);
                break;
            case DOWN:
                PersonajeMove.setImage(new Image("/cr/ac/una/pac/man/images/pacmanD.png"));
                moverPersonaje(1, 0, PersonajeMove);
                break;
            case LEFT:
                PersonajeMove.setImage(new Image("/cr/ac/una/pac/man/images/pacmanI.png"));
                moverPersonaje(0, -1, PersonajeMove);
                break;
            case RIGHT:
                PersonajeMove.setImage(new Image("/cr/ac/una/pac/man/images/pacmanR.png"));
                moverPersonaje(0, 1, PersonajeMove);
                break;
            default:
                break;
        }
    }

    public void moverPersonaje(int desplazamientoFila, int desplazamientoColumna, ImageView PersonajeMove) {
        if (!verificarBorde(PJ_Fila, PJ_Columna, desplazamientoFila, desplazamientoColumna)) {
            MoverCaja(desplazamientoFila, desplazamientoColumna, PersonajeMove);
        }
    }

    public void MoverCaja(int desplazamientoFila, int desplazamientoColumna, ImageView PersonajeMove) {
        Node nodeToRemove = null;
        gridGame.add(PersonajeMove, PJ_Columna + desplazamientoColumna, PJ_Fila + desplazamientoFila);
        for (Node node : gridGame.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == PJ_Fila
                    && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == PJ_Columna) {
                nodeToRemove = node;
                break;
            }
        }
        if (nodeToRemove != null) {
            gridGame.getChildren().remove(nodeToRemove);
        }



        MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "3";
        MatrizNumber[PJ_Fila][PJ_Columna] = "0";
        
        
               for (Node node : gridGame.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == PJ_Fila
                    && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == PJ_Columna) {
                nodeToRemove = node;
                break;
            }
        }
        if (nodeToRemove != null) {
            gridGame.getChildren().remove(nodeToRemove);
        }
        
        PJ_Columna += desplazamientoColumna;
        PJ_Fila += desplazamientoFila;
    }

    private boolean verificarBorde(int fila, int columna, int desplazamientoFila, int desplazamientoColumna) {
        return MatrizNumber[fila + desplazamientoFila][columna + desplazamientoColumna].equals("#");
    }
}
