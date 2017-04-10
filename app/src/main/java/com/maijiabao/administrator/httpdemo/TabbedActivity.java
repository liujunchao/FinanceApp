package com.maijiabao.administrator.httpdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.maijiabao.administrator.httpdemo.util.DateUtil;
import com.maijiabao.administrator.httpdemo.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount()-1);

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
        this.date = mSectionsPagerAdapter.getDate(mSectionsPagerAdapter.getCount()-1);
        this.setTitle(date);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        private MoneyRecordsFragment currentFragment;
        private ArrayList<String> dateList = new ArrayList<String>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.generateDays();
        }



        public void generateDays(){
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
            String currentDay  = ft.format(dNow);
            String prevDay = DateUtil.previousMonthByDate(currentDay);
            while (true){
                this.dateList.add(prevDay);
                if(currentDay.equals(prevDay)){
                    break;
                }
                prevDay = DateUtil.tonextday(prevDay);
            }
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            this.currentFragment = (MoneyRecordsFragment) object;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            String date = dateList.get(position);
            return MoneyRecordsFragment.newInstance(date);
        }

        public String getDate(int position){
            return  dateList.get(position);
        }

        @Override
        public int getCount() {
            return dateList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "testing";
        }
    }
}
