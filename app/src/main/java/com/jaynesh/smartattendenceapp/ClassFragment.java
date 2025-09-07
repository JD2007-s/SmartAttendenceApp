package com.jaynesh.smartattendenceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class ClassFragment extends Fragment {

    private ActivityResultLauncher<Intent> excelPickerLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    private List<Student> studentList; // Loaded from Excel

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_classes, container, false);

        // ✅ Import Excel FAB
        excelPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        String path = FileUtils.getPath(requireContext(), uri);
                        File file = new File(path);

                        studentList = ExcelImporter.importStudentsFromExcel(file);

                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase db = AppDatabase.getDatabase(requireContext().getApplicationContext());
                            StudentDao dao = db.studentDao();
                            dao.clearAll();
                            for (Student student : studentList) {
                                dao.insert(student);
                            }
                        });

                        Toast.makeText(getContext(), "Imported " + studentList.size() + " students", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        FloatingActionButton fabImport = root.findViewById(R.id.fab_import_excel);
        fabImport.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            excelPickerLauncher.launch(intent);
        });

        // ✅ OCR FAB
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        runTextRecognition(bitmap);
                    }
                }
        );

        FloatingActionButton fabOcr = root.findViewById(R.id.fab_take_photo);
        fabOcr.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        });

        return root;
    }

    // ✅ OCR and WhatsApp integration
    private void runTextRecognition(Bitmap bitmap) {
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            com.google.mlkit.vision.text.TextRecognizer recognizer =
                    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(this::processTextRecognitionResult)
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "OCR failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void processTextRecognitionResult(Text texts) {
        if (studentList == null || studentList.isEmpty()) {
            Toast.makeText(getContext(), "No students imported from Excel", Toast.LENGTH_SHORT).show();
            return;
        }

        String recognizedText = texts.getText().toLowerCase();
        Set<String> presentStudents = new HashSet<>();

        for (Student s : studentList) {
            if (recognizedText.contains(s.getName().toLowerCase())) {
                presentStudents.add(s.getName());
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
            String msg = "Absent Students: " + absentees.toString();
            sendWhatsAppMessage(msg);
        }
    }

    private void sendWhatsAppMessage(String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
