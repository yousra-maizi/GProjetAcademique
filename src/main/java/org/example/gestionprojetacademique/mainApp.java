package org.example.gestionprojetacademique;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.example.gestionprojetacademique.ConnectionDB.DBConnection;
import org.example.gestionprojetacademique.service.AuthService;
public class mainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // DEBUG — paste this temporarily
        var url = getClass().getResource("/org/example/gestionprojetacademique/styles/login.css");
        System.out.println("CSS found at: " + url);
        // Connect to DB immediately when app launches
        DBConnection.getConnection();
        // AuthService instance created
        AuthService.getInstance();

        //loading the FXML file
        Parent root = FXMLLoader.load(
                getClass().getResource("/org/example/gestionprojetacademique/view/LoginView1.fxml")
        );

        // Set app icon
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/org/example/gestionprojetacademique/assets/eheiologo.png")
        ));
        //stage.getIcons().add(icon);
        stage.setTitle("EHEIO administration");
        //stage.setIconified().add();
        stage.setScene(new Scene(root, 1100, 680));
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            DBConnection.closeConnection();
            Platform.exit();
        });
    }
}