package ps.eheio.gestionprojetacademique.model;

public class Eheiannee {
    private int    id;
    private String libelle;   // ex: "2025-2026"
    private String statut;    // "active" ou "archivée"
    private String dbName;    // ex: "ehei2026"


    public Eheiannee(int id, String libelle, String statut, String dbName) {
        this.id      = id;
        this.libelle = libelle;
        this.statut  = statut;
        this.dbName  = dbName;
    }

    public boolean isActive() { return "active".equals(statut); }

    public int    getId() { return id; }
    public String getLibelle() { return libelle; }
    public String getStatut()  { return statut; }
    public String getDbName()  { return dbName; }
}
