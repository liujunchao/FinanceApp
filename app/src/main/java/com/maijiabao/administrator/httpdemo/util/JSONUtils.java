package com.maijiabao.administrator.httpdemo.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 3/14/2017.
 */
public class JSONUtils {
    public static Map<String, String> toFlatStringMap(final JSONObject object) throws JSONException {
        final Map<String, String> map = new HashMap<String, String>();
        final Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            final String key = keysItr.next();
            final Object value = object.get(key);
            if (!(value instanceof String)) {
                continue;
            }

            map.put(key, (String) value);
        }

        return map;
    }
}
