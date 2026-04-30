package ps.eheio.gestionprojetacademique.service;

import ps.eheio.gestionprojetacademique.Repository.EheianneeRepository;
import ps.eheio.gestionprojetacademique.model.Eheiannee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EheianneeService {
    private EheianneeRepository repo = new EheianneeRepository();

    public List<Eheiannee> getAnnees() {

        List<String> dbs = repo.listAnnees();
        List<Eheiannee> result = new ArrayList<>();

        int currentYear = LocalDate.now().getYear();
        //System.out.println("ARRRAAAy :  "+dbs);
        for (String dbName : dbs) {

            String prefix = dbName.substring(0, 4); // ehei
            String year   = dbName.substring(4);    // 2026

            String baseName = prefix + " " + year;

            boolean isActive = year.equals(String.valueOf(currentYear));

            result.add(new Eheiannee(baseName, isActive));
        }

        return result;
    }
}
