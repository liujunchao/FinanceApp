package com.maijiabao.administrator.httpdemo.interfaces;

import org.json.JSONArray;

/**
 * Created by Administrator on 3/15/2017.
 */
public interface ICategoryApiResult {
    public void onSaveCategory(Result rlt);
    public void OnCategoriesReceived(JSONArray array);
}
