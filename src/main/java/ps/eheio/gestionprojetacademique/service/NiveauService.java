package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.ClasseRepository;
import ps.eheio.gestionprojetacademique.Repository.NiveauRepository;
import ps.eheio.gestionprojetacademique.model.Classe;
import ps.eheio.gestionprojetacademique.model.Niveau;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class NiveauService {

    private final NiveauRepository niveauRepo;
    private final ClasseRepository classeRepo;

    public NiveauService(Connection connection) {
        this.niveauRepo = new NiveauRepository(connection);
        this.classeRepo = new ClasseRepository(connection);
    }

    public NiveauService() throws DatabaseException {
        this.niveauRepo = new NiveauRepository();
        this.classeRepo = new ClasseRepository();
    }

    /**
     * Récupère tous les niveaux
     */
    public List<Niveau> getAllNiveaux() throws DatabaseException {
        return niveauRepo.findAll();
    }

    /**
     * Récupère les niveaux par année (3, 4, 5)
     * Pour 3 -> "3ème année", "3ème année Génie Info", etc.
     */
    public List<Niveau> getNiveauxByAnnee(int annee) throws DatabaseException {
        return niveauRepo.findByAnnee(annee);
    }

    /**
     * Récupère les filières (classes) d'un niveau
     */
    public List<Classe> getFilieresByNiveau(int niveauId) throws DatabaseException {
        return classeRepo.findByNiveau(niveauId);
    }

    /**
     * Récupère toutes les filières (classes)
     */
    public List<Classe> getAllFilieres() throws DatabaseException {
        return classeRepo.findAll();
    }
}
//v3