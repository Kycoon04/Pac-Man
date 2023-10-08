/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.Mensaje;
import cr.ac.una.pac.man.util.TextCSV;
import cr.ac.una.pac.man.util.User;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jomav
 */
public class MainViewController implements Initializable {

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

    User player;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.9);
        MainView.toFront();
        btnDelete.setDisable(true);
        iconCazador.setEffect(colorAdjust);
        iconClasico.setEffect(colorAdjust);
        iconEncierro.setEffect(colorAdjust);
        iconExperto.setEffect(colorAdjust);
        iconFlash.setEffect(colorAdjust);
        iconRey.setEffect(colorAdjust);

        Level2.setDisable(true);
        Level3.setDisable(true);
        Level4.setDisable(true);
        Level5.setDisable(true);
        Level6.setDisable(true);
        Level7.setDisable(true);
        Level8.setDisable(true);
        Level9.setDisable(true);
        Level10.setDisable(true);
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
        MainView.toFront();
        TextCSV text = new TextCSV();
        User user = new User();
        user.setName(NameField.getText());
        user.getTrophies().add("1");
        user.getTrophies().add("2");
        user.getTrophies().add("3");
        user.getTrophies().add("4");
        text.saveUserToFile(user, "user.txt");
    }

    @FXML
    private void LoadPlayer(ActionEvent event) {
        TextCSV text = new TextCSV();
        player = text.LoadUser(NameField.getText(), "user.txt");
        if (player == null) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", FlowController.getMainStage(), "Seleccione una jugador correcto");
        } else {
            btnDelete.setDisable(false);
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Selecionaste al jugador " + player.getName());
            SelectTrophies();
        }
    }

    public void SelectTrophies() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        for (int i = 0; i < player.getTrophies().size(); i++) {
            switch (player.getTrophies().get(i)) {
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
        Button pressedButton = (Button) event.getSource();
        int nivel = 0;
        Level1.getStyleClass().remove("nivel-seleccionado");
        Level2.getStyleClass().remove("nivel-seleccionado");
        Level3.getStyleClass().remove("nivel-seleccionado");
        Level4.getStyleClass().remove("nivel-seleccionado");
        Level5.getStyleClass().remove("nivel-seleccionado");
        Level6.getStyleClass().remove("nivel-seleccionado");
        Level7.getStyleClass().remove("nivel-seleccionado");
        Level8.getStyleClass().remove("nivel-seleccionado");
        Level9.getStyleClass().remove("nivel-seleccionado");
        Level10.getStyleClass().remove("nivel-seleccionado");
        switch (pressedButton.getId()) {
            case "Level1":
                nivel = 1;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level2":
                nivel = 2;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level3":
                nivel = 3;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level4":
                nivel = 4;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level5":
                nivel = 5;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level6":
                nivel = 6;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level7":
                nivel = 7;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level8":
                nivel = 8;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level9":
                nivel = 9;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            case "Level10":
                nivel = 10;
                pressedButton.getStyleClass().add("nivel-seleccionado");
                break;
            default:
                break;
        }
    }

}
