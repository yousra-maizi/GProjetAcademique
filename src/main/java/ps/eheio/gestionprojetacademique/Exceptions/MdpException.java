package ps.eheio.gestionprojetacademique.Exceptions;

public class MdpException  extends AuthenticationException {
    public MdpException() {
        super("Mot de passe incorrect");
    }
}
