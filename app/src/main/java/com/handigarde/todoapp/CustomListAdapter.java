package com.handigarde.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> listData;

    public CustomListAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvTask = (TextView) convertView.findViewById(R.id.tvTask);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvTask.setText(task.getTask());
        tvPriority.setText(task.getPriority());
        // Return the completed view to render on screen
        return convertView;
    }
}
