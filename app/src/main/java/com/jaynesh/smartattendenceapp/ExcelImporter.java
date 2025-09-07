package com.jaynesh.smartattendenceapp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelImporter {

    public static List<Student> importStudentsFromExcel(File file) {
        List<Student> students = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String name = row.getCell(0).getStringCellValue();
                String rollNo = row.getCell(1).getStringCellValue();

                Student student = new Student();
                student.setName(name);
                student.setRollNo(rollNo);

                students.add(student);
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
