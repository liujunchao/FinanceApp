package com.maijiabao.administrator.httpdemo.controls;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maijiabao.administrator.httpdemo.R;
import java.util.ArrayList;
import java.util.List;


public class ListFragment extends DialogFragment {

    private static final String DATA_SOURCE = "data_source";
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private Handler mHandler = new Handler(){ };
    public ListFragment() {  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            list = getArguments().getStringArrayList(DATA_SOURCE);
        }

    }

    public static ListFragment newInstance(ArrayList<String> list) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(DATA_SOURCE, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_list_layout, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ListViewAdapter(list));
        }
        return view;
    }


    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

        private final List<String> mValues ;

        public ListViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =   view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.default_list_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int pos) {
            holder.mItem = mValues.get(pos);
            if(holder.mItem!=null){
                Spanned html =  Html.fromHtml(holder.mItem,0);
                holder.mAmountView.setText(html);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mAmountView;
            public String mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAmountView = (TextView) view.findViewById(R.id.text1);

            }
        }
    }


}
