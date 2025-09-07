package com.jaynesh.smartattendenceapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String enrollment;
    private String parentPhone;
    private String studentClass;
    private boolean isPresent = false; // default absent

    public Student(String name, String enrollment, String parentPhone, String studentClass) {
        this.name = name;
        this.enrollment = enrollment;
        this.parentPhone = parentPhone;
        this.studentClass = studentClass;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public String getEnrollment() { return enrollment; }
    public String getParentPhone() { return parentPhone; }
    public String getStudentClass() { return studentClass; }

    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}
