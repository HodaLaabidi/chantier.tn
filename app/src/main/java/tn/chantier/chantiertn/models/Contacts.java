package tn.chantier.chantiertn.models;



public class Contacts {

    private String nom;
    private String tel ;
    private String email ;

    @Override
    public String toString() {
        return nom + " " +" "+tel + " "+ email + " ";
    }

    public Contacts(String nom, String tel, String email) {
        this.nom = nom;
        this.tel = tel;
        this.email = email;
    }

    public String getNom() {

        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
