package com.maijiabao.administrator.httpdemo.interfaces;

import org.json.JSONObject;

/**
 * Created by Administrator on 4/17/2017.
 */
public interface IVersionInfoFetched {
    public void VersionInfoReceived(String code,String downloadUrl);
}
