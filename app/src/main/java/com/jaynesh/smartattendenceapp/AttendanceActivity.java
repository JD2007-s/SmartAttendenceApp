package com.jaynesh.smartattendenceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yourpackage.StudentItem;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<StudentItem> studentList;
    private Button btnViewAbsentees;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewAttendance);
        btnViewAbsentees = findViewById(R.id.btnViewAbsentees);

        // Create sample student data
        studentList = new ArrayList<>();
        studentList.add(new StudentItem(0, "236400000000", "Jaynesh Solanki", "918759000000", "CO-53"));
        studentList.add(new StudentItem(0, "236400000001", "Yash Shah", "918759000001", "CO-54"));
        studentList.add(new StudentItem(0, "236400000002", "Swapnil Patel", "918759000002", "CO-55"));
        studentList.add(new StudentItem(0, "236400000003", "Nishi Shah", "918759000003", "CO-56"));
        studentList.add(new StudentItem(0, "236400000004", "Astha Shah", "918759000004", "CO-57"));

        // Set up RecyclerView with StudentAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        // Handle button click to view absentees
        btnViewAbsentees.setOnClickListener(v -> {
            ArrayList<StudentItem> absentees = adapter.getSelectedStudents();

            // Pass absentees list to next activity
            Intent intent = new Intent(AttendanceActivity.this, AbsenteesActivity.class);
            intent.putExtra("absentees", absentees);
            startActivity(intent);
        });
    }
}
