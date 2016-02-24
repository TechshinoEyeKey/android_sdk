package com.techshino.eyekeydemo.utils;

import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/9/16.
 */
public class SmartBarUtils {

    /**
     * 以下三个方法原作者为c跳跳(http://weibo.com/u/1698085875),
     * 由Shawn(http://weibo.com/linshen2011)在其基础上改进了一种判断SmartBar是否存在的方法,
     * 注意该方法反射的接口只存在于2013年6月之后魅族的flyme固件中
     */

    /**
     * 方法一:uc等在使用的方法(新旧版flyme均有效)，
     * 此方法需要配合requestWindowFeature(Window.FEATURE_NO_TITLE
     * )使用,缺点是程序无法使用系统actionbar
     *
     * @param decorView window.getDecorView
     */
    public static void hide(View decorView) {
        if (!hasSmartBar())
            return;

        try {
            @SuppressWarnings("rawtypes")
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Integer.TYPE;
            Method localMethod = View.class.getMethod("setSystemUiVisibility",
                    arrayOfClass);
            Field localField = View.class
                    .getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
            Object[] arrayOfObject = new Object[1];
            try {
                arrayOfObject[0] = localField.get(null);
            } catch (Exception e) {

            }
            localMethod.invoke(decorView, arrayOfObject);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新型号可用反射调用Build.hasSmartBar()来判断有无SmartBar
     *
     * @return
     */
    public static boolean hasSmartBar() {
        try {
            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }
}
