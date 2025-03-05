package com.sbjs.truek;

public class Message {
    private String text;
    private String sender;

    // Constructor vacío necesario para Firebase
    public Message() {}

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    // Getters y setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
