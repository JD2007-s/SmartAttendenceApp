package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // For logging

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<com.yourpackage.StudentItem> studentListFromAdapter; // To hold the list managed by the adapter
    private DBHelper dbHelper;
    private Button btnSendWhatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Activity starting.");

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        btnSendWhatsApp = findViewById(R.id.btnSendWhatsApp);

        // 1️⃣ Initialize an empty list for the adapter first
        // The actual data might be loaded from DB or be sample data
        studentListFromAdapter = new ArrayList<>();

        // 2️⃣ Setup RecyclerView with an empty adapter initially (or load from DB)
        // It's often better to load from DB first, then populate adapter.
        // For this example, we'll stick to your sample data flow.
        adapter = new StudentAdapter(studentListFromAdapter); // Pass the list to the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 3️⃣ Load sample students and update the adapter
        // In a real app, you might fetch this from the DBHelper.
        loadAndDisplaySampleStudents();

        // 4️⃣ Send WhatsApp messages for absentees
        btnSendWhatsApp.setOnClickListener(v -> {
            Log.d(TAG, "Send WhatsApp button clicked.");
            sendWhatsAppMessagesToAbsentees();
        });

        Log.d(TAG, "onCreate: Activity setup complete.");
    }

    /**
     * Loads sample students, adds them to the database if not present,
     * and updates the adapter to display them.
     */
    private void loadAndDisplaySampleStudents() {
        Log.d(TAG, "loadAndDisplaySampleStudents: Loading sample data.");
        ArrayList<com.yourpackage.StudentItem> sampleStudents = new ArrayList<>();
        // Assuming StudentItem constructor is (id, enrollment, name, parentPhone, className)
        // And it internally has a way to track presence (e.g., isPresent field defaulting to true)
        sampleStudents.add(new com.yourpackage.StudentItem(0, "236400000000", "Jaynesh Solanki", "9875900000", "CO-53"));
        sampleStudents.add(new com.yourpackage.StudentItem(0, "236400000001", "Yash Shah", "9875900001", "CO-54"));
        sampleStudents.add(new com.yourpackage.StudentItem(0, "236400000002", "Swapnil Patel", "9875900002", "CO-55"));
        sampleStudents.add(new com.yourpackage.StudentItem(0, "236400000003", "Nishi Shah", "9875900003", "CO-56"));
        sampleStudents.add(new com.yourpackage.StudentItem(0, "236400000004", "Astha Shah", "9875900004", "CO-57"));

        // Insert into DB if not already present
        // Note: For a real app, consider doing DB operations on a background thread.
        // Your DBHelper might already handle this, or it might block the UI.
        // For simplicity with your current structure, we'll keep it as is.
        for (com.yourpackage.StudentItem s : sampleStudents) {
            // A more robust check might involve querying by a unique ID if 'id' is from DB
            if (!isStudentInDb(s.getEnrollment())) { // You might need a more specific method in DBHelper
                Log.d(TAG, "Inserting new student to DB: " + s.getName());
                dbHelper.insertStudent(
                        s.getEnrollment(),
                        s.getName(),
                        s.getParentPhone(),
                        1 // Assuming classId = 1 is a valid placeholder
                );
            }
        }

        // Update the adapter's list with these students
        // The adapter should now manage this list.
        // If your adapter modifies the isPresent status, those changes are on its internal list.
        studentListFromAdapter.clear();
        studentListFromAdapter.addAll(sampleStudents);
        adapter.notifyDataSetChanged(); // Tell the adapter its data has changed
        Log.d(TAG, "loadAndDisplaySampleStudents: Adapter updated with " + studentListFromAdapter.size() + " students.");

        // Recommendation: In a more complex app, you would typically load data from
        // dbHelper.getAllStudents() and then populate the adapter.
        // The sample data could be an initial seed if the DB is empty.
    }

    /**
     * Helper method to check if a student (by enrollment) is already in the database.
     * This is a placeholder; your DBHelper should ideally provide such a method.
     * @param enrollment The enrollment number to check.
     * @return true if the student exists, false otherwise.
     */
    private boolean isStudentInDb(String enrollment) {
        // This is a simplified check. Your DBHelper.getAllEnrollments() might be inefficient
        // if the list of all enrollments is very large.
        // A direct query like dbHelper.doesEnrollmentExist(enrollment) would be better.
        ArrayList<String> allEnrollmentsInDb = dbHelper.getAllEnrollments();
        if (allEnrollmentsInDb != null) {
            return allEnrollmentsInDb.contains(enrollment);
        }
        return false;
    }

    /**
     * Gets the list of absent students from the adapter and sends WhatsApp messages.
     */
    private void sendWhatsAppMessagesToAbsentees() {
        if (adapter == null) {
            Log.e(TAG, "sendWhatsAppMessagesToAbsentees: Adapter is null!");
            Toast.makeText(this, "Error: Student adapter not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        // This is the CRITICAL line that relies on your StudentAdapter's implementation
        ArrayList<com.yourpackage.StudentItem> absentees = adapter.getAbsentStudents();
        Log.d(TAG, "sendWhatsAppMessagesToAbsentees: Found " + absentees.size() + " absent students.");

        if (absentees.isEmpty()) {
            Toast.makeText(this, "No absentees to notify today! 🎉", Toast.LENGTH_LONG).show();
            return;
        }

        int messagesSentCount = 0;
        for (com.yourpackage.StudentItem student : absentees) {
            if (student.getParentPhone() != null && !student.getParentPhone().trim().isEmpty()) {
                String message = "Dear Parent, your child " + student.getName() +
                        " (Enrollment: " + student.getEnrollment() + ") was marked absent today.";
                String phoneWithCountryCode = "+91" + student.getParentPhone().replaceAll("[^0-9]", ""); // Sanitize phone

                Log.i(TAG, "Attempting to send WhatsApp to " + phoneWithCountryCode + " for " + student.getName());
                sendWhatsAppIntent(phoneWithCountryCode, message);
                messagesSentCount++;
            } else {
                Log.w(TAG, "Skipping WhatsApp for " + student.getName() + " due to missing or empty parent phone.");
                Toast.makeText(this, "Missing phone for " + student.getName(), Toast.LENGTH_SHORT).show();
            }
        }

        if (messagesSentCount > 0) {
            Toast.makeText(this, messagesSentCount + " WhatsApp message(s) prepared for absentees!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No valid parent phone numbers for absent students.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Creates and starts an Intent to open WhatsApp with a pre-filled message.
     * @param phoneNumber The recipient's phone number (including country code).
     * @param message The message to send.
     */
    private void sendWhatsAppIntent(String phoneNumber, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Ensure the message is properly URL encoded
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
            intent.setData(Uri.parse(url));
            // Check if WhatsApp is installed (optional but good practice)
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.e(TAG, "WhatsApp not installed on this device.");
                Toast.makeText(this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending WhatsApp message via Intent", e);
            Toast.makeText(this, "Could not open WhatsApp.", Toast.LENGTH_SHORT).show();
        }
    }
}
