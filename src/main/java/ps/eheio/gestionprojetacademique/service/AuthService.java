package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.*;
import ps.eheio.gestionprojetacademique.Repository.AdminRepository;
import ps.eheio.gestionprojetacademique.model.Admin;

public class AuthService {
    // Single instance for the whole app
    private static AuthService instance;
    private final AdminRepository repAdmin = new AdminRepository();
    private static Admin loggedInAdmin = null;

    // Private constructor — no one can do new AuthService()
    private AuthService() {}

    // Global access point
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public void authentification(String login, String password) throws AuthenticationException, DatabaseException {

        if (login == null || login.isEmpty()) {
            throw new AuthenticationException("Login requis.");
        }

        if (password == null || password.isEmpty()) {
            throw new AuthenticationException("Mot de passe requis.");
        }

        try {
            Admin admin = repAdmin.findByLogin(login);

            if (admin == null) {
                throw new LoginException();
            }

            if (!admin.getPassword().equals(password)) {
                throw new MdpException();
            }

            loggedInAdmin = admin;

        } catch (LoginException | MdpException e) {
            throw e;

        } catch (Exception e) {
            throw new DatabaseException("Erreur d'accès à la base.", e);
        }
    }
        // session
        public static Admin getLoggedInAdmin() {
            return loggedInAdmin;
        }
        // verif si qlq est connecté !
        public static boolean isLoggedIn() {
            return loggedInAdmin != null;
        }
        // le déconnexion
        public static void logout() {
            System.out.println("Admin logged out: " +
                    (loggedInAdmin != null ? loggedInAdmin.getLogin() : "unknown"));
            loggedInAdmin = null;
        }
    }

