package org.example.gestionprojetacademique.Repository;

import org.example.gestionprojetacademique.ConnectionDB.DBConnection;
import org.example.gestionprojetacademique.model.Admin;
import org.example.gestionprojetacademique.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            stmt.setString(1, login);   //  first parameter
            stmt.setString(2, password); // second parameter

            ResultSet rs = stmt.executeQuery();

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

        } catch (Exception e){
            System.err.println("Erreur lors recherche : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
