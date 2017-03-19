package com.maijiabao.administrator.httpdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.maijiabao.administrator.httpdemo.interfaces.ICategoryApiResult;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ICategoryApiResult {

    Button btnSubmit;
    EditText txtCateDesc,txtCateName;
    ContentLoadingProgressBar loading;
    ListView listCategory;
    CategoryOperations operations;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = (Button) findViewById(R.id.submit);
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
       operations.getCategories();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operations.SaveCategory(txtCateName.getText().toString(),txtCateDesc.getText().toString());
            }
        });
    }


    @Override
    public void onSaveCategory(Result rlt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(rlt.message);
        builder.setTitle("提示");
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
             public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
             }
         });
         builder.create().show();
    }

    @Override
    public void OnCategoriesReceived(final JSONArray array) {
        listCategory.post(new Runnable() {
            @Override
            public void run() {
                try{

                    ArrayList<Category> categories =  JObjectConvertor.convert(array);
                    CategoryAdapterNew adapter = new CategoryAdapterNew(categories,MainActivity.this);
                //    CategoryAdapter adapterNew = new CategoryAdapter(MainActivity.this,categories);
                    listCategory.setAdapter(adapter);


                }catch (JSONException ex){
                    ex.printStackTrace();
                }
                progress.hide();
            }
        });



    }
}
