package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class City implements Serializable {

    private String code ;
    private String localite ;

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

    public City(String code, String localite) {

        this.code = code;
        this.localite = localite;
    }
}
