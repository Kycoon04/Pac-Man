package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.Mensaje;
import cr.ac.una.pac.man.util.TextCSV;
import cr.ac.una.pac.man.util.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jomav
 */
public class MainViewController extends Controller implements Initializable {

    @FXML
    private BorderPane MainView;
    @FXML
    private BorderPane PlayerView;
    @FXML
    private TextField NameField;
    @FXML
    private Button btnDelete;
    @FXML
    private ImageView iconCazador;
    @FXML
    private ImageView iconClasico;
    @FXML
    private ImageView iconEncierro;
    @FXML
    private ImageView iconExperto;
    @FXML
    private ImageView iconFlash;
    @FXML
    private ImageView iconRey;
    @FXML
    private BorderPane DifficultyView;
    @FXML
    private Button Facil;
    @FXML
    private Button Medio;
    @FXML
    private Button Dificil;
    @FXML
    private BorderPane LevelView;
    @FXML
    private Button Level1;
    @FXML
    private Button Level2;
    @FXML
    private Button Level3;
    @FXML
    private Button Level4;
    @FXML
    private Button Level5;
    @FXML
    private Button Level6;
    @FXML
    private Button Level7;
    @FXML
    private Button Level8;
    @FXML
    private Button Level9;
    @FXML
    private Button Level10;
    @FXML
    private Button btnUpdate;
    @FXML
    private Text pointsWin;

    User player;
    @FXML
    private Pane WinView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.9);
        MainView.toFront();
        iconCazador.setEffect(colorAdjust);
        iconClasico.setEffect(colorAdjust);
        iconEncierro.setEffect(colorAdjust);
        iconExperto.setEffect(colorAdjust);
        iconFlash.setEffect(colorAdjust);
        iconRey.setEffect(colorAdjust);
        if (FlowController.isClasico()) {
            FlowController.getInstance().getUsuario().getTrophies().add("2");
        }
        if (FlowController.isExperto()) {
            FlowController.getInstance().getUsuario().getTrophies().add("4");
        }
        if (FlowController.getContadorEncierro() == 5) {
            FlowController.getInstance().getUsuario().getTrophies().add("3");
        }
        if (FlowController.getContadorFlash() == 5) {
            FlowController.getInstance().getUsuario().getTrophies().add("5");
        }
        if (FlowController.getContadorFastantas() == 5) {
            FlowController.getInstance().getUsuario().getTrophies().add("1");
        }
        if (FlowController.getRey() == 10) {
            FlowController.getInstance().getUsuario().getTrophies().add("6");
        }
        if (FlowController.getInstance().getUsuario() != null) {
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            FlowController.getInstance().getUsuario().setPointWin(""+FlowController.getPuntos());
            FlowController.getInstance().getUsuario().setLivesLose(""+FlowController.getLostLive());
            SelectTrophies(FlowController.getInstance().getUsuario());
        } else {
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
        }
        switch (FlowController.getNivel()) {
            case 1:
                Level2.setDisable(true);
                Level3.setDisable(true);
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 2:
                Level3.setDisable(true);
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 3:
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 4:
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 5:
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 6:
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 7:
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 8:
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 9:
                Level10.setDisable(true);
                break;
        }
        if (FlowController.isWin()) {
            FlowController.setWin(false);
            pointsWin.setText(Integer.toString(FlowController.getPuntosWin()));
            WinView.toFront();
        }
    }

    @FXML
    private void Game(ActionEvent event) {
        DifficultyView.toFront();
    }

    @FXML
    private void Player(ActionEvent event) {
        PlayerView.toFront();
    }

    @FXML
    private void settings(ActionEvent event) {
    }

    @FXML
    private void exit(ActionEvent event) {
        FlowController.getInstance().salir();
    }

    @FXML
    private void SummitPlayer(ActionEvent event) throws IOException {
        TextCSV text = new TextCSV();
        User user = new User();
        user.setName(NameField.getText());
        user.getTrophies().add("1");
        user.getTrophies().add("2");
        user.getTrophies().add("3");
        user.getTrophies().add("4");
        text.saveUserToFile(user, "user.txt");
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Se registro correctamente el jugador");
    }

    @FXML
    private void LoadPlayer(ActionEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.9);
        iconCazador.setEffect(colorAdjust);
        iconClasico.setEffect(colorAdjust);
        iconEncierro.setEffect(colorAdjust);
        iconExperto.setEffect(colorAdjust);
        iconFlash.setEffect(colorAdjust);
        iconRey.setEffect(colorAdjust);
        TextCSV text = new TextCSV();
        player = text.LoadUser(NameField.getText(), "user.txt");
        if (player == null) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", FlowController.getMainStage(), "Seleccione una jugador correcto");
            NameField.setText("");
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
        } else {
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Selecionaste al jugador " + player.getName());
            SelectTrophies(player);
        }
        FlowController.getInstance().setUsuario(player);
        if(FlowController.getInstance().getUsuario().getNivel().size()==0){
        FlowController.setNivel(1);
        }else{
        FlowController.setNivel(FlowController.getInstance().getUsuario().getNivel().size());
        }
                switch (FlowController.getNivel()) {
            case 1:
                Level2.setDisable(true);
                Level3.setDisable(true);
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 2:
                Level3.setDisable(true);
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 3:
                Level4.setDisable(true);
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 4:
                Level5.setDisable(true);
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 5:
                Level6.setDisable(true);
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 6:
                Level7.setDisable(true);
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 7:
                Level8.setDisable(true);
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 8:
                Level9.setDisable(true);
                Level10.setDisable(true);
                break;
            case 9:
                Level10.setDisable(true);
                break;
        }
        
    }

    public void SelectTrophies(User user) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        for (int i = 0; i < user.getTrophies().size(); i++) {
            switch (user.getTrophies().get(i)) {
                case "1":
                    iconCazador.setEffect(colorAdjust);
                    break;
                case "2":
                    iconClasico.setEffect(colorAdjust);
                    break;
                case "3":
                    iconEncierro.setEffect(colorAdjust);
                    break;
                case "4":
                    iconExperto.setEffect(colorAdjust);
                    break;
                case "5":
                    iconFlash.setEffect(colorAdjust);
                    break;
                case "6":
                    iconRey.setEffect(colorAdjust);
                    break;
            }
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        MainView.toFront();
    }

    @FXML
    private void DeleteInfo(ActionEvent event) {
        User usernew = new User();
        usernew.setName(player.getName());
        TextCSV text = new TextCSV();
        text.saveUserToFile(usernew, "user.txt");
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Borraste la informacion de " + player.getName());
        btnDelete.setDisable(true);
    }

    @FXML
    private void SelectDifficulty(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button botonPresionado = (Button) source;
            if ("Facil".equals(botonPresionado.getId())) {
                FlowController.setDifficulty(1);
            } else if ("Medio".equals(botonPresionado.getId())) {
                FlowController.setDifficulty(2);
            } else if ("Dificil".equals(botonPresionado.getId())) {
                FlowController.setDifficulty(3);
            }
        }
        LevelView.toFront();
    }

    @FXML
    private void LevelSelect(ActionEvent event) {
        String buttonId = ((Button) event.getSource()).getId();
        int nivel = switch (buttonId) {
            case "Level1" ->
                1;
            case "Level2" ->
                2;
            case "Level3" ->
                3;
            case "Level4" ->
                4;
            case "Level5" ->
                5;
            case "Level6" ->
                6;
            case "Level7" ->
                7;
            case "Level8" ->
                8;
            case "Level9" ->
                9;
            case "Level10" ->
                10;
            default ->
                0;
        };
        FlowController.setNivel(nivel);
        FlowController.getInstance().goMain("GameView");
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void Update(ActionEvent event) {
        TextCSV text = new TextCSV();
        text.saveUserToFile(FlowController.getInstance().getUsuario(), "user.txt");
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Se actualizo correctamente el jugador");
        SelectTrophies(FlowController.getInstance().getUsuario());
    }

    @FXML
    private void CloseWin(ActionEvent event) {
        MainView.toFront();
    }
}
