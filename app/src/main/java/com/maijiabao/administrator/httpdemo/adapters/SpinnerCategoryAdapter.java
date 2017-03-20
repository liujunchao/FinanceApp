package com.maijiabao.administrator.httpdemo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maijiabao.administrator.httpdemo.Category;
import com.maijiabao.administrator.httpdemo.R;
import java.util.ArrayList;

public class SpinnerCategoryAdapter extends BaseCategoryAdapter {
    public SpinnerCategoryAdapter(ArrayList<Category> categories, Context context){
        super(categories,context);
    }
    @Override
    public View returnView(int i, View view, ViewGroup viewGroup) {
        Category category = (Category) getItem(i);
        // Check if an existing view is being reused, otherwise inflate the view

        View convertView = this.inflater.inflate(R.layout.category_spinner_layout,null);

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.categoryName);
        // Populate the data into the template view using the data object
        name.setText(category.categoryName);
        // Return the completed view to render on screen
        return convertView;
    }
}