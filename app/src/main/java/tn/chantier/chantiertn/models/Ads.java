package tn.chantier.chantiertn.models;

public class Ads {

    private String id ;
    private String id_user;
    private String type;
    private String insert_date;
    private String start_date;
    private String expire_date;
    private String link;
    private String secteurs;
    private String redirect_link;
    private String etat;
    private String seen_counter;
    private String click_counter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(String secteurs) {
        this.secteurs = secteurs;
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

    public String getSeen_counter() {
        return seen_counter;
    }

    public void setSeen_counter(String seen_counter) {
        this.seen_counter = seen_counter;
    }

    public String getClick_counter() {
        return click_counter;
    }

    public void setClick_counter(String click_counter) {
        this.click_counter = click_counter;
    }

    public Ads(String id, String id_user, String type, String insert_date, String start_date, String expire_date, String link, String secteurs, String redirect_link, String etat, String seen_counter, String click_counter) {

        this.id = id;
        this.id_user = id_user;
        this.type = type;
        this.insert_date = insert_date;
        this.start_date = start_date;
        this.expire_date = expire_date;
        this.link = link;
        this.secteurs = secteurs;
        this.redirect_link = redirect_link;
        this.etat = etat;
        this.seen_counter = seen_counter;
        this.click_counter = click_counter;
    }
}
