package com.maijiabao.administrator.httpdemo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.maijiabao.administrator.httpdemo.MoneyRecordsFragment;
import com.maijiabao.administrator.httpdemo.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public MoneyRecordsFragment currentFragment;
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
//            Log.i("MoneyRecordsFragment","setPrimaryItem:"+position);
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