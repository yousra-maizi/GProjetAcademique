package ps.eheio.gestionprojetacademique.model;

public class Etudiant extends User {
    private String nom;
    private String prenom;
    private int    groupeId;
    private int    classeId;
    private String classeLibelle;  // récupéré via JOIN
    private String niveauLibelle;  // récupéré via JOIN

    public Etudiant(int id, String login, String password, int roleId,
                    String nom, String prenom, int groupeId, int classeId,
                    String classeLibelle, String niveauLibelle) {
        super(id, login, password, roleId);
        this.nom           = nom;
        this.prenom        = prenom;
        this.groupeId      = groupeId;
        this.classeId      = classeId;
        this.classeLibelle = classeLibelle;
        this.niveauLibelle = niveauLibelle;
    }

    public String getNom()           { return nom; }
    public String getPrenom()        { return prenom; }
    public int    getGroupeId()      { return groupeId; }
    public int    getClasseId()      { return classeId; }
    public String getClasseLibelle() { return classeLibelle; }
    public String getNiveauLibelle() { return niveauLibelle; }
}