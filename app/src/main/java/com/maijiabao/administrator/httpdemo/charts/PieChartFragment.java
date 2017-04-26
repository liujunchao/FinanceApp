package com.maijiabao.administrator.httpdemo.charts;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle ;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.MoneyRecordsFormFragment;
import com.maijiabao.administrator.httpdemo.R;
import com.maijiabao.administrator.httpdemo.controls.ListFragment;
import com.maijiabao.administrator.httpdemo.interfaces.IRecordsByRangeReceived;
import com.maijiabao.administrator.httpdemo.models.CategorySummary;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;


public class PieChartFragment extends Fragment implements IRecordsByRangeReceived {

    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    private String startDate;
    private String endDate;

    private ArrayList<CategorySummary> summaries;
    private ArrayList<SliceValue> values;
    private Handler handler = new Handler();

    public PieChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        chart = (PieChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        //generateData();
        toggleLabelsOutside();
        return rootView;
    }

    // MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pie_chart, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            reset();
            generateData();
            return true;
        }
        if (id == R.id.action_explode) {
            explodeChart();
            return true;
        }
        if (id == R.id.action_center_circle) {
            hasCenterCircle = !hasCenterCircle;
            if (!hasCenterCircle) {
                hasCenterText1 = false;
                hasCenterText2 = false;
            }

            generateData();
            return true;
        }
        if (id == R.id.action_center_text1) {
            hasCenterText1 = !hasCenterText1;

            if (hasCenterText1) {
                hasCenterCircle = true;
            }

            hasCenterText2 = false;

            generateData();
            return true;
        }
        if (id == R.id.action_center_text2) {
            hasCenterText2 = !hasCenterText2;

            if (hasCenterText2) {
                hasCenterText1 = true;// text 2 need text 1 to by also drawn.
                hasCenterCircle = true;
            }

            generateData();
            return true;
        }
        if (id == R.id.action_toggle_labels) {
            toggleLabels();
            return true;
        }
        if (id == R.id.action_toggle_labels_outside) {
            toggleLabelsOutside();
            return true;
        }
        if (id == R.id.action_animate) {
            prepareDataAnimation();
            chart.startDataAnimation();
            return true;
        }
        if (id == R.id.action_toggle_selection_mode) {
            toggleLabelForSelected();
            Toast.makeText(getActivity(),
                    "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        chart.setCircleFillRatio(1.0f);
        hasLabels = false;
        hasLabelsOutside = true;
        hasCenterCircle = false;
        hasCenterText1 = false;
        hasCenterText2 = false;
        isExploded = false;
        hasLabelForSelected = false;
    }

    public void setCategoryData(ArrayList<CategorySummary> summaries,String startDate,String endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.summaries = summaries;
        values = new ArrayList<SliceValue>();
        for (int i = 0; i < summaries.size(); ++i) {
            CategorySummary itm  = summaries.get(i);
            SliceValue sliceValue = new SliceValue(new Float(itm.amount), ChartUtils.pickColor());

            sliceValue.setLabel(itm.categoryName+":"+itm.amount+"元");

            values.add(sliceValue);
        }
        generateData();
    }

    private void generateData() {


        data = new PieChartData(values);

        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        chart.setPieChartData(data);
    }

    private void explodeChart() {
        isExploded = !isExploded;
        generateData();

    }

    private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        if (hasLabelsOutside) {
            chart.setCircleFillRatio(0.7f);
        } else {
            chart.setCircleFillRatio(1.0f);
        }

        generateData();

    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData();
    }



    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    public void onRecordsByRangeReceived(JSONArray array){
        if(array.length() == 0) return ;
       final   ArrayList<String> list = new ArrayList<>();
        try{
            for(int i =0,len = array.length();i<len;i++){
                JSONObject json  = array.getJSONObject(i);
                StringBuilder builder = new StringBuilder();
                builder.append( json.get("creator").toString());
                builder.append( "于"+json.get("belongDate").toString()+"这天在");
                builder.append( "<b>"+json.get("categoryName").toString()+"</b>" );
                String desc = json.get("desc").toString();
                if(!desc.equals("")){
                    builder.append("<em>("+desc+")</em>");
                }
                builder.append("上花了");
                builder.append( "<font color='red'>"+json.get("amount").toString()+"</font>元" );
                list.add(builder.toString());
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    ListFragment fragment  =  ListFragment.newInstance(list);
                    fragment.show( getActivity().getFragmentManager(),"ListFragment");
                }
            });
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
          //  Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();

          //  MoneyRecordsOperations.getDetailsByRange();
           String label =  String.valueOf(value.getLabelAsChars());
            String categoryName = label.substring(0,label.indexOf(":"));
            String categoryId = "";
            for (int i = 0; i < summaries.size(); ++i) {
                CategorySummary itm  = summaries.get(i);
               if(itm.categoryName.equals(categoryName)){
                   categoryId = itm.categoryId;
                   break;
               }
            }
            if(categoryId!=""){
                MoneyRecordsOperations.getDetailsByRange(PieChartFragment.this,PieChartFragment.this.startDate,PieChartFragment.this.endDate,categoryId);
            }
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
