package ps.eheio.gestionprojetacademique.controller.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.TacheRepository;
import ps.eheio.gestionprojetacademique.controller.DashboardController;
import ps.eheio.gestionprojetacademique.model.*;
import ps.eheio.gestionprojetacademique.service.GroupeService;

import java.sql.Connection;
import java.util.List;

public class MembresController {

    @FXML private Label membresTitre;
    @FXML private Label membresProjet;

    @FXML private TableView<Etudiant>            membresTable;
    @FXML private TableColumn<Etudiant, String>  memColNom;
    @FXML private TableColumn<Etudiant, String>  memColPrenom;
    @FXML private TableColumn<Etudiant, String>  memColClasse;
    @FXML private TableColumn<Etudiant, String>  memColNiveau;

    @FXML private TableView<Tache>           tachesTable;
    @FXML private TableColumn<Tache, String> tacheColTitre;
    @FXML private TableColumn<Tache, String> tacheColProf;
    @FXML private TableColumn<Tache, String> tacheColDesc;
    @FXML private TableColumn<Tache, String> tacheColStatut;
    @FXML private TableColumn<Tache, String> tacheColNote;

    private DashboardController dashboard;
    private Connection          consultationConnection = null;

    @FXML
    public void initialize() {
        memColNom.prefWidthProperty().bind(
                membresTable.widthProperty().multiply(0.25));
        memColPrenom.prefWidthProperty().bind(
                membresTable.widthProperty().multiply(0.25));
        memColClasse.prefWidthProperty().bind(
                membresTable.widthProperty().multiply(0.25));
        memColNiveau.prefWidthProperty().bind(
                membresTable.widthProperty().multiply(0.25));

        tacheColTitre.prefWidthProperty().bind(
                tachesTable.widthProperty().multiply(0.20));
        tacheColProf.prefWidthProperty().bind(
                tachesTable.widthProperty().multiply(0.20));
        tacheColDesc.prefWidthProperty().bind(
                tachesTable.widthProperty().multiply(0.25));
        tacheColStatut.prefWidthProperty().bind(
                tachesTable.widthProperty().multiply(0.20));
        tacheColNote.prefWidthProperty().bind(
                tachesTable.widthProperty().multiply(0.15));
    }

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    public void setConnection(Connection conn) {
        this.consultationConnection = conn;
    }
    private GroupeService getGroupeService() throws DatabaseException {
        return consultationConnection!= null
                ? new GroupeService(consultationConnection)
                : new GroupeService();
    }

    public void load(Groupe groupe, Niveau niveau) {
        membresTitre.setText(groupe.getLibelle());
        membresProjet.setText("Projet : " + groupe.getProjetLibelle());

        try {
            Connection conn = consultationConnection != null
                    ? consultationConnection
                    : ConnectionManager.getActiveConnection();

            GroupeService service = getGroupeService();
            TacheRepository tacheRepo = new TacheRepository(conn);

            // membres
            List<Etudiant> etudiants = service
                    .getEtudiantsByGroupeAndNiveau(groupe.getId(), niveau.getId());

            memColNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            memColPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            memColClasse.setCellValueFactory(new PropertyValueFactory<>("classeLibelle"));
            memColNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauLibelle"));
            membresTable.setItems(FXCollections.observableArrayList(etudiants));

            // taches
            List<Tache> taches = tacheRepo.findByGroupe(groupe.getId());

            tacheColTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            tacheColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            tacheColProf.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            cell.getValue().getProfesseurPrenom() + " " +
                                    cell.getValue().getProfesseurNom()
                    )
            );

            tacheColStatut.setCellValueFactory(
                    new PropertyValueFactory<>("etatValidation"));
            tacheColStatut.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String s, boolean empty) {
                    super.updateItem(s, empty);
                    if (empty || s == null || s.equals("—")) {
                        setText("—"); setStyle("-fx-text-fill: #6b7280;");
                    } else if (s.toLowerCase().contains("valid")) {
                        setText(s);
                        setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
                    } else if (s.toLowerCase().contains("rejet")) {
                        setText(s);
                        setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;");
                    } else {
                        setText(s);
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
                        setText("—"); setStyle("-fx-text-fill: #6b7280;");
                    } else {
                        setText(note);
                        double val = Double.parseDouble(note);
                        if (val >= 0.7)
                            setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
                        else if (val >= 0.5)
                            setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
                        else
                            setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;");
                    }
                }
            });

            tachesTable.setItems(FXCollections.observableArrayList(taches));

        } catch (DatabaseException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println("error here");
        }
    }

    @FXML private void retour() {
        dashboard.navigateTo(
                dashboard.getGroupesPanel(), dashboard.getBtnGroupes(),
                dashboard.getGroupesController().getNiveauCourant().getLibelle(),
                "Groupes de la filière " +
                        dashboard.getGroupesController().getNiveauCourant().getLibelle()
        );
    }
}