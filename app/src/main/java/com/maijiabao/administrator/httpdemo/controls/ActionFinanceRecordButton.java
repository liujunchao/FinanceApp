package com.maijiabao.administrator.httpdemo.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.maijiabao.administrator.httpdemo.Category;
import com.maijiabao.administrator.httpdemo.R;
import com.maijiabao.administrator.httpdemo.adapters.SpinnerCategoryAdapter;

import java.util.ArrayList;


public class ActionFinanceRecordButton extends FloatingActionButton implements View.OnClickListener {
    public ActionFinanceRecordButton(Context context) {
        super(context);
        bindEvents();
    }

    public ActionFinanceRecordButton(Context context, AttributeSet attrs) {
        super(context,attrs);
        bindEvents();
    }

    public ActionFinanceRecordButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        bindEvents();
    }

   public void bindEvents(){
       this.setOnClickListener(this);
   }


    public void onClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("填写支出");//设置标题
        View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.money_item,null);//获得布局信息
        builder.setView(dlgView);//给对话框设置布局

        ArrayList<Category> list = new ArrayList<>();
        Category obj  = new Category("name1","desc1");
        list.add(obj);
        Spinner spinner = (Spinner)dlgView.findViewById(R.id.spinnerCategory);
        spinner.setAdapter(new SpinnerCategoryAdapter(list,this.getContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj  = parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object obj  = parent.getSelectedItem();
//            }
//        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );
        builder.show();
    }
}
