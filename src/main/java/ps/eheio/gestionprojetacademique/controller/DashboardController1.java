/*package ps.eheio.gestionprojetacademique.controller;

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
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
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

public class DashboardController1 {

    // ── TOP BAR ──
    @FXML private Label pageTitle;
    @FXML private Label pageSubtitle;
    @FXML private Label dateLabel;
    @FXML private Label adminNameLabel;
    @FXML private Label adminRoleLabel;

    // ── NAV BUTTONS ──
    @FXML private Button btnOverview;
    @FXML private Button btnGroupes;
    @FXML private Button btnArchive;

    // ── PANELS ──
    @FXML private VBox overviewPanel;
    @FXML private VBox archivePanel;
    @FXML private VBox projetTypesPanel;
    @FXML private VBox niveauxPanel;
    @FXML private VBox groupesPanel;
    @FXML private VBox membresPanel;

    // ── OVERVIEW STATS ──
    @FXML private Label statGroupes;
    @FXML private Label statProjets;
    @FXML private Label statEtudiants;
    @FXML private Label statArchives;
    @FXML private Label welcomeTitle;
    @FXML private Label welcomeEyebrow;

    // ── BADGE ──
    @FXML private Label badgeAnnee;

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

    // ── ARCHIVE ──
    @FXML private TableView<Eheiannee> archiveTable;
    @FXML private TableColumn<Eheiannee, String> archColAnnee;
    @FXML private TableColumn<Eheiannee, String> archColStatus;
    @FXML private TableColumn<Eheiannee, Void> archColActions;

    // ── SERVICES ──
    private Connection consultationConnection = null;
    private Button activeNavBtn;
    private AnneeService anneeservice = new AnneeService();
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
            showError("Erreur d'initialisation", e.getMessage());
        }

        // Configuration des largeurs
        grpColNom.prefWidthProperty().bind(groupesTable.widthProperty().multiply(0.45));
        grpColProjet.prefWidthProperty().bind(groupesTable.widthProperty().multiply(0.35));
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

        // Info admin
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("ADMINISTRATEUR");
            welcomeTitle.setText("Bonjour, " + admin.getPrenom() + " !");
        }

        updateBadgeAnnee(ConnectionManager.getActiveDbName(), true);
        activeNavBtn = btnOverview;
        loadOverviewStats();
    }

    // ═══════════════════════════════════════════════════════
    // NAVIGATION
    // ═══════════════════════════════════════════════════════

    @FXML private void showOverview() {
        switchPanel(overviewPanel, btnOverview, "Accueil", "Bienvenue dans l'espace administration.");
        loadOverviewStats();
    }

    @FXML private void showProjetTypes() {
        switchPanel(projetTypesPanel, btnGroupes, "Types de projets", "Sélectionnez PS, PFA ou PFE");
    }

    @FXML private void showArchive() {
        switchPanel(archivePanel, btnArchive, "Archive", "Gérez et consultez les archives universitaires.");
        loadArchive();
    }

    @FXML private void showFilierePS() {
        showNiveauxByAnnee(3, "Projet de Synthèse - 3ème année");
    }

    @FXML private void showFilierePFA() {
        showNiveauxByAnnee(4, "Projet de Fin d'Année - 4ème année");
    }

    @FXML private void showFilierePFE() {
        showNiveauxByAnnee(5, "Projet de Fin d'Étude - 5ème année");
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
    // AFFICHAGE DES NIVEAUX PAR ANNEE
    // ═══════════════════════════════════════════════════════

    private void showNiveauxByAnnee(int annee, String titre) {
        try {
            List<Niveau> niveaux = niveauService.getNiveauxByAnnee(annee);
            if (niveaux.isEmpty()) {
                showInfo("Aucune filière", "Aucune filière trouvée pour l'année " + annee);
                return;
            }

            nivColLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            nivColActions.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); return; }
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
            showError("Erreur", e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    // AFFICHAGE DES GROUPES PAR NIVEAU
    // ═══════════════════════════════════════════════════════

    private void showGroupesByNiveau(Niveau niveau) {
        try {
            List<Groupe> groupes = groupeService.getGroupesByNiveau(niveau.getId());

            grpColNom.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            grpColProjet.setCellValueFactory(new PropertyValueFactory<>("projetLibelle"));
            grpColActions.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); return; }
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
            showError("Erreur", e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    // CONSULTATION D'UN GROUPE
    // ═══════════════════════════════════════════════════════

    private void handleConsulterGroupe(Groupe groupe) {
        try {
            Connection conn = (consultationConnection != null) ? consultationConnection : ConnectionManager.getActiveConnection();

            TacheRepository tacheRepo = new TacheRepository(conn);

            GroupeService service;
            if (consultationConnection != null) {
                service = new GroupeService(consultationConnection);
            } else {
                service = groupeService;
            }

            // Passer le niveauId pour filtrer les étudiants
            List<Etudiant> etudiants = service.getEtudiantsByGroupeAndNiveau(groupe.getId(), groupe.getNiveauId());

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
            tacheColNote.setCellValueFactory(new PropertyValueFactory<>("note"));

            tachesTable.setItems(FXCollections.observableArrayList(taches));

            membresTitre.setText(groupe.getLibelle());
            membresProjet.setText("Projet : " + groupe.getProjetLibelle());

            groupesPanel.setVisible(false);
            membresPanel.setVisible(true);
            pageTitle.setText(groupe.getLibelle());
            pageSubtitle.setText("Membres et tâches assignées");

        } catch (Exception e) {
            showError("Erreur", e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    // BOUTONS RETOUR
    // ═══════════════════════════════════════════════════════

    @FXML private void retourProjetTypes() {
        niveauxPanel.setVisible(false);
        projetTypesPanel.setVisible(true);
        pageTitle.setText("Types de projets");
        pageSubtitle.setText("Sélectionnez PS, PFA ou PFE");
    }

    @FXML private void retourNiveaux() {
        groupesPanel.setVisible(false);
        niveauxPanel.setVisible(true);
        pageSubtitle.setText("Sélectionnez une filière");
    }

    @FXML private void retourGroupes() {
        membresPanel.setVisible(false);
        groupesPanel.setVisible(true);
        pageSubtitle.setText("Groupes de " + groupesTitre.getText());
    }

    // ═══════════════════════════════════════════════════════
    // ARCHIVE
    // ═══════════════════════════════════════════════════════

    private void loadArchive() {
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        ObservableList<Eheiannee> data = FXCollections.observableArrayList(anneeservice.getAllAnnees());
        archiveTable.setItems(data);

        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }

                Eheiannee annee = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8);

                if (annee.isActive()) {
                    Button btnArchiver = new Button("📂 Archiver");
                    btnArchiver.setOnAction(e -> handleArchiver(annee));
                    box.getChildren().add(btnArchiver);
                }

                Button btnConsulter = new Button("👁 Consulter");
                btnConsulter.setOnAction(e -> handleConsulterArchive(annee));
                box.getChildren().add(btnConsulter);
                setGraphic(box);
            }
        });
    }

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

    private void handleConsulterArchive(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Consulter " + annee.getLibelle());
        confirm.setHeaderText("Basculer vers " + annee.getLibelle() + " ?");
        confirm.setContentText("Les données affichées seront celles de cette année.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    consultationConnection = ConnectionManager.getConnection(annee.getDbName());
                    updateBadgeAnnee(annee.getDbName(), annee.isActive());

                    niveauService = new NiveauService(consultationConnection);
                    groupeService = new GroupeService(consultationConnection);

                    showProjetTypes();
                } catch (Exception e) {
                    showError("Erreur", "Impossible de se connecter");
                }
            }
        });
    }

    // ═══════════════════════════════════════════════════════
    // UTILITAIRES
    // ═══════════════════════════════════════════════════════

    private void loadOverviewStats() {
        statGroupes.setText("8");
        statProjets.setText("12");
        statEtudiants.setText("64");
        statArchives.setText("3");
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void handleLogout() {
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

    @FXML private void rechercherGroupe() {
        String query = groupSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) return;

        try {
            List<Groupe> tousLesGroupes = groupeService.getAllGroupes();
            List<Groupe> filtres = tousLesGroupes.stream()
                    .filter(g -> g.getLibelle().toLowerCase().contains(query) ||
                            g.getProjetLibelle().toLowerCase().contains(query))
                    .collect(java.util.stream.Collectors.toList());
            groupesTable.setItems(FXCollections.observableArrayList(filtres));
            pageSubtitle.setText(filtres.size() + " groupe(s) trouvé(s)");
        } catch (DatabaseException e) {
            showError("Erreur recherche", e.getMessage());
        }
    }
}

 */