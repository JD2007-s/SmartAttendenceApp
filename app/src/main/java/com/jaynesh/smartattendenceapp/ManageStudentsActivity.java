package com.jaynesh.smartattendenceapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageStudentsActivity extends AppCompatActivity {

    private static final String TAG = "ManageStudentsActivity";
    private DBHelper dbHelper;
    private Button btnImportExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        dbHelper = new DBHelper(this);
        btnImportExcel = findViewById(R.id.btnImportExcel);

        btnImportExcel.setOnClickListener(v -> {
            // Run the import process on a background thread to avoid freezing the UI
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(this::importStudentsFromExcel);
        });
    }

    private void importStudentsFromExcel() {
        try {
            // Open the Excel file from the assets folder
            InputStream is = getAssets().open("students.xlsx");
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            // DataFormatter helps in fetching cell values as strings irrespective of their type
            DataFormatter dataFormatter = new DataFormatter();
            int importedCount = 0;

            // Start from row 1 to skip the header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue; // Skip empty rows
                }

                // Use DataFormatter to safely get cell content as a String
                // It handles null cells and different data types (e.g., numeric, string) correctly
                String enrollment = dataFormatter.formatCellValue(row.getCell(0)).trim();
                String name = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String phone = dataFormatter.formatCellValue(row.getCell(2)).trim();

                // Basic validation: Do not insert if essential fields like enrollment or name are empty
                if (enrollment.isEmpty() || name.isEmpty()) {
                    continue;
                }

                dbHelper.insertStudent(enrollment, name, phone,1);
                importedCount++;
            }

            workbook.close();
            is.close();

            // Display success message on the UI thread
            int finalImportedCount = importedCount;
            runOnUiThread(() -> Toast.makeText(this, finalImportedCount + " Students Imported Successfully!", Toast.LENGTH_SHORT).show());

        } catch (Exception e) {
            Log.e(TAG, "Error importing from Excel", e);
            // Display error message on the UI thread
            runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}