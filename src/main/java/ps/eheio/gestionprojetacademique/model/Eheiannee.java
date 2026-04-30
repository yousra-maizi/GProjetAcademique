package ps.eheio.gestionprojetacademique.model;

public class Eheiannee {
    private  String annee;
    private boolean isActive;
    private String status;



    public Eheiannee(String annee, boolean isActive) {
        this.annee =annee;
        this.isActive = isActive;
        this.status = isActive ? "Active" : "Archivé";
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
