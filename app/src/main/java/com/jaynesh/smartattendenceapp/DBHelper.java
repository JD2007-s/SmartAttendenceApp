package com.jaynesh.smartattendenceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STUDENTS = "students";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_ENROLLMENT = "enrollment";
    private static final String COL_PARENT_PHONE = "parentPhone";
    private static final String COL_CLASS = "studentClass";
    private static final String COL_PRESENT = "isPresent";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_ENROLLMENT + " TEXT,"
                + COL_PARENT_PHONE + " TEXT,"
                + COL_CLASS + " TEXT,"
                + COL_PRESENT + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // ✅ Add Student
    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, student.getName());
        values.put(COL_ENROLLMENT, student.getEnrollment());
        values.put(COL_PARENT_PHONE, student.getParentPhone());
        values.put(COL_CLASS, student.getStudentClass());
        values.put(COL_PRESENT, student.isPresent() ? 1 : 0);
        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }

    // ✅ Get all students
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);
        if (cursor.moveToFirst()) {
            do {
                Student s = new Student(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ENROLLMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PARENT_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CLASS))
                );
                s.setPresent(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRESENT)) == 1);
                students.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return students;
    }

    // ✅ Update attendance
    public void updateAttendance(String enrollment, boolean isPresent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRESENT, isPresent ? 1 : 0);
        db.update(TABLE_STUDENTS, values, COL_ENROLLMENT + "=?", new String[]{enrollment});
        db.close();
    }

    // ✅ Clear all students
    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, null, null);
        db.close();
    }
    // Add this inside DBHelper class
    public ArrayList<String> getAllEnrollments() {
        ArrayList<String> enrollments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT enrollment_no FROM students_table", null);

        if (cursor.moveToFirst()) {
            do {
                enrollments.add(cursor.getString(cursor.getColumnIndexOrThrow("enrollment_no")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return enrollments;
    }
    // Replace your existing insertStudent method with this
    public void insertStudent(String enrollment, String name, String phone, int classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("enrollment_no", enrollment); // enrollment number
        values.put("student_name", name);        // student name
        values.put("class_id", classId);         // class ID (integer)

        // Insert into students_table, ignore if already exists
        db.insertWithOnConflict("students_table", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

}
