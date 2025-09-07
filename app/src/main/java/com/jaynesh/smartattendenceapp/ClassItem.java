package com.jaynesh.smartattendenceapp;

public class ClassItem {
    private int classId;
    private String className;
    private String subjectName;

    public ClassItem(int classId, String className, String subjectName) {
        this.classId = classId;
        this.className = className;
        this.subjectName = subjectName;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getSubjectName() {
        return subjectName;
    }
}