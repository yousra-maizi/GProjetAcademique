package ps.eheio.gestionprojetacademique.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ps.eheio.gestionprojetacademique.Exceptions.ArchiveException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.AnneeRepository;
import ps.eheio.gestionprojetacademique.model.Eheiannee;
import ps.eheio.gestionprojetacademique.service.AuthService;
import ps.eheio.gestionprojetacademique.service.AnneeService;

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

    // ── OVERVIEW ──
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
    @FXML private TableView<Eheiannee> archiveTable;
    @FXML private TableColumn<Eheiannee, String> archColAnnee;
    @FXML private TableColumn<Eheiannee, String> archColStatus;
    @FXML private TableColumn<Eheiannee, Void>   archColActions;


    // ── STATE ──
    private Button activeNavBtn;
    private AnneeRepository anneeRepository = new AnneeRepository();
    private AnneeService anneeservice = new AnneeService();

    // ═══════════════════════════════════════════════════════
    // INIT
    // ═══════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        //anneeRepository.findAll();
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("Administrateur");
        }

        activeNavBtn = btnOverview;
        loadOverviewStats();
        loadRecentActivity();
    }

    // ═══════════════════════════════════════════════════════
    // NAV SWITCHING
    // ═══════════════════════════════════════════════════════
    @FXML private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Acceuil", "Welcome back! Here's what's happening.");
        loadOverviewStats();
    }

    @FXML private void showProfessors() {
        switchPanel(professorsPanel, btnProfessors, "Professors", "Manage faculty members and their details.");
        loadProfessors();
    }

    @FXML private void showStudents() {
        switchPanel(studentsPanel, btnStudents, "Students", "View and manage enrolled students.");
        loadStudents();
    }

    @FXML private void showArchive() {
        switchPanel(archivePanel, btnArchive, "Archive", "Manage and consult academic year records.");
        loadArchive();
    }

    private void switchPanel(VBox panel, Button navBtn, String title, String subtitle) {
        overviewPanel.setVisible(false);
        professorsPanel.setVisible(false);
        studentsPanel.setVisible(false);
        archivePanel.setVisible(false);
        panel.setVisible(true);

        if (activeNavBtn != null) activeNavBtn.getStyleClass().remove("nav-active");
        navBtn.getStyleClass().add("nav-active");
        activeNavBtn = navBtn;

        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);
    }

    // ═══════════════════════════════════════════════════════
    // DATA LOADING
    // ═══════════════════════════════════════════════════════
    private void loadOverviewStats() {
        // TODO: replace with real repository counts
        statProfessors.setText("12");
        statStudents.setText("148");
        statProjects.setText("34");
        statArchived.setText("87");
    }

    private void loadRecentActivity() {
        activityList.setItems(FXCollections.observableArrayList(
                "🟢  New student enrolled — Ahmed Benali",
                "📝  Project submitted — Machine Learning Study",
                "👨‍🏫  Professor added — Dr. Sarah Martin",
                "🗂️  Record archived — Année 2022-2023",
                "🔄  Student updated — Fatima Zahra Alami",
                "📋  New project assigned — Web Application Dev",
                "✅  Project validated — IoT Systems Research"
        ));
    }

    private void loadProfessors() {
        // TODO: bind ProfessorRepository.findAll() to professorsTable
    }

    private void loadStudents() {
        // TODO: bind StudentRepository.findAll() to studentsTable
    }

    private void loadArchive() {
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // boutons dynamiques
        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }

                Eheiannee annee = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8);

                if (annee.isActive()) {
                    Button btnArchiver = new Button("🗂  Archiver");
                    btnArchiver.setOnAction(e -> handleArchiver(annee));
                    box.getChildren().add(btnArchiver);
                }

                Button btnConsulter = new Button("👁  Consulter");
                btnConsulter.setOnAction(e -> handleConsulter(annee));
                box.getChildren().add(btnConsulter);

                setGraphic(box);
            }
        });

        // données réelles depuis les BDs
        ObservableList<Eheiannee> data = FXCollections.observableArrayList(
                anneeservice.getAllAnnees()
        );
        archiveTable.setItems(data);
        System.out.println("208 dash Annees trouvées: " + data.size());
    }

    // ═══════════════════════════════════════════════════════
    // ARCHIVE ACTIONS
    // ═══════════════════════════════════════════════════════
    //btn archiver
    private void handleArchiver(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Archiver l'année");
        confirm.setHeaderText("Archiver " + annee.getLibelle() + " ?");
        confirm.setContentText("Une nouvelle BD sera créée pour l'année suivante.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    anneeservice.archiverAnneeActive();
                    loadArchive();
                } catch (ArchiveException e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Archivage échoué: " + e.getMessage()
                    ).show();
                }catch (DatabaseException e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Erreur de connexion: " + e.getMessage()
                    ).show();
                }
            }
        });
    }

    private void handleConsulter(Eheiannee row) {
        // TODO: open a detail view for this year's projects
       // System.out.println("Consulting year: " + row.getAnnee());
    }

    // ═══════════════════════════════════════════════════════
    // OTHER ACTIONS
    // ═══════════════════════════════════════════════════════
    @FXML private void addProfessor() {
        // TODO: open Add Professor dialog
        System.out.println("Add professor clicked");
    }

    @FXML private void addStudent() {
        // TODO: open Add Student dialog
        System.out.println("Add student clicked");
    }

    @FXML private void handleLogout() {
        AuthService.logout();
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ps/eheio/gestionprojetacademique/view/LoginView1.fxml")
            );
            Stage stage = (Stage) pageTitle.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 680));
            stage.setTitle("EHEIO Administration");
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
