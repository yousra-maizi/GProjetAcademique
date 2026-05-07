package ps.eheio.gestionprojetacademique.Exceptions;

public class AuthenticationException extends Exception{
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException() {
        super("message");
    }
   /* public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }*/
}
