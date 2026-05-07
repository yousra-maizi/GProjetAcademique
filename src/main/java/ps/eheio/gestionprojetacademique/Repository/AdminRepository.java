package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionFactory;
import ps.eheio.gestionprojetacademique.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepository {

    public Admin findByLogin(String login) {
        String req = "SELECT u.id, u.login, u.password, u.role_id, a.nom, a.prenom " +
                "FROM user u " +
                "INNER JOIN administrateur a ON u.id = a.id " +
                "INNER JOIN role r ON u.role_id = r.id " +
                "WHERE u.login = ? AND r.libelle = 'administrateur'";

        try {

            Connection conn = ConnectionFactory.getActiveConnection();
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
