package com.jaynesh.smartattendenceapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class ClassFragment extends Fragment {

    private List<Student> studentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_classes, container, false);

        FloatingActionButton fabImport = root.findViewById(R.id.fab_import_excel);
        fabImport.setOnClickListener(v -> importStudentsFromAssets());

        FloatingActionButton fabOcr = root.findViewById(R.id.fab_take_photo);
        fabOcr.setOnClickListener(v -> Toast.makeText(getContext(), "OCR feature can be added here", Toast.LENGTH_SHORT).show());

        return root;
    }

    // ----------------- Import students.xlsx from assets -----------------
    private void importStudentsFromAssets() {
        try {
            // ✅ Read students.xlsx from assets folder
            studentList = new ArrayList<>(ExcelImporter.importFromAssets(getContext(), "students.xlsx"));

            if (studentList.isEmpty()) {
                Toast.makeText(getContext(), "No students found in Excel", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Save to DB
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(requireContext().getApplicationContext());
                db.studentDao().clearAll();
                for (Student s : studentList) {
                    db.studentDao().insert(s);
                }
            });

            Toast.makeText(getContext(), "Imported " + studentList.size() + " students", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Excel import failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // ----------------- OCR Recognition (optional) -----------------
    private void runTextRecognition(Bitmap bitmap) {
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(this::processTextRecognitionResult)
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "OCR failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void processTextRecognitionResult(Text texts) {
        if (studentList.isEmpty()) {
            Toast.makeText(getContext(), "No students imported", Toast.LENGTH_SHORT).show();
            return;
        }

        String recognizedText = texts.getText().toLowerCase();
        Set<String> presentStudents = new HashSet<>();

        for (Student s : studentList) {
            if (recognizedText.contains(s.getName().toLowerCase())) {
                s.setPresent(true);
                presentStudents.add(s.getName());
            } else {
                s.setPresent(false);
            }
        }

        StringBuilder absentees = new StringBuilder();
        for (Student s : studentList) {
            if (!presentStudents.contains(s.getName())) {
                absentees.append(s.getName()).append(", ");
            }
        }

        if (absentees.length() == 0) {
            Toast.makeText(getContext(), "All students are present!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Absent Students: " + absentees.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
