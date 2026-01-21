package com.example.finalproject.model;

public class ChatMessage {
    public String message;
    public boolean isUser; //true = user chat (right), false = chat bot (left)

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}