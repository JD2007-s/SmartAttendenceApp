package com.jaynesh.smartattendenceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "attendance_db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_CLASSES = "classes_table";
    private static final String TABLE_STUDENTS = "students_table";
    private static final String TABLE_ATTENDANCE = "attendance_table";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Classes table
        db.execSQL("CREATE TABLE " + TABLE_CLASSES + " (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_name TEXT, " +
                "subject_name TEXT)");

        // Create Students table
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "enrollment_no TEXT, " +
                "student_name TEXT, " +
                "class_id INTEGER, " +
                "FOREIGN KEY(class_id) REFERENCES " + TABLE_CLASSES + "(class_id))");

        // Create Attendance table
        db.execSQL("CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                "attendance_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_id INTEGER, " +
                "enrollment_no TEXT, " +
                "date TEXT, " +
                "status TEXT, " +
                "UNIQUE(class_id, enrollment_no, date) ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }

    // ✅ METHOD 1: Get all classes/subjects
    public ArrayList<ClassItem> getAllClasses() {
        ArrayList<ClassItem> classItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLASSES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("class_id"));
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                String subjectName = cursor.getString(cursor.getColumnIndexOrThrow("subject_name"));
                classItems.add(new ClassItem(id, className, subjectName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return classItems;
    }

    // ✅ METHOD 2: Get students for a specific class
    public ArrayList<StudentItem> getStudentsByClass(int classId) {
        ArrayList<StudentItem> studentItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENTS + " WHERE class_id = " + classId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("student_id"));
                String enrollment = cursor.getString(cursor.getColumnIndexOrThrow("enrollment_no"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("student_name"));
                studentItems.add(new StudentItem(id, enrollment, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentItems;
    }

    // ✅ Helper: Get today's date
    private String getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // ✅ METHOD 3: Check if attendance was already taken for a student today
    public boolean isPresent(int classId, String enrollmentNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE class_id = ? AND enrollment_no = ? AND date = ?";
        String todayDate = getTodayDate();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classId), enrollmentNo, todayDate});
        boolean isPresent = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isPresent;
    }

    // ✅ METHOD 4: Insert or update an attendance record
    public void insertAttendance(int classId, String enrollmentNo, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("class_id", classId);
        values.put("enrollment_no", enrollmentNo);
        values.put("date", getTodayDate());
        values.put("status", status); // "P" for Present, "A" for Absent

        db.insertWithOnConflict(TABLE_ATTENDANCE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertStudent(String enrollment, String name, String phone) {

    }
}
