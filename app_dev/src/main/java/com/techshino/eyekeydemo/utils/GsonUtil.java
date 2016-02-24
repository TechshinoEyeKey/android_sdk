package com.techshino.eyekeydemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by wangzhi on 2015/12/8.
 */
public class GsonUtil {

    private static GsonUtil mInstance = null;

    private Gson mGson = null;

    private GsonUtil() {
        mGson = new Gson();
    }

    public static GsonUtil getInstance() {
        if (mInstance == null) {
            mInstance = new GsonUtil();
        }
        return mInstance;
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T gsonToEntity(JSONObject jsonObject, Class<T> clazz) {
        return gsonToEntity(jsonObject.toString(), clazz);
    }

    public <T> T gsonToEntity(JsonObject jsonObject, Type type) {
        return gsonToEntity(jsonObject.toString(), type);
    }

    public <T> T gsonToEntity(String string, Type type) {
        T obj = mGson.fromJson(string, type);
        return obj;
    }

    public <T> T gsonToEntity(String string, Class<T> clazz) {
        T obj = mGson.fromJson(string, clazz);
        return obj;
    }
}
