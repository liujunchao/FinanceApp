package com.maijiabao.administrator.httpdemo;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.models.MoneyType;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFormFragment extends DialogFragment implements IOnSaveCategory {

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getDialog().dismiss();
            CategoryFormFragment.this.notifyObserver(msg.obj);
        }
    };
     public CategoryFormFragment() {  }

    private IOnSaveCategory observer;

    public String type;

    public void addObserver(IOnSaveCategory observer){
        this.observer = observer;
    }

    public void notifyObserver(Object obj){
        if(this.observer!=null){
            this.observer.OnSaveCategory((Result) obj);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_category_form, container, false);
        final RadioGroup radioGroup =(RadioGroup) view.findViewById(R.id.type);
        final RadioButton btnTypeIn =(RadioButton) view.findViewById(R.id.typeIn);
        final RadioButton btnTypeOut =(RadioButton) view.findViewById(R.id.typeOut);
        final EditText txtName =(EditText) view.findViewById(R.id.cName);
        final EditText txtDesc =(EditText) view.findViewById(R.id.cDesc);
        type = MoneyType.expenses.toString();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == btnTypeIn.getId()){
                    type= MoneyType.expenses.toString();
                }
                if(checkedId == btnTypeOut.getId()){
                    type= MoneyType.earnings.toString();
                }
            }
        });
        Button btn  = (Button) view.findViewById(R.id.btnSubmit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryOperations.SaveCategory(CategoryFormFragment.this,txtName.getText().toString(),txtDesc.getText().toString(),type);
            }
        });
        return view;
    }

    @Override
    public void OnSaveCategory(Result rlt) {
        this.mhandler.sendMessage(rlt.ConvertToMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );

    }
}
