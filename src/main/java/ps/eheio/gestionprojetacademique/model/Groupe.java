package ps.eheio.gestionprojetacademique.model;

public class Groupe {
    private int    id;
    private String libelle;
    private int    projetId;
    private String projetLibelle;
    private String classeLibelle;  // Ajouté pour afficher la filière

    // Constructeur avec 5 paramètres (avec classeLibelle)
    public Groupe(int id, String libelle, int projetId, String projetLibelle, String classeLibelle) {
        this.id            = id;
        this.libelle       = libelle;
        this.projetId      = projetId;
        this.projetLibelle = projetLibelle;
        this.classeLibelle = classeLibelle;
    }

    // Constructeur avec 4 paramètres (sans classeLibelle)
    public Groupe(int id, String libelle, int projetId, String projetLibelle) {
        this.id            = id;
        this.libelle       = libelle;
        this.projetId      = projetId;
        this.projetLibelle = projetLibelle;
        this.classeLibelle = null;
    }

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

    public String getProjetLibelle() {
        return projetLibelle;
    }

    public void setProjetLibelle(String projetLibelle) {
        this.projetLibelle = projetLibelle;
    }

    public String getClasseLibelle() {
        return classeLibelle;
    }

    public void setClasseLibelle(String classeLibelle) {
        this.classeLibelle = classeLibelle;
    }
}