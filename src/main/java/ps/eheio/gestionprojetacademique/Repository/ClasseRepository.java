package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.model.Classe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//////////////////////NOT USED IN ANYTHING !!!!!!
public class ClasseRepository {

    private final Connection connection;

    public ClasseRepository(Connection connection) {
        this.connection = connection;
    }

    public ClasseRepository() throws DatabaseException {
        try {
            this.connection = ConnectionManager.getActiveConnection();
        } catch (ConnectionException e) {
            throw new DatabaseException("Connexion impossible", e);
        }
    }

    /**
     * Récupère toutes les classes (filières)
     */
    public List<Classe> findAll() throws DatabaseException {
        List<Classe> classes = new ArrayList<>();
        String req = "SELECT c.id, c.libelle, c.niveau_id, n.libelle AS niveauLibelle " +
                "FROM classe c " +
                "LEFT JOIN niveau n ON c.niveau_id = n.id " +
                "ORDER BY n.id, c.libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(new Classe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("niveau_id"),
                        rs.getString("niveauLibelle")
                ));
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement classes", e);
        }
        return classes;
    }

    /**
     * Récupère les classes d'un niveau spécifique
     */
    public List<Classe> findByNiveau(int niveauId) throws DatabaseException {
        List<Classe> classes = new ArrayList<>();
        String req = "SELECT c.id, c.libelle, c.niveau_id, n.libelle AS niveauLibelle " +
                "FROM classe c " +
                "LEFT JOIN niveau n ON c.niveau_id = n.id " +
                "WHERE c.niveau_id = ? " +
                "ORDER BY c.libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, niveauId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(new Classe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("niveau_id"),
                        rs.getString("niveauLibelle")
                ));
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement classes du niveau: " + niveauId, e);
        }
        return classes;
    }
}