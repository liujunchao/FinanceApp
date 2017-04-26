package com.maijiabao.administrator.httpdemo.util;

        import android.util.Log;

        import com.maijiabao.administrator.httpdemo.interfaces.ICategoryRemoved;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnCategoriesReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordDeleted;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnMoneyRecordReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveCategory;
        import com.maijiabao.administrator.httpdemo.interfaces.IOnSaveMoneyRecords;
        import com.maijiabao.administrator.httpdemo.interfaces.IRecordsByRangeReceived;
        import com.maijiabao.administrator.httpdemo.interfaces.IVersionInfoFetched;
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
                // Log.i("MoneyRecordsFragment","fetch data by http post with date  "+date);
                String str =  HttpUtil.sendPost(map,"getMoneyRecords");

                try{
                    JSONArray array  = new JSONArray();
                    if(!str.equals("[]")){
                        array  = new JSONArray(str);
                    }
                    op.OnRecordsReceived(array,date);
                    Log.i("MoneyRecordsFragment","receive response  "+date);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }



    public static void dropRecord(final IOnMoneyRecordDeleted op, final String id ){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id",id);
                String str = HttpUtil.sendPost(map,"dropMoneyRecord");
                try{
                    JSONObject obj = new JSONObject(str);
                    op.MoneyRecordDeleted(new Result(obj));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public static void getVersion(final IVersionInfoFetched op  ){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                String str = HttpUtil.sendPost(map,"version");
                try{
                    JSONObject obj = new JSONObject(str);
                    op.VersionInfoReceived(obj.getString("code"),obj.getString("url"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void getCategoryGroupDataByRange(final IRecordsByRangeReceived op, final String startDate,final String endDate){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("startDate", startDate);
                map.put("endDate", endDate);
                String str =  HttpUtil.sendPost(map,"getMoneyRecordsByRange");

                try{
                    JSONArray array  = new JSONArray();
                    if(!str.equals("[]")){
                        array  = new JSONArray(str);
                    }
                    op.onRecordsByRangeReceived(array);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public static void getDetailsByRange(final IRecordsByRangeReceived op, final String startDate,final String endDate,final String categoryId){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("startDate", startDate);
                map.put("endDate", endDate);
                map.put("categoryId", categoryId);
                String str =  HttpUtil.sendPost(map,"getDetailsByRange");

                try{
                    JSONArray array  = new JSONArray();
                    if(!str.equals("[]")){
                        array  = new JSONArray(str);
                    }
                    op.onRecordsByRangeReceived(array);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
