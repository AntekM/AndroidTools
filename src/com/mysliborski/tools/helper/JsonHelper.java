package com.mysliborski.tools.helper;

import com.mysliborski.tools.exception.ServiceException;
import com.mysliborski.tools.exception.ServiceExceptionCodes;
import com.mysliborski.tools.exception.UnknownException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysliborski.tools.helper.LogHelper.loge;

public class JsonHelper {

    public static List<Long> getAsLongList(JSONArray jsonArray) throws JSONException {
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getLong(i));
        }
        return list;
    }

    public static JSONObject getObj(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            return obj.getJSONObject(name);
        } else {
            return null;
        }
    }

    public static String getString(JSONObject obj, String name) throws ServiceException {
        if (obj.has(name)) {
            try {
                return obj.getString(name);
            } catch (JSONException e) {
                throw new UnknownException(e);
            }
        } else {
            return null;
        }
    }

    public static Integer getInteger(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            try {
                return obj.getInt(name);
            } catch (JSONException e) {
                try {
                    return Integer.getInteger(obj.getString(name));
                } catch (JSONException ex) {
                    loge(ex, "Cannot convert value " + obj.get(name));
                    return null;
                }
            }
        }
        return null;
    }

    public static Long getLong(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            try {
                return obj.getLong(name);
            } catch (JSONException e) {
                try {
                    return Long.getLong(obj.getString(name));
                } catch (JSONException ex) {
                    loge(ex, "Cannot convert value " + obj.get(name));
                    return null;
                }
            }
        }
        return null;
    }

    public static JSONArray jsonArray(Object... objs) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Object obj : objs) {
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    /**
     * Converts to flat JSON object
     *
     * @param objs - list of alterating keys and values
     * @return
     */
    public static JSONObject asJsonObject(Object... objs) throws ServiceException {
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        if (objs.length % 2 == 1) {
            throw new IllegalArgumentException("Must have even number of arguments");
        }
        for (int i = 0; i < objs.length / 2; i++) {
            String key = (String) objs[i * 2];
            Object val = objs[i * 2 + 1];
            stringObjectHashMap.put(key, val);
        }
        return asJsonObject(stringObjectHashMap);
    }

    public static JSONObject asJsonObject(Map<String, Object> map) throws ServiceException {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                throw new ServiceException(ServiceExceptionCodes.JSON_EXCEPTION);
            }
        }
        return jsonObject;

    }

    public static boolean getBool(JSONObject obj, String name, boolean def) throws JSONException {
        if (obj.has(name))
            return obj.getBoolean(name);
        else
            return def;
    }


}