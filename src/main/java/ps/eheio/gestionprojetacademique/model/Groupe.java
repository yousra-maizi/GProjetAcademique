package ps.eheio.gestionprojetacademique.model;

public class Groupe {
    private int    id;
    private String libelle;
    private int    projetId;
    private String projetLibelle;
    private int    niveauId;  // Ajouté

    public Groupe(int id, String libelle, int projetId, String projetLibelle) {
        this.id = id;
        this.libelle = libelle;
        this.projetId = projetId;
        this.projetLibelle = projetLibelle;
    }

    public Groupe(int id, String libelle, int projetId, String projetLibelle, int niveauId) {
        this.id = id;
        this.libelle = libelle;
        this.projetId = projetId;
        this.projetLibelle = projetLibelle;
        this.niveauId = niveauId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public int getProjetId() { return projetId; }
    public void setProjetId(int projetId) { this.projetId = projetId; }
    public String getProjetLibelle() { return projetLibelle; }
    public void setProjetLibelle(String projetLibelle) { this.projetLibelle = projetLibelle; }
    public int getNiveauId() { return niveauId; }
    public void setNiveauId(int niveauId) { this.niveauId = niveauId; }
}