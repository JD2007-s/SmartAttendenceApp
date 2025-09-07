package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import com.yourpackage.StudentItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AbsenteesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<com.yourpackage.StudentItem> absentees;
    private Button btnNotifyParents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absentees);

        recyclerView = findViewById(R.id.recyclerViewAbsentees);
        btnNotifyParents = findViewById(R.id.btnNotifyParents);

        // Get absentees from intent
        absentees = (ArrayList<com.yourpackage.StudentItem>) getIntent().getSerializableExtra("absentees");
        if (absentees == null) absentees = new ArrayList<>();

        // Show absentees in list
        adapter = new StudentAdapter(absentees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Notify parents on button click
        btnNotifyParents.setOnClickListener(v -> {
            for (com.yourpackage.StudentItem student : absentees) {
                sendWhatsAppMessage(student);
            }
        });
    }

    private void sendWhatsAppMessage(com.yourpackage.StudentItem student) {
        try {
            String phoneNumber = student.getParentPhone(); // must include country code
            String message = "Dear Parent,\n\nYour child " + student.getName() +
                    " (" + student.getEnrollment() + ") was absent today.\n\nRegards,\nCollege";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message)));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
