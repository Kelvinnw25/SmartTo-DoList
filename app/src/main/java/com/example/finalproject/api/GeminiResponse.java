package com.example.finalproject.api;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;

    // Helper method biar kita gampang ambil teks-nya nanti
    public String getOutputText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate candidate = candidates.get(0);
            if (candidate.content != null && candidate.content.parts != null && !candidate.content.parts.isEmpty()) {
                return candidate.content.parts.get(0).text;
            }
        }
        return null;
    }

    // Inner classes struktur JSON Response
    private static class Candidate {
        private Content content;
    }
    private static class Content {
        private List<Part> parts;
    }
    private static class Part {
        private String text;
    }
}