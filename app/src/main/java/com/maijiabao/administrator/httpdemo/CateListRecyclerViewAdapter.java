package com.maijiabao.administrator.httpdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.ItemFragment.OnListFragmentInteractionListener;
import com.maijiabao.administrator.httpdemo.dummy.DummyContent.DummyItem;
import com.maijiabao.administrator.httpdemo.interfaces.ICategoryRemoved;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.models.Category;
import com.maijiabao.administrator.httpdemo.models.MoneyType;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CateListRecyclerViewAdapter extends RecyclerView.Adapter<CateListRecyclerViewAdapter.ViewHolder>     {

    private final ArrayList<Category>  mValues;
    private final ICategoryRemoved mListener;
    private int pos;
    public CateListRecyclerViewAdapter(ArrayList<Category>  items, ICategoryRemoved listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.categoryName);
        String type  = "花费";
        if(holder.mItem.type.equals( MoneyType.earnings.toString())){
            type="收入";
        }
        holder.mContentView.setText(type);
        holder.mBtnRemoveCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                CategoryOperations.dropCategory(mListener,holder.mItem.id);

            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                }
            }
        });
    }

    public void refreshDataChanged(ArrayList<Category> newDataSet){
        this.mValues.clear();
        this.mValues.addAll(newDataSet);
        notifyDataSetChanged();
    }
    public void refreshDataChanged(){
        mValues.remove(this.pos);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageButton mBtnRemoveCate;
        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mBtnRemoveCate  = (ImageButton) view.findViewById(R.id.btnRemoveCate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
