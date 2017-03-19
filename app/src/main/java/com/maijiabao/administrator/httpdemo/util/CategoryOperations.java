package com.maijiabao.administrator.httpdemo.util;

import android.content.Context;

import com.maijiabao.administrator.httpdemo.interfaces.ICategoryApiResult;
import com.maijiabao.administrator.httpdemo.interfaces.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/14/2017.
 */
public class CategoryOperations {

    private ICategoryApiResult categoryApi;

    public CategoryOperations(ICategoryApiResult apiResult){
        this.categoryApi = apiResult;
    }

    public void SaveCategory(final String name, final String desc){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("categoryName",name);
                map.put("categoryDesc",desc);
                String str = HttpUtil.sendPost(map,"insertCategory");
                try{
                    JSONObject obj = new JSONObject(str);
                    CategoryOperations.this.categoryApi.onSaveCategory(new Result(obj));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void getCategories(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("categoryName",name);
//                map.put("categoryDesc",desc);
                String str =  HttpUtil.sendPost(map,"getCategories");
                try{
                    JSONArray array  = new JSONArray(str);
                    CategoryOperations.this.categoryApi.OnCategoriesReceived(array);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
