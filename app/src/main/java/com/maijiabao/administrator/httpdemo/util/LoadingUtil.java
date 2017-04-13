package com.maijiabao.administrator.httpdemo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Administrator on 4/13/2017.
 */
public class LoadingUtil {
    private static Context ctx;
    private static ProgressDialog dlg;

    public static void initContext(Context newCtx){
        ctx = newCtx;
    }

    public static void loading(Handler handler){
        if(dlg!=null){
            return ;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(dlg!=null){
                    return ;
                }
                if(ctx!=null){
                    dlg  = new ProgressDialog(ctx);
                    dlg.show();
                    dlg.setCancelable(false);
                    dlg.setMessage("后台处理中");
                }
            }
        });
    }
    public static void dismiss(Handler handler){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(dlg!=null){
                    dlg.hide();
                    dlg.dismiss();
                    dlg = null;
                }
            }
        });

    }
}
