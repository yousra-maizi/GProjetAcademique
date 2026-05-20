package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.fxml.FXML;
import ps.eheio.gestionprojetacademique.controller.DashboardController;

public class ProjetTypesController {

    private DashboardController dashboard;

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    @FXML private void showPS() {
        dashboard.navigateTo(
                dashboard.getNiveauxPanel(), dashboard.getBtnGroupes(),
                "Projet de Synthèse", "3ème année "
        );
        dashboard.getNiveauxController().loadByAnnee(3, "Projet de Synthèse");
    }

    @FXML private void showPFA() {
        dashboard.navigateTo(
                dashboard.getNiveauxPanel(), dashboard.getBtnGroupes(),
                "Projet de Fin d'Année", "4ème année"
        );
        dashboard.getNiveauxController().loadByAnnee(4, "Projet de Fin d'Année");
    }

    @FXML private void showPFE() {
        dashboard.navigateTo(
                dashboard.getNiveauxPanel(), dashboard.getBtnGroupes(),
                "Projet de Fin d'Étude", "5ème année"
        );
        dashboard.getNiveauxController().loadByAnnee(5, "Projet de Fin d'Étude");
    }
}