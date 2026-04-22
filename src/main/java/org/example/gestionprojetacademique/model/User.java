package org.example.gestionprojetacademique.model;

public class User {
    private int id;
    private String login;
    private  String password;
    private int role_id;

    public User(int id, String login, String password, int role_id){
        this.id=id;
        this.login=login;
        this.password=password;
        this.role_id=role_id;
    }
    public int getId(){
        return id;
    }
    public void  setId(int id){
        this.id=id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return role_id;
    }

    public void setRoleId(int role_id) {
        this.role_id = role_id;
    }
}
