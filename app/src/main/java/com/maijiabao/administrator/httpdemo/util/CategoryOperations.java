package com.maijiabao.administrator.httpdemo.util;

import com.maijiabao.administrator.httpdemo.interfaces.ICategoryRemoved;
import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
import com.maijiabao.administrator.httpdemo.interfaces.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class CategoryOperations {
    private CategoryOperations(){}

    public static void SaveCategory(final IOnSaveCategory op, final String name, final String desc,final String type){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("categoryName",name);
                map.put("categoryDesc",desc);
                map.put("type",type);
                String str = HttpUtil.sendPost(map,"insertCategory");
                try{
                    JSONObject obj = new JSONObject(str);
                    op.OnSaveCategory(new Result(obj));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void getCategories(final IOnCategoriesReceived op){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                String str =  HttpUtil.sendPost(map,"getCategories");
                try{
                    JSONArray array  = new JSONArray(str);
                    op.OnCategoriesReceived(array);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void dropCategory(final ICategoryRemoved op, final String id ){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id",id);
                String str = HttpUtil.sendPost(map,"dropCategory");
                try{
                    JSONObject obj = new JSONObject(str);
                    op.CategoryRemoved(new Result(obj));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
