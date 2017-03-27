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
import com.maijiabao.administrator.httpdemo.interfaces.ICategoryRemoved;
import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class categoryFragment extends Fragment implements IOnCategoriesReceived,ICategoryRemoved,IOnSaveCategory{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private boolean isDataReload = false;
    private ItemFragment.OnListFragmentInteractionListener mListener;
    private CateListRecyclerViewAdapter adapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5:
                    ArrayList<Category> refreshList  = (ArrayList<Category>)msg.obj;
                    adapter.refreshDataChanged(refreshList);
                    break;
                case 10:
                    adapter.refreshDataChanged();
                    break;
                default:
                    ArrayList<Category> list  = (ArrayList<Category>)msg.obj;
                    RecyclerView view =  (RecyclerView)getView();
                    adapter = new CateListRecyclerViewAdapter(list, categoryFragment.this);
                    view.setAdapter(adapter);
                    break;
            }

        }
    };
    public categoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static categoryFragment newInstance(int columnCount) {
        categoryFragment fragment = new categoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void OnCategoriesReceived(JSONArray array) {

        ArrayList<Category> list = JObjectConvertor.convert(array);
        Message msg  = new Message();
        msg.obj = list;
        if(isDataReload){
            msg.what = 5;
        }
        this.mHandler.sendMessage(msg);



    }

    @Override
    public void CategoryRemoved(Result rlt) {
        Message msg  = new Message();
        msg.what = 10;
        this.mHandler.sendMessage( msg);
    }

    @Override
    public void OnSaveCategory(Result rlt) {
        isDataReload = true;
        CategoryOperations.getCategories(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
         //   recyclerView.setAdapter(new CateListRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        CategoryOperations.getCategories(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemFragment.OnListFragmentInteractionListener) {
            mListener = (ItemFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
