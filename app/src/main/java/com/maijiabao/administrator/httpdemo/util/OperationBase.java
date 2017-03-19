package com.maijiabao.administrator.httpdemo.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by liujunchao on 3/19/17.
 */

public class OperationBase {
    private Context context;
    private ProgressDialog progress;
    public OperationBase(Context context){
        this.context = context;
        progress = new ProgressDialog(this.context);
    }



}
