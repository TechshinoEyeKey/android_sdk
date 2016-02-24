package com.techshino.eyekeydemo.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by wangzhi on 2015/12/4.
 */
public class MessageManager {

    public static void sendToTarget(Handler handler, int what) {
        if (handler == null)
            return;
        Message msg = Message.obtain(handler, what);
        msg.sendToTarget();
    }

    public static void sendToTarget(Handler handler, int what, Object obj) {
        if (handler == null)
            return;
        Message msg = Message.obtain(handler, what, obj);
        msg.sendToTarget();
    }
}
