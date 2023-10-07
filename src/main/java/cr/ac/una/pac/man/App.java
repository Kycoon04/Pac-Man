package cr.ac.una.pac.man;

import cr.ac.una.pac.man.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        stage.setTitle("Sokoban");
        FlowController.getInstance().goMain();
    }
}