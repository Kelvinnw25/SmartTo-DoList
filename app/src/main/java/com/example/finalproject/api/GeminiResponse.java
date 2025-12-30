package com.example.finalproject.api;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;

    public String getOutputText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate firstCandidate = candidates.get(0);
            if (firstCandidate.content != null &&
                    firstCandidate.content.parts != null &&
                    !firstCandidate.content.parts.isEmpty()) {
                return firstCandidate.content.parts.get(0).text;
            }
        }
        return "Tidak ada respon dari AI.";
    }

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