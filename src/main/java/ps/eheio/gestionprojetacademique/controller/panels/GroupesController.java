package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.controller.DashboardController;
import ps.eheio.gestionprojetacademique.model.Groupe;
import ps.eheio.gestionprojetacademique.model.Niveau;
import ps.eheio.gestionprojetacademique.service.GroupeService;

import java.sql.Connection;
import java.util.List;

public class GroupesController {

    @FXML private Label                        groupesTitre;
    @FXML private TableView<Groupe>            groupesTable;
    @FXML private TableColumn<Groupe, String>  grpColNom;
    @FXML private TableColumn<Groupe, String>  grpColProjet;
    @FXML private TableColumn<Groupe, Void>    grpColActions;
    @FXML private TextField                    groupSearch;

    private DashboardController dashboard;
    private GroupeService        groupeService;
    private Niveau               niveauCourant;
    private Connection currentConnection = null;
    @FXML
    public void initialize() {
        grpColNom.prefWidthProperty().bind(
                groupesTable.widthProperty().multiply(0.40));
        grpColProjet.prefWidthProperty().bind(
                groupesTable.widthProperty().multiply(0.40));
        grpColActions.prefWidthProperty().bind(
                groupesTable.widthProperty().multiply(0.20));
    }

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    public void setConnection(Connection conn) {
        this.currentConnection = conn;
    }
    private GroupeService getService() throws DatabaseException {
        return currentConnection != null
                ? new GroupeService(currentConnection)
                : new GroupeService();
    }

    public Niveau getNiveauCourant() { return niveauCourant; }

    public void load(Niveau niveau) {
        this.niveauCourant = niveau;
        groupesTitre.setText(niveau.getLibelle());

        try {
            GroupeService service = getService();
            List<Groupe> groupes = service.getGroupesByNiveau(niveau.getId());
            ObservableList<Groupe> data = FXCollections.observableArrayList(groupes);

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
                            "-fx-background-color: #1e3a5f;" +
                                    "-fx-text-fill: #60a5fa;" +
                                    "-fx-font-size: 11px;" +
                                    "-fx-background-radius: 6;" +
                                    "-fx-cursor: hand;"
                    );
                    btn.setOnAction(e -> goToMembres(groupe));
                    setGraphic(btn);
                }
            });

            groupesTable.setItems(data);

            // recherche en temps réel pour trouver un groupe:
            groupSearch.textProperty().addListener((obs, old, newVal) -> {
                if (newVal.trim().isEmpty()) {
                    groupesTable.setItems(data);
                } else {
                    String q = newVal.trim().toLowerCase();
                    groupesTable.setItems(data.filtered(g ->
                            g.getLibelle().toLowerCase().contains(q) ||
                                    g.getProjetLibelle().toLowerCase().contains(q)
                    ));
                }
            });

        } catch (DatabaseException e) {
            showError("Erreur groupes", e.getMessage());
        }
    }

    private void goToMembres(Groupe groupe) {
        dashboard.navigateTo(
                dashboard.getMembresPanel(), null,
                groupe.getLibelle(), "Membres et tâches assignées"
        );
        dashboard.getMembresController().load(groupe, niveauCourant);
    }

    @FXML private void retour() {
        dashboard.navigateTo(
                dashboard.getNiveauxPanel(), dashboard.getBtnGroupes(),
                dashboard.getNiveauxController().getNiveauxTitre(),
                "Sélectionnez une filière"
        );
    }

    private void showError(String title, String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}