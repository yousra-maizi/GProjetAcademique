package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionFactory;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.model.Etudiant;
import ps.eheio.gestionprojetacademique.model.Groupe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupeRepository {

    private final Connection connection;

    // constructeur — reçoit la connexion depuis le controller
    // si consultationConnection existe → on l'utilise
    // sinon → connexion active
    public GroupeRepository(Connection connection) {
        this.connection = connection;
    }

    // constructeur par défaut — utilise la connexion active
    public GroupeRepository() throws DatabaseException {
        try {
            this.connection = ConnectionFactory.getActiveConnection();
        } catch (Exception e) {
            throw new DatabaseException("Impossible d'obtenir la connexion active", e);
        }
    }

    public List<Groupe> findAll() throws DatabaseException {
        List<Groupe> groupes = new ArrayList<>();
        String req ="SELECT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id ";
                /*                 "SELECT g.id,g.libelle, p.libelle FROM groupe g INNER JOIN projet p on g.projet_id=p.id";

                "SELECT g.id, g.libelle, " +
                        "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle " +
                        "FROM groupe g " +
                        "LEFT JOIN projet p ON g.projet_id = p.id " +
                        "ORDER BY g.libelle";*/
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                groupes.add(new Groupe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("projet_id"),
                        rs.getString("projetLibelle")
                ));
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors du chargement des groupes", e);
        }
        return groupes;
    }

    public List<Etudiant> findEtudiantsByGroupe(int groupeId) throws DatabaseException {
        List<Etudiant> etudiants = new ArrayList<>();
        String req =
                "SELECT u.id, u.login, u.password, u.role_id, " +
                        "e.nom, e.prenom, e.groupe_id, e.classe_id, " +
                        "c.libelle AS classeLibelle, " +
                        "n.libelle AS niveauLibelle " +
                        "FROM etudiant e " +
                        "JOIN user u    ON e.id = u.id " +
                        "LEFT JOIN classe  c ON e.classe_id = c.id " +
                        "LEFT JOIN niveau  n ON c.niveau_id = n.id " +
                        "WHERE e.groupe_id = ? " +
                        "ORDER BY e.nom";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, groupeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                etudiants.add(new Etudiant(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("groupe_id"),
                        rs.getInt("classe_id"),
                        rs.getString("classeLibelle") != null
                                ? rs.getString("classeLibelle") : "—",
                        rs.getString("niveauLibelle") != null
                                ? rs.getString("niveauLibelle") : "—"
                ));
            }
        } catch (Exception e) {
            throw new DatabaseException(
                    "Erreur chargement étudiants du groupe: " + groupeId, e
            );
        }
        return etudiants;
    }
}