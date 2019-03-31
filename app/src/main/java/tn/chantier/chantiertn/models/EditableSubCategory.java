package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class EditableSubCategory implements Serializable {

    private String id;
    private String nom_activite;
    private String id_act;
    private String id_sec ;


    public String getId_sec() {
        return id_sec;
    }

    public void setId_sec(String id_sec) {
        this.id_sec = id_sec;
    }



    @Override
    public String toString() {
        return id + " "+ nom_activite + " "+ id_act  + " "+ id_sec;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom_activite() {
        return nom_activite;
    }

    public void setNom_activite(String nom_activite) {
        this.nom_activite = nom_activite;
    }

    public String getId_act() {
        return id_act;
    }

    public void setId_act(String id_act) {
        this.id_act = id_act;
    }

    public EditableSubCategory(String id, String nom_activite, String id_act , String id_sec) {

        this.id = id;
        this.nom_activite = nom_activite;
        this.id_act = id_act;
        this.id_sec = id_sec;
    }
    public EditableSubCategory() {

    }
}
