package com.maijiabao.administrator.httpdemo.util;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.telephony.TelephonyManager;
        import android.util.Log;

        import org.apache.http.Header;
        import org.apache.http.HeaderElement;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.ParseException;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.protocol.HTTP;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.UnsupportedEncodingException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class HttpUtil {

    //final String url = "http://139.199.189.192:3000/";

    public static  void setIMEI(String imei){
        IMEI = imei;
    }

   public static String IMEI = "";
    public static String sendPost( HashMap<String,String> dic,String method ){
        HttpUtil util = new HttpUtil();
        return util.sendPostImpl(dic,method);
    }
    public  String sendPostImpl( HashMap<String,String> dic,String method ){
    //     String url = "http://192.168.1.51:3000/";
        String url = "http://chaoyiyi.cn:3000/";
        String requestUrl = url+method;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String,String> entry: dic.entrySet()){
            String key  = entry.getKey();
            String value = entry.getValue();
            pairs.add(new BasicNameValuePair(key,value));
        }
        HttpClient client  = new DefaultHttpClient();

        HttpPost post  = new HttpPost(requestUrl);
        try{
            post.addHeader("imei",IMEI);
            post.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.i("MoneyRecordsFragment",e.getMessage());
        }
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if ( entity != null ) {
                InputStream instream = entity.getContent();
                byte[] bytes =  readStream(instream);
                String str  = new String(bytes);
                Log.i("MoneyRecordsFragment","HttpPostResponse:"+str);
               return str;
            }else{
                Log.i("MoneyRecordsFragment","HttpPostResponse:Null");
            }
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
            Log.i("MoneyRecordsFragment",e.getMessage());
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            Log.i("MoneyRecordsFragment",e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Log.i("MoneyRecordsFragment",e.getMessage());
        }
        return null;
    }

    public byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

}
