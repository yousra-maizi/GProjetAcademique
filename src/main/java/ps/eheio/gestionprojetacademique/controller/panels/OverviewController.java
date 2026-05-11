package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ps.eheio.gestionprojetacademique.controller.DashboardController;

public class OverviewController {

    @FXML private Label welcomeTitle;
    @FXML private Label statGroupes;
    @FXML private Label statProjets;
    @FXML private Label statEtudiants;
    @FXML private Label statArchives;

    private DashboardController dashboard;

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    public void setAdminName(String prenom) {
        welcomeTitle.setText("Bonjour, " + prenom + " !");
    }

    public void load() {
        // TODO: remplacer par vrais counts depuis repositories
        statGroupes.setText("8");
        statProjets.setText("12");
        statEtudiants.setText("64");
        statArchives.setText("3");
    }

    @FXML private void goToGroupes() {
        dashboard.showProjetTypes();
    }

    @FXML private void goToArchive() {
        dashboard.showArchive();
    }
}