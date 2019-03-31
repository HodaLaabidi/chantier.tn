package tn.chantier.chantiertn.models;

import java.io.Serializable;

public class Topic implements Serializable {

    private String topic ;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Topic(String topic) {

        this.topic = topic;
    }
}
