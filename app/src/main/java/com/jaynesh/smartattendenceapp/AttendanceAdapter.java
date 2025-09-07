package com.jaynesh.smartattendenceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.yourpackage.StudentItem;
import java.util.ArrayList;

public class AttendanceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StudentItem> studentList;

    public AttendanceAdapter(Context context, ArrayList<StudentItem> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvStudentName);
        TextView tvEnrollment = convertView.findViewById(R.id.tvEnrollment);
        CheckBox cbPresent = convertView.findViewById(R.id.cbPresent);

        StudentItem student = studentList.get(position);

        tvName.setText(student.getName());
        tvEnrollment.setText(student.getEnrollment());
        cbPresent.setChecked(student.isPresent());

        cbPresent.setOnCheckedChangeListener((buttonView, isChecked) -> student.setPresent(isChecked));

        return convertView;
    }

    // Optional helper: get absentees
    public ArrayList<StudentItem> getSelectedStudents() {
        ArrayList<StudentItem> absentees = new ArrayList<>();
        for (StudentItem s : studentList) {
            if (!s.isPresent()) {
                absentees.add(s);
            }
        }
        return absentees;
    }
}
