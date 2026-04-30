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
import ps.eheio.gestionprojetacademique.Repository.EheianneeRepository;
import ps.eheio.gestionprojetacademique.model.Eheiannee;
import ps.eheio.gestionprojetacademique.service.AuthService;
import ps.eheio.gestionprojetacademique.service.EheianneeService;

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
    private EheianneeRepository anneeRepository = new EheianneeRepository();
    private EheianneeService anneeservice = new EheianneeService();

    // ═══════════════════════════════════════════════════════
    // INIT
    // ═══════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        anneeRepository.listAnnees();
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("Administrator");
        }

        activeNavBtn = btnOverview;
        loadOverviewStats();
        loadRecentActivity();
    }

    // ═══════════════════════════════════════════════════════
    // NAV SWITCHING
    // ═══════════════════════════════════════════════════════
    @FXML private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Overview", "Welcome back! Here's what's happening.");
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
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Eheiannee row = getTableView().getItems().get(getIndex());
                HBox box = new HBox(10);

                Button consulter = new Button("Consulter");
                consulter.setOnAction(e -> handleConsulter(row));
                box.getChildren().add(consulter);

                if (row.isActive()) {
                    Button archiver = new Button("Archiver");
                    archiver.setOnAction(e -> handleArchiver(row));
                    box.getChildren().add(archiver);
                }

                setGraphic(box);
            }
        });

        ObservableList<Eheiannee> rows =
                FXCollections.observableArrayList(anneeservice .getAnnees());

        archiveTable.setItems(rows);
    }

    // ═══════════════════════════════════════════════════════
    // ARCHIVE ACTIONS
    // ═══════════════════════════════════════════════════════
    //btn archiver
    private void handleArchiver(Eheiannee row) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Archiver l'année");
        confirm.setHeaderText("Archiver " + row.getAnnee() + " ?");
        confirm.setContentText("Cette action archivera tous les données de cette année universitaire.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: call ArchiveService.archiveAnnee(row.getAnnee())
                System.out.println("Archiving year: " + row.getAnnee());
                loadArchive(); // refresh table
            }
        });
    }

    private void handleConsulter(Eheiannee row) {
        // TODO: open a detail view for this year's projects
        System.out.println("Consulting year: " + row.getAnnee());
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
