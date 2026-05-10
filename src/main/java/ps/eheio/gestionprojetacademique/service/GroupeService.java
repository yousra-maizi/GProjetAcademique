package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.GroupeRepository;
import ps.eheio.gestionprojetacademique.model.Etudiant;
import ps.eheio.gestionprojetacademique.model.Groupe;

import java.sql.Connection;
import java.util.List;

public class GroupeService {

    private final GroupeRepository groupeRepo;

    public GroupeService(Connection connection) {
        this.groupeRepo = new GroupeRepository(connection);
    }

    public GroupeService() throws DatabaseException {
        this.groupeRepo = new GroupeRepository();
    }

    /**
     * Récupère tous les groupes
     */
    public List<Groupe> getAllGroupes() throws DatabaseException {
        return groupeRepo.findAll();
    }

    /**
     * Récupère les groupes d'un niveau
     */
    public List<Groupe> getGroupesByNiveau(int niveauId) throws DatabaseException {
        return groupeRepo.findByNiveau(niveauId);
    }

    /**
     * Récupère les groupes d'une filière (classe)
     */
    public List<Groupe> getGroupesByClasse(int classeId) throws DatabaseException {
        return groupeRepo.findByClasse(classeId);
    }

    /**
     * Récupère les étudiants d'un groupe
     */
    public List<Etudiant> getEtudiantsByGroupe(int groupeId) throws DatabaseException {
        return groupeRepo.findEtudiantsByGroupe(groupeId);
    }
}