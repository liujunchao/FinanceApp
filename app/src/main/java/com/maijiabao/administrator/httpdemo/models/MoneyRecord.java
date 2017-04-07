package com.maijiabao.administrator.httpdemo.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/29/2017.
 */
public class MoneyRecord {
    public MoneyRecord(){ }
    public String desc;
    public String amount;
    public MoneyType type;
    public String creator;
    //public String createTime;
    public String categoryId;
    public String categoryName;
    public String belongDate;
    public String id;

    public static ArrayList<MoneyRecord> convert(JSONArray array) {
        ArrayList<MoneyRecord> list = new ArrayList<MoneyRecord>();
        try{
            for(int i =0,len = array.length();i<len;i++){
                JSONObject json  = array.getJSONObject(i);
                MoneyRecord obj  = new MoneyRecord();
                obj.id =  json.get("_id").toString();
                obj.amount =  json.get("amount").toString();
                obj.belongDate =  json.get("belongDate").toString();
                obj.type = MoneyType.valueOf(json.get("type").toString());
                obj.categoryName = json.get("categoryName").toString();
                obj.categoryId = json.get("categoryId").toString();
                obj.desc =  json.get("desc").toString();
                obj.creator = json.get("creator").toString();
                // obj.id = id;
                list.add(obj);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }


        return list;

    }
}
