package com.maijiabao.administrator.httpdemo.charts;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.R;
import com.maijiabao.administrator.httpdemo.interfaces.IRecordsByRangeReceived;
import com.maijiabao.administrator.httpdemo.models.CategorySummary;
import com.maijiabao.administrator.httpdemo.util.DateUtil;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PieChartActivity extends AppCompatActivity implements View.OnClickListener ,IRecordsByRangeReceived {

    private TextView txtStart,txtEnd;
    private Button btnSearch;
    private PieChartFragment fragment;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        if (savedInstanceState == null) {
            fragment =  new PieChartFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtStart = (TextView) findViewById(R.id.txtStartDate);
        txtEnd = (TextView) findViewById(R.id.txtEndDate);

        String currentDay  = DateUtil.getCurrentSimpleDate();
        String startDate  = DateUtil.getCurrentMonthFirstDayByDate(currentDay);
        txtStart.setText("开始时间:"+startDate);
        txtEnd.setText("结束时间:"+currentDay);
        txtStart.setOnClickListener(this);
        txtEnd.setOnClickListener(this);
        txtStart.setTag(startDate);
        txtEnd.setTag(currentDay);
        btnSearch.setOnClickListener(this);
        fetchCategoriesData();
        //DateUtil.getCurDateStr()
    }

    public void fetchCategoriesData(){
        String start = txtStart.getTag().toString();
        String end = txtEnd.getTag().toString();
        MoneyRecordsOperations.getCategoryGroupDataByRange(PieChartActivity.this,start,end);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.txtStartDate || id == R.id.txtEndDate) {
            final TextView txt = (TextView) v;
            DatePickerDialog dlg = new DatePickerDialog(PieChartActivity.this);
            dlg.show();
            dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month+1;
                    String date = year + "" +  (month>=10?month:"0"+month) + "" + (dayOfMonth>=10?dayOfMonth:"0"+dayOfMonth);
                    Toast.makeText(PieChartActivity.this, date, Toast.LENGTH_SHORT).show();
                    String title = id == R.id.txtStartDate?"开始时间:":"结束时间:";
                    txt.setText(title+date);
                    txt.setTag(date);
                }
            });
        }
        if(id == R.id.btnSearch){
            fetchCategoriesData();
        }


    }
    public void onRecordsByRangeReceived(JSONArray array){
        final ArrayList<CategorySummary> list = CategorySummary.convert(array);
        handler.post(new Runnable() {
            @Override
            public void run() {
                PieChartActivity.this.fragment.setCategoryData(list);
            }
        });
    }

}
