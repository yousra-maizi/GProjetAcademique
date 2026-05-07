package ps.eheio.gestionprojetacademique.Exceptions;

public class ArchiveException extends DatabaseException {
    public ArchiveException(String message) {
        super(message);
    }
    public ArchiveException(String message, Throwable cause) {
        super(message, cause);
    }
}