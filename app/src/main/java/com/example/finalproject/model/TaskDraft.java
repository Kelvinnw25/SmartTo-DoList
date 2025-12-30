package com.example.finalproject.model;

public class TaskDraft {
    // Nama variabel ini HARUS SAMA PERSIS kayak yang kita minta di Prompt nanti
    public String title;
    public String description;
    public String deadline; // Format string: "yyyy-MM-dd HH:mm"
    public int importance;  // 1-5
    public String category;
}