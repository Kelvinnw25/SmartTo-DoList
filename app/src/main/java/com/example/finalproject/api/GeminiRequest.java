package com.example.finalproject.api;

import java.util.Collections;
import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text) {
        // Otomatis bungkus teks jadi struktur yg diminta Gemini
        this.contents = Collections.singletonList(new Content(new Part(text)));
    }

    // Inner classes buat struktur JSON-nya
    private static class Content {
        private List<Part> parts;
        public Content(Part part) { this.parts = Collections.singletonList(part); }
    }

    private static class Part {
        private String text;
        public Part(String text) { this.text = text; }
    }
}