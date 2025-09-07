package com.jaynesh.smartattendenceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class AbsenteesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Student> studentList;
    private ArrayList<Student> selectedStudents;

    public AbsenteesAdapter(Context context, ArrayList<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.selectedStudents = new ArrayList<>();
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

    static class ViewHolder {
        TextView tvName, tvClass;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_absentee, parent, false);
            holder.tvName = convertView.findViewById(R.id.tvStudentName);
            holder.tvClass = convertView.findViewById(R.id.tvStudentClass);
            holder.checkBox = convertView.findViewById(R.id.checkboxAbsent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvClass.setText(student.getClassName());

        // Prevent checkbox recycling issues
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedStudents.contains(student));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                if(!selectedStudents.contains(student)){
                    selectedStudents.add(student);
                }
            } else {
                selectedStudents.remove(student);
            }
        });

        return convertView;
    }

    // Return the list of selected (absent) students
    public ArrayList<Student> getSelectedStudents() {
        return selectedStudents;
    }
}