package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.DBConnection;
import ps.eheio.gestionprojetacademique.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepository {

    public Admin findByLoginPassword(String login, String password){
        String req = "SELECT u.id, u.login, u.password, u.role_id, a.nom, a.prenom " +
                "FROM user u " +
                "INNER JOIN administrateur a ON u.id = a.id " +
                "INNER JOIN role r ON u.role_id = r.id " +
                "WHERE u.login = ? AND u.password = ? " +
                "AND r.libelle = 'administrateur'";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            System.out.println("hello cnx");
            if (rs.next()) {
                System.out.println("Admin found: " + rs.getString("login")); // DEBUG
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                );


            } else {
                System.out.println("Aucun admin trouvé !"); // DEBUG
            }

        } catch (SQLException e){
            System.err.println("Erreur lors recherche : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
