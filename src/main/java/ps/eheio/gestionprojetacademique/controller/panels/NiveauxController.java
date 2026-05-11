package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.controller.DashboardController;
import ps.eheio.gestionprojetacademique.model.Niveau;
import ps.eheio.gestionprojetacademique.service.NiveauService;

import java.sql.Connection;
import java.util.List;

public class NiveauxController {

    @FXML private Label                         niveauxTitre;
    @FXML private TableView<Niveau>             niveauxTable;
    @FXML private TableColumn<Niveau, String>   nivColLibelle;
    @FXML private TableColumn<Niveau, Void>     nivColActions;

    private DashboardController dashboard;
    private NiveauService       niveauService;
    private int                 anneeCourante;

    @FXML
    public void initialize() {
        try {
            niveauService = new NiveauService();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        nivColLibelle.prefWidthProperty().bind(
                niveauxTable.widthProperty().multiply(0.75));
        nivColActions.prefWidthProperty().bind(
                niveauxTable.widthProperty().multiply(0.25));
    }

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    public void setConnection(Connection conn) {
        this.niveauService = new NiveauService(conn);
    }

    public void loadByAnnee(int annee, String titre) {
        this.anneeCourante = annee;
        niveauxTitre.setText(titre);

        try {
            List<Niveau> niveaux = niveauService.getNiveauxByAnnee(annee);

            nivColLibelle.setCellValueFactory(
                    new PropertyValueFactory<>("libelle"));

            nivColActions.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); return; }
                    Niveau niveau = getTableView().getItems().get(getIndex());
                    Button btn = new Button("👁  Voir les groupes");
                    btn.setStyle(
                            "-fx-background-color: #1e3a5f;" +
                                    "-fx-text-fill: #60a5fa;" +
                                    "-fx-font-size: 11px;" +
                                    "-fx-background-radius: 6;" +
                                    "-fx-cursor: hand;"
                    );
                    btn.setOnAction(e -> goToGroupes(niveau));
                    setGraphic(btn);
                }
            });

            niveauxTable.setItems(FXCollections.observableArrayList(niveaux));

        } catch (DatabaseException e) {
            showError("Erreur filières", e.getMessage());
        }
    }

    private void goToGroupes(Niveau niveau) {
        dashboard.navigateTo(
                dashboard.getGroupesPanel(), dashboard.getBtnGroupes(),
                niveau.getLibelle(),
                "Groupes de la filière " + niveau.getLibelle()
        );
        dashboard.getGroupesController().load(niveau);
    }

    @FXML private void retour() {
        dashboard.navigateTo(
                dashboard.getProjetTypesPanel(), dashboard.getBtnGroupes(),
                "Types de projets", "Sélectionnez PS, PFA ou PFE"
        );
    }

    private void showError(String title, String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
    //getter titre:
    public String getNiveauxTitre() { return niveauxTitre.getText(); }
}