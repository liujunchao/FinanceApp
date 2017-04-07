package com.maijiabao.administrator.httpdemo.util;

        import com.maijiabao.administrator.httpdemo.interfaces.ICategoryRemoved;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveMoneyRecords;
        import com.maijiabao.administrator.httpdemo.interfaces.Result;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;


public class MoneyRecordsOperations {
    private MoneyRecordsOperations(){}

    public static void SaveRecord(final IOnSaveMoneyRecords op, final String amount, final String desc,final String cateId,final String belongDate,final  String type){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("amount", amount);
                map.put("desc",desc);
                map.put("categoryId",cateId);
                map.put("belongDate",belongDate);
                map.put("type",type);
                String str = HttpUtil.sendPost(map,"insertMoneyRecord");
                try{
                    JSONObject obj = new JSONObject(str);
                    op.OnSaveMoneyRecords(new Result(obj));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void getRecords(final IOnMoneyRecordReceived op, final String date){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("belongDate", date);
                String str =  HttpUtil.sendPost(map,"getMoneyRecords");

                try{
                    JSONArray array  = new JSONArray(str);
                    op.OnRecordsReceived(array);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void dropRecord(final ICategoryRemoved op, final String id ){
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
