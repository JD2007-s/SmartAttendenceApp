package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentsFragment extends Fragment {

    public StudentsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        Button btnManageStudents = view.findViewById(R.id.btnOpenManageStudents);
        btnManageStudents.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ManageStudentsActivity.class)));

        return view;
    }
}
