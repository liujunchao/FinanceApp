package com.maijiabao.administrator.httpdemo.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/29/2017.
 */
public class JObjectMoneyRecordConvertor {
    public static ArrayList<MoneyRecord> convert(JSONArray array) {
        ArrayList<MoneyRecord> list = new ArrayList<MoneyRecord>();
        try{
            for(int i =0,len = array.length();i<len;i++){
                String name =  array.getJSONObject(i).get("categoryName").toString();
//                String desc =  array.getJSONObject(i).get("categoryDesc").toString();
//                String id =  array.getJSONObject(i).get("_id").toString();
                MoneyRecord obj  = new MoneyRecord();
               // obj.id = id;
                list.add(obj);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }


        return list;

    }
}
