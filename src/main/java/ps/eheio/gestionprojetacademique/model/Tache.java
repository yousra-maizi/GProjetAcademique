package ps.eheio.gestionprojetacademique.model;

public class Tache {
    private int    id;
    private String titre;
    private String description;
    private String professeurNom;
    private String professeurPrenom;
    private String etatValidation;  // depuis submission
    private Double note;            // peut être null si pas encore noté
    private String statut=null;

    public Tache(int id, String titre, String description,
                 String professeurNom, String professeurPrenom,
                 String etatValidation, Double note, String statut) {
        this.id               = id;
        this.titre            = titre;
        this.description      = description;
        this.professeurNom    = professeurNom;
        this.professeurPrenom = professeurPrenom;
        this.etatValidation   = etatValidation;
        this.note             = note;
        this.statut=statut;

    }
    public Tache(int id, String titre, String description,
                 String professeurNom, String professeurPrenom,
                 String etatValidation, Double note) {
        this.id               = id;
        this.titre            = titre;
        this.description      = description;
        this.professeurNom    = professeurNom;
        this.professeurPrenom = professeurPrenom;
        this.etatValidation   = etatValidation;
        this.note             = note;

    }

    public int    getId()               { return id; }
    public String getTitre()            { return titre; }
    public String getDescription()      { return description; }
    public String getProfesseurNom()    { return professeurNom; }
    public String getProfesseurPrenom() { return professeurPrenom; }
    public String getEtatValidation()   { return etatValidation != null ? etatValidation : "—"; }
    public String getNote()             { return note != null ? String.format("%.2f", note) : "—"; }
}