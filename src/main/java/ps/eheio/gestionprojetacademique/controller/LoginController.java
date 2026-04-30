package ps.eheio.gestionprojetacademique.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ps.eheio.gestionprojetacademique.service.AuthService;

public class LoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    //private final AuthService authService = new AuthService();
    //private AuthService authService;
    @FXML
    public void initialize() {
        passwordField.setOnAction(event -> handleLogin());
        errorLabel.setText("");
    }

    @FXML
    private void handleLogin() {
       try {
            String login = loginField.getText().trim();
            String password = passwordField.getText();

           /* if (login.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }*/
           if (login.isEmpty()) {
               errorLabel.setText("Veuillez entrer le login");
               loginField.requestFocus();
               return;
           }
           if (password.isEmpty()) {
               errorLabel.setText("Veuillez entrer le mot de passe");
               passwordField.requestFocus();
               return;
           }

            loginButton.setDisable(true);
            loginButton.setText("Logging in...");
            // Get the single shared instance
            boolean success = AuthService.getInstance().auth(login, password);

            if (success) {
                loadDashboard();
            } else {
                errorLabel.setText("Invalid login or password.");
                passwordField.clear();
                loginButton.setDisable(false);
                loginButton.setText("Login");
            }
        }catch(Exception ex){}
    }

    private void loadDashboard() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ps/eheio/gestionprojetacademique/view/DashboardView1.fxml")
            );
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 700));
            stage.setTitle("EHEIO administration");
            stage.setResizable(true);
        } catch (Exception e) {
            errorLabel.setText("Failed to load dashboard");
            loginButton.setDisable(false);
            loginButton.setText("Login");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleExit() {
        Platform.exit();
    }

}
