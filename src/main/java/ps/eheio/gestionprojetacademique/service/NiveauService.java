package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.NiveauRepository;
import ps.eheio.gestionprojetacademique.model.Niveau;

import java.sql.Connection;
import java.util.List;

public class NiveauService {

    private final NiveauRepository niveauRepo;

    public NiveauService(Connection connection) {
        this.niveauRepo = new NiveauRepository(connection);
    }

    public NiveauService() throws DatabaseException {
        this.niveauRepo = new NiveauRepository();
    }

   /* public List<Niveau> getAllNiveaux() throws DatabaseException {
        return niveauRepo.findAll();
    }*/

    public List<Niveau> getNiveauxByAnnee(int annee) throws DatabaseException {
        return niveauRepo.findByAnnee(annee);
    }
}