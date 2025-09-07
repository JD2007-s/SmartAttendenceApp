package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yourpackage.StudentItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<StudentItem> studentList;
    private Button btnViewAbsentees;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAttendance);
        btnViewAbsentees = view.findViewById(R.id.btnViewAbsentees);

        // Sample student data
        studentList = new ArrayList<>();
        studentList.add(new StudentItem(0, "236400000000", "Jaynesh Solanki", "918759000000", "CO-53"));
        studentList.add(new StudentItem(0, "236400000001", "Yash Shah", "918759000001", "CO-54"));
        studentList.add(new StudentItem(0, "236400000002", "Swapnil Patel", "918759000002", "CO-55"));
        studentList.add(new StudentItem(0, "236400000003", "Nishi Shah", "918759000003", "CO-56"));
        studentList.add(new StudentItem(0, "236400000004", "Astha Shah", "918759000004", "CO-57"));

        // Setup RecyclerView
        adapter = new StudentAdapter(studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Handle button click
        btnViewAbsentees.setOnClickListener(v -> {
            ArrayList<StudentItem> absentees = new ArrayList<>(adapter.getSelectedStudents());
            Intent intent = new Intent(getActivity(), AbsenteesActivity.class);
            intent.putExtra("absentees", absentees);
            startActivity(intent);
        });

        return view;
    }
}
