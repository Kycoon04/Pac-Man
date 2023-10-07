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
    
    User player;
    @FXML
    private Button btnDelete;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainView.toFront();
        btnDelete.setDisable(true);
    }

    @FXML
    private void Game(ActionEvent event) {
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
        text.saveUserToFile(user, "user.txt");
    }

    @FXML
    private void LoadPlayer(ActionEvent event) {
        TextCSV text = new TextCSV();
        player = text.LoadUser(NameField.getText(), "user.txt");
        if(player == null){
        new Mensaje().showModal(Alert.AlertType.ERROR, "Error", FlowController.getMainStage(), "Seleccione una jugador correcto");
        }else{
        btnDelete.setDisable(false);
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Selecionaste al jugador "+player.getName());
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
        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Aviso", FlowController.getMainStage(), "Borraste la informacion de "+player.getName());
        btnDelete.setDisable(true);
    }
}
