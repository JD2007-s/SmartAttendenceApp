package com.yourpackage; // Replace with your actual package name

public class StudentItem {
    // ... other fields like id, enrollment, name, parentPhone, className ...
    private int id;
    private String enrollment;
    private String name;
    private String parentPhone;
    private String className;
    private boolean isPresent; // <-- Key field for tracking attendance

    // Constructor
    public StudentItem(int id, String enrollment, String name, String parentPhone, String className) {
        this.id = id;
        this.enrollment = enrollment;
        this.name = name;
        this.parentPhone = parentPhone;
        this.className = className;
        this.isPresent = true; // Default to present, or false if you prefer
    }

    // Getters
    public int getId() { return id; }
    public String getEnrollment() { return enrollment; }
    public String getName() { return name; }
    public String getParentPhone() { return parentPhone; }
    public String getClassName() { return className; }
    public boolean isPresent() { return isPresent; }

    // Setter for presence (to be called by your adapter when a checkbox changes, for example)
    public void setPresent(boolean present) {
        isPresent = present;
    }

    // ... other setters if needed ...
}