package org.example.gestionprojetacademique.service;

import org.example.gestionprojetacademique.Repository.AdminRepository;
import org.example.gestionprojetacademique.model.Admin;

public class AuthService {
    // Single instance for the whole app
    private static AuthService instance;
    private final AdminRepository repo = new AdminRepository();
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

    public boolean auth(String login, String password){
        if(login == null || login.isEmpty() ||
            password == null || password.isEmpty()){
            return false;
        }
        // recuperation  d'obj admin depuis la bd
        Admin admin = repo.findByLoginPassword(login, password);

        if(admin == null) return false;
        // authentification reussie yay
        loggedInAdmin = admin;
        return true;
    }
        public static Admin getLoggedInAdmin() {
            return loggedInAdmin;
        }

        public static boolean isLoggedIn() {
            return loggedInAdmin != null;
        }

        public static void logout() {
            System.out.println("Admin logged out: " +
                    (loggedInAdmin != null ? loggedInAdmin.getLogin() : "unknown"));
            loggedInAdmin = null;
        }
    }

