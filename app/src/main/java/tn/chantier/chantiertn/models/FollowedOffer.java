package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class FollowedOffer implements Serializable {

    private String type;
    private String slug ;
    private String id_client ;
    private String nom_client;
    private String prenom_client ;
    private String tel_client;
    private String email_client;
    private String id;
    private String titre ;
    private String date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        slug = slug;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public String getPrenom_client() {
        return prenom_client;
    }

    public void setPrenom_client(String prenom_client) {
        this.prenom_client = prenom_client;
    }

    public String getTel_client() {
        return tel_client;
    }

    public void setTel_client(String tel_client) {
        this.tel_client = tel_client;
    }

    public String getEmail_client() {
        return email_client;
    }

    public void setEmail_client(String email_client) {
        this.email_client = email_client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FollowedOffer(String type, String slug, String id_client, String nom_client, String prenom_client, String tel_client, String email_client, String id, String titre, String date) {

        this.type = type;
        this.slug = slug;
        this.id_client = id_client;
        this.nom_client = nom_client;
        this.prenom_client = prenom_client;
        this.tel_client = tel_client;
        this.email_client = email_client;
        this.id = id;
        this.titre = titre;
        this.date = date;
    }

    public FollowedOffer() {

    }

    @Override
    public String toString() {
        return type + " "+ slug + " " + nom_client + " "+ tel_client + " "+ prenom_client + " "+ email_client + " "+ titre + " " + date ;
    }
}
