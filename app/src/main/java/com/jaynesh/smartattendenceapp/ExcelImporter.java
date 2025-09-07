package com.jaynesh.smartattendenceapp;

import android.content.Context;
import android.util.Log;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelImporter {

    // ✅ Import Excel from assets folder
    public static List<Student> importFromAssets(Context context, String fileName) {
        List<Student> students = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                String name = row.getCell(0).getStringCellValue();
                String enrollment = row.getCell(1).getStringCellValue();
                String phone = row.getCell(2).getStringCellValue();
                String className = row.getCell(3).getStringCellValue();

                students.add(new Student(name, enrollment, phone, className));
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            Log.e("ExcelImporter", "Error reading Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }
}
