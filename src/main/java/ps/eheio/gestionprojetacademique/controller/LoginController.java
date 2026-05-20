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
import ps.eheio.gestionprojetacademique.Exceptions.AuthenticationException;
import ps.eheio.gestionprojetacademique.Exceptions.LoginException;
import ps.eheio.gestionprojetacademique.Exceptions.MdpException;
import ps.eheio.gestionprojetacademique.service.AuthentificationService;

public class LoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
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

            if (login.isEmpty()) {
                errorLabel.setText("Veuillez entrer le login");
                return;
            }

            if (password.isEmpty()) {
                errorLabel.setText("Veuillez entrer le mot de passe");
                return;
            }

            loginButton.setDisable(true);
            loginButton.setText("Logging in...");

            AuthentificationService.getInstance().authentification(login, password);

            loadDashboard();

        } catch (LoginException e) {
            errorLabel.setText(e.getMessage());

        } catch (MdpException e) {
            errorLabel.setText(e.getMessage());
            passwordField.clear();

        } catch (AuthenticationException e) {
            errorLabel.setText(e.getMessage());

        } catch (Exception e) {
            errorLabel.setText("Erreur technique.");
            e.printStackTrace();

        } finally {
            loginButton.setDisable(false);
            loginButton.setText("Login");
        }
    }

    private void loadDashboard() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ps/eheio/gestionprojetacademique/view/DashboardView.fxml")
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
