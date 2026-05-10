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
import ps.eheio.gestionprojetacademique.Repository.GroupeRepository;
import ps.eheio.gestionprojetacademique.Repository.TacheRepository;
import ps.eheio.gestionprojetacademique.model.*;
import ps.eheio.gestionprojetacademique.service.AuthService;
import ps.eheio.gestionprojetacademique.service.AnneeService;
import ps.eheio.gestionprojetacademique.service.GroupeService;
import ps.eheio.gestionprojetacademique.service.NiveauService;

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
    @FXML private Button btnGroupes;  // AJOUTER CETTE LIGNE
    @FXML private Button btnArchive;

    // ── PANELS ──
    @FXML private VBox overviewPanel;
    @FXML private VBox archivePanel;
    @FXML private VBox projetTypesPanel;
    @FXML private VBox niveauxPanel;   // Utilisé pour afficher 3GI, 3RSI, etc.
    @FXML private VBox groupesPanel;
    @FXML private VBox membresPanel;

    // ── OVERVIEW ──
    @FXML private Label statGroupes;
    @FXML private Label statProjets;
    @FXML private Label statEtudiants;
    @FXML private Label statArchives;

    // ── ARCHIVE TABLE ──
    @FXML private TableView<Eheiannee> archiveTable;
    @FXML private TableColumn<Eheiannee, String> archColAnnee;
    @FXML private TableColumn<Eheiannee, String> archColStatus;
    @FXML private TableColumn<Eheiannee, Void> archColActions;

    // ── BADGE ──
    @FXML private Label badgeAnnee;
    @FXML private Label welcomeTitle;
    @FXML private Label welcomeEyebrow;

    // ── NIVEAUX (FILIERES) ──
    @FXML private TableView<Niveau> niveauxTable;
    @FXML private TableColumn<Niveau, String> nivColLibelle;
    @FXML private TableColumn<Niveau, Void> nivColActions;

    // ── GROUPES ──
    @FXML private TableView<Groupe> groupesTable;
    @FXML private TableColumn<Groupe, String> grpColNom;
    @FXML private TableColumn<Groupe, String> grpColProjet;
    @FXML private TableColumn<Groupe, Void> grpColActions;
    @FXML private TextField groupSearch;
    @FXML private Label groupesTitre;

    // ── MEMBRES ──
    @FXML private TableView<Etudiant> membresTable;
    @FXML private TableColumn<Etudiant, String> memColNom;
    @FXML private TableColumn<Etudiant, String> memColPrenom;
    @FXML private TableColumn<Etudiant, String> memColClasse;
    @FXML private TableColumn<Etudiant, String> memColNiveau;
    @FXML private Label membresTitre;
    @FXML private Label membresProjet;
    @FXML private Button btnRetourGroupes;

    // ── TACHES ──
    @FXML private TableView<Tache> tachesTable;
    @FXML private TableColumn<Tache, String> tacheColTitre;
    @FXML private TableColumn<Tache, String> tacheColProf;
    @FXML private TableColumn<Tache, String> tacheColDesc;
    @FXML private TableColumn<Tache, String> tacheColStatut;
    @FXML private TableColumn<Tache, String> tacheColNote;

    // connexion courante pour consultation archive
    private Connection consultationConnection = null;

    // ── STATE ──
    private Button activeNavBtn;
    private AnneeService anneeservice = new AnneeService();

    // ── SERVICES ──
    private NiveauService niveauService;
    private GroupeService groupeService;

    // ═══════════════════════════════════════════════════════
    // INIT
    // ═══════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        try {
            niveauService = new NiveauService();
            groupeService = new GroupeService();
        } catch (DatabaseException e) {
            showError("Erreur d'initialisation", "Impossible d'initialiser les services: " + e.getMessage());
        }

        // Configuration des largeurs des colonnes
        grpColNom.prefWidthProperty().bind(groupesTable.widthProperty().multiply(0.35));
        grpColProjet.prefWidthProperty().bind(groupesTable.widthProperty().multiply(0.45));
        grpColActions.prefWidthProperty().bind(groupesTable.widthProperty().multiply(0.20));

        memColNom.prefWidthProperty().bind(membresTable.widthProperty().multiply(0.25));
        memColPrenom.prefWidthProperty().bind(membresTable.widthProperty().multiply(0.25));
        memColClasse.prefWidthProperty().bind(membresTable.widthProperty().multiply(0.25));
        memColNiveau.prefWidthProperty().bind(membresTable.widthProperty().multiply(0.25));

        tacheColTitre.prefWidthProperty().bind(tachesTable.widthProperty().multiply(0.20));
        tacheColProf.prefWidthProperty().bind(tachesTable.widthProperty().multiply(0.20));
        tacheColDesc.prefWidthProperty().bind(tachesTable.widthProperty().multiply(0.25));
        tacheColStatut.prefWidthProperty().bind(tachesTable.widthProperty().multiply(0.20));
        tacheColNote.prefWidthProperty().bind(tachesTable.widthProperty().multiply(0.15));

        archColAnnee.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.35));
        archColStatus.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.25));
        archColActions.prefWidthProperty().bind(archiveTable.widthProperty().multiply(0.40));

        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("ADMINISTRATEUR");
            welcomeTitle.setText("Bonjour, " + admin.getPrenom() + " !");
        }

        updateBadgeAnnee(ConnectionFactory.getActiveDbName(), true);
        activeNavBtn = btnOverview;
        loadOverviewStats();
    }

    // ═══════════════════════════════════════════════════════
    // NAV SWITCHING
    // ═══════════════════════════════════════════════════════

    @FXML
    private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Accueil", "Bienvenue dans l'espace administration.");
        loadOverviewStats();
    }

    @FXML
    private void showProjetTypes() {
        switchPanel(projetTypesPanel, btnGroupes, "Types de projets", "Sélectionnez PS, PFA ou PFE");
    }

    @FXML
    private void showArchive() {
        switchPanel(archivePanel, btnArchive, "Archive", "Gérez et consultez les archives universitaires.");
        loadArchive();
    }

    private void switchPanel(VBox panel, Button navBtn, String title, String subtitle) {
        overviewPanel.setVisible(false);
        projetTypesPanel.setVisible(false);
        niveauxPanel.setVisible(false);
        groupesPanel.setVisible(false);
        membresPanel.setVisible(false);
        archivePanel.setVisible(false);
        panel.setVisible(true);

        if (activeNavBtn != null) activeNavBtn.getStyleClass().remove("nav-active");
        navBtn.getStyleClass().add("nav-active");
        activeNavBtn = navBtn;

        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);
    }

    // ═══════════════════════════════════════════════════════
    // PS - PFA - PFE : AFFICHAGE DES FILIERES PAR ANNEE
    // ═══════════════════════════════════════════════════════

    @FXML
    private void showFilierePS() {
        showNiveauxByAnnee(3, "Projet de Synthèse - 3ème année");
    }

    @FXML
    private void showFilierePFA() {
        showNiveauxByAnnee(4, "Projet de Fin d'Année - 4ème année");
    }

    @FXML
    private void showFilierePFE() {
        showNiveauxByAnnee(5, "Projet de Fin d'Étude - 5ème année");
    }

    /**
     * Affiche les niveaux (filières) d'une année spécifique
     * Exemple: annee=3 → 3GI, 3RSI, 3MEC, 3GC
     */
    private void showNiveauxByAnnee(int annee, String titre) {
        try {
            NiveauService service = getNiveauService();
            List<Niveau> niveaux = service.getNiveauxByAnnee(annee);

            if (niveaux == null || niveaux.isEmpty()) {
                showInfo("Aucune filière", "Aucune filière trouvée pour l'année " + annee);
                return;
            }

            nivColLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            nivColActions.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        return;
                    }
                    Niveau niveau = getTableView().getItems().get(getIndex());
                    Button btn = new Button("👁 Voir les groupes");
                    btn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa;" +
                            "-fx-font-size: 11px; -fx-background-radius: 6; -fx-cursor: hand;");
                    btn.setOnAction(e -> showGroupesByNiveau(niveau));
                    setGraphic(btn);
                }
            });

            niveauxTable.setItems(FXCollections.observableArrayList(niveaux));
            projetTypesPanel.setVisible(false);
            niveauxPanel.setVisible(true);
            pageTitle.setText(titre);
            pageSubtitle.setText("Sélectionnez une filière");

        } catch (DatabaseException e) {
            showError("Erreur", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche les groupes d'un niveau spécifique (ex: 3GI)
     */
    private void showGroupesByNiveau(Niveau niveau) {
        try {
            GroupeRepository repo = getGroupeRepository();
            List<Groupe> groupes = repo.findByNiveau(niveau.getId());

            grpColNom.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            grpColProjet.setCellValueFactory(new PropertyValueFactory<>("projetLibelle"));
            grpColActions.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        return;
                    }
                    Groupe groupe = getTableView().getItems().get(getIndex());
                    Button btn = new Button("👁 Consulter");
                    btn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa;" +
                            "-fx-font-size: 11px; -fx-background-radius: 6; -fx-cursor: hand;");
                    btn.setOnAction(e -> handleConsulterGroupe(groupe));
                    setGraphic(btn);
                }
            });

            groupesTable.setItems(FXCollections.observableArrayList(groupes));
            niveauxPanel.setVisible(false);
            groupesPanel.setVisible(true);
            groupesTitre.setText(niveau.getLibelle());
            pageTitle.setText(niveau.getLibelle());
            pageSubtitle.setText("Groupes de " + niveau.getLibelle());

        } catch (DatabaseException e) {
            showError("Erreur", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    // CHARGEMENT DES DONNÉES
    // ═══════════════════════════════════════════════════════

    private void loadOverviewStats() {
        statGroupes.setText("8");
        statProjets.setText("12");
        statEtudiants.setText("64");
        statArchives.setText("3");
    }

    private void loadArchive() {
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        ObservableList<Eheiannee> data = FXCollections.observableArrayList(anneeservice.getAllAnnees());
        archiveTable.setItems(data);

        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Eheiannee annee = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8);

                if (annee.isActive()) {
                    Button btnArchiver = new Button("📂 Archiver");
                    btnArchiver.setOnAction(e -> handleArchiver(annee));
                    box.getChildren().add(btnArchiver);
                }

                Button btnConsulter = new Button("👁 Consulter");
                btnConsulter.setOnAction(e -> handleConsulter(annee));
                box.getChildren().add(btnConsulter);

                setGraphic(box);
            }
        });
    }

    // ═══════════════════════════════════════════════════════
    // ACTIONS ARCHIVE
    // ═══════════════════════════════════════════════════════

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
                } catch (DatabaseException e) {
                    showError("Archivage échoué", e.getMessage());
                }
            }
        });
    }

    private void handleConsulter(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Basculer vers " + annee.getLibelle());
        confirm.setHeaderText("Voulez-vous basculer vers " + annee.getLibelle() + " ?");
        confirm.setContentText("Les données affichées seront celles de cette année.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    consultationConnection = ConnectionFactory.getConnection(annee.getDbName());
                    updateBadgeAnnee(annee.getDbName(), annee.isActive());

                    niveauService = new NiveauService(consultationConnection);
                    groupeService = new GroupeService(consultationConnection);

                    showProjetTypes();

                } catch (DatabaseException e) {
                    showError("Erreur de connexion", "Impossible de se connecter à " + annee.getLibelle());
                }
            }
        });
    }

    // ═══════════════════════════════════════════════════════
    // CONSULTATION GROUPE
    // ═══════════════════════════════════════════════════════

    private void handleConsulterGroupe(Groupe groupe) {
        try {
            Connection conn = (consultationConnection != null) ? consultationConnection : ConnectionFactory.getActiveConnection();

            GroupeRepository groupeRepo = new GroupeRepository(conn);
            TacheRepository tacheRepo = new TacheRepository(conn);

            List<Etudiant> etudiants = groupeRepo.findEtudiantsByGroupe(groupe.getId());

            memColNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            memColPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            memColClasse.setCellValueFactory(new PropertyValueFactory<>("classeLibelle"));
            memColNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauLibelle"));

            membresTable.setItems(FXCollections.observableArrayList(etudiants));

            List<Tache> taches = tacheRepo.findByGroupe(groupe.getId());
            tacheColTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            tacheColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

            tacheColProf.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getProfesseurPrenom() + " " + cellData.getValue().getProfesseurNom()
                    ));

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
                        try {
                            double val = Double.parseDouble(note);
                            if (val >= 0.7) {
                                setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
                            } else if (val >= 0.5) {
                                setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
                            } else {
                                setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;");
                            }
                        } catch (NumberFormatException e) {
                            setStyle("-fx-text-fill: #6b7280;");
                        }
                    }
                }
            });

            tachesTable.setItems(FXCollections.observableArrayList(taches));

            membresTitre.setText(groupe.getLibelle());
            membresProjet.setText("Projet : " + groupe.getProjetLibelle());

            overviewPanel.setVisible(false);
            groupesPanel.setVisible(false);
            archivePanel.setVisible(false);
            membresPanel.setVisible(true);

            pageTitle.setText(groupe.getLibelle());
            pageSubtitle.setText("Membres et tâches assignées");

        } catch (Exception e) {
            showError("Erreur", "Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    // BOUTONS RETOUR
    // ═══════════════════════════════════════════════════════

    @FXML
    private void retourProjetTypes() {
        niveauxPanel.setVisible(false);
        projetTypesPanel.setVisible(true);
        pageTitle.setText("Types de projets");
        pageSubtitle.setText("Sélectionnez PS, PFA ou PFE");
    }

    @FXML
    private void retourNiveaux() {
        groupesPanel.setVisible(false);
        niveauxPanel.setVisible(true);
        pageTitle.setText(pageTitle.getText());
        pageSubtitle.setText("Sélectionnez une filière");
    }

    @FXML
    private void retourGroupes() {
        membresPanel.setVisible(false);
        groupesPanel.setVisible(true);
        pageTitle.setText(groupesTitre.getText());
        pageSubtitle.setText("Groupes de " + groupesTitre.getText());
    }

    // ═══════════════════════════════════════════════════════
    // UTILITAIRES
    // ═══════════════════════════════════════════════════════

    @FXML
    private void handleLogout() {
        AuthService.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ps/eheio/gestionprojetacademique/view/LoginView1.fxml"));
            Stage stage = (Stage) pageTitle.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 680));
            stage.setTitle("EHEIO Administration");
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBadgeAnnee(String dbName, boolean isActive) {
        String libelle = dbName.replace("ehei", "");
        if (isActive) {
            badgeAnnee.setText("🟢 " + libelle + " (active)");
            badgeAnnee.getStyleClass().setAll("badge-annee-active");
        } else {
            badgeAnnee.setText("📂 " + libelle + " (archivée)");
            badgeAnnee.getStyleClass().setAll("badge-annee-archivee");
        }
    }

    private NiveauService getNiveauService() throws DatabaseException {
        if (niveauService == null) {
            niveauService = (consultationConnection != null)
                    ? new NiveauService(consultationConnection)
                    : new NiveauService();
        }
        return niveauService;
    }

    private GroupeRepository getGroupeRepository() throws DatabaseException {
        if (consultationConnection != null) {
            return new GroupeRepository(consultationConnection);
        }
        return new GroupeRepository();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void rechercherGroupe() {
        String query = groupSearch.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            try {
                GroupeRepository repo = getGroupeRepository();
                groupesTable.setItems(FXCollections.observableArrayList(repo.findAll()));
            } catch (DatabaseException e) {
                showError("Erreur", e.getMessage());
            }
            return;
        }

        try {
            GroupeRepository repo = getGroupeRepository();
            List<Groupe> tousLesGroupes = repo.findAll();

            List<Groupe> filtres = tousLesGroupes.stream()
                    .filter(g -> g.getLibelle().toLowerCase().contains(query) ||
                            g.getProjetLibelle().toLowerCase().contains(query))
                    .collect(java.util.stream.Collectors.toList());

            groupesTable.setItems(FXCollections.observableArrayList(filtres));

            if (filtres.isEmpty()) {
                pageSubtitle.setText("Aucun groupe trouvé pour : " + query);
            } else {
                pageSubtitle.setText(filtres.size() + " groupe(s) trouvé(s)");
            }

        } catch (DatabaseException e) {
            showError("Erreur recherche", e.getMessage());
        }
    }
}