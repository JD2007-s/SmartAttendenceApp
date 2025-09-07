package com.jaynesh.smartattendenceapp;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    public static ArrayList<Student> readStudents(Context context) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("students.xlsx");
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);
                String enrollment = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String studentClass = row.getCell(2).getStringCellValue();
                String parentName = row.getCell(3).getStringCellValue();
                String parentPhone = row.getCell(4).getStringCellValue();

                students.add(new Student(enrollment, name, studentClass, parentName, parentPhone));
            }
            workbook.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
