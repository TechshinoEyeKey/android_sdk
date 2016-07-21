package com.techshino.eyekeydemo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtils {

  private ToastUtils() {
    throw new AssertionError();
  }

  public static void show(Context context, int resId) {
    if (context == null)
      return;
    show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
  }

  public static void show(Context context, int resId, int duration) {
    if (context == null)
      return;
    show(context, context.getResources().getText(resId), duration);
  }

  public static void show(Context context, CharSequence text) {
    show(context, text, Toast.LENGTH_SHORT);
  }

  public static void show(Context context, CharSequence text, int duration) {
    if (context == null)
      return;
    Toast.makeText(context, text, duration).show();
  }

  public static void show(Context context, int resId, Object... args) {
    if (context == null)
      return;
    show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
  }

  public static void show(Context context, String format, Object... args) {
    if (context == null)
      return;
    show(context, String.format(format, args), Toast.LENGTH_SHORT);
  }

  public static void show(Context context, int resId, int duration, Object... args) {
    if (context == null)
      return;
    show(context, String.format(context.getResources().getString(resId), args), duration);
  }

  public static void show(Context context, String format, int duration, Object... args) {
    if (context == null)
      return;
    show(context, String.format(format, args), duration);
  }
}
