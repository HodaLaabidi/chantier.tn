package tn.chantier.chantiertn.models;

import com.plumillonforge.android.chipview.Chip;

public class Tag implements Chip {

    private String name ;
    private int type ;

    public Tag( String name , int type){
        this.name = name ;
        this.type = type ;
    }

    public Tag ( String name ){
        this.name = name;
    }
    @Override
    public String getText() {
        return null;
    }

    public int getType(){
        return type;
    }
}
