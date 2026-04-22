package ps.eheio.gestionprojetacademique.model;

public class Admin extends User{
    //private int id;
    private String nom;
    private String prenom;

    public Admin(int id, String login, String password, int role_id, String nom, String prenom){
        super(id, login,password,role_id);
        //this.id=idAdmin;
        this.nom=nom;
        this.prenom=prenom;
    }

    /*@Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }*/

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
