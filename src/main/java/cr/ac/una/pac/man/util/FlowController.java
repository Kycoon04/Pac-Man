/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr.ac.una.pac.man.util;

import cr.ac.una.pac.man.App;
import cr.ac.una.pac.man.controller.Controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FlowController {

    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();
    private static Controller controller;
    private static int Difficulty;
    private static int Nivel=1;
    private User usuario ;
    static boolean Importar=false;
    static String[][] MatrizRespaldo = new String[13][13];
    static int vidas = 6;
    static boolean AuxExperto = true;
    static boolean isPause= false;
    static int Puntos = 0;
    static int PuntosWin = 0;
    static boolean Win = false;
    static boolean clasico=false;
    static boolean Experto = false;
    static int ContadorEncierro=0;
    static int ContadorFlash=0;
    static int ContadorFastantas=0;
    static String TimeTotal="";
    static int Rey;
    static int LostLive;
    static int nivelTotal=1;
    static int BestTime=0;
    static int puntosXpartidaMax=0;
    private FlowController() {
    }

    public User getUsuario() {
        return usuario;
    }

    public static int getPuntosXpartidaMax() {
        return puntosXpartidaMax;
    }

    public static void setPuntosXpartidaMax(int puntosXpartidaMax) {
        FlowController.puntosXpartidaMax = puntosXpartidaMax;
    }

    public static int getBestTime() {
        return BestTime;
    }

    public static void setBestTime(int BestTime) {
        FlowController.BestTime = BestTime;
    }

    public static int getNivelTotal() {
        return nivelTotal;
    }

    public static void setNivelTotal(int nivelTotal) {
        FlowController.nivelTotal = nivelTotal;
    }

    public static String getTimeTotal() {
        return TimeTotal;
    }

    public static void setTimeTotal(String TimeTotal) {
        FlowController.TimeTotal = TimeTotal;
    }

    public static int getLostLive() {
        return LostLive;
    }

    public static void setLostLive(int LostLive) {
        FlowController.LostLive = LostLive;
    }

    public static int getRey() {
        return Rey;
    }

    public static void setRey(int Rey) {
        FlowController.Rey = Rey;
    }

    public static int getContadorFastantas() {
        return ContadorFastantas;
    }

    public static void setContadorFastantas(int ContadorFastantas) {
        FlowController.ContadorFastantas = ContadorFastantas;
    }

    public static int getContadorFlash() {
        return ContadorFlash;
    }

    public static void setContadorFlash(int ContadorFlash) {
        FlowController.ContadorFlash = ContadorFlash;
    }

    public static int getContadorEncierro() {
        return ContadorEncierro;
    }

    public static void setContadorEncierro(int ContadorEncierro) {
        FlowController.ContadorEncierro = ContadorEncierro;
    }

    public static boolean isAuxExperto() {
        return AuxExperto;
    }

    public static void setAuxExperto(boolean AuxExperto) {
        FlowController.AuxExperto = AuxExperto;
    }

    public static boolean isExperto() {
        return Experto;
    }

    public static void setExperto(boolean Experto) {
        FlowController.Experto = Experto;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public static boolean isWin() {
        return Win;
    }

    public static boolean isClasico() {
        return clasico;
    }

    public static void setClasico(boolean clasico) {
        FlowController.clasico = clasico;
    }

    public static void setWin(boolean Win) {
        FlowController.Win = Win;
    }

    public static int getVidas() {
        return vidas;
    }

    public static int getPuntosWin() {
        return PuntosWin;
    }

    public static void setPuntosWin(int PuntosWin) {
        FlowController.PuntosWin = PuntosWin;
    }

    public static boolean isIsPause() {
        return isPause;
    }

    public static int getPuntos() {
        return Puntos;
    }

    public static void setPuntos(int Puntos) {
        FlowController.Puntos = Puntos;
    }

    public static void setIsPause(boolean isPause) {
        FlowController.isPause = isPause;
    }

    public static void setVidas(int vidas) {
        FlowController.vidas = vidas;
    }

    public static String[][] getMatrizRespaldo() {
        return MatrizRespaldo;
    }

    public static void setMatrizRespaldo(String[][] MatrizRespaldo) {
        FlowController.MatrizRespaldo = MatrizRespaldo;
    }


    public static int getNivel() {
        return Nivel;
    }

    public static void setNivel(int Nivel) {
        FlowController.Nivel = Nivel;
    }

    public int getDifficulty() {
        return Difficulty;
    }

    public static void setDifficulty(int Difficulty) {
        FlowController.Difficulty = Difficulty;
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        this.mainStage = stage;
        this.idioma = idioma;
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource("/cr/ac/una/pac/man/view/" + name + ".fxml"), this.idioma);
                        loader.load();
                        loaders.put(name, loader);
                    } catch (Exception ex) {
                        loader = null;
                        java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Creando loader [" + name + "].", ex);
                    }
                }
            }
        }
        if (!name.equals("LogIng2")) {
            this.controller = loader.getController();
        }
        return loader;
    }

    public void goMain(String MainView) {
        try {
            this.mainStage.setScene(new Scene(FXMLLoader.load(App.class.getResource("/cr/ac/una/pac/man/view/"+MainView+".fxml"), this.idioma)));
            this.mainStage.show();
            mainStage.setResizable(false);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Error inicializando la vista base.", ex);
        }
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
    }

    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }
    public static boolean isImportar() {
        return Importar;
    }

    public static void setImportar(boolean Importar) {
        FlowController.Importar = Importar;
    }
    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        Stage stage = controller.getStage();
        if (stage == null) {
            stage = this.mainStage;
            controller.setStage(stage);
        }
        switch (location) {
            case "Center":
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().clear();
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().add(loader.getRoot());
                break;
            case "Top":
                break;
            case "Bottom":
                break;
            case "Right":
                break;
            case "Left":
                break;
            default:
                break;
        }
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/unaplanilla/resources/Usuario-48.png")));
        stage.setTitle("UNA PLANILLA");
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    public void goLogInWindowModal(Boolean resizable) {
        goViewInWindowModal("LogInView", this.controller.getStage(), resizable);
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/unaplanilla/resources/Usuario-48.png")));
        stage.setTitle("UNA PLANILLA");
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }
    
    public void initialize() {
        this.loaders.clear();
    }

    public void salir() {
        this.mainStage.close();
    }

}