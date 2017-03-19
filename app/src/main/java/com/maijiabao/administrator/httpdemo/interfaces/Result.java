package com.maijiabao.administrator.httpdemo.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 3/15/2017.
 */
public class Result {
    public Result(JSONObject obj){
        try{
            this.status = obj.getString("status");
            this.message = obj.getString("msg");
        }catch (JSONException ex){
            ex.printStackTrace();
        }

    }
    public String status;
    public String message;
}
