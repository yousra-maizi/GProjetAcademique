package ps.eheio.gestionprojetacademique;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionFactory;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.service.AuthService;

public class mainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // DEBUG — paste this temporarily
        /*var url = getClass().getResource("/ps/eheio/gestionprojetacademique/styles/login.css");
        System.out.println("CSS found at: " + url);*/
        // Connect to DB immediately when app launches
        //DBConnection.getConnection();
        // AuthService instance created
       // AuthService.getInstance();

        // 1 — lire config.properties et se connecter sur la BD active
        ConnectionFactory.initialize();

        // 2 — préparer la session
        AuthService.getInstance();
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
                ConnectionFactory.closeAll();
            } catch (ConnectionException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
        });
    }
}