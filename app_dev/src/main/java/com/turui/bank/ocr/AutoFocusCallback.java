package com.turui.bank.ocr;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class AutoFocusCallback
    implements Camera.AutoFocusCallback {
  private static final String TAG = AutoFocusCallback.class.getSimpleName();
  private static final long AUTOFOCUS_INTERVAL_MS = 1000L;
  private Handler autoFocusHandler;
  private int autoFocusMessage;

  void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
    this.autoFocusHandler = autoFocusHandler;
    this.autoFocusMessage = autoFocusMessage;
  }

  public void onAutoFocus(boolean success, Camera camera) {
    if (this.autoFocusHandler != null) {
      Message message = this.autoFocusHandler.obtainMessage(this.autoFocusMessage, Boolean.valueOf(success));
      this.autoFocusHandler.sendMessageDelayed(message, 1000L);
      this.autoFocusHandler = null;
    } else {
      Log.d(TAG, "Got auto-focus callback, but no handler for it");
    }
  }
}
