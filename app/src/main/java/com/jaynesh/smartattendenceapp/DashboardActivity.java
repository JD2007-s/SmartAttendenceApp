package com.jaynesh.smartattendenceapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // load default fragment
        loadFragment(new ClassFragment());

        // handle bottom item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_classes) {
                selected = new ClassFragment();
            } else if (itemId == R.id.nav_students) {
                selected = new StudentsFragment();
            } else if (itemId == R.id.nav_attendance) {
                selected = new AttendanceFragment();
            } else if (itemId == R.id.nav_history) {
                selected = new HistoryFragment();
            } else if (itemId == R.id.nav_reports) {
                selected = new ReportsFragment();
            }

            if (selected != null) {
                loadFragment(selected);
            }
            return true;
        });
    }

    private void loadFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
