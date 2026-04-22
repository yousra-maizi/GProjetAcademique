package org.example.gestionprojetacademique.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.io.IOException;

/**
 * LoginController — wires LoginView.fxml to AuthService.
 * Handles input validation, async login, error feedback, and navigation.
 */
public class cons {

    // ─── FXML Bindings ───────────────────────────
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private ProgressIndicator loadingSpinner;

    // ─── Services ────────────────────────────────
   /* private final AuthService authService = new AuthService();

    // ─────────────────────────────────────────────
    //  Initialization
    // ─────────────────────────────────────────────

    @FXML
    public void initialize() {
        // Hide feedback elements initially
        errorLabel.setVisible(false);
        loadingSpinner.setVisible(false);

        // Allow Enter key to trigger login from any field
        usernameField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);

        // Clear error when user starts typing again
        usernameField.textProperty().addListener((obs, o, n) -> clearError());
        passwordField.textProperty().addListener((obs, o, n) -> clearError());
    }

    // ─────────────────────────────────────────────
    //  Event Handlers
    // ─────────────────────────────────────────────

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Client-side validation
        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        // Run auth in background thread to keep UI responsive
        setLoading(true);

        Task<Admin> loginTask = new Task<>() {
            @Override
            protected Admin call() {
                return authService.login(username, password);
            }
        };

        loginTask.setOnSucceeded(event -> {
            Admin admin = loginTask.getValue();
            setLoading(false);
            if (admin != null) {
                navigateToDashboard(admin);
            } else {
                showError("Invalid username or password.");
                passwordField.clear();
                passwordField.requestFocus();
            }
        });

        loginTask.setOnFailed(event -> {
            setLoading(false);
            showError("Connection error. Check database settings.");
        });

        new Thread(loginTask).start();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    // ─────────────────────────────────────────────
    //  Private Helpers
    // ─────────────────────────────────────────────

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private void setLoading(boolean loading) {
        loginButton.setDisable(loading);
        loadingSpinner.setVisible(loading);
        usernameField.setDisable(loading);
        passwordField.setDisable(loading);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void clearError() {
        errorLabel.setVisible(false);
    }

    private void navigateToDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/DashboardView.fxml")
            );
            Parent root = loader.load();

            // Pass admin to dashboard controller
            DashboardController dashboardController = loader.getController();
            dashboardController.initAdmin(admin);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setTitle("Academic Projects — Dashboard");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showError("Failed to load dashboard. Contact support.");
            System.err.println("[LoginController] Navigation error: " + e.getMessage());
        }
    }

    */
}
