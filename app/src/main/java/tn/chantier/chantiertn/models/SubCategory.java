package tn.chantier.chantiertn.models;

import android.widget.CheckBox;

import java.io.Serializable;

public class SubCategory implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    String id;
    CheckBox checkBox;


    public SubCategory(String id, CheckBox checkBox) {
        this.checkBox = checkBox;
        this.id = id;

    }


}
