package tn.chantier.chantiertn.models;

import com.plumillonforge.android.chipview.Chip;

public class PackChip implements Chip {

    private String nom_activite ;
    private int id ;
    boolean isSelected = false ;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {

        return isSelected;
    }

    @Override
    public String getText() {
        return null;
    }

    public PackChip(String label) {
        this.nom_activite = label;
    }

    public PackChip() {

    }

    public PackChip(String label, int id) {

        this.nom_activite = label;
        this.id = id;
    }

    public void setLabel(String label) {

        this.nom_activite = label;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {

        return nom_activite;
    }

    public int getId() {
        return id;
    }
}
