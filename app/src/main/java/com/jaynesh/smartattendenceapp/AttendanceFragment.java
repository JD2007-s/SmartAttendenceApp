package com.jaynesh.smartattendenceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;

public class AttendanceFragment extends Fragment {

    private Button btnTakePhoto;
    private DBHelper dbHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        dbHelper = new DBHelper(getContext());

        btnTakePhoto.setOnClickListener(v -> openCamera());

        return view;
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                runTextRecognition(imageBitmap);
            }
        }
    }

    private void runTextRecognition(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        com.google.mlkit.vision.text.TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    StringBuilder scannedText = new StringBuilder();
                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        scannedText.append(block.getText()).append("\n");
                    }
                    checkAttendance(scannedText.toString());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Failed to read text: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void checkAttendance(String scannedText) {
        List<String> allStudents = dbHelper.getAllEnrollments();  // from DB
        List<String> presentStudents = new ArrayList<>();

        for (String line : scannedText.split("\n")) {
            line = line.trim();
            if (allStudents.contains(line)) {
                presentStudents.add(line);
            }
        }

        // Absent = all - present
        List<String> absentStudents = new ArrayList<>(allStudents);
        absentStudents.removeAll(presentStudents);

        if (absentStudents.isEmpty()) {
            Toast.makeText(getContext(), "All students are present!", Toast.LENGTH_SHORT).show();
        } else {
            sendAbsenteeWhatsApp(absentStudents);
        }
    }

    private void sendAbsenteeWhatsApp(List<String> absentList) {
        for (String enrollment : absentList) {
            String phone = dbHelper.getPhoneByEnrollment(enrollment); // must include country code
            String msg = "Dear Parent, your child with enrollment " + enrollment + " was absent today.";

            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + Uri.encode(msg);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
