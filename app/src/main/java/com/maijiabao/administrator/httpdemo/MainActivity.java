package com.maijiabao.administrator.httpdemo;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maijiabao.administrator.httpdemo.dummy.DummyContent;
import com.maijiabao.administrator.httpdemo.interfaces.Result;
import com.maijiabao.administrator.httpdemo.util.HttpUtil;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {


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

    private FloatingActionButton fabAddCate;

    private categoryFragment cateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddCate = (FloatingActionButton)findViewById(R.id.fabAddCate);
        cateFragment  = (categoryFragment)  getSupportFragmentManager().findFragmentById(R.id.mycate);

        fabAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFormFragment fragment  = new CategoryFormFragment();
                fragment.addObserver(cateFragment);
                fragment.show(getFragmentManager(),"CategoryFormFragment");
            }
        });
       //  loading = (ContentLoadingProgressBar)findViewById(R.id.loadingBar);
      //  loading.setVisibility(View.GONE);
        // progress = new ProgressDialog(this);
//        progress.setMessage("save category");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.show();
       // operations.getCategories();


//        btnFinance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,FinanceActivity.class);
//                startActivity(intent);
//            }
//        });
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


}
