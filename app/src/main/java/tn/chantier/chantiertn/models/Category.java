package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class Category implements Serializable {

    int id ;
    String nom_secteur ;
    String slug ;
    String icon ;
    public  Category(){

    }

    public Category(int id, String categoryLabel, String slug, String icon) {
        this.id = id;
        this.nom_secteur = categoryLabel;
        this.slug = slug;
        this.icon = icon;
    }

    public int getId() {

        return id;
    }

    public String getCategoryLabel() {
        return nom_secteur;
    }

    public String getSlug() {
        return slug;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.nom_secteur = categoryLabel;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
