package com.example.finalproject.model;

public class Task {

    private int id;
    private String title;
    private String description;
    private long deadlineTimestamp;
    private int importanceLevel;
    private boolean isCompleted;
    private double priorityScore; //untuk menentukan priority scorenya

    //constructor kosong
    public Task() {

    }

    //Full Constructor (with ID)
    private Task(int id, String title, String description, long deadlineTimestamp, int importanceLevel, boolean isCompleted, double priorityScore) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
        this.importanceLevel = importanceLevel;
        this.isCompleted = isCompleted;
        this.priorityScore = priorityScore;
    }

    //Constructor for Insert (without ID)
    private Task(String title, String description, long deadlineTimestamp, int importanceLevel, boolean isCompleted, double priorityScore) {
        // ID diabaikan karena akan diisi oleh SQLite
        this.title = title;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
        this.importanceLevel = importanceLevel;
        this.isCompleted = isCompleted;
        this.priorityScore = priorityScore;
    }

    //WRITE
    public static Task createNewTask(String title, String description, long deadlineTimestamp, int importanceLevel) {
        return new Task(title, description, deadlineTimestamp, importanceLevel, false, 0.0);
    }

    //READ
    public static Task fromDatabase(int id, String title, String description, long deadlineTimestamp, int importanceLevel, boolean isCompleted, double priorityScore) {
        return new Task(id, title, description, deadlineTimestamp, importanceLevel, isCompleted, priorityScore);
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

}
