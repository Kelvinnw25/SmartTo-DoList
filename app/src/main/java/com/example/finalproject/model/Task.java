package com.example.finalproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {

    private int id;
    private String title;
    private String description;
    private long deadlineTimestamp;
    private int importanceLevel;
    private boolean isCompleted;
    private double priorityScore;
    private String category;

    //empty constructor
    public Task() {

    }

    //Full Constructor (with ID)
    private Task(int id, String title, String description, long deadlineTimestamp, int importanceLevel, boolean isCompleted, double priorityScore, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
        this.importanceLevel = importanceLevel;
        this.isCompleted = isCompleted;
        this.priorityScore = priorityScore;
        this.category = category;
    }

    //Constructor for Insert (without ID)
    private Task(String title, String description, long deadlineTimestamp, int importanceLevel, boolean isCompleted, double priorityScore, String category) {
        //ID ignored, filled automatically by SQLite
        this.title = title;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
        this.importanceLevel = importanceLevel;
        this.isCompleted = isCompleted;
        this.priorityScore = priorityScore;
        this.category = category;
    }

    //WRITE
    public static Task createNewTask(String title, String description, long deadlineTimestamp, int importanceLevel, String category) {
        //Default isCompleted = false, priorityScore = 0.0
        return new Task(title, description, deadlineTimestamp, importanceLevel, false, 0.0, category);
    }

    //READ
    public static Task fromDatabase(int id, String title, String description, long deadlineTimestamp,
                                    int importanceLevel, boolean isCompleted, double priorityScore, String category) {
        return new Task(id, title, description, deadlineTimestamp, importanceLevel, isCompleted, priorityScore, category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDeadlineTimestamp() {
        return deadlineTimestamp;
    }

    public void setDeadlineTimestamp(long deadlineTimestamp) {
        this.deadlineTimestamp = deadlineTimestamp;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(int importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    //helper methods
    //convert timestamp to string date (ex: "12 Dec 2025")
    public String getFormattedDate() {
        if (deadlineTimestamp == 0) return "-";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(deadlineTimestamp));
    }

    //convert importance Level (int) to string label
    public String getPriorityLabel() {
        switch (importanceLevel) {
            case 3: return "High Priority";
            case 2: return "Medium Priority";
            case 1: return "Low Priority";
            default: return "Normal";
        }
    }

    //convert boolean to status text
    public String getStatusLabel() {
        return isCompleted ? "Done" : "Pending";
    }
}