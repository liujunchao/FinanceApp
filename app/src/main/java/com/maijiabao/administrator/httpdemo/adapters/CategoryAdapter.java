package com.maijiabao.administrator.httpdemo.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maijiabao.administrator.httpdemo.Category;
import com.maijiabao.administrator.httpdemo.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, ArrayList<Category> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Category category = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_layout, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.categoryName);
        TextView desc = (TextView) convertView.findViewById(R.id.categoryDesc);
        // Populate the data into the template view using the data object
        name.setText(category.categoryName);
        desc.setText(category.categoryDesc);
        // Return the completed view to render on screen
        return convertView;
    }
}