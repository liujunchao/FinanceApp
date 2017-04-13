package com.maijiabao.administrator.httpdemo.interfaces;

import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {
    public Result(JSONObject obj){
        try{
            this.status = obj.getString("status");
            this.message = obj.getString("msg");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
    //success 1 fail 0
    public String status;
    public String message;

    public Message ConvertToMessage(){
        Message msg  = new Message();
        msg.what = Integer.parseInt(this.status);
        msg.obj = this;
        return msg;
    }

    public boolean isSuccess(){
        return this.status.equals("1");
    }
}
