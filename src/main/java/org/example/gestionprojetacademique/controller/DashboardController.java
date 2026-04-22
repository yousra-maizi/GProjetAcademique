package org.example.gestionprojetacademique.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.gestionprojetacademique.service.AuthService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    // ── TOP BAR ──
    @FXML private Label pageTitle;
    @FXML private Label pageSubtitle;
    @FXML private Label dateLabel;
    @FXML private Label adminNameLabel;
    @FXML private Label adminRoleLabel;

    // ── NAV BUTTONS ──
    @FXML private Button btnOverview;
    @FXML private Button btnProfessors;
    @FXML private Button btnStudents;
    @FXML private Button btnArchive;

    // ── PANELS ──
    @FXML private VBox overviewPanel;
    @FXML private VBox professorsPanel;
    @FXML private VBox studentsPanel;
    @FXML private VBox archivePanel;

    // ── OVERVIEW STATS ──
    @FXML private Label statProfessors;
    @FXML private Label statStudents;
    @FXML private Label statProjects;
    @FXML private Label statArchived;
    @FXML private ListView<String> activityList;

    // ── PROFESSORS TABLE ──
    @FXML private TableView<?> professorsTable;
    @FXML private TableColumn<?, ?> profColId;
    @FXML private TableColumn<?, ?> profColNom;
    @FXML private TableColumn<?, ?> profColPrenom;
    @FXML private TableColumn<?, ?> profColEmail;
    @FXML private TableColumn<?, ?> profColSpecialite;
    @FXML private TableColumn<?, ?> profColActions;
    @FXML private TextField profSearch;
    @FXML private ComboBox<String> profFilter;

    // ── STUDENTS TABLE ──
    @FXML private TableView<?> studentsTable;
    @FXML private TableColumn<?, ?> studColId;
    @FXML private TableColumn<?, ?> studColNom;
    @FXML private TableColumn<?, ?> studColPrenom;
    @FXML private TableColumn<?, ?> studColEmail;
    @FXML private TableColumn<?, ?> studColNiveau;
    @FXML private TableColumn<?, ?> studColActions;
    @FXML private TextField studSearch;
    @FXML private ComboBox<String> studFilter;

    // ── ARCHIVE TABLE ──
    @FXML private TableView<?> archiveTable;
    @FXML private TableColumn<?, ?> archColId;
    @FXML private TableColumn<?, ?> archColTitre;
    @FXML private TableColumn<?, ?> archColType;
    @FXML private TableColumn<?, ?> archColAnnee;
    @FXML private TableColumn<?, ?> archColEtudiant;
    @FXML private TableColumn<?, ?> archColActions;
    @FXML private TextField archSearch;
    @FXML private ComboBox<String> archFilter;

    // ── ACTIVE NAV TRACKING ──
    private Button activeNavBtn;

    @FXML
    public void initialize() {
        // Set date
        dateLabel.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        // Set admin info from logged in session
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("Administrator");
        }

        // Set default active nav
        activeNavBtn = btnOverview;

        // Load overview data
        loadOverviewStats();
        loadRecentActivity();
    }

    // ═══════════════════════════════════════
    // NAV SWITCHING
    // ═══════════════════════════════════════

    @FXML
    private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Overview", "Welcome back! Here's what's happening.");
        loadOverviewStats();
    }

    @FXML
    private void showProfessors() {
        switchPanel(professorsPanel, btnProfessors, "Professors", "Manage faculty members and their details.");
        loadProfessors();
    }

    @FXML
    private void showStudents() {
        switchPanel(studentsPanel, btnStudents, "Students", "View and manage enrolled students.");
        loadStudents();
    }

    @FXML
    private void showArchive() {
        switchPanel(archivePanel, btnArchive, "Archive", "Browse and export archived academic records.");
        loadArchive();
    }

    private void switchPanel(VBox panel, Button navBtn, String title, String subtitle) {
        // Hide all panels
        overviewPanel.setVisible(false);
        professorsPanel.setVisible(false);
        studentsPanel.setVisible(false);
        archivePanel.setVisible(false);

        // Show selected
        panel.setVisible(true);

        // Update nav styles
        if (activeNavBtn != null) {
            activeNavBtn.getStyleClass().remove("nav-active");
        }
        navBtn.getStyleClass().add("nav-active");
        activeNavBtn = navBtn;

        // Update title
        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);
    }

    // ═══════════════════════════════════════
    // DATA LOADING — replace with real repo calls
    // ═══════════════════════════════════════

    private void loadOverviewStats() {
        // TODO: replace with real counts from repositories
        statProfessors.setText("12");
        statStudents.setText("148");
        statProjects.setText("34");
        statArchived.setText("87");
    }

    private void loadRecentActivity() {
        ObservableList<String> activities = FXCollections.observableArrayList(
                "🟢  New student enrolled — Ahmed Benali",
                "📝  Project submitted — Machine Learning Study",
                "👨‍🏫  Professor added — Dr. Sarah Martin",
                "🗂️  Record archived — Projet 2023-045",
                "🔄  Student updated — Fatima Zahra Alami",
                "📋  New project assigned — Web Application Dev",
                "✅  Project validated — IoT Systems Research"
        );
        activityList.setItems(activities);
    }

    private void loadProfessors() {
        // TODO: load from ProfessorRepository and bind to professorsTable
        // Example:
        // ObservableList<Professor> data = FXCollections.observableArrayList(repo.findAll());
        // professorsTable.setItems(data);
    }

    private void loadStudents() {
        // TODO: load from StudentRepository and bind to studentsTable
    }

    private void loadArchive() {
        // TODO: load from ArchiveRepository and bind to archiveTable
    }

    // ═══════════════════════════════════════
    // ACTIONS
    // ═══════════════════════════════════════

    @FXML
    private void addProfessor() {
        // TODO: open Add Professor dialog/view
        System.out.println("Add professor clicked");
    }

    @FXML
    private void addStudent() {
        // TODO: open Add Student dialog/view
        System.out.println("Add student clicked");
    }

    @FXML
    private void exportArchive() {
        // TODO: export archive to CSV/PDF
        System.out.println("Export archive clicked");
    }

    @FXML
    private void handleLogout() {
        AuthService.logout();
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/gestionprojetacademique/view/LoginView1.fxml")
            );
            Stage stage = (Stage) pageTitle.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 680));
            stage.setTitle("EHEIO administration");
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
