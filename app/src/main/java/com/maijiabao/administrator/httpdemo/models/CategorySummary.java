package com.maijiabao.administrator.httpdemo.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategorySummary {
    public CategorySummary(){ }
    public String amount;
    public String categoryId;
    public String categoryName;

    public static ArrayList<CategorySummary> convert(JSONArray array) {
        ArrayList<CategorySummary> list = new ArrayList<CategorySummary>();
        if(array.length() == 0){
            return list;
        }
        try{
            for(int i =0,len = array.length();i<len;i++){
                JSONObject json  = array.getJSONObject(i);
                CategorySummary obj  = new CategorySummary();
                obj.amount =  json.get("amount").toString();
                obj.categoryName = json.get("categoryName").toString();
                obj.categoryId = json.get("categoryId").toString();
                list.add(obj);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }


        return list;

    }
}