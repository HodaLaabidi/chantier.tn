package tn.chantier.chantiertn.models;

public class Ads {

    private String link;
    private String redirect_link;
    private String etat;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



    public String getRedirect_link() {
        return redirect_link;
    }

    public void setRedirect_link(String redirect_link) {
        this.redirect_link = redirect_link;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    public Ads( String link,  String redirect_link, String etat) {

        this.link = link;
        this.redirect_link = redirect_link;
        this.etat = etat;
    }
}
