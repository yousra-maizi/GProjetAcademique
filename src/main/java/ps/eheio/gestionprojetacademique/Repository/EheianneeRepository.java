package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.DBConnection;
import ps.eheio.gestionprojetacademique.model.Eheiannee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EheianneeRepository {

    public List<String> listAnnees() {
        List<String> databases = new ArrayList<>();

        try (
               // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","");
               Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW DATABASES LIKE 'ehei%'")) {

            while (rs.next()) {
                String dbName = rs.getString(1); // ex: ehei2026
                databases.add(dbName);
                System.out.println(dbName);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return databases;
    }
}
