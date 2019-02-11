package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class EditableSubCategory implements Serializable {

    private String id;
    private String nom_activite;
    private String id_act;

    @Override
    public String toString() {
        return id + " "+ nom_activite + " "+ id_act ;
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

    public EditableSubCategory(String id, String nom_activite, String id_act) {

        this.id = id;
        this.nom_activite = nom_activite;
        this.id_act = id_act;
    }
}
