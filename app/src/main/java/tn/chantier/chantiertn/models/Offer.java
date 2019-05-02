package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class Offer implements Serializable {

    private int id ;
    private String titre ;
    private int code_postal ;
    private int id_sec ;
    private int id_act ;
    private String echeance;
    private String quantite ;
    private String unite ;
    private String contact ;
    private int id_cli ;
    private int check ;
    private String description ;
    private String info_supp;
    private String budget_min;
    private String budget_max ;
    private String date_demande ;
    private String date_mod ;
    private String fichier ;
    private String appelle ;
    private String objet ;
    private String commission ;
    private String typeClient ;


    public Offer(int id, String titre, int code_postal, int id_sec, int id_act, String echeance, String quantite, String unite, String contact, int id_cli, String description, String info_supp, String budget_min, String budget_max, String date_demande, String date_mod, String fichier, String appelle, String objet, String commission, String typeClient , int check) {
        this.id = id;
        this.check = check ;
        this.titre = titre;
        this.code_postal = code_postal;
        this.id_sec = id_sec;
        this.id_act = id_act;
        this.echeance = echeance;
        this.quantite = quantite;
        this.unite = unite;
        this.contact = contact;
        this.id_cli = id_cli;
        this.description = description;
        this.info_supp = info_supp;
        this.budget_min = budget_min;
        this.budget_max = budget_max;
        this.date_demande = date_demande;
        this.date_mod = date_mod;
        this.fichier = fichier;
        this.appelle = appelle;
        this.objet = objet;
        this.commission = commission;
        this.typeClient = typeClient;
    }

    public void setId(int id) {

        this.id = id;
    }


    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setId_cli(int id_cli) {
        this.id_cli = id_cli;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInfo_supp(String info_supp) {
        this.info_supp = info_supp;
    }

    public void setBudget_min(String budget_min) {
        this.budget_min = budget_min;
    }

    public void setBudget_max(String budget_max) {
        this.budget_max = budget_max;
    }

    public void setDate_demande(String date_demande) {
        this.date_demande = date_demande;
    }

    public void setDate_mod(String date_mod) {
        this.date_mod = date_mod;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public void setAppelle(String appelle) {
        this.appelle = appelle;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

    public int getId_cli() {

        return id_cli;
    }

    public String getDescription() {
        return description;
    }

    public String getInfo_supp() {
        return info_supp;
    }

    public String getBudget_min() {
        return budget_min;
    }

    public String getBudget_max() {
        return budget_max;
    }

    public String getDate_demande() {
        return date_demande;
    }

    public String getDate_mod() {
        return date_mod;
    }

    public String getFichier() {
        return fichier;
    }

    public String getAppelle() {
        return appelle;
    }

    public String getObjet() {
        return objet;
    }

    public String getCommission() {
        return commission;
    }

    public String getTypeClient() {
        return typeClient;
    }

    @Override
    public String toString() {
       /* private int id ;
        private String titre ;
        private int code_postal ;
        private int id_sec ;
        private int id_act ;
        private String echeance;
        private String quantite ;
        private String unite ;
        private String contact ;
        private int id_cli ;
        private int check ;
        private String description ;
        private String info_supp;
        private String budget_min;
        private String budget_max ;
        private String date_demande ;
        private String date_mod ;
        private String fichier ;
        private String appelle ;
        private String objet ;
        private String commission ;
        private String typeClient ;*/
        return  " id "+id+ " titre "+titre+" code_postal "+code_postal+ " id_sec "+id_sec+ " id_act "+id_act+" "+" echeance "+ echeance +" "+ "quantite "
                + quantite +" "+ "contact "+ contact +" "
                + "id_cli "+ id_cli +" "
                + "check "+ check +" "
                + "description "+ description +" "
                + "info_supp "+ info_supp +" "
                + "budget_min "+ budget_min +" "
                + "budget_max "+ budget_max +" "
                + "date_demande "+ date_demande +" "
                + "date_mod "+ date_mod +" "
                + "fichier "+ fichier +" "
                + "appelle "+ appelle +" "
                + "objet "+ objet +" "
                + "commission "+ commission +" "
                + "typeClient "+ typeClient +" "
               ;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setCode_postal(int code_postal) {
        this.code_postal = code_postal;
    }

    public void setId_sec(int id_sec) {
        this.id_sec = id_sec;
    }

    public void setId_act(int id_act) {
        this.id_act = id_act;
    }


    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {

        return id;
    }


    public String getTitre() {
        return titre;
    }

    public int getCode_postal() {
        return code_postal;
    }

    public int getId_sec() {
        return id_sec;
    }

    public int getId_act() {
        return id_act;
    }


    public String getEcheance() {
        return echeance;
    }

    public String getQuantite() {
        return quantite;
    }

    public String getUnite() {
        return unite;
    }

    public String getContact() {
        return contact;
    }

  /*  public Offer(int id, String msg_admin, String titre, int code_postal, int id_sec, int id_act, String description, String echeance, String quantite, String unite, String contact) {

        this.id = id;
        this.titre = titre;
        this.code_postal = code_postal;
        this.id_sec = id_sec;
        this.id_act = id_act;
        this.echeance = echeance;
        this.quantite = quantite;
        this.unite = unite;
        this.contact = contact;
    }
*/
    public Offer() {

    }

}
