package ps.eheio.gestionprojetacademique.model;

public class Niveau {
    private int    id;
    private String libelle;

    public Niveau(int id, String libelle) {
        this.id      = id;
        this.libelle = libelle;
    }

    public int    getId()      { return id; }
    public String getLibelle() { return libelle; }
}