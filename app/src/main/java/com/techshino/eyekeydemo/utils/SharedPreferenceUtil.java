package com.techshino.eyekeydemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author wangzhi
 */
public class SharedPreferenceUtil {
	
	private static final String APP_NAME = "EyeKeySDK";
	private static SharedPreferenceUtil mInstance = null;
	private Context mContext;
	
	private SharedPreferenceUtil() {
	}
	
	public static SharedPreferenceUtil getInstance() {
		if (mInstance == null) {
			mInstance = new SharedPreferenceUtil();
		}
		return mInstance;
	}
	
	public void init(Context context) {
		mContext = context;
	}
	
	public void save(String key, boolean value) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, false);
	}

	public void saveString(String key, String value) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}

	public int getInt(String key) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);
	}

	public void saveInt(String key, int value) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				APP_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
}
