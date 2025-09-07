package com.jaynesh.smartattendenceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AbsenteesAdapter extends BaseAdapter {

    private Context context;
    private List<Student> absentees;

    public AbsenteesAdapter(Context context, List<Student> absentees) {
        this.context = context;
        this.absentees = absentees;
    }

    @Override
    public int getCount() {
        return absentees.size();
    }

    @Override
    public Object getItem(int position) {
        return absentees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_absentee, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvAbsenteeName);
        TextView tvEnrollment = convertView.findViewById(R.id.tvAbsenteeEnrollment);

        Student student = absentees.get(position);
        tvName.setText(student.getName());
        tvEnrollment.setText(student.getEnrollment());

        return convertView;
    }
}
