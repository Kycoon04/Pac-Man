/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.TextCSV;
import cr.ac.una.pac.man.util.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainView.toFront();
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
        user.setGhosteat("2");
        text.saveUserToFile(user, "user.txt");
    }
    
}
