package com.techshino.eyekeydemo.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.techshino.eyekeydemo.R;

/**
 * @author wangzhi
 */
public class SnackBarUtils {

  public static void show(View view, int resId) {
    if (view == null)
      return;
    Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
  }

  public static void show(View view, String text) {
    if (view == null)
      return;
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
  }

  public static void show(View view, String text, int colorId) {
    if (view == null)
      return;
    Snackbar snackbar = getSnackBar(view, text, colorId);
    snackbar.setActionTextColor(colorId);
    snackbar.show();
  }

  public static void show(View view, int resId, int colorId) {
    if (view == null)
      return;
    Snackbar snackbar = getSnackBar(view, resId, colorId);
    snackbar.setActionTextColor(colorId);
    snackbar.show();
  }

  public static void showError(View view, int resId) {
    if (view == null)
      return;
    getSnackBar(view, resId, Color.parseColor("#ff443e")).show();
  }

  public static void showError(View view, String text) {
    if (view == null)
      return;
    getSnackBar(view, text, Color.parseColor("#ff443e")).show();
  }

  public static Snackbar getSnackBar(View view, int resId) {
    Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
    snackbar.setCallback(new Snackbar.Callback() {
      @Override
      public void onDismissed(Snackbar snackbar, int event) {
        super.onDismissed(snackbar, event);
      }
    });
    return Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
  }

  public static Snackbar getSnackBar(View view, String text) {
    return Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
  }

  public static Snackbar getSnackBar(View view, int resId, int colorId) {
    Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
    setSnackbarBackground(snackbar, colorId);
    return snackbar;
  }

  public static Snackbar getSnackBar(View view, String text, int colorId) {
    Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
    setSnackbarBackground(snackbar, colorId);
    return snackbar;
  }

  public static void setSnackbarBackground(Snackbar snackbar, int background) {
    View view = snackbar.getView();
    view.setBackgroundColor(background);
  }

  public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
    View view = snackbar.getView();
    ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
  }

}
