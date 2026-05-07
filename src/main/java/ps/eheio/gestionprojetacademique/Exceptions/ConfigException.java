package ps.eheio.gestionprojetacademique.Exceptions;

public class ConfigException extends DatabaseException {
    public ConfigException(String message) {
        super(message);
    }
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}