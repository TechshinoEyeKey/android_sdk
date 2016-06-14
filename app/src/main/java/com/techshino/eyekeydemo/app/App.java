package com.techshino.eyekeydemo.app;

import android.app.Application;

import com.techshino.eyekeydemo.BuildConfig;
import com.techshino.eyekeydemo.utils.Logs;
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
		// 初始化eyekey接口 （需在AndroidManifest.xml中添加appid和appkey）
		CheckAPI.init(getApplicationContext());
		Logs.setsIsLogEnabled(BuildConfig.DEBUG);
	}
}
