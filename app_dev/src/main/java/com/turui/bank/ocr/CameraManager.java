package com.turui.bank.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;

public final class CameraManager {
  static final int SDK_INT;
  private static final String TAG = CameraManager.class.getSimpleName();
  private static CameraManager cameraManager;

  static {
    int sdkInt;
    try {
      sdkInt = Integer.parseInt(Build.VERSION.SDK);
    } catch (NumberFormatException nfe) {
      sdkInt = 10000;
    }
    SDK_INT = sdkInt;
  }

  private final Context context;
  private final CameraConfigurationManager configManager;
  private final boolean useOneShotPreviewCallback;
  private final PreviewCallback previewCallback;
  private final AutoFocusCallback autoFocusCallback;
  private Camera.Parameters mParameters;
  private Camera camera;
  private Rect framingRect;
  private Rect framingRectInPreview;
  private boolean initialized;
  private boolean previewing;
  private int mOrientation;

  private CameraManager(Context context) {
    this.context = context;
    this.configManager = new CameraConfigurationManager(context);

    this.useOneShotPreviewCallback = (Integer.parseInt(Build.VERSION.SDK) > 3);

    this.previewCallback = new PreviewCallback(this.configManager,
        this.useOneShotPreviewCallback);
    this.autoFocusCallback = new AutoFocusCallback();
  }

  public static void init(Context context) {
    if (cameraManager == null) {
      cameraManager = new CameraManager(context);
    }
  }

  public static CameraManager get() {
    return cameraManager;
  }

  public void openDriver(SurfaceHolder holder) throws IOException {
    if (this.camera == null) {
      this.camera = Camera.open();
      if (this.camera == null) {
        throw new IOException();
      }
      this.camera.setPreviewDisplay(holder);
      if (!this.initialized) {
        this.initialized = true;
        this.configManager.initFromCameraParameters(this.camera);
        setCameraDisplayOrientation(context, 0, camera);
      }
      this.configManager.setDesiredCameraParameters(this.camera);

      FlashlightManager.enableFlashlight();
    }
  }

  public void closeDriver() {
    if (this.camera != null) {
      FlashlightManager.disableFlashlight();
      this.camera.release();
      this.camera = null;
    }
  }

  public void startPreview() {
    if ((this.camera != null) && (!this.previewing)) {
      this.camera.startPreview();
      this.previewing = true;
    }
  }

  public void stopPreview() {
    if ((this.camera != null) && (this.previewing)) {
      if (!this.useOneShotPreviewCallback) {
        this.camera.setPreviewCallback(null);
      }
      this.camera.stopPreview();
      this.previewCallback.setHandler(null, 0);
      this.autoFocusCallback.setHandler(null, 0);
      this.previewing = false;
    }
  }

  public void requestPreviewFrame(Handler handler, int message) {
    if ((this.camera != null) && (this.previewing)) {
      this.previewCallback.setHandler(handler, message);
      if (this.useOneShotPreviewCallback) {
        this.camera.setOneShotPreviewCallback(this.previewCallback);
      } else {
        this.camera.setPreviewCallback(this.previewCallback);
      }
    }
  }

  public void requestAutoFocus(Handler handler, int message) {
    if ((this.camera != null) && (this.previewing)) {
      this.autoFocusCallback.setHandler(handler, message);

      this.camera.autoFocus(this.autoFocusCallback);
    }
  }

  public Rect getSize(int width, int height, int rate_w, int rate_h) {
    int realW = 0;
    int realH = 0;
    Rect rect = new Rect();
    if ((width != 0) && (height != 0)) {
      realW = width * rate_w >> 10;
      realH = height * rate_h >> 10;
      if (realW % 2 != 0) {
        realW++;
      }
      if (realH % 2 != 0) {
        realH++;
      }
      if (realH * 203 <= realW << 7) {
        int thresExt = 16;
        int DistZoomY = height / thresExt;
        int RectHeight = height - (DistZoomY << 1);
        int RectWidth = (RectHeight * 203 * rate_h >> 7) / rate_w;
        int DistZoomX = width - RectWidth >> 1;

        rect.left = DistZoomX;
        rect.top = DistZoomY;
        rect.right = (rect.left + RectWidth - 1);
        rect.bottom = (rect.top + RectHeight - 1);
      } else {
        int thresExt = 16;
        int DistZoomX = width / thresExt;
        int RectWidth = width - (DistZoomX << 1);
        int RectHeight = (RectWidth * 81 >> 7) * rate_w / rate_h;
        int DistZoomY = height - RectHeight >> 1;

        rect.left = DistZoomX;
        rect.top = DistZoomY;
        rect.right = (rect.left + RectWidth - 1);
        rect.bottom = (rect.top + RectHeight - 1);
      }
    }
    return rect;
  }

  public Rect getFramingRect() {
    Point screenResolution = this.configManager.getCameraResolution();
    if (screenResolution == null) {
      return null;
    }
    WindowManager manager = (WindowManager) this.context
        .getSystemService("window");
    Display display = manager.getDefaultDisplay();
    Point point = new Point(display.getWidth(), display.getHeight());
    int width = 0;
    int height = 0;
    width = point.x;
    height = point.y;
    int rate_x = 0;
    int rate_y = 0;

    rate_x = (screenResolution.x << 10) / width;
    rate_y = (screenResolution.y << 10) / height;

    Rect rect = getSize(width, height, rate_x, rate_y);
    this.framingRect = new Rect();
    this.framingRect.left = rect.left;
    this.framingRect.right = rect.right;
    this.framingRect.top = rect.top;
    this.framingRect.bottom = rect.bottom;
    return this.framingRect;
  }

  public Point getScreenPoint() {
    Point screenResolution = this.configManager.getScreenResolution();
    return screenResolution;
  }

  public Rect getFramingRectInPreview() {
    Point screenResolution = this.configManager.getCameraResolution();
    int h = screenResolution.y;
    int w = screenResolution.x;
    int x1 = 0;
    int x2 = 0;
    int y1 = 0;
    int y2 = 0;
    if (h * 203 >> 7 < w) {
      int thresExt = 16;
      int DistZoomY = h / thresExt;
      int RectHeight = h - (DistZoomY << 1);
      int RectWidth = RectHeight * 203 >> 7;
      int DistZoomX = w - RectWidth >> 1;

      x1 = DistZoomX;
      y1 = DistZoomY;
      x2 = x1 + RectWidth - 1;
      y2 = y1 + RectHeight - 1;
    } else {
      int thresExt = 16;
      int DistZoomX = w / thresExt;
      int RectWidth = w - (DistZoomX << 1);
      int RectHeight = RectWidth * 81 >> 7;
      int DistZoomY = h - RectHeight >> 1;

      x1 = DistZoomX;
      y1 = DistZoomY;
      x2 = x1 + RectWidth - 1;
      y2 = y1 + RectHeight - 1;
    }
    Rect rect = new Rect();
    rect.left = x1;
    rect.right = x2;
    rect.top = y1;
    rect.bottom = y2;
    this.framingRectInPreview = rect;
    return this.framingRectInPreview;
  }

  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
                                                       int width, int height) {
    Rect rect = getFramingRectInPreview();
    int previewFormat = this.configManager.getPreviewFormat();
    String previewFormatString = this.configManager
        .getPreviewFormatString();
    switch (previewFormat) {
      case 16:
      case 17:
        return new PlanarYUVLuminanceSource(data, width, height, rect.left,
            rect.top, rect.width(), rect.height());
    }
    if ("yuv420p".equals(previewFormatString)) {
      return new PlanarYUVLuminanceSource(data, width, height, rect.left,
          rect.top, rect.width(), rect.height());
    }
    throw new IllegalArgumentException("Unsupported picture format: "
        + previewFormat + '/' + previewFormatString);
  }

  public Context getContext() {
    return this.context;
  }

  public Camera getCamera() {
    if (this.camera != null) {
      return this.camera;
    }
    return null;
  }

  @SuppressLint("NewApi")
  public void setCameraDisplayOrientation(Context context, int cameraId,
                                          android.hardware.Camera camera) {
    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    android.hardware.Camera.getCameraInfo(cameraId, info);
    WindowManager windowManager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    int rotation = windowManager.getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
    mOrientation = result;
    Log.d(TAG, "result:" + result);
  }
}
