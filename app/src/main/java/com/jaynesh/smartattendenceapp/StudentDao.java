package com.jaynesh.smartattendenceapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Query("SELECT * FROM students")
    LiveData<List<Student>> getAllStudents();   // 👈 LiveData for observe()

    @Query("DELETE FROM students")
    void clearAll();
}
