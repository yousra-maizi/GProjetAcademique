package ps.eheio.gestionprojetacademique.ConnectionDB;

public class DataSourceConfig {
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public static String buildUrl(String dbName) {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
    }

    public static String buildServerUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&serverTimezone=UTC";
    }
}
