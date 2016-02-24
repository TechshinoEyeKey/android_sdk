package com.techshino.eyekeydemo.app;

import android.app.Application;

import com.techshino.eyekeydemo.utils.SharedPreferenceUtil;
import com.techshino.eyekeysdk.api.CheckAPI;

/**
 * 程序入口
 * 
 * @author wangzhi
 *
 */
public class App extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferenceUtil.getInstance().init(getApplicationContext());
		CheckAPI.init(getApplicationContext());
	}
}
