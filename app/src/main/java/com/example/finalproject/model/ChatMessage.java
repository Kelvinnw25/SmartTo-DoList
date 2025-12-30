package com.example.finalproject.model;

public class ChatMessage {
    public String message;
    public boolean isUser; // true = chat user (kanan), false = chat bot (kiri)

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }
}