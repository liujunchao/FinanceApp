package com.maijiabao.administrator.httpdemo;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maijiabao.administrator.httpdemo.ItemFragment.OnListFragmentInteractionListener;
import com.maijiabao.administrator.httpdemo.models.Category;
import com.maijiabao.administrator.httpdemo.models.MoneyRecord;
import com.maijiabao.administrator.httpdemo.models.MoneyType;

import java.util.List;


public class MoneyRecordsRecyclerViewAdapter extends RecyclerView.Adapter<MoneyRecordsRecyclerViewAdapter.ViewHolder> {

    private final List<MoneyRecord> mValues ;
    private final OnListFragmentInteractionListener mListener;

    public MoneyRecordsRecyclerViewAdapter(List<MoneyRecord> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if(holder.mItem!=null && holder.mAmountView!=null){
           // holder.mAmountView.setText(holder.mItem.amount);
         //   holder.mCategoryNameView.setText(holder.mItem.categoryName);
            String desc = String.format("<span style=\"font-size:%.0fpx\";>good</span>", new Float(15));
            if(holder.mItem.type == MoneyType.expenses){
                desc +=holder.mItem.creator+"在'"+holder.mItem.categoryName+"'花了"+"<font color='red' face='big'>"+holder.mItem.amount+"</font>块钱";
            }else  if(holder.mItem.type == MoneyType.earnings){
                desc +=holder.mItem.creator+"在'"+holder.mItem.categoryName+"'挣了"+"<font color='green' face='small'>"+holder.mItem.amount+"</font>块钱";
            }

         Spanned html =  Html.fromHtml(desc,0);
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
//        public final TextView mCreatorView;
//        public final TextView mCategoryNameView;
        public MoneyRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAmountView = (TextView) view.findViewById(R.id.amount);
//            mCreatorView = (TextView) view.findViewById(R.id.creator);
//            mCategoryNameView = (TextView) view.findViewById(R.id.categoryName);
        }
        //@Override
//        public String toString() {
//            return super.toString() + " '" + mAmountView.getText() + "'";
//        }
    }
}
