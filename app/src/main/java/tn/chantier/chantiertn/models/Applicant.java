package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class Applicant implements Serializable {

    private String nom ;
    private String tel;
    private String email ;
    private String raison_social;
    private String gsm ;
    private String logo;
    private boolean admin ;

    public String
    getNom() {
        return nom;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getRaison_social() {
        return raison_social;
    }

    public String getGsm() {
        return gsm;
    }

    public String getLogo() {
        return logo;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRaison_social(String raison_social) {
        this.raison_social = raison_social;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Applicant(String nom, String tel, String email, String raison_social, String gsm, String logo, boolean admin) {

        this.nom = nom;

        this.tel = tel;
        this.email = email;
        this.raison_social = raison_social;
        this.gsm = gsm;
        this.logo = logo;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return email + " "+ tel ;
    }
}
