package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class City implements Serializable {

    private String code ;
    private String localite ;
    private String gouvernerat;

    public String getGouvernorat() {
        return gouvernerat;
    }

    public void setGouvernorat(String gouvernorat) {
        this.gouvernerat = gouvernorat;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getCode() {

        return code;
    }

    public String getLocalite() {
        return localite;
    }

    public City(String code, String localite, String gouvernerat) {
        this.gouvernerat = gouvernerat ;
        this.code = code;
        this.localite = localite;
    }

    public City(){

    }

    @Override
    public String toString() {
        return "code = " +this.code + " localite = "+this.getLocalite() +"gouvernorat = "+ this.gouvernerat +" !"  ;
    }
}
