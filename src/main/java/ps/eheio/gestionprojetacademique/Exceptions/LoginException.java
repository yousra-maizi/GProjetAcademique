package ps.eheio.gestionprojetacademique.Exceptions;

public class LoginException  extends AuthenticationException {
    public  LoginException() {
        super("Login introuvable.");
    }
}
