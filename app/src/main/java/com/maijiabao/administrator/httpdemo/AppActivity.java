package com.maijiabao.administrator.httpdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.maijiabao.administrator.httpdemo.adapters.SectionsPagerAdapter;
import com.maijiabao.administrator.httpdemo.charts.PieChartActivity;
import com.maijiabao.administrator.httpdemo.interfaces.IVersionInfoFetched;
import com.maijiabao.administrator.httpdemo.util.CategoryOperations;
import com.maijiabao.administrator.httpdemo.util.DownloadManager;
import com.maijiabao.administrator.httpdemo.util.HttpUtil;
import com.maijiabao.administrator.httpdemo.util.LoadingUtil;
import com.maijiabao.administrator.httpdemo.util.MoneyRecordsOperations;

import java.io.File;

public class AppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IVersionInfoFetched {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private String date;

    private String mDownloadUrl;

    Handler handler = new Handler() { };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


         MoneyRecordsOperations.getVersion(this);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }else{
            TelephonyManager mngr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            HttpUtil.setIMEI(mngr.getDeviceId(0));
            this.initPagerAdapter();
        }
    }

    private void initPagerAdapter(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoneyRecordsFormFragment fragment  = new MoneyRecordsFormFragment();
                fragment.SetDate( AppActivity.this.date);
                fragment.show(getFragmentManager(),"MoneyRecordsFormFragment");
                fragment.addObserver(mSectionsPagerAdapter.currentFragment);
            }
        });
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount()-1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppActivity.this.date = mSectionsPagerAdapter.getDate(position);
                AppActivity.this.setTitle(date);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int lastIndex = mSectionsPagerAdapter.getCount()-1;
        this.date = mSectionsPagerAdapter.getDate(lastIndex);
        this.setTitle(date);
    }


    //安装apk
    protected void installApk(File apkFile) {


        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(AppActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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
                    int permissionCheck = ContextCompat.checkSelfPermission(AppActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        AppActivity.this.mDownloadUrl = downloadUrl;
                        ActivityCompat.requestPermissions(AppActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    }else{
                        showUpdataDialog(downloadUrl);
                    }
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
                    if(file == null){
                        pd.dismiss();
                    }else{
                        sleep(3000);
                        installApk(file);
                        pd.dismiss(); //结束掉进度条对话框
                    }
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
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),  0);
        return packInfo.versionCode;
    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadingUtil.initContext(AppActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    TelephonyManager mngr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                    HttpUtil.setIMEI(mngr.getDeviceId(0));
                    this.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AppActivity.this.initPagerAdapter();
                        }
                    });
                }else{
                    Toast.makeText(AppActivity.this,"没权限，无法使用该程序",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                     this.showUpdataDialog(this.mDownloadUrl);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.action_settings) {
            Intent intent  = new Intent(AppActivity.this,CategoryActivity.class);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
        if (id == R.id.nav_share) {
            Intent intent  = new Intent(AppActivity.this,PieChartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent  = new Intent(AppActivity.this,CategoryActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
