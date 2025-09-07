package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AbsenteesActivity extends AppCompatActivity {

    private ListView listView;
    private Button sendMessagesBtn;
    private ArrayList<Student> absentees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absentees);

        listView = findViewById(R.id.absenteesListView);
        sendMessagesBtn = findViewById(R.id.sendMessagesBtn);

        // Retrieve absentees from Intent
        absentees = (ArrayList<Student>) getIntent().getSerializableExtra("absentees");

        // Display absentees in the ListView
        ArrayList<String> names = new ArrayList<>();
        if (absentees != null && !absentees.isEmpty()) {
            for (Student s : absentees) {
                names.add(s.getName() + " - " + s.getClassName());
            }
        } else {
            names.add("No absentees today!");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

        // WhatsApp button click
        sendMessagesBtn.setOnClickListener(v -> {
            if (absentees == null || absentees.isEmpty()) {
                Toast.makeText(this, "No absentees to message!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Student s : absentees) {
                String phone = s.getPhone(); // Must include country code, e.g., 919876543210
                String message = "Hi " + s.getName() + ", you were absent today.";

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://wa.me/" + phone + "?text=" + Uri.encode(message)));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "WhatsApp not installed for " + s.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}