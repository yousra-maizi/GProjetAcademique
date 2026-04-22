package org.example.gestionprojetacademique.ConnectionDB;

//import com.almasb.fxgl.input.Input;
//import com.almasb.fxgl.net.Connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static Connection connection = null;

    public static Connection getConnection(){
        if(connection != null) return connection;
        try {
            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            Properties props = new Properties();
            props.load(input);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.pass");

           // Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connected successfully.");


        }catch (Exception e){
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
