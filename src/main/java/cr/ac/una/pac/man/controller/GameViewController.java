package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.Posicion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    @FXML
    private Text TextLevel;
    @FXML
    private Text TextPoints;
    @FXML
    private Button Life1;
    @FXML
    private Button Life2;
    @FXML
    private Button Life3;
    @FXML
    private Button Life4;
    @FXML
    private Button Life5;
    @FXML
    private Button Life6;

    private int PJ_Columna;
    private int PJ_Fila;
    private int Blinky_Fila = 5;
    private int Blinky_Columna = 6;
    private int Pinky_Fila = 6;
    private int Pinky_Columna = 6;
    private int Inky_Fila = 6;
    private int Inky_Columna = 5;
    private int Clyde_Fila = 6;
    private int Clyde_Columna = 7;
    String[] numeros;
    private String[][] MatrizNumber = new String[13][13];
    private String[][] MatrizRespaldo = new String[13][13];
    private boolean Comenzar = false;
    private boolean isMoving = false;
    private boolean isMovingPinky = false;
    private boolean isMovingInky = false;
    private boolean isMovingClyde = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextLevel.setText(Integer.toString(FlowController.getNivel()));
        //Background.setImage(new Image("/cr/ac/una/pac/man/images/Nivel" + FlowController.getNivel() + ".jpg"));
        CargarNivel();
        Platform.runLater(() -> {
            Node node = Background;
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

    private boolean esFilaValida(int fila, String[][] matriz) {
        return fila >= 0 && fila < matriz.length;
    }

    private boolean esColumnaValida(int columna, String[][] matriz, int fila) {
        return columna >= 0 && columna < matriz[fila].length;
    }

    public void Pintar(String[][] matriz) {
        int i = 0;
        ImageView imageView = new ImageView();
        ImageView imageView2;
        for (int Fila = 0; Fila < 13; Fila++) {
            for (int columna = 0; columna < 13; columna++) {
                switch (matriz[Fila][columna]) {
                    case "#":
                        if (Fila == 0 && columna != 12) {
                            if (matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorIzquierda.png"));
                            } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónSuperior.png"));
                            } else {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                            }
                        } else if (Fila == 0 && columna == 12) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorDerecha.png"));
                        } else if (Fila == matriz.length - 1 && columna != 12) {
                            if (matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorIzquierda.png"));
                            } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#")) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónInferior.png"));
                            } else {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                            }
                        } else if (Fila == matriz.length - 1 && columna == 12) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorDerecha.png"));
                        } else if ((columna == 0 || columna == matriz.length - 1) && (Fila > 0 && Fila < matriz.length - 1)) {
                            if (columna == 0) {
                                if (matriz[Fila - 1][columna].equals("#") && matriz[Fila + 1][columna].equals("#") && matriz[Fila][columna + 1].equals("#")) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónIzquierda.png"));
                                } else if (matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorIzquierda.png"));
                                } else if (matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorIzquierda.png"));
                                } else if (matriz[Fila][columna + 1].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalIzquierdo.png"));
                                } else if (!matriz[Fila - 1][columna].equals("#") && !matriz[Fila + 1][columna].equals("#") && matriz[Fila][columna + 1].equals("#")) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                                } else {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioVertical.png"));
                                }
                            } else {
                                if (matriz[Fila - 1][columna].equals("#") && matriz[Fila + 1][columna].equals("#") && matriz[Fila][columna - 1].equals("#")) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónDerecha.png"));
                                } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila + 1][columna].equals("#") && !esColumnaValida(columna + 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorDerecha.png"));
                                } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila - 1][columna].equals("#") && !esColumnaValida(columna + 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorDerecha.png"));
                                } else if (matriz[Fila][columna - 1].equals("#") && !esColumnaValida(columna + 1, matriz, Fila)) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalDerecho.png"));
                                } else if (!matriz[Fila - 1][columna].equals("#") && !matriz[Fila + 1][columna].equals("#") && matriz[Fila][columna - 1].equals("#")) {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                                } else {
                                    imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioVertical.png"));
                                }
                            }
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila - 1][columna].equals("#") && matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/Intersección.png"));
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila - 1][columna].equals("#") && matriz[Fila][columna + 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónInferior.png"));
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila + 1][columna].equals("#") && matriz[Fila][columna + 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónSuperior.png"));
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila + 1][columna].equals("#") && matriz[Fila - 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónDerecha.png"));
                        } else if (matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#") && matriz[Fila - 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónIzquierda.png"));
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorDerecha.png"));
                        } else if (matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorIzquierda.png"));
                        } else if (matriz[Fila - 1][columna].equals("#") && matriz[Fila][columna - 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorDerecha.png"));
                        } else if (matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorIzquierda.png"));
                        } else if (matriz[Fila - 1][columna].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioVertical.png"));
                        } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila][columna + 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                        } else if (matriz[Fila][columna - 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalDerecho.png"));
                        } else if (matriz[Fila][columna + 1].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalIzquierdo.png"));
                        } else if (matriz[Fila + 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalSuperior.png"));
                        } else if (matriz[Fila - 1][columna].equals("#")) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/FinalInferior.png"));
                        } else {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroSolo.png"));
                        }
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
        if (Comenzar) {
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
            if (!isMoving) {
                BlinkyMove(true);
            }
            PauseTransition delay1 = new PauseTransition(Duration.seconds(0.3));
            delay1.setOnFinished(e -> {
                if (!isMovingPinky) {
                    PinkyMove(true);
                }

                PauseTransition delay2 = new PauseTransition(Duration.seconds(0.3));
                delay2.setOnFinished(ev -> {
                    if (!isMovingInky) {
                        InkyMove(true);
                    }
                    PauseTransition delay3 = new PauseTransition(Duration.seconds(0.3));
                    delay3.setOnFinished(evt -> {
                        if (!isMovingClyde) {
                            ClydeMove(true);
                        }
                    });
                    delay3.play();
                });
                delay2.play();
            });
            delay1.play();
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
        MatrizRespaldo[PJ_Fila][PJ_Columna] = "0";
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

    private void restaurar(int fila, int columna) {
        int i = 0;
        ImageView imageView = new ImageView();
        switch (MatrizNumber[fila][columna]) {
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
        }
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        gridGame.setColumnIndex(imageView, columna);
        gridGame.setRowIndex(imageView, fila);
        gridGame.add(imageView, columna, fila);
        i++;
    }

    private boolean verificarBorde(int fila, int columna, int desplazamientoFila, int desplazamientoColumna) {
        return MatrizNumber[fila + desplazamientoFila][columna + desplazamientoColumna].equals("#");
    }

    @FXML
    private void StartGame(ActionEvent event) {
        Comenzar = true;

        if (!isMoving) {
            BlinkyMove(true);
        }
        PauseTransition delay1 = new PauseTransition(Duration.seconds(0.3));
        delay1.setOnFinished(e -> {
            if (!isMovingPinky) {
                PinkyMove(true);
            }

            PauseTransition delay2 = new PauseTransition(Duration.seconds(0.3));
            delay2.setOnFinished(ev -> {
                if (!isMovingInky) {
                    InkyMove(true);
                }
                PauseTransition delay3 = new PauseTransition(Duration.seconds(0.3));
                delay3.setOnFinished(evt -> {
                    if (!isMovingClyde) {
                        ClydeMove(true);
                    }
                });
                delay3.play();
            });
            delay2.play();
        });
        delay1.play();
    }

    private void BlinkyMove(Boolean PrimeraVez) {
        if (!isMoving) {
            isMoving = true;
            Posicion inicio = new Posicion(Blinky_Fila, Blinky_Columna);
            Posicion objetivo = new Posicion(PJ_Fila, PJ_Columna);
            List<Posicion> ruta = obtenerRutaMasCorta(MatrizNumber, inicio, objetivo, PrimeraVez);
            MoverRutaBlinky(ruta);
        }
    }

    private void PinkyMove(Boolean PrimeraVez) {
        if (!isMovingPinky) {
            isMovingPinky = true;
            Posicion inicio = new Posicion(Pinky_Fila, Pinky_Fila);
            Posicion objetivo = new Posicion(PJ_Fila, PJ_Columna);
            List<Posicion> ruta = obtenerRutaMasCorta(MatrizNumber, inicio, objetivo, PrimeraVez);
            MoverRutaPinky(ruta);
        }
    }

    private void InkyMove(Boolean PrimeraVez) {
        if (!isMovingInky) {
            isMovingInky = true;
            Posicion inicio = new Posicion(Inky_Fila, Inky_Columna);
            Posicion objetivo = new Posicion(PJ_Fila, PJ_Columna);
            List<Posicion> ruta = obtenerRutaMasCorta(MatrizNumber, inicio, objetivo, PrimeraVez);
            MoverRutaInky(ruta);
        }
    }

    private void ClydeMove(Boolean PrimeraVez) {
        if (!isMovingClyde) {
            isMovingClyde = true;
            Posicion inicio = new Posicion(Clyde_Fila, Clyde_Fila);
            Posicion objetivo = new Posicion(PJ_Fila, PJ_Columna);
            List<Posicion> ruta = obtenerRutaMasCorta(MatrizNumber, inicio, objetivo, PrimeraVez);
            MoverRutaClyde(ruta);
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        FlowController.getInstance().goMain("MainView");
    }

    public List<Posicion> obtenerRutaMasCorta(String[][] matriz, Posicion inicio, Posicion objetivo, Boolean PrimeraVez) {
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

    private boolean esPosicionValida(int fila, int columna, int filas, int columnas) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

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

    private void MoverRutaBlinky(List<Posicion> ruta) {
        final Node[] nodeToRemove = {null};
        if (!isMoving) {
            return;
        }
        SequentialTransition seqTransition = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(0.30));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove = new ImageView(new Image("/cr/ac/una/pac/man/images/Blinky.png"));
                PersonajeMove.setFitHeight(50);
                PersonajeMove.setFitWidth(50);
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);

                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Blinky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Blinky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                if (index == 1) {
                    MatrizNumber[Blinky_Fila][Blinky_Columna] = "0";
                } else {
                    if (MatrizRespaldo[Blinky_Fila][Blinky_Columna].equals("2")) {
                        MatrizNumber[Blinky_Fila][Blinky_Columna] = "2";
                    } else {
                        MatrizNumber[Blinky_Fila][Blinky_Columna] = "0";
                    }
                }
                MatrizNumber[ruta.get(index).fila][ruta.get(index).columna] = "4";
                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Blinky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Blinky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                restaurar(Blinky_Fila, Blinky_Columna);
                Blinky_Columna = ruta.get(index).columna;
                Blinky_Fila = ruta.get(index).fila;
            });
            seqTransition.getChildren().add(delayAppearance);
        }

        seqTransition.setOnFinished(event -> {
            isMoving = false;
        });
        isMoving = true;
        seqTransition.play();
    }

    private void MoverRutaPinky(List<Posicion> ruta) {
        final Node[] nodeToRemove = {null};
        if (!isMovingPinky) {
            return;
        }
        SequentialTransition seqTransition = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(0.30));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove = new ImageView(new Image("/cr/ac/una/pac/man/images/Pinky.png"));
                PersonajeMove.setFitHeight(50);
                PersonajeMove.setFitWidth(50);
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);

                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Pinky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Pinky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                if (index == 1) {
                    MatrizNumber[Pinky_Fila][Pinky_Columna] = "0";
                } else {
                    if (MatrizRespaldo[Pinky_Fila][Pinky_Columna].equals("2")) {
                        MatrizNumber[Pinky_Fila][Pinky_Columna] = "2";
                    } else {
                        MatrizNumber[Pinky_Fila][Pinky_Columna] = "0";
                    }
                }
                MatrizNumber[ruta.get(index).fila][ruta.get(index).columna] = "7";
                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Pinky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Pinky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                restaurar(Pinky_Fila, Pinky_Columna);
                Pinky_Columna = ruta.get(index).columna;
                Pinky_Fila = ruta.get(index).fila;

            });
            seqTransition.getChildren().add(delayAppearance);
        }
        seqTransition.setOnFinished(event -> {
            isMovingPinky = false;
        });
        isMovingPinky = true;
        seqTransition.play();
    }

    private void MoverRutaInky(List<Posicion> ruta) {
        final Node[] nodeToRemove = {null};
        if (!isMovingInky) {
            return;
        }
        SequentialTransition seqTransition = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(0.30));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove = new ImageView(new Image("/cr/ac/una/pac/man/images/Inky.png"));
                PersonajeMove.setFitHeight(50);
                PersonajeMove.setFitWidth(50);
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);

                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Inky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Inky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                if (index == 1) {
                    MatrizNumber[Inky_Fila][Inky_Columna] = "0";
                } else {
                    if (MatrizRespaldo[Inky_Fila][Inky_Columna].equals("2")) {
                        MatrizNumber[Inky_Fila][Inky_Columna] = "2";
                    } else {
                        MatrizNumber[Inky_Fila][Inky_Columna] = "0";
                    }
                }
                MatrizNumber[ruta.get(index).fila][ruta.get(index).columna] = "6";
                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Inky_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Inky_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                restaurar(Inky_Fila, Inky_Columna);
                Inky_Columna = ruta.get(index).columna;
                Inky_Fila = ruta.get(index).fila;

            });
            seqTransition.getChildren().add(delayAppearance);
        }
        seqTransition.setOnFinished(event -> {
            isMovingInky = false;
        });
        isMovingInky = true;
        seqTransition.play();
    }

    private void MoverRutaClyde(List<Posicion> ruta) {
        final Node[] nodeToRemove = {null};
        if (!isMovingClyde) {
            return;
        }
        SequentialTransition seqTransition = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(0.30));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove = new ImageView(new Image("/cr/ac/una/pac/man/images/Clyde.png"));
                PersonajeMove.setFitHeight(50);
                PersonajeMove.setFitWidth(50);
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);

                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Clyde_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Clyde_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                if (index == 1) {
                    MatrizNumber[Clyde_Fila][Clyde_Columna] = "0";
                } else {
                    if (MatrizRespaldo[Clyde_Fila][Clyde_Columna].equals("2")) {
                        MatrizNumber[Clyde_Fila][Clyde_Columna] = "2";
                    } else {
                        MatrizNumber[Clyde_Fila][Clyde_Columna] = "0";
                    }
                }
                MatrizNumber[ruta.get(index).fila][ruta.get(index).columna] = "5";
                for (Node node : gridGame.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == Clyde_Fila
                            && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == Clyde_Columna) {
                        nodeToRemove[0] = node;
                        break;
                    }
                }
                if (nodeToRemove[0] != null) {
                    gridGame.getChildren().remove(nodeToRemove[0]);
                }
                restaurar(Clyde_Fila, Clyde_Columna);
                Clyde_Columna = ruta.get(index).columna;
                Clyde_Fila = ruta.get(index).fila;

            });
            seqTransition.getChildren().add(delayAppearance);
        }
        seqTransition.setOnFinished(event -> {
            isMovingClyde = false;
        });
        isMovingClyde = true;
        seqTransition.play();
    }
}
