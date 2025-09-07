package com.jaynesh.smartattendenceapp;

public class StudentItem {
    private int studentId;
    private String enrollment;
    private String name;

    public StudentItem(int studentId, String enrollment, String name) {
        this.studentId = studentId;
        this.enrollment = enrollment;
        this.name = name;
    }

    // Getters for all fields
    public int getStudentId() { return studentId; }
    public String getEnrollment() { return enrollment; }
    public String getName() { return name; }
}