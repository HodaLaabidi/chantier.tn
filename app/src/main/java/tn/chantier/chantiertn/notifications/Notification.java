package tn.chantier.chantiertn.notifications;

import java.io.Serializable;

public class Notification implements Serializable {

    private String title ;
    private String content;
    private String date ;
    private String type;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Notification(String title, String body , String date, String type) {
        this.title = title;
        this.content = body;
        this.date = date;

        this.type = type;

    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return content;
    }

    public void setBody(String body) {
        this.content = body;
    }
}
