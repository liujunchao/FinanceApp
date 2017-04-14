package com.maijiabao.administrator.httpdemo;
        import android.app.Activity;
        import android.app.DialogFragment;
        import android.content.Context;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.RecyclerView;
        import android.util.DisplayMetrics;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.maijiabao.administrator.httpdemo.adapters.SpinnerCategoryAdapter;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveMoneyRecords;
        import com.maijiabao.administrator.httpdemo.interfaces.Result;
        import com.maijiabao.administrator.httpdemo.models.Category;
        import com.maijiabao.administrator.httpdemo.models.JObjectCategoryConvertor;
        import com.maijiabao.administrator.httpdemo.models.MoneyType;
        import com.maijiabao.administrator.httpdemo.util.CategoryOperations;
        import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

        import org.json.JSONArray;

        import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyRecordsFormFragment extends DialogFragment implements IOnCategoriesReceived {

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<Category> list  = (ArrayList<Category>)msg.obj;
            if(getView() == null)
                return ;
              Spinner spinner = (Spinner)getView().findViewById(R.id.spinnerCategory);


            Context ctx = MoneyRecordsFormFragment.this.getActivity();
        spinner.setAdapter(new SpinnerCategoryAdapter(list,ctx));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category obj  = (Category) parent.getSelectedItem();
                MoneyRecordsFormFragment.this.selectedCategory = obj.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        }
    };
    public MoneyRecordsFormFragment() {  }

    private IOnSaveMoneyRecords observer;

    private String selectedDate;
    private String selectedCategory;
    private String type;

    public void addObserver(IOnSaveMoneyRecords observer){
        this.observer = observer;
    }



    public void SetDate(String date){
        this.selectedDate  = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.money_item, container, false);

        final EditText txtAmount =(EditText) view.findViewById(R.id.txtAmount);
        final EditText txtDesc =(EditText) view.findViewById(R.id.txtDesc);
        final RadioGroup radioGroup =(RadioGroup) view.findViewById(R.id.type);
        final RadioButton btnTypeIn =(RadioButton) view.findViewById(R.id.typeIn);
        final RadioButton btnTypeOut =(RadioButton) view.findViewById(R.id.typeOut);
        Button btn  = (Button) view.findViewById(R.id.btnSubmitMoney);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = txtAmount.getText().toString();
                String desc = txtDesc.getText().toString();
                MoneyRecordsFormFragment.this.dismiss();
                MoneyRecordsOperations.SaveRecord(MoneyRecordsFormFragment.this.observer,amount,desc,MoneyRecordsFormFragment.this.selectedCategory,MoneyRecordsFormFragment.this.selectedDate,MoneyRecordsFormFragment.this.type);
            }
        });
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
                CategoryOperations.getCategoriesByType(MoneyRecordsFormFragment.this,type);
            }
        });

        CategoryOperations.getCategoriesByType(MoneyRecordsFormFragment.this,type);
        return view;
    }



    @Override
    public void OnCategoriesReceived(JSONArray array) {
        ArrayList<Category> list = JObjectCategoryConvertor.convert(array);
        Message msg  = new Message();
        msg.obj = list;
        this.mhandler.sendMessage(msg);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );

    }
}
