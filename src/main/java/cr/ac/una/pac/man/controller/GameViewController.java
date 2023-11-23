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
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML
    private Text TextPerdisteVida;
    @FXML
    private Button btnPower;

    private int PJ_Columna;
    private int PJ_Fila;
    private int Blinky_Fila;
    private int Blinky_Columna;
    private int Pinky_Fila;
    private int Pinky_Columna;
    private int Inky_Fila;
    private int Inky_Columna;
    private int Clyde_Fila;
    private int Clyde_Columna;
    private boolean Comenzar = false;
    private boolean isMoving = false;
    private boolean isMovingPinky = false;
    private boolean isMovingInky = false;
    private boolean isMovingClyde = false;
    private double velocidadBlinky = 1;
    private double velocidadPinky = 1;
    private double velocidadClyde = 1;
    private double velocidadInky = 1;
    static final int WEIGHT_HEIGHT_IMAGE = 25;
    private int ValiteMove = 0;
    private String[] numeros;
    private String[][] MatrizNumber = new String[25][25];
    private String[][] MatrizRespaldo = new String[25][25];
    private String[][] MatrizGhost = new String[25][25];
    private String Blinky = "/cr/ac/una/pac/man/images/Ghosts/Blinky.png";
    private String Pinky = "/cr/ac/una/pac/man/images/Ghosts/Pinky.png";
    private String Inky = "/cr/ac/una/pac/man/images/Ghosts/Inky.png";
    private String Clyde = "/cr/ac/una/pac/man/images/Ghosts/Clyde.png";
    private String EatGhost = "/cr/ac/una/pac/man/images/Ghosts/Eat.png";
    private boolean CanEat = false;
    private SequentialTransition seqTransitionBlinky;
    private SequentialTransition seqTransitionPinky;
    private SequentialTransition seqTransitionInky;
    private SequentialTransition seqTransitionClyde;
    private boolean consecutivo = false;
    private Algoritmos algoritmos = new Algoritmos();
    private int coinstotal = 0;
    private int coinseat = 0;
    private boolean withoutdead = true;
    private boolean pausa = false;
    private boolean move = false;
    private KeyEvent eventAux = null;
    private double velocidadPacMan = 0.2;
    private int coints = 10;
    private int puntos = 0;
    Text tiempoText = new Text("Tiempo: 00:00");
    @FXML
    private Button btnComprar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPower.setDisable(true);
        if (FlowController.getInstance().getPuntos() > 1500) {
            btnComprar.setDisable(false);
        } else {
            btnComprar.setDisable(true);
        }
        TextLevel.setText(Integer.toString(FlowController.getNivel()));
        Background.setImage(new Image("/cr/ac/una/pac/man/images/Backgrounds/Nivel" + FlowController.getNivel() + ".jpg"));
        CargarNivel();
        Platform.runLater(() -> {
            Node node = Background;
            Scene scene = node.getScene();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                MovimientoPersonaje(event);
                move = true;
            });
        });
        FlowController.getInstance().setImportar(true);
        TextPoints.setText("" + FlowController.getInstance().getPuntos());
        if (FlowController.getInstance().getPuntos() > 900) {
            velocidadBlinky = 0.20;
        }
        final int[] segundosTranscurridos = {0};
        // Crear un Timeline para actualizar el cronómetro
        Timeline cronometro = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        segundosTranscurridos[0]++;
                        int minutos = segundosTranscurridos[0] / 60;
                        int segundos = segundosTranscurridos[0];
                        tiempoText.setText(String.format("%02d", segundos));
                    }
                })
        );
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    public void CargarNivel() {
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
        if (FlowController.getInstance().isImportar()) {
            TextPerdisteVida.setText("¡Perdiste una vida!");
            PauseTransition delay = new PauseTransition(Duration.seconds(3)); // Suponiendo que quieras esperar 3 segundos
            delay.setOnFinished(e -> {
                Platform.runLater(() -> {
                    adaptar();
                    Pintar(MatrizNumber);
                    for (int i = 1; i <= Math.abs(FlowController.getInstance().getVidas() - 6); i++) {
                        VidasRestantes(Math.abs(i - 6));
                    }
                    TextPerdisteVida.setText(" ");
                });
            });
            delay.play();
        } else {
            Pintar(MatrizNumber);
        }
    }

    public void adaptar() {
        for (int i = 0; i < MatrizNumber.length; i++) {
            for (int j = 0; j < MatrizNumber.length; j++) {
                if (FlowController.getInstance().getMatrizRespaldo()[i][j].equals("0") && MatrizNumber[i][j].equals("2")) {
                    MatrizNumber[i][j] = "0";
                    MatrizRespaldo[i][j] = "0";
                }
            }
        }
    }

    public void CargarMatriz(String[] numeros) {
        int index = 0;
        for (int i = 0; i < MatrizNumber.length; i++) {
            for (int j = 0; j < MatrizNumber.length; j++) {
                MatrizNumber[i][j] = numeros[index];
                MatrizRespaldo[i][j] = MatrizNumber[i][j];
                MatrizGhost[i][j] = MatrizNumber[i][j];
                index++;
            }
        }
    }

    public ImageView CrearImagen(String path) {
        ImageView image = new ImageView(new Image(path));
        image.setFitHeight(WEIGHT_HEIGHT_IMAGE);
        image.setFitWidth(WEIGHT_HEIGHT_IMAGE);
        return image;
    }

    private boolean esColumnaValida(int columna, String[][] matriz, int fila) {
        return columna >= 0 && columna < matriz[fila].length;
    }

    public void Pintar(String[][] matriz) {
        int i = 0;
        ImageView imageView = new ImageView();
        for (int Fila = 0; Fila < 25; Fila++) {
            for (int columna = 0; columna < 25; columna++) {
                switch (matriz[Fila][columna]) {
                    case "#":
                        if (Fila == 0 && columna != 24) {
                            if (matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorIzquierda.png"));
                            } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila][columna + 1].equals("#") && matriz[Fila + 1][columna].equals("#")) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónSuperior.png"));
                            } else {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                            }
                        } else if (Fila == 0 && columna == 24) {
                            imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaSuperiorDerecha.png"));
                        } else if (Fila == matriz.length - 1 && columna != 24) {
                            if (matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#") && !esColumnaValida(columna - 1, matriz, Fila)) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/EsquinaInferiorIzquierda.png"));
                            } else if (matriz[Fila][columna - 1].equals("#") && matriz[Fila][columna + 1].equals("#") && matriz[Fila - 1][columna].equals("#")) {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/IntersecciónInferior.png"));
                            } else {
                                imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/mapa/MuroIntermedioHorizontal.png"));
                            }
                        } else if (Fila == matriz.length - 1 && columna == 24) {
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
                    case "1":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/BigCoin.png"));
                        break;
                    case "2":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/Coin.png"));
                        coinstotal++;
                        coinseat++;
                        break;
                    case "4":
                        imageView = new ImageView(new Image(Blinky));
                        Blinky_Fila = Fila;
                        Blinky_Columna = columna;
                        break;
                    case "5":
                        imageView = new ImageView(new Image(Clyde));
                        Clyde_Fila = Fila;
                        Clyde_Columna = columna;
                        break;
                    case "6":
                        imageView = new ImageView(new Image(Inky));
                        Inky_Fila = Fila;
                        Inky_Columna = columna;
                        break;
                    case "7":
                        imageView = new ImageView(new Image(Pinky));
                        Pinky_Fila = Fila;
                        Pinky_Columna = columna;
                        break;
                    case "3":
                        imageView = new ImageView(new Image("/cr/ac/una/pac/man/images/pacmanR.png"));
                        PJ_Columna = columna;
                        PJ_Fila = Fila;
                        break;
                }
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                gridGame.setColumnIndex(imageView, columna);
                gridGame.setRowIndex(imageView, Fila);
                gridGame.add(imageView, columna, Fila);
                i++;
                imageView = new ImageView();
            }
        }
    }
    private Timeline movementTimeline;

    public void MovimientoPersonaje(KeyEvent event) {
        if (movementTimeline != null) {
            movementTimeline.stop();
        }

        switch (event.getCode()) {
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
                eventAux = event;
                break;
            default:
                return;
        }
        movementTimeline = new Timeline(new KeyFrame(Duration.seconds(velocidadPacMan), e -> moveCharacter()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }

    private void moveCharacter() {
        if (Comenzar) {
            ValiteMove++;
            ImageView PersonajeMove = CrearImagen("/cr/ac/una/pac/man/images/pacmanR.png");

            switch (eventAux.getCode()) {
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

            if (ValiteMove % 5 == 0) {
                if (!isMoving && !pausa) {
                    BlinkyMove(PJ_Fila, PJ_Columna, false);
                }
                PauseTransition delay1 = new PauseTransition(Duration.seconds(0.6));
                delay1.setOnFinished(e -> {
                    if (!isMovingPinky && !pausa) {
                        PinkyMove(PJ_Fila, PJ_Columna, false);
                    }
                    PauseTransition delay2 = new PauseTransition(Duration.seconds(0.6));
                    delay2.setOnFinished(ev -> {
                        if (!isMovingInky) {
                            InkyMove(0, 0, false);
                        }
                        PauseTransition delay3 = new PauseTransition(Duration.seconds(0.6));
                        delay3.setOnFinished(evt -> {
                            if (!isMovingClyde) {
                                ClydeMove(0, 0, false);
                            }
                        });
                        delay3.play();
                    });
                    delay2.play();
                });
                delay1.play();
            }
        }
    }

    public void moverPersonaje(int desplazamientoFila, int desplazamientoColumna, ImageView PersonajeMove) {
        if (!verificarBorde(PJ_Fila, PJ_Columna, desplazamientoFila, desplazamientoColumna)) {
            Mover(desplazamientoFila, desplazamientoColumna, PersonajeMove);
        }
    }

    public void VidasRestantes(int vida) {
        switch (vida) {
            case 6:
                Life6.setDisable(true);
                break;
            case 5:
                Life6.setDisable(true);
                break;
            case 4:
                Life5.setDisable(true);
                break;
            case 3:
                Life4.setDisable(true);
                break;
            case 2:
                Life3.setDisable(true);
                break;
            case 1:
                Life2.setDisable(true);
                break;
            case 0:
                Life1.setDisable(true);
                break;
        }
    }

    public void VidasRestantesMas(int vida) {
        switch (vida) {
            case 6:
                Life6.setDisable(false);
                break;
            case 5:
                Life5.setDisable(false);
                break;
            case 4:
                Life4.setDisable(false);
                break;
            case 3:
                Life3.setDisable(false);
                break;
            case 2:
                Life2.setDisable(false);
                break;
            case 1:
                Life1.setDisable(false);
                break;
            case 0:
                Life1.setDisable(true);
                break;
        }
    }

    private void iniciarContadorDe30Segundos() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        pausa = true;

        executor.schedule(() -> {
            pausa = false;
            executor.shutdown();
        }, 30, TimeUnit.SECONDS);
    }

    public void Mover(int desplazamientoFila, int desplazamientoColumna, ImageView PersonajeMove) {
        int AuxFila = 0;
        int AuxColum = 0;
        ImageView ghost;
        Node nodeToRemove = null;

        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("@")) {
            MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "X";
            for (int i = 0; i < MatrizNumber.length; i++) {
                for (int j = 0; j < MatrizNumber.length; j++) {
                    if (MatrizNumber[i][j].equals("@")) {
                        gridGame.add(PersonajeMove, j, i);
                        AuxFila = i;
                        AuxColum = j;
                    }
                }
            }
            MatrizNumber[PJ_Fila][PJ_Columna] = "@";
            MatrizRespaldo[PJ_Fila][PJ_Columna] = "@";
            MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "@";
        } else {
            gridGame.add(PersonajeMove, PJ_Columna + desplazamientoColumna, PJ_Fila + desplazamientoFila);
        }

        if ((MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("4") || MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("5")
                || MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("6") || MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("7"))
                && !CanEat) {
            withoutdead = false;
            FlowController.setLostLive(FlowController.getLostLive() + 1);
            FlowController.setAuxExperto(false);
            FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() - 1);
            VidasRestantes(FlowController.getInstance().getVidas());
            movementTimeline.stop();
            cancelPinky();
            cancelInky();
            cancelBlinky();
            cancelClyde();
            if (FlowController.getInstance().getVidas() == 0) {
                FlowController.getInstance().goMain("MainView");
            } else {
                FlowController.getInstance().setMatrizRespaldo(MatrizRespaldo);
                FlowController.getInstance().goMain("GameView");
            }
        }

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

        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("2")) {
            FlowController.getInstance().setPuntos(FlowController.getInstance().getPuntos() + coints);
            puntos += coints;
            TextPoints.setText("" + FlowController.getInstance().getPuntos());
            if (FlowController.getInstance().getPuntos() > 900) {
                velocidadBlinky = 0.20;
            }
            coinseat--;
            if (coinseat == (coinstotal / 2) && withoutdead) {
                cancelBlinky();
                cancelPinky();
                isMoving = false;
                isMovingPinky = false;
                velocidadBlinky = 0.20;
                velocidadPinky = 0.20;
                BlinkyMove(9, 12, false);
                PinkyMove(10, 12, false);
                isMoving = true;
                isMovingPinky = true;
                iniciarContadorDe30Segundos();
                FlowController.setContadorEncierro(FlowController.getContadorEncierro() + 1);
            }
        }
        if (FlowController.getInstance().getPuntos() > 1500) {
            btnComprar.setDisable(false);
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("1")) {
            CanEat = true;
            Blinky = EatGhost;
            Pinky = EatGhost;
            Inky = EatGhost;
            Clyde = EatGhost;
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                CanEat = false;
                consecutivo = false;
                Blinky = "/cr/ac/una/pac/man/images/Ghosts/Blinky.png";
                Pinky = "/cr/ac/una/pac/man/images/Ghosts/Pinky.png";
                Inky = "/cr/ac/una/pac/man/images/Ghosts/Inky.png";
                Clyde = "/cr/ac/una/pac/man/images/Ghosts/Clyde.png";
            });
            pause.play();
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("4") && CanEat) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            cancelBlinky();
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("7") && CanEat) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            cancelPinky();
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("5") && CanEat) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            cancelClyde();
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("6") && CanEat) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            cancelInky();
        }
        if (MatrizGhost[PJ_Fila][PJ_Columna].equals("4")) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            seguidos();
            Blinky = "/cr/ac/una/pac/man/images/Ghosts/Dead.png";
            ghost = CrearImagen(Blinky);
            MatrizNumber[PJ_Fila][PJ_Columna] = MatrizRespaldo[PJ_Fila][PJ_Columna];
            gridGame.add(ghost, PJ_Columna, PJ_Fila);
            Blinky_Fila = PJ_Fila;
            Blinky_Columna = PJ_Columna;
            isMoving = false;
            velocidadBlinky = 0.30;
            BlinkyMove(9, 12, true);
            isMoving = true;

        } else if (MatrizGhost[PJ_Fila][PJ_Columna].equals("7")) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            seguidos();
            Pinky = "/cr/ac/una/pac/man/images/Ghosts/Dead.png";
            ghost = CrearImagen(Pinky);
            MatrizNumber[PJ_Fila][PJ_Columna] = MatrizRespaldo[PJ_Fila][PJ_Columna];
            gridGame.add(ghost, PJ_Columna, PJ_Fila);
            Pinky_Fila = PJ_Fila;
            Pinky_Columna = PJ_Columna;
            isMovingPinky = false;
            velocidadPinky = 0.30;
            PinkyMove(10, 12, true);
            isMovingPinky = true;

        } else if (MatrizGhost[PJ_Fila][PJ_Columna].equals("5")) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            seguidos();
            Clyde = "/cr/ac/una/pac/man/images/Ghosts/Dead.png";
            ghost = CrearImagen(Clyde);
            MatrizNumber[PJ_Fila][PJ_Columna] = MatrizRespaldo[PJ_Fila][PJ_Columna];
            gridGame.add(ghost, PJ_Columna, PJ_Fila);
            Clyde_Fila = PJ_Fila;
            Clyde_Columna = PJ_Columna;
            isMovingClyde = false;
            velocidadClyde = 0.30;
            ClydeMove(10, 13, true);
            isMovingClyde = true;

        } else if (MatrizGhost[PJ_Fila][PJ_Columna].equals("6")) {
            FlowController.setContadorFastantas(FlowController.getContadorFastantas() + 1);
            seguidos();
            Inky = "/cr/ac/una/pac/man/images/Ghosts/Dead.png";
            ghost = CrearImagen(Inky);
            MatrizNumber[PJ_Fila][PJ_Columna] = MatrizRespaldo[PJ_Fila][PJ_Columna];
            gridGame.add(ghost, PJ_Columna, PJ_Fila);
            Inky_Fila = PJ_Fila;
            Inky_Columna = PJ_Columna;
            isMovingInky = false;
            velocidadInky = 0.30;
            InkyMove(10, 11, true);
            isMovingInky = true;
        } else {
            MatrizNumber[PJ_Fila][PJ_Columna] = "0";
            MatrizRespaldo[PJ_Fila][PJ_Columna] = "0";
        }
        if (MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna].equals("@")) {
            MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "@";
            MatrizRespaldo[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "@";
        } else {
            MatrizNumber[PJ_Fila + desplazamientoFila][PJ_Columna + desplazamientoColumna] = "3";
        }
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
        if (MatrizGhost[PJ_Fila][PJ_Columna].equals("@")) {
            MatrizNumber[PJ_Fila][PJ_Columna] = "@";
            MatrizRespaldo[PJ_Fila][PJ_Columna] = "@";
        }
        if (AuxFila == 0 && AuxColum == 0) {
            PJ_Columna += desplazamientoColumna;
            PJ_Fila += desplazamientoFila;
        } else {
            PJ_Columna = AuxColum;
            PJ_Fila = AuxFila;
        }
        if (!Continue()) {
            FlowController.setWin(true);
            FlowController.setPuntosWin(FlowController.getNivel() * 100);
            puntos += FlowController.getNivel() * 100;
            FlowController.setPuntos(FlowController.getPuntos() + (FlowController.getNivel() * 100));
            FlowController.setPuntosXpartidaMax(puntos);
            if (FlowController.getNivel() + 1 == 10) {
                FlowController.setClasico(true);
                FlowController.setNivel(10);
            } else {
                System.out.println(FlowController.getNivel());
                FlowController.setNivel(FlowController.getNivel() + 1);
            }
            FlowController.setNivelTotal(FlowController.getNivel() + 1);
            FlowController.setBestTime(Integer.parseUnsignedInt(tiempoText.getText()));
            if (FlowController.isAuxExperto()) {
                FlowController.setExperto(true);
            }
            if (FlowController.getInstance().getDifficulty() == 3) {
                FlowController.setRey(FlowController.getRey() + 1);
            }
            FlowController.setTimeTotal(tiempoText.getText());
            FlowController.getInstance().goMain("MainView");
        }
    }

    public void seguidos() {
        if (!consecutivo) {
            FlowController.setPuntos(FlowController.getPuntos() + 300);
            puntos += 300;
            TextPoints.setText("" + FlowController.getInstance().getPuntos());
            consecutivo = true;
        } else {
            btnPower.setDisable(false);
            FlowController.setPuntos(FlowController.getPuntos() + 400);
            puntos += 400;
            TextPoints.setText("" + FlowController.getInstance().getPuntos());
        }
    }

    public boolean Continue() {

        for (int i = 0; i < MatrizNumber.length; i++) {
            for (int j = 0; j < MatrizNumber[i].length; j++) {
                if (MatrizNumber[i][j].equals("2") || MatrizNumber[i][j].equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void restaurar(int fila, int columna) {
        int i = 0;
        ImageView imageView = new ImageView();
        switch (MatrizNumber[fila][columna]) {
            case "2":
                imageView = CrearImagen("/cr/ac/una/pac/man/images/Coin.png");
                break;
            case "4":
                imageView = CrearImagen(Blinky);
                break;
            case "5":
                imageView = CrearImagen(Clyde);
                break;
            case "6":
                imageView = CrearImagen(Inky);
                break;
            case "7":
                imageView = CrearImagen(Pinky);
                break;
        }
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
            BlinkyMove(PJ_Fila, PJ_Columna, false);
        }
        PauseTransition delay1 = new PauseTransition(Duration.seconds(1));
        delay1.setOnFinished(e -> {
            if (!isMovingPinky) {
                int fila = 0;
                int columna = 0;
                while (MatrizNumber[fila][columna].equals("#")) {
                    Random random = new Random();
                    fila = random.nextInt(24);
                    columna = random.nextInt(24);
                }
                PinkyMove(fila, columna, false);
            }
            PauseTransition delay2 = new PauseTransition(Duration.seconds(1));
            delay2.setOnFinished(ev -> {
                if (!isMovingInky) {
                    InkyMove(0, 0, false);
                }
                PauseTransition delay3 = new PauseTransition(Duration.seconds(1.5));
                delay3.setOnFinished(evt -> {
                    if (!isMovingClyde) {
                        ClydeMove(0, 0, false);
                    }
                });
                delay3.play();
            });
            delay2.play();
        });
        delay1.play();
    }

    private void BlinkyMove(int fila, int columna, boolean death) {
        if (!isMoving) {
            isMoving = true;
            Posicion inicio = new Posicion(Blinky_Fila, Blinky_Columna, 0);
            Posicion objetivo = new Posicion(fila, columna, 0);
            List<Posicion> ruta = new ArrayList<>();
            if (FlowController.getInstance().getDifficulty() == 1) {
                ruta = algoritmos.DijkstraLarga(MatrizNumber, inicio, objetivo);
            } else if (FlowController.getInstance().getDifficulty() == 3) {
                ruta = algoritmos.Dijsktra(MatrizNumber, inicio, objetivo);
            } else {
                Random random = new Random();
                int randomNumber = random.nextInt(2);
                if (randomNumber == 1) {
                    ruta = algoritmos.DijkstraLarga(MatrizNumber, inicio, objetivo);
                } else {
                    ruta = algoritmos.Dijsktra(MatrizNumber, inicio, objetivo);
                }
            }
            MoverRutaBlinky(ruta, death);
        }
    }

    private void PinkyMove(int Fila, int Columna, boolean death) {
        if (!isMovingPinky) {
            isMovingPinky = true;
            Posicion inicio = new Posicion(Pinky_Fila, Pinky_Columna, 0);
            Posicion objetivo = new Posicion(Fila, Columna, 0);
            List<Posicion> ruta = new ArrayList<>();
            if (FlowController.getInstance().getDifficulty() == 1) {
                ruta = algoritmos.DijkstraLarga(MatrizNumber, inicio, objetivo);
            } else if (FlowController.getInstance().getDifficulty() == 3) {
                ruta = algoritmos.Dijsktra(MatrizNumber, inicio, objetivo);
            } else {
                Random random = new Random();
                int randomNumber = random.nextInt(2);
                if (randomNumber == 1) {
                    ruta = algoritmos.DijkstraLarga(MatrizNumber, inicio, objetivo);
                } else {
                    ruta = algoritmos.Dijsktra(MatrizNumber, inicio, objetivo);
                }
            }
            MoverRutaPinky(ruta, death);
        }
    }

    private void InkyMove(int Fila, int Columna, boolean death) {

        if (Fila == 0 && Columna == 0) {
            Fila = Pinky_Fila;
            Columna = Pinky_Fila;
        }

        if (!isMovingInky) {
            isMovingInky = true;
            Posicion inicio = new Posicion(Inky_Fila, Inky_Columna, 0);
            Posicion objetivo = new Posicion(Fila, Columna, 0);
            List<Posicion> ruta = algoritmos.BFS(MatrizNumber, inicio, objetivo);
            MoverRutaInky(ruta, death);
        }
    }

    private void ClydeMove(int Fila, int Columna, boolean death) {
        if (!isMovingClyde) {
            if (Fila == 0 && Columna == 0) {
                while (MatrizNumber[Fila][Columna].equals("#")) {
                    Random random = new Random();
                    Fila = random.nextInt(24);
                    Columna = random.nextInt(24);
                }
            }
            isMovingClyde = true;
            Posicion inicio = new Posicion(Clyde_Fila, Clyde_Columna, 0);
            Posicion objetivo = new Posicion(Fila, Columna, 0);
            List<Posicion> ruta = algoritmos.Floyd(MatrizNumber, inicio, objetivo);
            MoverRutaClyde(ruta, death);
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        FlowController.getInstance().setImportar(false);
        cancelPinky();
        cancelInky();
        cancelBlinky();
        cancelClyde();
        FlowController.setTimeTotal(tiempoText.getText());
        FlowController.getInstance().goMain("MainView");
    }

//----------------------------------------------------------------------MOVER RUTA----------------------------------------------------------------------
    private void MoverRutaBlinky(List<Posicion> ruta, boolean death) {
        final Node[] nodeToRemove = {null};
        if (!isMoving) {
            return;
        }
        seqTransitionBlinky = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(velocidadBlinky));
            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove;
                if (!death) {
                    PersonajeMove = CrearImagen(Blinky);
                } else {
                    PersonajeMove = CrearImagen("/cr/ac/una/pac/man/images/Ghosts/Dead.png");
                }
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && !CanEat) {
                    cancelPinky();
                    cancelInky();
                    cancelBlinky();
                    cancelClyde();
                    FlowController.setLostLive(FlowController.getLostLive() + 1);
                    FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() - 1);
                    FlowController.setAuxExperto(false);
                    VidasRestantes(FlowController.getInstance().getVidas());
                    movementTimeline.stop();
                    if (FlowController.getInstance().getVidas() == 0) {
                        movementTimeline.stop();
                        FlowController.getInstance().goMain("MainView");
                    } else {
                        FlowController.getInstance().setMatrizRespaldo(MatrizRespaldo);
                        FlowController.getInstance().goMain("GameView");
                    }
                    withoutdead = false;
                }
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
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && CanEat) {
                    cancelBlinky();
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
                MatrizGhost[ruta.get(index).fila][ruta.get(index).columna] = "4";
                MatrizGhost[Blinky_Fila][Blinky_Columna] = "0";
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
            seqTransitionBlinky.getChildren().add(delayAppearance);
        }

        seqTransitionBlinky.setOnFinished(event -> {
            isMoving = false;
            if (!CanEat) {
                Blinky = "/cr/ac/una/pac/man/images/Ghosts/Blinky.png";
            }
            velocidadBlinky = 1;
        });
        isMoving = true;
        seqTransitionBlinky.play();
    }

    private void MoverRutaPinky(List<Posicion> ruta, boolean death) {
        final Node[] nodeToRemove = {null};
        if (!isMovingPinky) {
            return;
        }
        seqTransitionPinky = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(velocidadPinky));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove;
                if (!death) {
                    PersonajeMove = CrearImagen(Pinky);
                } else {
                    PersonajeMove = CrearImagen("/cr/ac/una/pac/man/images/Ghosts/Dead.png");
                }
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && !CanEat) {
                    cancelPinky();
                    cancelInky();
                    cancelBlinky();
                    cancelClyde();
                    FlowController.setLostLive(FlowController.getLostLive() + 1);
                    FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() - 1);
                    FlowController.setAuxExperto(false);
                    VidasRestantes(FlowController.getInstance().getVidas());
                    movementTimeline.stop();
                    if (FlowController.getInstance().getVidas() == 0) {
                        FlowController.getInstance().goMain("MainView");
                    } else {
                        FlowController.getInstance().setMatrizRespaldo(MatrizRespaldo);
                        FlowController.getInstance().goMain("GameView");
                    }
                    withoutdead = false;
                }
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
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && CanEat) {
                    cancelPinky();
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
                MatrizGhost[ruta.get(index).fila][ruta.get(index).columna] = "7";
                MatrizGhost[Pinky_Fila][Pinky_Columna] = "0";
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
            seqTransitionPinky.getChildren().add(delayAppearance);
        }
        seqTransitionPinky.setOnFinished(event -> {
            isMovingPinky = false;
            if (!CanEat) {
                Pinky = "/cr/ac/una/pac/man/images/Ghosts/Pinky.png";
            }
            velocidadPinky = 1;
        });
        isMovingPinky = true;
        seqTransitionPinky.play();
    }

    private void MoverRutaInky(List<Posicion> ruta, boolean death) {
        final Node[] nodeToRemove = {null};
        if (!isMovingInky) {
            return;
        }
        seqTransitionInky = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(velocidadInky));

            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove;
                if (!death) {
                    PersonajeMove = CrearImagen(Inky);
                } else {
                    PersonajeMove = CrearImagen("/cr/ac/una/pac/man/images/Ghosts/Dead.png");
                }
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && !CanEat) {
                    cancelPinky();
                    cancelInky();
                    cancelBlinky();
                    cancelClyde();
                    FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() - 1);
                    FlowController.setLostLive(FlowController.getLostLive() + 1);
                    FlowController.setAuxExperto(false);
                    VidasRestantes(FlowController.getInstance().getVidas());
                    movementTimeline.stop();
                    if (FlowController.getInstance().getVidas() == 0) {
                        FlowController.getInstance().goMain("MainView");
                    } else {
                        FlowController.getInstance().setMatrizRespaldo(MatrizRespaldo);
                        FlowController.getInstance().goMain("GameView");
                    }
                    withoutdead = false;
                }
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
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && CanEat) {
                    cancelInky();
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
                MatrizGhost[ruta.get(index).fila][ruta.get(index).columna] = "6";
                MatrizGhost[Inky_Fila][Inky_Columna] = "0";
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
            seqTransitionInky.getChildren().add(delayAppearance);
        }
        seqTransitionInky.setOnFinished(event -> {
            isMovingInky = false;
            if (!CanEat) {
                Inky = "/cr/ac/una/pac/man/images/Ghosts/Inky.png";
                velocidadInky = 1;
            }
        });
        isMovingInky = true;
        seqTransitionInky.play();
    }

    private void MoverRutaClyde(List<Posicion> ruta, boolean death) {
        final Node[] nodeToRemove = {null};
        if (!isMovingClyde) {
            return;
        }
        seqTransitionClyde = new SequentialTransition();
        for (int i = 1; i < ruta.size(); i++) {
            final int index = i;
            PauseTransition delayAppearance = new PauseTransition(Duration.seconds(velocidadClyde));
            delayAppearance.setOnFinished(event -> {
                ImageView PersonajeMove;
                if (!death) {
                    PersonajeMove = CrearImagen(Clyde);
                } else {
                    PersonajeMove = CrearImagen("/cr/ac/una/pac/man/images/Ghosts/Dead.png");
                }
                gridGame.add(PersonajeMove, ruta.get(index).columna, ruta.get(index).fila);

                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && !CanEat) {
                    cancelPinky();
                    cancelInky();
                    cancelBlinky();
                    cancelClyde();
                    FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() - 1);
                    FlowController.setLostLive(FlowController.getLostLive() + 1);
                    FlowController.setAuxExperto(false);
                    VidasRestantes(FlowController.getInstance().getVidas());
                    movementTimeline.stop();
                    if (FlowController.getInstance().getVidas() == 0) {
                        FlowController.getInstance().goMain("MainView");
                    } else {
                        FlowController.getInstance().setMatrizRespaldo(MatrizRespaldo);
                        FlowController.getInstance().goMain("GameView");
                    }
                    withoutdead = false;
                }
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
                if (MatrizNumber[ruta.get(index).fila][ruta.get(index).columna].equals("3") && CanEat) {
                    cancelClyde();
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
                MatrizGhost[ruta.get(index).fila][ruta.get(index).columna] = "5";
                MatrizGhost[Clyde_Fila][Clyde_Columna] = "0";
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
            seqTransitionClyde.getChildren().add(delayAppearance);
        }
        seqTransitionClyde.setOnFinished(event -> {
            isMovingClyde = false;
            if (!CanEat) {
                Clyde = "/cr/ac/una/pac/man/images/Ghosts/Clyde.png";
                velocidadClyde = 1;
            }
        });
        isMovingClyde = true;
        seqTransitionClyde.play();
    }

    private void cancelBlinky() {
        if (seqTransitionBlinky != null) {
            seqTransitionBlinky.stop();
        }
    }

    private void cancelClyde() {
        if (seqTransitionClyde != null) {
            seqTransitionClyde.stop();
        }
    }

    private void cancelInky() {
        if (seqTransitionInky != null) {
            seqTransitionInky.stop();
        }
    }

    private void cancelPinky() {
        if (seqTransitionPinky != null) {
            seqTransitionPinky.stop();
        }
    }

    @FXML
    private void Power(ActionEvent events) {
        FlowController.setContadorFlash(FlowController.getContadorFlash() + 1);
        velocidadPacMan = 0.08;
        coints = 20;
        PauseTransition pausePower = new PauseTransition(Duration.seconds(6));
        pausePower.setOnFinished(event -> {
            velocidadPacMan = 0.2;
            coints = 10;
        });
        pausePower.play();
    }

    @FXML
    private void Comprar(ActionEvent event) {

        FlowController.getInstance().setPuntos(FlowController.getInstance().getPuntos() - 1500);
        puntos -= 1500;
        TextPoints.setText("" + FlowController.getInstance().getPuntos());
        FlowController.setAuxExperto(false);
        if (FlowController.getInstance().getPuntos() > 1500) {
            btnComprar.setDisable(false);
        } else {
            btnComprar.setDisable(true);
        }
        FlowController.getInstance().setVidas(FlowController.getInstance().getVidas() + 1);
        VidasRestantesMas(FlowController.getInstance().getVidas());

    }

}
