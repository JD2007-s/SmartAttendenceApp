package com.jaynesh.smartattendenceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yourpackage.StudentItem;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<StudentItem> studentList;

    // Constructor
    public StudentAdapter(ArrayList<StudentItem> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentItem currentStudent = studentList.get(position);
        holder.textViewStudentName.setText(currentStudent.getName());

        // Prevent firing listener during recycling
        holder.checkBoxPresent.setOnCheckedChangeListener(null);
        holder.checkBoxPresent.setChecked(currentStudent.isPresent());

        // Update StudentItem when checkbox changes
        holder.checkBoxPresent.setOnCheckedChangeListener((buttonView, isChecked) ->
                currentStudent.setPresent(isChecked)
        );
    }

    @Override
    public int getItemCount() {
        return studentList == null ? 0 : studentList.size();
    }

    public void setStudents(List<StudentItem> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }

    public ArrayList<StudentItem> getAbsentStudents() {
        ArrayList<StudentItem> absentStudents = new ArrayList<>();
        if (studentList != null) {
            for (StudentItem student : studentList) {
                if (!student.isPresent()) {
                    absentStudents.add(student);
                }
            }
        }
        return absentStudents;
    }

    public ArrayList<StudentItem> getSelectedStudents() {
        ArrayList<StudentItem> selectedStudents = new ArrayList<>();
        if (studentList != null) {
            for (StudentItem student : studentList) {
                if (student.isPresent()) {
                    selectedStudents.add(student);
                }
            }
        }
        return selectedStudents;
    }

    // ViewHolder Class
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;
        CheckBox checkBoxPresent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            checkBoxPresent = itemView.findViewById(R.id.checkBoxPresent);
        }
    }
}
