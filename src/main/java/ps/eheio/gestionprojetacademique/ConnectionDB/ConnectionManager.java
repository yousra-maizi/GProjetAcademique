package ps.eheio.gestionprojetacademique.ConnectionDB;


import ps.eheio.gestionprojetacademique.Exceptions.ConfigException;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionManager {

    private static Connection activeConnection = null;
    private static String     activeDbName     = null; // Nom BD active


    private ConnectionManager(){}


    public static void initialize() throws ConnectionException, ConfigException {
     try {

         Properties props  = loadProps();

         String     dbName = props.getProperty("db.active");
         if (dbName == null || dbName.isEmpty()) {
             throw new ConfigException(
                     "La propriété 'db.active' est manquante dans config.properties"
             );
         }
         // Crée la connexion et la stocke dans activeConnection
         activeConnection = createConnection(dbName);
         activeDbName     = dbName;
         System.out.println("Connecté sur: " + dbName);
     } catch (ConfigException e) {
         throw e;
     } catch (Exception e) {
         throw new ConnectionException(
                 "Impossible d'initialiser la connexion à la base de données", e
         );

     }
    }
    // connexion vers n'importe quelle BD
    public static Connection getConnection(String dbName) throws ConnectionException {
        return createConnection(dbName);
    }
    // connexion au serveur MySQL
    public static Connection getServerConnection() throws ConnectionException {
        try {

            Properties props = loadProps();
            String url = "jdbc:mysql://"
                    + props.getProperty("db.host")
                    + ":" + props.getProperty("db.port")
                    + "/?useSSL=false&serverTimezone=UTC";
            System.out.println("helo serverr");
            return DriverManager.getConnection(
                    url,
                    props.getProperty("db.user"),
                    props.getProperty("db.pass")
            );

        } catch (Exception e) {
            System.out.println("jjj");
            throw new ConnectionException(
                    "Impossible de se connecter au serveur MySQL", e
            );
        }
    }
    // retourne la connexion active
    public static Connection getActiveConnection() throws ConnectionException {
        if (activeConnection == null) {
            throw new ConnectionException(
                    "Aucune connexion active "
            );

        }
        System.out.println("activated db"+activeDbName);
        return activeConnection;
    }

    public static String getActiveDbName() {
        return activeDbName;
    }
    // change la BD active appelée après archivage
    public static void switchActiveConnection(String newDbName) throws ConnectionException {
        try {
            if (activeConnection != null && !activeConnection.isClosed()) {
                activeConnection.close();
            }
            activeConnection = createConnection(newDbName);
            activeDbName     = newDbName;
            updateConfig(newDbName);
        } catch (Exception e) {
            throw new ConnectionException(
                    "Impossible de basculer vers la BD: " + newDbName, e
            );
        }
    }


    private static Connection createConnection(String dbName) throws ConnectionException {
        try {
            Properties props = loadProps();
            String url = "jdbc:mysql://"
                    + props.getProperty("db.host")
                    + ":" + props.getProperty("db.port")
                    + "/" + dbName
                    + "?useSSL=false&serverTimezone=UTC";
            return DriverManager.getConnection(
                    url,
                    props.getProperty("db.user"),
                    props.getProperty("db.pass")
            );
        } catch (Exception e) {
            throw new ConnectionException(
                    "Connexion échouée vers la BD: " + dbName, e
            );
        }
    }
// Met à jour le fichier config.properties avec la nouvelle BD active
private static void updateConfig(String newDbName) throws ConfigException {
    try {
        File file = new File("config/config.properties");

        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        props.setProperty("db.active", newDbName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, null);
        }

    } catch (Exception e) {

        throw new ConfigException("Impossible de mettre à jour config.properties", e);
    }
}
    // Charge les propriétés depuis config.properties

    private static Properties loadProps() throws ConfigException {
        try {
            File file = new File("config/config.properties");

            if (!file.exists()) {
                throw new ConfigException("config/config.properties introuvable");
            }

            Properties props = new Properties();

            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
            }

            return props;

        } catch (Exception e) {
            throw new ConfigException("Erreur lecture config.properties", e);
        }
    }
    public static void closeAll() throws ConnectionException {
        try {
            if (activeConnection != null && !activeConnection.isClosed()) {
                activeConnection.close();
            }
        } catch (Exception e) {
            throw new ConnectionException(
                    "Erreur lors de la fermeture de la connexion", e
            );
        }
    }
    /*
    // singleton conection
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("made new cnx");
                return connection;
            }

            InputStream input = ConnectionFactory.class
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

            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connected successfully.");

        } catch (Exception e) {
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
    }*/
}
