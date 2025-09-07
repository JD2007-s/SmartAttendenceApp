package com.jaynesh.smartattendenceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    private ListView listView;
    private AbsenteesAdapter adapter;
    private ArrayList<Student> studentList;
    private Button btnViewAbsentees;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize views
        listView = findViewById(R.id.listViewAttendance);
        btnViewAbsentees = findViewById(R.id.btnViewAbsentees);

        // Create sample student data
        studentList = new ArrayList<>();
        studentList.add(new Student("101", "Rahul Sharma", "10A", "Mr Sharma", "919876543210"));
        studentList.add(new Student("102", "Priya Mehta", "10A", "Mrs Mehta", "919812345678"));
        studentList.add(new Student("103", "Amit Patel", "10A", "Mr Patel", "919899988877"));

        // Set up the adapter
        adapter = new AbsenteesAdapter(this, studentList);
        listView.setAdapter(adapter);

        // Handle button click to view absentees
        btnViewAbsentees.setOnClickListener(v -> {
            ArrayList<Student> absentees = adapter.getSelectedStudents();
            Intent intent = new Intent(AttendanceActivity.this, AbsenteesActivity.class);
            intent.putExtra("absentees", absentees);
            startActivity(intent);
        });
    }
}