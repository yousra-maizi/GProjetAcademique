package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.model.Tache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TacheRepository {

    private final Connection connection;

    public TacheRepository(Connection connection) {
        this.connection = connection;
    }
//////////soumission
    public List<Tache> findByGroupe(int groupeId) throws DatabaseException {
        List<Tache> taches = new ArrayList<>();
        String req =
                "SELECT t.id, t.titre, t.description, " +
                        "p.nom AS professeurNom, p.prenom AS professeurPrenom, " +
                        "null as etatValidation, s.note " +
                        "FROM tache t " +
                        "JOIN cible_tache_groupe ctg ON t.id = ctg.tache_id " +
                        "JOIN professeur p ON t.professeur_id = p.id " +
                        "LEFT JOIN submission s ON s.cible_id = ctg.id " +
                        "WHERE ctg.groupe_id = ? " +
                        "ORDER BY t.titre";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, groupeId); // pour le LEFT JOIN submission
          // stmt.setInt(2, groupeId); // pour le WHERE
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // note peut être null si pas de soumission
                Double note = rs.getObject("note") != null
                        ? rs.getDouble("note")
                        : null;
                taches.add(new Tache(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("professeurNom"),
                        rs.getString("professeurPrenom"),
                        rs.getString("etatValidation"),
                        note
                ));
            }
        } catch(SQLException e) {
           // System.out.println( e.getMessage());
            throw new DatabaseException(
                    "Erreur chargement tâches du groupe: " + groupeId, e
            );
        }
        return taches;
    }
}