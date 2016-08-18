package com.turui.bank.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.List;
import java.util.regex.Pattern;

public final class CameraConfigurationManager {
  private static final String TAG = CameraConfigurationManager.class
      .getSimpleName();
  private static final int TEN_DESIRED_ZOOM = 27;
  private static final int DESIRED_SHARPNESS = 30;
  private static final Pattern COMMA_PATTERN = Pattern.compile(",");
  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  private int previewFormat;
  private String previewFormatString;

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  private static Point getCameraResolution(Camera.Parameters parameters,
                                           Point screenResolution) {
    String previewSizeValueString = parameters.get("preview-size-values");
    if (previewSizeValueString == null) {
      previewSizeValueString = parameters.get("preview-size-value");
    }
    Point cameraResolution = null;
    if (previewSizeValueString != null) {
      Log.d(TAG, "preview-size-values parameter: "
          + previewSizeValueString);
      cameraResolution = findBestPreviewSizeValue(previewSizeValueString,
          screenResolution);
    }
    if (cameraResolution == null) {
      cameraResolution = new Point(screenResolution.x >> 3 << 3,
          screenResolution.y >> 3 << 3);
    }
    return cameraResolution;
  }

  private static Point findBestPreviewSizeValue(
      CharSequence previewSizeValueString, Point screenResolution) {
    int bestX = 0;
    int bestY = 0;
    int diff = 2147483647;
    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {
      previewSize = previewSize.trim();
      int dimPosition = previewSize.indexOf('x');
      if (dimPosition < 0) {
        Log.w(TAG, "Bad preview-size: " + previewSize);
      } else {
        try {
          int newX = Integer.parseInt(previewSize.substring(0,
              dimPosition));
//					newY = Integer.parseInt(previewSize
//							.substring(dimPosition + 1));
        } catch (NumberFormatException nfe) {
          int newY;
          Log.w(TAG, "Bad preview-size: " + previewSize);
          continue;
        }
        int newY = 0;
        int newX = 0;
        int newDiff = Math.abs(newX - screenResolution.x)
            + Math.abs(newY - screenResolution.y);
        if (newDiff == 0) {
          bestX = newX;
          bestY = newY;
          break;
        }
        if (newDiff < diff) {
          bestX = newX;
          bestY = newY;
          diff = newDiff;
        }
      }
    }
    if ((bestX > 0) && (bestY > 0)) {
      return new Point(bestX, bestY);
    }
    return null;
  }

  private static int findBestMotZoomValue(CharSequence stringValues,
                                          int tenDesiredZoom) {
    int tenBestValue = 0;
    for (String stringValue : COMMA_PATTERN.split(stringValues)) {
      stringValue = stringValue.trim();
      try {
//				value = Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        double value;
        return tenDesiredZoom;
      }
      double value = 0.0;
      int tenValue = (int) (10.0D * value);
      if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
          - tenBestValue)) {
        tenBestValue = tenValue;
      }
    }
    return tenBestValue;
  }

  public static int getDesiredSharpness() {
    return 30;
  }

  private Camera.Size getSizeForPreviewSize(Camera.Parameters parameters) {
    List<Camera.Size> ViewSize = parameters.getSupportedPreviewSizes();
    for (int j = ViewSize.size() - 1; j >= 0; j--) {
      for (int i = 0; i < j; i++) {
        if (((Camera.Size) ViewSize.get(i)).width > ((Camera.Size) ViewSize
            .get(i + 1)).width) {
          Camera.Size tempSize = (Camera.Size) ViewSize.get(i + 1);
          ViewSize.set(i + 1, (Camera.Size) ViewSize.get(i));
          ViewSize.set(i, tempSize);
        }
      }
    }
    for (int i = ViewSize.size() - 1; i >= 0; i--) {
      if (((Camera.Size) ViewSize.get(i)).width <= 640) {
        return (Camera.Size) ViewSize.get(i);
      }
    }
    return null;
  }

  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    this.previewFormat = parameters.getPreviewFormat();
    this.previewFormatString = parameters.get("preview-format");
    Log.d(TAG, "Default preview format: " + this.previewFormat + '/'
        + this.previewFormatString);
    WindowManager manager = (WindowManager) this.context
        .getSystemService("window");
    Display display = manager.getDefaultDisplay();
    this.screenResolution = new Point(display.getWidth(),
        display.getHeight());
    Log.d(TAG, "Screen resolution: " + this.screenResolution);
    Camera.Size size = getSizeForPreviewSize(camera.getParameters());
    if (size != null) {
      this.cameraResolution = new Point();
      this.cameraResolution.x = size.width;
      this.cameraResolution.y = size.height;
    } else {
      this.cameraResolution = getCameraResolution(parameters,
          this.screenResolution);
    }
    Log.d(TAG, "Camera resolution: " + this.screenResolution);
  }

  void setDesiredCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    Log.d(TAG, "Setting preview size: " + this.cameraResolution);
    parameters.setPreviewSize(this.cameraResolution.x,
        this.cameraResolution.y);
    setFlash(parameters);
    setZoom(parameters);

    parameters.set("rotation", 0);
//		camera.setDisplayOrientation(0);
    setCameraDisplayOrientation(context, 0, camera);
    camera.setParameters(parameters);
  }

  Point getCameraResolution() {
    return this.cameraResolution;
  }

  Point getScreenResolution() {
    return this.screenResolution;
  }

  int getPreviewFormat() {
    return this.previewFormat;
  }

  String getPreviewFormatString() {
    return this.previewFormatString;
  }

  private void setFlash(Camera.Parameters parameters) {
    if ((Build.MODEL.contains("Behold II")) && (CameraManager.SDK_INT == 3)) {
      parameters.set("flash-value", 1);
    } else {
      parameters.set("flash-value", 2);
    }
    parameters.set("flash-mode", "off");
  }

  private void setZoom(Camera.Parameters parameters) {
    String zoomSupportedString = parameters.get("zoom-supported");
    if ((zoomSupportedString != null)
        && (!Boolean.parseBoolean(zoomSupportedString))) {
      return;
    }
    int tenDesiredZoom = 27;

    String maxZoomString = parameters.get("max-zoom");
    if (maxZoomString != null) {
      try {
        int tenMaxZoom = (int) (10.0D * Double
            .parseDouble(maxZoomString));
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad max-zoom: " + maxZoomString);
      }
    }
    String takingPictureZoomMaxString = parameters
        .get("taking-picture-zoom-max");
    if (takingPictureZoomMaxString != null) {
      try {
        int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
        Log.w(TAG, "Bad taking-picture-zoom-max: "
            + takingPictureZoomMaxString);
      }
    }
    String motZoomValuesString = parameters.get("mot-zoom-values");
    if (motZoomValuesString != null) {
      tenDesiredZoom = findBestMotZoomValue(motZoomValuesString,
          tenDesiredZoom);
    }
    String motZoomStepString = parameters.get("mot-zoom-step");
    if (motZoomStepString != null) {
      try {
        double motZoomStep = Double.parseDouble(motZoomStepString
            .trim());
        int tenZoomStep = (int) (10.0D * motZoomStep);
        if (tenZoomStep > 1) {
          tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
        }
      } catch (NumberFormatException localNumberFormatException1) {
      }
    }
    if ((maxZoomString != null) || (motZoomValuesString != null)) {
      parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0D));
    }
    if (takingPictureZoomMaxString != null) {
      parameters.set("taking-picture-zoom", tenDesiredZoom);
    }
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

    Log.d(TAG, "result:" + result);
  }
}
