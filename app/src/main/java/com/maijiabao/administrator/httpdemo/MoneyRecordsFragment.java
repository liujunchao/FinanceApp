package com.maijiabao.administrator.httpdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.dummy.DummyContent;
import com.maijiabao.administrator.httpdemo.dummy.DummyContent.DummyItem;
import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordDeleted;
import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordReceived;
import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveMoneyRecords;
import com.maijiabao.administrator.httpdemo.interfaces.MoneyRecordListener;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.models.Category;
import com.maijiabao.administrator.httpdemo.models.MoneyRecord;
import com.maijiabao.administrator.httpdemo.util.LoadingUtil;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MoneyRecordsFragment extends Fragment implements IOnMoneyRecordReceived,IOnSaveMoneyRecords,MoneyRecordListener,IOnMoneyRecordDeleted {

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
            LoadingUtil.loading(mHandler);
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
                view.setAdapter(new MoneyRecordsRecyclerViewAdapter(list, MoneyRecordsFragment.this));
            }
        });
        LoadingUtil.dismiss(mHandler);
    }



    @Override
    public void OnSaveMoneyRecords(Result rlt) {
        this.Toast(rlt.message);
        if(rlt.isSuccess()){
            MoneyRecordsOperations.getRecords(MoneyRecordsFragment.this,mDate);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void Toast(final String msg){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MoneyRecordsFragment.this.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void MoneyRecordDeleted(Result rlt){

       this.Toast(rlt.message);
        if(rlt.isSuccess()){
            MoneyRecordsOperations.getRecords(MoneyRecordsFragment.this,mDate);
        }

    }

    @Override
    public void onDelete(final MoneyRecord record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MoneyRecordsFragment.this.getContext());
        builder.setMessage("确定删除吗？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MoneyRecordsOperations.dropRecord(MoneyRecordsFragment.this,record.id);
                //    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "删除取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}
