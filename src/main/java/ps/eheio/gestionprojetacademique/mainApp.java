package ps.eheio.gestionprojetacademique;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.service.AuthentificationService;

public class mainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {



        ConnectionManager.initialize();


        AuthentificationService.getInstance();
        //loading the FXML file
        Parent root = FXMLLoader.load(
                getClass().getResource("/ps/eheio/gestionprojetacademique/view/LoginView1.fxml")
        );

        // Set app icon
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/ps/eheio/gestionprojetacademique/assets/eheiologo.png")
        ));
        //stage.getIcons().add(icon);
        stage.setTitle("EHEIO administration");
        //stage.setIconified().add();
        stage.setScene(new Scene(root, 1100, 680));
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            try {
                ConnectionManager.closeAll();
            } catch (ConnectionException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
        });
    }
}