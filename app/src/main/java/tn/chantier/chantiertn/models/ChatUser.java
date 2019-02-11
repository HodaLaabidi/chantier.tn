package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class ChatUser implements Serializable {

    private String id ;
    private String id_plateform;


    public ChatUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_plateform() {
        return id_plateform;
    }

    public void setId_plateform(String id_plateform) {
        this.id_plateform = id_plateform;
    }

    public ChatUser(String id, String role, String username, String id_plateform) {

        this.id = id;
        this.role = role;
        this.username = username;
        this.id_plateform = id_plateform;
    }

    private String role ;
    private String username ;

}
