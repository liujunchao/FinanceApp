package com.maijiabao.administrator.httpdemo.interfaces;

import android.os.Message;

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

    public Message ConvertToMessage(){
        Message msg  = new Message();
        msg.what = Integer.parseInt(this.status);
        msg.obj = this;
        return msg;
    }
}
