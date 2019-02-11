package tn.chantier.chantiertn.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Professional implements Serializable {

    private int id ;
    private String nom ;
    private String prenom ;
    private String tel ;
    private String gsm;
    private int code_postal ;
    private String adresse ;
    private int partenaire ;
    private int pack ;
    private String raison_social ;
    private String email ;
    private String password ;
    private String token ;


    public Professional(){}

    @Override
    public String toString() {
        return id+" "+ nom + " "+ prenom + tel+" "+ gsm + " "+ code_postal+ adresse+" "+ partenaire + " "+ pack+ raison_social+" "+ email + " "+ password ;
    }


    public Professional(int id, String nom, String prenom, String tel, String gsm, int code_postal, String adresse, int partenaire, int pack, String raison_social, String email, String password, String token) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.gsm = gsm;
        this.code_postal = code_postal;
        this.adresse = adresse;
        this.partenaire = partenaire;
        this.pack = pack;
        this.raison_social = raison_social;
        this.email = email;
        this.password = password;
        this.token = token;
    }


    public Professional( String nom, String prenom, String tel, String gsm, int code_postal, String adresse, int partenaire, int pack, String raison_social, String email, String password, String token) {

        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.gsm = gsm;
        this.code_postal = code_postal;
        this.adresse = adresse;
        this.partenaire = partenaire;
        this.pack = pack;
        this.raison_social = raison_social;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public void setCode_postal(int code_postal) {
        this.code_postal = code_postal;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setPartenaire(int partenaire) {
        this.partenaire = partenaire;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    public void setRaison_social(String raison_social) {
        this.raison_social = raison_social;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {

        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public String getGsm() {
        return gsm;
    }

    public int getCode_postal() {
        return code_postal;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getPartenaire() {
        return partenaire;
    }

    public int getPack() {
        return pack;
    }

    public String getRaison_social() {
        return raison_social;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
