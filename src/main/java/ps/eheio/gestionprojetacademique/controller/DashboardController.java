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
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionFactory;
import ps.eheio.gestionprojetacademique.Exceptions.ArchiveException;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.AnneeRepository;
import ps.eheio.gestionprojetacademique.Repository.TacheRepository;
import ps.eheio.gestionprojetacademique.model.Eheiannee;
import ps.eheio.gestionprojetacademique.model.Etudiant;
import ps.eheio.gestionprojetacademique.model.Groupe;
import ps.eheio.gestionprojetacademique.model.Tache;
import ps.eheio.gestionprojetacademique.service.AuthService;
import ps.eheio.gestionprojetacademique.service.AnneeService;
import ps.eheio.gestionprojetacademique.service.GroupeService;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    // ── TOP BAR ──
    @FXML private Label pageTitle;
    @FXML private Label pageSubtitle;
    @FXML private Label dateLabel;
    @FXML private Label adminNameLabel;
    @FXML private Label adminRoleLabel;

    // ── NAV BUTTONS ──
    @FXML private Button btnOverview;
   /* @FXML private Button btnProfessors;
    @FXML private Button btnStudents;*/
    @FXML private Button btnArchive;

    // ── PANELS ──
    @FXML private VBox overviewPanel;
   /* @FXML private VBox professorsPanel;
    @FXML private VBox studentsPanel;*/
    @FXML private VBox archivePanel;

    // ── OVERVIEW ──
    @FXML private Label statProfessors;
    @FXML private Label statStudents;
    @FXML private Label statProjects;
    @FXML private Label statArchived;
    //@FXML private ListView<String> activityList;

    // ── PROFESSORS TABLE ──
    /*@FXML private TableView<?> professorsTable;
    @FXML private TableColumn<?, ?> profColId;
    @FXML private TableColumn<?, ?> profColNom;
    @FXML private TableColumn<?, ?> profColPrenom;
    @FXML private TableColumn<?, ?> profColEmail;
    @FXML private TableColumn<?, ?> profColSpecialite;
    @FXML private TableColumn<?, ?> profColActions;
    @FXML private TextField profSearch;
    @FXML private ComboBox<String> profFilter;
*/
    // ── STUDENTS TABLE ──
   /* @FXML private TableView<?> studentsTable;
    @FXML private TableColumn<?, ?> studColId;
    @FXML private TableColumn<?, ?> studColNom;
    @FXML private TableColumn<?, ?> studColPrenom;
    @FXML private TableColumn<?, ?> studColEmail;
    @FXML private TableColumn<?, ?> studColNiveau;
    @FXML private TableColumn<?, ?> studColActions;
    @FXML private TextField studSearch;
    @FXML private ComboBox<String> studFilter;
*/
    @FXML private Label welcomeEyebrow;
    // ── ARCHIVE TABLE ──
    @FXML private TableView<Eheiannee> archiveTable;
    @FXML private TableColumn<Eheiannee, String> archColAnnee;
    @FXML private TableColumn<Eheiannee, String> archColStatus;
    @FXML private TableColumn<Eheiannee, Void>   archColActions;
    // ── nouveaux champs FXML ──
    @FXML private Label badgeAnnee;
    @FXML private Label welcomeTitle;
    /*@FXML private Label welcomeEyebrow;
    @FXML private Label statGroupes;
    @FXML private Label statProjets;
    @FXML private Label statEtudiants;
    @FXML private Label statArchives;*/

    // panel groupes
    @FXML private VBox groupesPanel;
    @FXML private Button btnGroupes;
   // @FXML private TableView<Groupe> groupesTable;
  //  @FXML private TableColumn<Groupe, Integer> grpColId;
    @FXML private TableColumn<Groupe, String>  grpColNom;
    @FXML private TableColumn<Groupe, String>  grpColProjet;
    @FXML private TableColumn<Groupe, Void>    grpColActions;
    @FXML private TextField groupSearch;

    // ── GROUPES TABLE ──
    @FXML private TableView<Groupe> groupesTable;
   //---- membre panel
   @FXML private VBox   membresPanel;
    @FXML private Label  membresTitre;
    @FXML private Label  membresProjet;
    @FXML private Button btnRetourGroupes;

    @FXML private TableView<Etudiant>       membresTable;
    @FXML private TableColumn<Etudiant, String>  memColNom;
    @FXML private TableColumn<Etudiant, String>  memColPrenom;
    @FXML private TableColumn<Etudiant, String> memColClasse;
    @FXML private TableColumn<Etudiant, String> memColNiveau;
    // ── TACHES TABLE ──
    @FXML private TableView<Tache>           tachesTable;
    @FXML private TableColumn<Tache, String> tacheColTitre;
    @FXML private TableColumn<Tache, String> tacheColProf;
    @FXML private TableColumn<Tache, String> tacheColDesc;
    @FXML private TableColumn<Tache, String> tacheColStatut;
    @FXML private TableColumn<Tache, String> tacheColNote;
    // connexion courante pour consultation archive
    private Connection consultationConnection = null;
    private String     consultationAnnee      = null;
    // GARDER les nouveaux
    @FXML private Label statGroupes;
    @FXML private Label statProjets;
    @FXML private Label statEtudiants;
    @FXML private Label statArchives;
    // ── STATE ──
    private Button activeNavBtn;
    private AnneeRepository anneeRepository = new AnneeRepository();
    private AnneeService anneeservice = new AnneeService();

    // ═══════════════════════════════════════════════════════
    // INIT
    // ═══════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        // nouveau depuis JavaFX 20
        groupesTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        membresTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tachesTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        archiveTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        // groupes — proportions en %
        grpColNom.setMaxWidth(Double.MAX_VALUE);
        grpColProjet.setMaxWidth(Double.MAX_VALUE);
        grpColActions.setMaxWidth(Double.MAX_VALUE);

        // membres
        memColNom.setMaxWidth(Double.MAX_VALUE);
        memColPrenom.setMaxWidth(Double.MAX_VALUE);
        memColClasse.setMaxWidth(Double.MAX_VALUE);
        memColNiveau.setMaxWidth(Double.MAX_VALUE);

        // taches
        tacheColTitre.setMaxWidth(Double.MAX_VALUE);
        tacheColProf.setMaxWidth(Double.MAX_VALUE);
        tacheColDesc.setMaxWidth(Double.MAX_VALUE);
        tacheColStatut.setMaxWidth(Double.MAX_VALUE);
        tacheColNote.setMaxWidth(Double.MAX_VALUE);

        // archive
        archColAnnee.setMaxWidth(Double.MAX_VALUE);
        archColStatus.setMaxWidth(Double.MAX_VALUE);
        archColActions.setMaxWidth(Double.MAX_VALUE);

        dateLabel.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        //anneeRepository.findAll();
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("ADMINISTRATEUR");
            welcomeTitle.setText("Bonjour, " + admin.getPrenom() + " !");
        }

        // badge année active
        updateBadgeAnnee(ConnectionFactory.getActiveDbName(), true);

        activeNavBtn = btnOverview;
        loadOverviewStats();
      //  loadRecentActivity();
    }

    // ═══════════════════════════════════════════════════════
    // NAV SWITCHING
    // ═══════════════════════════════════════════════════════
    // ── navigation ──
    @FXML private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Accueil",
                "Bienvenue dans l'espace administration.");
        loadOverviewStats();
    }

    @FXML private void showGroupes() {
        switchPanel(groupesPanel, btnGroupes, "Groupes",
                "Consultez et gérez les groupes de l'année en cours.");
        loadGroupes();
    }

    @FXML private void showArchive() {
        switchPanel(archivePanel, btnArchive, "Archive",
                "Gérez et consultez les archives universitaires.");
        loadArchive();
    }

    private void switchPanel(VBox panel, Button navBtn, String title, String subtitle) {
        overviewPanel.setVisible(false);
        groupesPanel.setVisible(false);
        archivePanel.setVisible(false);
        membresPanel.setVisible(false);  // membre
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
        statGroupes.setText("8");
        statProjets.setText("12");
        statEtudiants.setText("64");
        statArchives.setText("3");
    }

   /* private void loadRecentActivity() {
        activityList.setItems(FXCollections.observableArrayList(
                "🟢  New student enrolled — Ahmed Benali",
                "📝  Project submitted — Machine Learning Study",
                "👨‍🏫  Professor added — Dr. Sarah Martin",
                "🗂️  Record archived — Année 2022-2023",
                "🔄  Student updated — Fatima Zahra Alami",
                "📋  New project assigned — Web Application Dev",
                "✅  Project validated — IoT Systems Research"
        ));
    }*/

    private void loadProfessors() {
        // TODO: bind ProfessorRepository.findAll() to professorsTable
    }

    private void loadStudents() {
        // TODO: bind StudentRepository.findAll() to studentsTable
    }

    private void loadArchive() {
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // données réelles depuis les BDs
        ObservableList<Eheiannee> data = FXCollections.observableArrayList(
                anneeservice.getAllAnnees()
        );
        archiveTable.setItems(data);

        // boutons dynamiques
        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }

                Eheiannee annee = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8);

                if (annee.isActive()) {
                    Button btnArchiver = new Button("📂  Archiver");
                    btnArchiver.setOnAction(e -> handleArchiver(annee));
                    box.getChildren().add(btnArchiver);
                }

                Button btnConsulter = new Button("👁  Consulter");
                btnConsulter.setOnAction(e -> handleConsulter(annee));
                box.getChildren().add(btnConsulter);

                setGraphic(box);
            }
        });


        //System.out.println("208 dash Annees trouvées: " + data.size());
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

   /* private void handleConsulter(Eheiannee row) {
        // TODO: open a detail view for this year's projects
       // System.out.println("Consulting year: " + row.getAnnee());
    }*/

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
    //--------------------- le 07/05/2026
    // ── badge année ──
    private void updateBadgeAnnee(String dbName, boolean isActive) {
        // extraire le libelle depuis la BD
        String libelle = dbName.replace("ehei", "");
        if (isActive) {
            badgeAnnee.setText("🟢  " + libelle+" (active)");
            badgeAnnee.getStyleClass().setAll("badge-annee-active");
        } else {
            badgeAnnee.setText("📂  " + libelle + " (archivée)");
            badgeAnnee.getStyleClass().setAll("badge-annee-archivee");
        }
    }
    // ── groupes ──
    private void loadGroupes() {
        //grpColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        grpColNom.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        grpColProjet.setCellValueFactory(new PropertyValueFactory<>("projetLibelle"));

        grpColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Groupe groupe = getTableView().getItems().get(getIndex());
                Button btn = new Button("👁  Consulter");
                btn.setStyle(
                        "-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa;" +
                                "-fx-font-size: 11px; -fx-background-radius: 6; -fx-cursor: hand;"
                );
                btn.setOnAction(e -> handleConsulterGroupe(groupe));
                setGraphic(btn);
            }
        });

        try {
            // si consultationConnection existe → on l'utilise (année archivée)
            // sinon → connexion active (année courante)
            GroupeService service = (consultationConnection != null)
                    ? new GroupeService(consultationConnection)
                    : new GroupeService();

            ObservableList<Groupe> data = FXCollections.observableArrayList(
                    service.getAllGroupes()
            );
            groupesTable.setItems(data);
            // recherche en temps réel
            groupSearch.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.trim().isEmpty()) {
                    groupesTable.setItems(data);
                    return;
                }
                String q = newVal.trim().toLowerCase();
                groupesTable.setItems(data.filtered(g ->
                        g.getLibelle().toLowerCase().contains(q) ||
                                g.getProjetLibelle().toLowerCase().contains(q)
                ));
            });

        } catch (DatabaseException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Erreur chargement groupes: " + e.getMessage()
            ).show();
        }
    }
   /*
    private void loadGroupes() {
        grpColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        grpColNom.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        grpColProjet.setCellValueFactory(new PropertyValueFactory<>("projetLibelle"));

        grpColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Groupe groupe = getTableView().getItems().get(getIndex());
                Button btn = new Button("👁  Consulter");
                btn.setStyle(
                        "-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa;" +
                                "-fx-font-size: 11px; -fx-background-radius: 6; -fx-cursor: hand;"
                );
                btn.setOnAction(e -> handleConsulterGroupe(groupe));
                setGraphic(btn);
            }
        });

        // TODO: remplacer par GroupeRepository.findAll()
        // groupesTable.setItems(FXCollections.observableArrayList(groupeRepo.findAll()));
    }
    */
    // ── consulter archive ──
    private void handleConsulter(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Basculer vers " + annee.getLibelle());
        confirm.setHeaderText("Voulez-vous basculer vers " + annee.getLibelle() + " ?");
        confirm.setContentText("Les données affichées seront celles de cette année.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // connexion temporaire — PAS updateConfig

                    consultationConnection = ConnectionFactory.getConnection(annee.getDbName());
                    consultationAnnee = annee.getLibelle();
                    System.out.println("active sur"+annee.getDbName());
                    System.out.println("herrrr"+  ConnectionFactory.getActiveConnection());
                    // badge passe en archivé
                    updateBadgeAnnee(annee.getDbName(), annee.isActive());

                    // basculer vers groupes pour voir les données
                    showGroupes();

                } catch (ConnectionException e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible de se connecter à " + annee.getLibelle()
                    ).show();
                }
            }
        });
    }

    private void handleConsulterGroupe(Groupe groupe) {
        try {
            Connection conn = (consultationConnection != null)
                    ? consultationConnection
                    : ConnectionFactory.getActiveConnection();

            GroupeService groupeService = new GroupeService(conn);
            TacheRepository tacheRepo  = new TacheRepository(conn);

            // ── membres ──
            List<Etudiant> etudiants = groupeService.getEtudiantsByGroupe(groupe.getId());
            memColNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            memColPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            memColClasse.setCellValueFactory(new PropertyValueFactory<>("classeLibelle"));
            memColNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauLibelle"));

            membresTable.setItems(FXCollections.observableArrayList(etudiants));

            // ── tâches ──
            List<Tache> taches = tacheRepo.findByGroupe(groupe.getId());
            tacheColTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            tacheColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

            // colonne professeur — nom + prénom combinés
            tacheColProf.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getProfesseurPrenom() + " " +
                                    cellData.getValue().getProfesseurNom()
                    )
            );

            // statut — coloré selon l'état
            tacheColStatut.setCellValueFactory(new PropertyValueFactory<>("etatValidation"));
            tacheColStatut.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String statut, boolean empty) {
                    super.updateItem(statut, empty);
                    if (empty || statut == null || statut.equals("—")) {
                        setText("—");
                        setStyle("-fx-text-fill: #6b7280;");
                    } else if (statut.toLowerCase().contains("valid")) {
                        setText(statut);
                        setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
                    } else if (statut.toLowerCase().contains("rejet")) {
                        setText(statut);
                        setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;");
                    } else {
                        setText(statut);
                        setStyle("-fx-text-fill: #fbbf24;");
                    }
                }
            });

// note — colorée selon la valeur
            tacheColNote.setCellValueFactory(new PropertyValueFactory<>("note"));
            tacheColNote.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String note, boolean empty) {
                    super.updateItem(note, empty);
                    if (empty || note == null || note.equals("—")) {
                        setText("—");
                        setStyle("-fx-text-fill: #6b7280;");
                    } else {
                        setText(note);
                        double val = Double.parseDouble(note);
                        if (val >= 0.7) {
                            setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;"); // vert
                        } else if (val >= 0.5) {
                            setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;"); // jaune
                        } else {
                            setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;"); // rouge
                        }
                    }
                }
            });

            tachesTable.setItems(FXCollections.observableArrayList(taches));

            // ── titres ──
            membresTitre.setText( groupe.getLibelle());
            membresProjet.setText("Projet : " + groupe.getProjetLibelle());

            // ── switcher panel ──
            overviewPanel.setVisible(false);
            groupesPanel.setVisible(false);
            archivePanel.setVisible(false);
            membresPanel.setVisible(true);

            pageTitle.setText(groupe.getLibelle());
            pageSubtitle.setText("Membres et tâches assignées");

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Erreur: " + e.getMessage()
            ).show();
            e.printStackTrace();
        }
    }
    // bouton retour:
    @FXML
    private void retourGroupes() {
        membresPanel.setVisible(false);
        groupesPanel.setVisible(true);
        pageTitle.setText("Groupes");
        pageSubtitle.setText("Consultez et gérez les groupes de l'année en cours.");
    }
    // rechercher un groupe:
    @FXML
    private void rechercherGroupe() {
        String query = groupSearch.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            // si vide — recharger tous les groupes
            loadGroupes();
            return;
        }

        try {
            GroupeService service = (consultationConnection != null)
                    ? new GroupeService(consultationConnection)
                    : new GroupeService();

            List<Groupe> tousLesGroupes = service.getAllGroupes();

            // filtrer par libelle ou projet
            List<Groupe> filtres = tousLesGroupes.stream()
                    .filter(g ->
                            g.getLibelle().toLowerCase().contains(query) ||
                                    g.getProjetLibelle().toLowerCase().contains(query)
                    )
                    .collect(java.util.stream.Collectors.toList());

            groupesTable.setItems(FXCollections.observableArrayList(filtres));

            if (filtres.isEmpty()) {
                pageSubtitle.setText("Aucun groupe trouvé pour : " + query);
            } else {
                pageSubtitle.setText(filtres.size() + " groupe(s) trouvé(s)");
            }

        } catch (DatabaseException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Erreur recherche: " + e.getMessage()
            ).show();
        }
    }
}
