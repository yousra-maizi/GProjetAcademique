package ps.eheio.gestionprojetacademique.model;

public class Groupe {
    private int    id;
    private String libelle;
    private int projetId;
    private String projetLibelle;


    public Groupe(int id, String libelle, int projetId, String projetLibelle) {
        this.id            = id;
        this.libelle       = libelle;
        this.projetId      = projetId;
        this.projetLibelle = projetLibelle;
    }
    /*
    public Groupe(int id, String libelle, int projetId, private String projetLibelle;){

        this.id=id;
        this.libelle=libelle;
        this.projetId=projetId;
        this.projetLibelle=projetLibelle;
    }
*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }
    public String getProjetLibelle()  { return projetLibelle; }
}
