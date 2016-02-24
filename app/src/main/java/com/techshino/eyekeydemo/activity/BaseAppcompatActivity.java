package com.techshino.eyekeydemo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.techshino.eyekeydemo.R;
import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeydemo.utils.SmartBarUtils;

import butterknife.ButterKnife;

/**
 * Created by wangzhi on 2015/12/25.
 */
public abstract class BaseAppcompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initWindow();
        SmartBarUtils.hide(getWindow().getDecorView());

        initData();
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            /** 透明状态栏 */
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /** 透明导航栏 */
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
        }
    }

    public abstract void initData();

    public abstract int getLayoutId();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        CheckAPI.cancelAllCall();
    }

    @Override
    public void finish() {
        super.finish();
    }

    protected void startActivity(Class<?> cls,Bundle bundle) {
        Intent intent = new Intent(this,cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivityThenKill(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }


}
