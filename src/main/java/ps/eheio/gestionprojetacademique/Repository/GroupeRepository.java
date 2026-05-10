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

    /**
     * Constructeur avec connexion personnalisée (pour les archives)
     */
    public GroupeRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructeur par défaut - utilise la connexion active
     */
    public GroupeRepository() throws DatabaseException {
        try {
            this.connection = ConnectionFactory.getActiveConnection();
        } catch (Exception e) {
            throw new DatabaseException("Impossible d'obtenir la connexion active", e);
        }
    }

    /**
     * Récupère tous les groupes avec leur projet
     */
    public List<Groupe> findAll() throws DatabaseException {
        List<Groupe> groupes = new ArrayList<>();
        String req = "SELECT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id " +
                "ORDER BY g.libelle";
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
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors du chargement des groupes", e);
        }
        return groupes;
    }

    /**
     * Récupère les groupes d'un niveau (toutes les filières de ce niveau)
     */
    /**
     * Récupère les groupes dont les étudiants appartiennent à un niveau spécifique
     * Exemple: niveauId = id de 3GI → tous les groupes avec des étudiants en 3GI
     */
    public List<Groupe> findByNiveau(int niveauId) throws DatabaseException {
        List<Groupe> groupes = new ArrayList<>();
        String req = "SELECT DISTINCT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id " +
                "INNER JOIN etudiant e ON e.groupe_id = g.id " +
                "INNER JOIN classe c ON e.classe_id = c.id " +
                "WHERE c.niveau_id = ? " +
                "ORDER BY g.libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, niveauId);
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
            throw new DatabaseException("Erreur chargement groupes du niveau: " + niveauId, e);
        }
        return groupes;
    }
    /**
     * Récupère les groupes d'une filière spécifique (classe)
     */
    public List<Groupe> findByClasse(int classeId) throws DatabaseException {
        List<Groupe> groupes = new ArrayList<>();
        String req = "SELECT DISTINCT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle, " +
                "c.libelle AS classeLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id " +
                "INNER JOIN etudiant e ON e.groupe_id = g.id " +
                "INNER JOIN classe c ON e.classe_id = c.id " +
                "WHERE e.classe_id = ? " +
                "ORDER BY g.libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Groupe groupe = new Groupe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("projet_id"),
                        rs.getString("projetLibelle")
                );
                groupe.setClasseLibelle(rs.getString("classeLibelle"));
                groupes.add(groupe);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement groupes de la filière: " + classeId, e);
        }
        return groupes;
    }

    /**
     * Récupère les groupes d'une année spécifique (3, 4, 5)
     * via le libellé du niveau qui commence par l'année
     */
    public List<Groupe> findByAnnee(int annee) throws DatabaseException {
        List<Groupe> groupes = new ArrayList<>();
        String req = "SELECT DISTINCT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle, " +
                "c.libelle AS classeLibelle, n.libelle AS niveauLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id " +
                "INNER JOIN etudiant e ON e.groupe_id = g.id " +
                "INNER JOIN classe c ON e.classe_id = c.id " +
                "INNER JOIN niveau n ON c.niveau_id = n.id " +
                "WHERE n.libelle LIKE ? " +
                "ORDER BY c.libelle, g.libelle";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setString(1, annee + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Groupe groupe = new Groupe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("projet_id"),
                        rs.getString("projetLibelle")
                );
                groupe.setClasseLibelle(rs.getString("classeLibelle"));
                groupes.add(groupe);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement groupes de l'année: " + annee, e);
        }
        return groupes;
    }

    /**
     * Récupère les étudiants d'un groupe avec leurs informations (classe, niveau)
     */
    public List<Etudiant> findEtudiantsByGroupe(int groupeId) throws DatabaseException {
        List<Etudiant> etudiants = new ArrayList<>();
        String req = "SELECT u.id, u.login, u.password, u.role_id, " +
                "e.nom, e.prenom, e.groupe_id, e.classe_id, " +
                "COALESCE(c.libelle, '—') AS classeLibelle, " +
                "COALESCE(n.libelle, '—') AS niveauLibelle " +
                "FROM etudiant e " +
                "JOIN user u ON e.id = u.id " +
                "LEFT JOIN classe c ON e.classe_id = c.id " +
                "LEFT JOIN niveau n ON c.niveau_id = n.id " +
                "WHERE e.groupe_id = ? " +
                "ORDER BY e.nom";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, groupeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Etudiant etudiant = new Etudiant(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("groupe_id"),
                        rs.getInt("classe_id"),
                        rs.getString("classeLibelle"),
                        rs.getString("niveauLibelle")
                );
                etudiants.add(etudiant);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement étudiants du groupe: " + groupeId, e);
        }
        return etudiants;
    }

    /**
     * Récupère un groupe par son ID
     */
    public Groupe findById(int groupeId) throws DatabaseException {
        String req = "SELECT g.id, g.libelle, g.projet_id, " +
                "COALESCE(p.libelle, 'Aucun projet') AS projetLibelle " +
                "FROM groupe g " +
                "LEFT JOIN projet p ON g.projet_id = p.id " +
                "WHERE g.id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, groupeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Groupe(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("projet_id"),
                        rs.getString("projetLibelle")
                );
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Erreur chargement groupe: " + groupeId, e);
        }
        return null;
    }
}