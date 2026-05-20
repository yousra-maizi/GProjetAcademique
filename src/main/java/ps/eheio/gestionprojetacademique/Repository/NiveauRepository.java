package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.model.Niveau;

import java.sql.*;
import java.util.*;

public class NiveauRepository {

    private final Connection connection;

    public NiveauRepository(Connection connection) {
        this.connection = connection;
    }

    public NiveauRepository() throws DatabaseException {
        try {
            this.connection = ConnectionManager.getActiveConnection();
        } catch (ConnectionException e) {
            throw new DatabaseException("Connexion impossible", e);
        }
    }

    /**
     * Récupère tous les niveaux
     */
   /* public List<Niveau> findAll() throws DatabaseException {
        List<Niveau> niveaux = new ArrayList<>();
        String req = "SELECT id, libelle FROM niveau ORDER BY id";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                niveaux.add(new Niveau(rs.getInt("id"), rs.getString("libelle")));
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement niveaux", e);
        }
        return niveaux;
    }
*/
    /**
     * Récupère les niveaux par année (3, 4, 5)
     * Exemple: annee=3 → 3GI, 3IG
     */
    public List<Niveau> findByAnnee(int annee) throws DatabaseException {
        List<Niveau> niveaux = new ArrayList<>();
        String req = "SELECT id, libelle FROM niveau WHERE libelle LIKE ? ORDER BY libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setString(1, annee + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                niveaux.add(new Niveau(rs.getInt("id"), rs.getString("libelle")));
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur niveaux année " + annee, e);
        }
        return niveaux;
    }
}
