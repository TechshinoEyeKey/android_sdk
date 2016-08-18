package com.techshino.eyekeydev.app;

import android.app.Application;

import com.techshino.eyekeydev.BuildConfig;
import com.techshino.eyekeydev.utils.Logs;
import com.techshino.eyekeydev.utils.SharedPreferenceUtil;

/**
 * 程序入口
 *
 * @author wangzhi
 */
public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Logs.setsIsLogEnabled(BuildConfig.DEBUG);
    SharedPreferenceUtil.getInstance().init(getApplicationContext());
  }
}
