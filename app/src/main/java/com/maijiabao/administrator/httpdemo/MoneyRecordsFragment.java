package com.maijiabao.administrator.httpdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maijiabao.administrator.httpdemo.dummy.DummyContent;
import com.maijiabao.administrator.httpdemo.dummy.DummyContent.DummyItem;
import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordReceived;
import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveMoneyRecords;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.models.Category;
import com.maijiabao.administrator.httpdemo.models.MoneyRecord;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MoneyRecordsFragment extends Fragment implements IOnMoneyRecordReceived,IOnSaveMoneyRecords {

    // TODO: Customize parameter argument names
    private static final String DATE_KEY = "dateKey";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mDate;
    private ItemFragment.OnListFragmentInteractionListener mListener;

    private Handler mHandler = new Handler(){ };

    public MoneyRecordsFragment() {
    }


    public static MoneyRecordsFragment newInstance(String date) {
        MoneyRecordsFragment fragment = new MoneyRecordsFragment();
        Bundle args = new Bundle();
        args.putString(DATE_KEY, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDate = getArguments().getString(DATE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            MoneyRecordsOperations.getRecords(MoneyRecordsFragment.this,mDate);
        }

        return view;
    }

    @Override
    public void OnRecordsReceived(JSONArray array) {
      final ArrayList<MoneyRecord> list =  MoneyRecord.convert(array);
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView view =  (RecyclerView)getView();
                view.setAdapter(new MoneyRecordsRecyclerViewAdapter(list, mListener));
            }
        });
    }



    @Override
    public void OnSaveMoneyRecords(Result rlt) {
        MoneyRecordsOperations.getRecords(MoneyRecordsFragment.this,mDate);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
