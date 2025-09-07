package com.jaynesh.smartattendenceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my); // Assuming a layout file activity_my.xml exists

        TextView myTextView = findViewById(R.id.my_text_view_in_layout); // Assuming this ID exists in activity_my.xml
        if (myTextView != null) {
            myTextView.setText("Hello from MyActivity!");
            Log.d(TAG, "TextView text updated successfully.");
        } else {
            Log.e(TAG, "TextView with ID my_text_view_in_layout not found!");
        }

        Log.i(TAG, "onCreate method finished.");
    }
}
