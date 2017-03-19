package com.maijiabao.administrator.httpdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by liujunchao on 3/18/17.
 */

public class CategoryAdapterNew extends BaseAdapter {

    ArrayList<Category> categories;
    private LayoutInflater inflater;

    public CategoryAdapterNew(ArrayList<Category> categories, Context context){
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Category category = (Category) getItem(i);
        // Check if an existing view is being reused, otherwise inflate the view

        View convertView = this.inflater.inflate(R.layout.category_layout,null);

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.categoryName);
        TextView desc = (TextView) convertView.findViewById(R.id.categoryDesc);
        // Populate the data into the template view using the data object
        name.setText(category.categoryName);
        desc.setText(category.categoryDesc);
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

}
