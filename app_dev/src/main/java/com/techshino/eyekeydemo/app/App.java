package com.techshino.eyekeydemo.app;

import android.app.Application;

import com.techshino.eyekeydemo.utils.SharedPreferenceUtil;

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
	}
}
