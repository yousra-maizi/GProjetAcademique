package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Exceptions.ArchiveException;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.Repository.AnneeRepository;
import ps.eheio.gestionprojetacademique.model.Eheiannee;

import java.util.List;

public class AnneeService {
    
    private AnneeRepository repo = new AnneeRepository();


    public List<Eheiannee> getAllAnnees() {
        return repo.findAll();
    }

    public Eheiannee getAnneeActive() {
        return repo.findAll().stream()
                .filter(Eheiannee::isActive)
                .findFirst()
                .orElse(null);
    }

    public  void archiverAnneeActive() throws ArchiveException, DatabaseException {
        Eheiannee active = getAnneeActive();
        if (active == null) {
            throw new ArchiveException(
                    "Aucune année active trouvée ,archivage impossible"
            );
        }
        repo.archiverEtCreerNouvelle(active);
    }
   /* public List<Eheiannee> getAnnees() {

        List<Eheiannee> dbs = repo.findAll();
        List<Eheiannee> result = new ArrayList<>();

        int currentYear = LocalDate.now().getYear();
        //System.out.println("ARRRAAAy :  "+dbs);
        for (Eheiannee dbName : dbs) {

            String prefix = dbName.substring(0, 4); // ehei
            String year   = dbName.substring(4);    // 2026

            String baseName = prefix + " " + year;

            boolean isActive = year.equals(String.valueOf(currentYear));

            result.add(new Eheiannee(baseName, isActive));
        }

        return result;
    }*/
}
