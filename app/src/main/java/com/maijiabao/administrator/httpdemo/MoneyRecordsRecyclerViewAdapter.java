package com.maijiabao.administrator.httpdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.ItemFragment.OnListFragmentInteractionListener;
import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordDeleted;
import com.maijiabao.administrator.httpdemo.interfaces.MoneyRecordListener;
import com.maijiabao.administrator.httpdemo.models.MoneyRecord;
import com.maijiabao.administrator.httpdemo.models.MoneyType;

import java.util.List;


public class MoneyRecordsRecyclerViewAdapter extends RecyclerView.Adapter<MoneyRecordsRecyclerViewAdapter.ViewHolder> {

    private final List<MoneyRecord> mValues ;
    private final MoneyRecordListener mListener;
    private int position = 0;


    public MoneyRecordsRecyclerViewAdapter(List<MoneyRecord> items, MoneyRecordListener listener) {
        mValues = items;
        mListener = listener;
        this.position =0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MoneyRecord record = mValues.get(position);
        View view = null;
        if(record.isCurrent){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_record_left, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_record_right, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        this.position = position+1;
        holder.mItem = mValues.get(position);
        if(holder.mItem!=null){

       //   holder.bubbleLayout.measureA
           // holder.mAmountView.setText(holder.mItem.amount);
         //   holder.mCategoryNameView.setText(holder.mItem.categoryName);
            String desc = "";
            if(holder.mItem.type == MoneyType.expenses){
                desc +=holder.mItem.creator+"在<b>"+holder.mItem.categoryName+"</b>花了"+"<b><font color='black' face='big'>"+holder.mItem.amount+"</font></b>块钱";
            }else  if(holder.mItem.type == MoneyType.earnings){
                desc +=holder.mItem.creator+"在<b>"+holder.mItem.categoryName+"</b> 挣了"+"<b><font color='black' face='small'>"+holder.mItem.amount+"</font></b>块钱";
            }

            Spanned html =  Html.fromHtml(desc,0);
            holder.mAmountView.setText(html);
        }

       if(holder.img!=null){
           holder.img.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   MoneyRecordsRecyclerViewAdapter.this.mListener.onDelete(holder.mItem);
               }
           });
       }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAmountView;
        public final ImageButton img;
        public MoneyRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAmountView = (TextView) view.findViewById(R.id.amount);
            img  = (ImageButton) view.findViewById(R.id.btnRemoveRecord);
        }
    }
}
