package com.jaynesh.smartattendenceapp;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name;
    private String className;
    private String teacherName;
    private String phone;

    public Student(String id, String name, String className, String teacherName, String phone) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.teacherName = teacherName;
        this.phone = phone;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getClassName() { return className; }
    public String getTeacherName() { return teacherName; }
    public String getPhone() { return phone; }
}