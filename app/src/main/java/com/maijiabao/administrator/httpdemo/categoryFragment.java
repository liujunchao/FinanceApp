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
import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class categoryFragment extends Fragment implements IOnCategoriesReceived{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ItemFragment.OnListFragmentInteractionListener mListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ArrayList<Category> list  = (ArrayList<Category>)msg.obj;
            RecyclerView view =  (RecyclerView)getView();
            view.setAdapter(new CateListRecyclerViewAdapter(list, null));
        }
    };
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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
        Message msg  = new Message();
        msg.obj = JObjectConvertor.convert(array);
        this.mHandler.sendMessage(msg);
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
        CategoryOperations operations = new CategoryOperations();
        operations.getCategories(this);

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
