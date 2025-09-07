package com.jaynesh.smartattendenceapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

public class ReportsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reports, container, false);

        // Button to open PDF
        Button openPdfBtn = root.findViewById(R.id.btn_open_pdf);
        openPdfBtn.setOnClickListener(v -> {
            // Change path if your PDFs are in a different folder
            File pdf = new File(Environment.getExternalStorageDirectory(), "Reports/report.pdf");
            if (pdf.exists()) {
                openPdf(pdf);
            } else {
                Toast.makeText(getContext(), "PDF file not found!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    // Method to open PDF safely using FileProvider
    public void openPdf(File pdfFile) {
        try {
            Uri uri = FileProvider.getUriForFile(
                    getContext(),
                    getContext().getApplicationContext().getPackageName() + ".provider",
                    pdfFile
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to open PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
