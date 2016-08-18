package com.turui.bank.ocr;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class PreviewCallback
    implements Camera.PreviewCallback {
  private static final String TAG = PreviewCallback.class.getSimpleName();
  private final CameraConfigurationManager configManager;
  private final boolean useOneShotPreviewCallback;
  private Handler previewHandler;
  private int previewMessage;

  PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
    this.configManager = configManager;
    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
  }

  void setHandler(Handler previewHandler, int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    Point cameraResolution = this.configManager.getCameraResolution();
    if (!this.useOneShotPreviewCallback) {
      camera.setPreviewCallback(null);
    }
    if (this.previewHandler != null) {
      Message message = this.previewHandler.obtainMessage(this.previewMessage, cameraResolution.x,
          cameraResolution.y, data);
      message.sendToTarget();
      this.previewHandler = null;
    } else {
      Log.d(TAG, "Got preview callback, but no handler for it");
    }
  }
}
