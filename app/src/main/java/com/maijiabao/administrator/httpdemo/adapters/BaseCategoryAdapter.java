package com.maijiabao.administrator.httpdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.maijiabao.administrator.httpdemo.models.Category;

import java.util.ArrayList;

/**
 * Created by liujunchao on 3/18/17.
 */

public abstract class BaseCategoryAdapter extends BaseAdapter {

    protected ArrayList<Category> categories;
    protected LayoutInflater inflater;

    public BaseCategoryAdapter(ArrayList<Category> categories, Context context){
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return returnView(i,view,viewGroup);
    }

    public abstract View returnView(int i, View view, ViewGroup viewGroup)  ;

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


//public class CategoryAdapterNew extends BaseCategoryAdapter{
//
//}