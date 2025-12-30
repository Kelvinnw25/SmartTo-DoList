package com.example.finalproject.api;

import java.util.ArrayList;
import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String userPrompt) {
        this.contents = new ArrayList<>();
        this.contents.add(new Content(userPrompt));
    }

    private static class Content {
        private List<Part> parts;

        public Content(String text) {
            this.parts = new ArrayList<>();
            this.parts.add(new Part(text));
        }
    }

    private static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}