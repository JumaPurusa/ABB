package com.example.abb.Model;

public class Message {

    private String id;
    private String title, message, sent_date;

    public Message(){

    }

    public Message(String id, String title, String message, String sent_date) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.sent_date = sent_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }
}
