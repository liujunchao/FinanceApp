package com.maijiabao.administrator.httpdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.adapters.SectionsPagerAdapter;
import com.maijiabao.administrator.httpdemo.interfaces.IVersionInfoFetched;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;
import com.maijiabao.administrator.httpdemo.util.DateUtil;
import com.maijiabao.administrator.httpdemo.util.DownloadManager;
import com.maijiabao.administrator.httpdemo.util.HttpUtil;
import com.maijiabao.administrator.httpdemo.util.LoadingUtil;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TabbedActivity extends AppCompatActivity implements IVersionInfoFetched {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private String date;
    Handler handler = new Handler() { };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Log.i("MoneyRecordsFragment","activity start");
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount()-1);

       // MoneyRecordsOperations.getVersion(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabbedActivity.this.date = mSectionsPagerAdapter.getDate(position);
                TabbedActivity.this.setTitle(date);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int lastIndex = mSectionsPagerAdapter.getCount()-1;
        this.date = mSectionsPagerAdapter.getDate(lastIndex);
        this.setTitle(date);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.TRANSPARENT);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoneyRecordsFormFragment fragment  = new MoneyRecordsFormFragment();
                fragment.SetDate( TabbedActivity.this.date);
                fragment.show(getFragmentManager(),"MoneyRecordsFormFragment");
                fragment.addObserver(mSectionsPagerAdapter.currentFragment);
            }
        });
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }else{
            TelephonyManager mngr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            HttpUtil.setIMEI(mngr.getDeviceId(0));
        }
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void VersionInfoReceived(String code,final String downloadUrl) {
       int iCode =   Integer.parseInt(code);

        try {
            int currentCode = getVersionCode();
            if(iCode == currentCode){
                return ;
            }
           this.handler.post(new Runnable() {
               @Override
               public void run() {
                   showUpdataDialog(downloadUrl);
               }
           });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(final String url){
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = DownloadManager
                            .getFileFromServer(url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();
    }

    protected void showUpdataDialog(final  String downloadPath) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage("版本升级，请在WIFI环境下进行");
        //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               downloadFile(downloadPath);
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }
    private int getVersionCode() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionCode;
    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadingUtil.initContext(TabbedActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    TelephonyManager mngr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                     HttpUtil.setIMEI(mngr.getDeviceId(0));
                }
                break;

            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent  = new Intent(TabbedActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_changeName){
           final   EditText txt  = new EditText(this);
            new AlertDialog.Builder(this).setTitle("请输入名称").setIcon(android.R.drawable.ic_dialog_info).setView(
                    txt).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String msg  = txt.getText().toString();
                    if(msg!=null&& msg!=""){
                        CategoryOperations.updateUserProfile(msg);
                    }
                }
            }).setNegativeButton("取消", null).show();
        }

        return super.onOptionsItemSelected(item);
    }



}
