package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.GroupeRepository;
import ps.eheio.gestionprojetacademique.model.Etudiant;
import ps.eheio.gestionprojetacademique.model.Groupe;

import java.sql.Connection;
import java.util.List;

public class GroupeService {

    private final GroupeRepository repo;

    // connexion passée depuis le controller
    public GroupeService(Connection connection) {
        this.repo = new GroupeRepository(connection);
    }

    // utilise la connexion active par défaut
    public GroupeService() throws DatabaseException {
        this.repo = new GroupeRepository();
    }

    public List<Groupe> getAllGroupes() throws DatabaseException {
        return repo.findAll();
    }

    public List<Etudiant> getEtudiantsByGroupe(int groupeId) throws DatabaseException {
        return repo.findEtudiantsByGroupe(groupeId);
    }
}