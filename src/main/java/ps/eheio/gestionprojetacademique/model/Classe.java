package ps.eheio.gestionprojetacademique.model;

public class Classe {
    private int    id;
    private String libelle;
    private int    niveauId;
    private String niveauLibelle;

    public Classe(int id, String libelle, int niveauId, String niveauLibelle) {
        this.id            = id;
        this.libelle       = libelle;
        this.niveauId      = niveauId;
        this.niveauLibelle = niveauLibelle;
    }

    // Constructeur sans niveauLibelle (pour chargement simple)
    public Classe(int id, String libelle, int niveauId) {
        this.id = id;
        this.libelle = libelle;
        this.niveauId = niveauId;
        this.niveauLibelle = null;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getNiveauId() {
        return niveauId;
    }

    public String getNiveauLibelle() {
        return niveauLibelle;
    }

    public void setNiveauLibelle(String niveauLibelle) {
        this.niveauLibelle = niveauLibelle;
    }
}