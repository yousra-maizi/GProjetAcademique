package ps.eheio.gestionprojetacademique.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionFactory;
import ps.eheio.gestionprojetacademique.controller.panels.*;
import ps.eheio.gestionprojetacademique.service.AuthService;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    // ── TOP BAR ──
    @FXML private Label  pageTitle;
    @FXML private Label  pageSubtitle;
    @FXML private Label  dateLabel;
    @FXML private Label  adminNameLabel;
    @FXML private Label  adminRoleLabel;
    @FXML private Label  badgeAnnee;

    // ── NAV ──
    @FXML private Button btnOverview;
    @FXML private Button btnGroupes;
    @FXML private Button btnArchive;

    // ── PANELS (racines des fx:include) ──
    @FXML private VBox overview;
    @FXML private VBox projetTypes;
    @FXML private VBox niveaux;
    @FXML private VBox groupes;
    @FXML private VBox membres;
    @FXML private VBox archive;

    // ── SOUS-CONTROLLERS injectés par JavaFX ──
    @FXML private OverviewController    overviewController;
    @FXML private ProjetTypesController projetTypesController;
    @FXML private NiveauxController     niveauxController;
    @FXML private GroupesController     groupesController;
    @FXML private MembresController     membresController;
    @FXML private ArchiveController     archiveController;

    private Button activeNavBtn;

    @FXML
    public void initialize() {
        // injecter la référence dashboard dans chaque sous-controller
        overviewController.setDashboard(this);
        projetTypesController.setDashboard(this);
        niveauxController.setDashboard(this);
        groupesController.setDashboard(this);
        membresController.setDashboard(this);
        archiveController.setDashboard(this);

        // topbar
        dateLabel.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        var admin = AuthService.getLoggedInAdmin();
        if (admin != null) {
            adminNameLabel.setText(admin.getPrenom() + " " + admin.getNom());
            adminRoleLabel.setText("ADMINISTRATEUR");
            overviewController.setAdminName(admin.getPrenom());
        }

        updateBadgeAnnee(ConnectionFactory.getActiveDbName(), true);
        activeNavBtn = btnOverview;
        overviewController.load();
        // afficher overview au démarrage
        navigateTo(overview, btnOverview,
                "Accueil", "Bienvenue dans l'espace administration.");
        overviewController.load();
    }

    // ── NAVIGATION — appelée par les sous-controllers ──
    public void navigateTo(VBox panel, Button navBtn,
                           String title, String subtitle) {
        overview.setVisible(false);
        projetTypes.setVisible(false);
        niveaux.setVisible(false);
        groupes.setVisible(false);
        membres.setVisible(false);
        archive.setVisible(false);
        panel.setVisible(true);

        if (activeNavBtn != null)
            activeNavBtn.getStyleClass().remove("nav-active");
        if (navBtn != null) {
            navBtn.getStyleClass().add("nav-active");
            activeNavBtn = navBtn;
        }
        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);
    }

    // ── GETTERS pour les sous-controllers ──
    public VBox getOverviewPanel()    { return overview; }
    public VBox getProjetTypesPanel() { return projetTypes; }
    public VBox getNiveauxPanel()     { return niveaux; }
    public VBox getGroupesPanel()     { return groupes; }
    public VBox getMembresPanel()     { return membres; }
    public VBox getArchivePanel()     { return archive; }
    public Button getBtnOverview()    { return btnOverview; }
    public Button getBtnGroupes()     { return btnGroupes; }
    public Button getBtnArchive()     { return btnArchive; }

    // ── connexion consultation ──
    public void onConsultationChanged(Connection conn) {
        niveauxController.setConnection(conn);
        groupesController.setConnection(conn);
        membresController.setConnection(conn);
    }

    public void updateBadgeAnnee(String dbName, boolean isActive) {
        String libelle = dbName.replace("ehei", "");
        if (isActive) {
            badgeAnnee.setText("🟢 " + libelle + " (active)");
            badgeAnnee.getStyleClass().setAll("badge-annee-active");
        } else {
            badgeAnnee.setText("📂 " + libelle + " (archivée)");
            badgeAnnee.getStyleClass().setAll("badge-annee-archivee");
        }
    }

    // ── SIDEBAR ──
    @FXML public void showOverview() {
        navigateTo(overview, btnOverview,
                "Accueil", "Bienvenue dans l'espace administration.");
        overviewController.load();
    }

    @FXML public void showProjetTypes() {
        navigateTo(projetTypes, btnGroupes,
                "Types de projets", "Sélectionnez PS, PFA ou PFE");
    }

    @FXML public void showArchive() {
        navigateTo(archive, btnArchive,
                "Archive", "Gérez et consultez les archives universitaires.");
        archiveController.load();
    }

    @FXML private void handleLogout() {
        AuthService.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/ps/eheio/gestionprojetacademique/view/LoginView1.fxml"));
            Stage stage = (Stage) pageTitle.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 680));
            stage.setTitle("EHEIO Administration");
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Getters pour que les sous-controllers puissent naviguer
    public NiveauxController getNiveauxController() { return niveauxController; }
    public GroupesController  getGroupesController()  { return groupesController; }
    public MembresController  getMembresController()  { return membresController; }

}