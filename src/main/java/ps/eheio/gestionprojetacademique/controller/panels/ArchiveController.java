package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.controller.DashboardController;
import ps.eheio.gestionprojetacademique.model.Eheiannee;
import ps.eheio.gestionprojetacademique.service.AnneeService;

import java.sql.Connection;

public class ArchiveController {

    @FXML private TableView<Eheiannee>           archiveTable;
    @FXML private TableColumn<Eheiannee, String> archColAnnee;
    @FXML private TableColumn<Eheiannee, String> archColStatus;
    @FXML private TableColumn<Eheiannee, Void>   archColActions;

    private DashboardController dashboard;

    private final AnneeService  anneeService = new AnneeService();

    @FXML
    public void initialize() {
        archColAnnee.prefWidthProperty().bind(
                archiveTable.widthProperty().multiply(0.35));
        archColStatus.prefWidthProperty().bind(
                archiveTable.widthProperty().multiply(0.25));
        archColActions.prefWidthProperty().bind(
                archiveTable.widthProperty().multiply(0.40));
    }

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    public void load() {
        archColAnnee.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        archColStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));
        archiveTable.setItems(FXCollections.observableArrayList(
                anneeService.getAllAnnees()));

        archColActions.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Eheiannee annee = getTableView().getItems().get(getIndex());
                HBox box = new HBox(8);

                if (annee.isActive()) {
                    Button btnA = new Button("📂  Archiver");
                    btnA.setStyle(
                            "-fx-background-color: #2e1a08;" +
                                    "-fx-text-fill: #ffb060;" +
                                    "-fx-font-size: 11px;" +
                                    "-fx-background-radius: 6;" +
                                    "-fx-cursor: hand;"
                    );
                    btnA.setOnAction(e -> handleArchiver(annee));
                    box.getChildren().add(btnA);
                }

                Button btnC = new Button("👁  Consulter");
                btnC.setStyle(
                        "-fx-background-color: #0d2a52;" +
                                "-fx-text-fill: #6ab0ff;" +
                                "-fx-font-size: 11px;" +
                                "-fx-background-radius: 6;" +
                                "-fx-cursor: hand;"
                );
                btnC.setOnAction(e -> handleConsulter(annee));
                box.getChildren().add(btnC);
                setGraphic(box);
            }
        });
    }

    private void handleArchiver(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Archiver " + annee.getLibelle() + " ?");
        confirm.setContentText("Une nouvelle BD sera créée pour l'année suivante.");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    anneeService.archiverAnneeActive();
                    load();
                } catch (DatabaseException e) {
                    showError("Erreur archivage", e.getMessage());
                }
            }
        });
    }

    private void handleConsulter(Eheiannee annee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Basculer vers " + annee.getLibelle() + " ?");
        confirm.setContentText("Les données affichées seront celles de cette année.");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    Connection conn = ConnectionManager.getConnection(
                            annee.getDbName());
                    dashboard.updateBadgeAnnee(
                            annee.getDbName(), annee.isActive());
                    dashboard.onConsultationChanged(conn);
                    dashboard.navigateTo(
                            dashboard.getProjetTypesPanel(),
                            dashboard.getBtnGroupes(),
                            "Types de projets",
                            "Données de " + annee.getLibelle()
                    );
                } catch (Exception e) {
                    showError("Erreur", "Impossible de se connecter");
                }
            }
        });
    }

    private void showError(String title, String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}