package com.maijiabao.administrator.httpdemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.maijiabao.administrator.httpdemo.adapters.CategoryAdapterNew;
import com.maijiabao.administrator.httpdemo.dummy.DummyContent;
import com.maijiabao.administrator.httpdemo.interfaces.ICategoryApiResult;
import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ICategoryApiResult,ItemFragment.OnListFragmentInteractionListener,IOnSaveCategory {

    Button btnSubmit,btnFinance;
    EditText txtCateDesc,txtCateName;
    ContentLoadingProgressBar loading;
    ListView listCategory;
    CategoryOperations operations;
    ProgressDialog progress;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Result rlt =  (Result)msg.obj;
                    MainActivity.this.notifyMessage(rlt.message);
                    //rlt.message
                    break;
                case 0:
                    Result rlt2 =  (Result)msg.obj;
                    MainActivity.this.notifyMessage(rlt2.message);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = (Button) findViewById(R.id.submit);
        btnFinance = (Button) findViewById(R.id.btnToFinance);
        txtCateDesc = (EditText)findViewById(R.id.categoryDesc);
        txtCateName = (EditText)findViewById(R.id.categoryName);
       //  loading = (ContentLoadingProgressBar)findViewById(R.id.loadingBar);
      //  loading.setVisibility(View.GONE);
         progress = new ProgressDialog(this);
        this.listCategory = (ListView) findViewById(R.id.listCategories);
        operations = new CategoryOperations(this);
        progress.setMessage("save category");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
       // operations.getCategories();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // operations.SaveCategory(MainActivity.this,txtCateName.getText().toString(),txtCateDesc.getText().toString());
                CategoryFormFragment fragment  = new CategoryFormFragment();
                fragment.show(getFragmentManager(),"CategoryFormFragment");
            }
        });

        btnFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FinanceActivity.class);
                startActivity(intent);
            }
        });
//        categoryFragment fragment = new categoryFragment();
//        this.addContentView(fragment);
    }

    public void notifyMessage(String content){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(content);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onSaveCategory(Result rlt) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(rlt.message);
//        builder.setTitle("提示");
//        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
//            @Override
//             public void onClick(DialogInterface dialog, int which) {
//               dialog.dismiss();
//             }
//         });
//         builder.create().show();



    }

    public void OnSaveCategory(Result rlt) {
        this.mHandler.sendMessage(rlt.ConvertToMessage());
        //this.mHandler.sendMessage()
    }

    @Override
    public void OnCategoriesReceived(final JSONArray array) {
        listCategory.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Category> categories =  JObjectConvertor.convert(array);
                CategoryAdapterNew adapter = new CategoryAdapterNew(categories,MainActivity.this);
                //    CategoryAdapter adapterNew = new CategoryAdapter(MainActivity.this,categories);
                listCategory.setAdapter(adapter);
                progress.hide();
            }
        });



    }
}
