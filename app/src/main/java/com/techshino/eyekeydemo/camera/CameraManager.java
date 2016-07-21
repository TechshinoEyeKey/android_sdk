/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.techshino.eyekeydemo.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;

/**
 * This object wraps the Camera service object and expects to be the only one talking to it. The
 * implementation encapsulates the steps needed to take preview-sized images, which are used for
 * both preview and decoding.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraManager {

  private static final String TAG = CameraManager.class.getSimpleName();

  private static final int MIN_FRAME_WIDTH = 240;
  private static final int MIN_FRAME_HEIGHT = 240;

  private static final int MAX_FRAME_WIDTH = 960; // = 1920/2
  private static final int MAX_FRAME_HEIGHT = 540; // = 1080/2

  private static CameraManager mCameraInterface;

  private final Context context;
  private final View view;
  private final CameraConfigurationManager configManager;
  /**
   * Preview frames are delivered here, which we pass on to the registered handler. Make sure to
   * clear the handler so it will only receive one message.
   */
  private final PreviewCallback previewCallback;
  private Camera camera;
  private AutoFocusManager autoFocusManager;
  private RectF framingRect;
  private Rect framingRectInPreview;
  private boolean initialized;
  private boolean previewing;
  private int requestedFramingRectWidth;
  private int requestedFramingRectHeight;
  private int requestedCameraId = -1;
  private int mOrientation;

  private CameraManager(Context context, View view) {
    this.context = context;
    this.view = view;
    this.configManager = new CameraConfigurationManager(context, view);
    previewCallback = new PreviewCallback(configManager);
  }

  public static synchronized CameraManager newInstance(Context context, View view) {
    mCameraInterface = new CameraManager(context, view);
    return mCameraInterface;
  }

  public static CameraManager getInstance() {
    if (mCameraInterface == null)
      throw new NullPointerException("CameraManager is null!");
    return mCameraInterface;
  }

  private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
    int dim = resolution * 3 / 4; // Target 75% of each dimension
    /*if (dim < hardMin) {
      return resolution < hardMin ? resolution : hardMin;
    }
    if (dim > hardMax) {
      return hardMax;
    }*/
    return dim;
  }

  public int getOrientation() {
    return mOrientation;
  }

  /**
   * Opens the camera driver and initializes the hardware parameters.
   *
   * @param holder The surface object which the camera will draw preview frames into.
   * @throws IOException Indicates the camera driver failed to open.
   */
  public synchronized void openDriver(SurfaceHolder holder) throws IOException {
    Camera theCamera = camera;
    //orientation=getDisplayRotation(activity);
    //模拟器测试用
    //orientation=getDisplayRotation(activity)+90;
    if (theCamera == null) {

      if (requestedCameraId >= 0) {
        theCamera = GingerbreadOpenCameraInterface.open(requestedCameraId);
      } else {
        theCamera = GingerbreadOpenCameraInterface.open();
      }

      if (theCamera == null) {
        throw new IOException();
      }
      camera = theCamera;
    }
    theCamera.setPreviewDisplay(holder);

    if (!initialized) {
      initialized = true;
      configManager.initFromCameraParameters(theCamera);
      if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
        setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
        requestedFramingRectWidth = 0;
        requestedFramingRectHeight = 0;
      }
    }

    Camera.Parameters parameters = theCamera.getParameters();
    String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
    try {
      setCameraDisplayOrientation(context, requestedCameraId);
      configManager.setDesiredCameraParameters(theCamera, false);
    } catch (RuntimeException re) {
      // Driver failed
      Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
      Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
      // Reset:
      if (parametersFlattened != null) {
        parameters = theCamera.getParameters();
        parameters.unflatten(parametersFlattened);
        try {
          theCamera.setParameters(parameters);
          configManager.setDesiredCameraParameters(theCamera, true);
        } catch (RuntimeException re2) {
          // Well, darn. Give up
          Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
        }
      }
    }

  }

  public synchronized boolean isOpen() {
    return camera != null;
  }

  /**
   * Closes the camera driver if still in use.
   */
  public synchronized void closeDriver() {
    if (camera != null) {
      camera.setPreviewCallback(null);
      camera.stopPreview();
      camera.release();
      camera = null;
      // Make sure to clear these each time we close the camera, so that any scanning rect
      // requested by intent is forgotten.
      framingRect = null;
      framingRectInPreview = null;
    }
  }

  /**
   * Asks the camera hardware to begin drawing preview frames to the screen.
   */
  public synchronized void startPreview() {
    Camera theCamera = camera;
    if (theCamera != null && !previewing) {
      theCamera.startPreview();
      previewing = true;
      autoFocusManager = new AutoFocusManager(context, camera);
    }
  }

  /**
   * Tells the camera to stop drawing preview frames.
   */
  public synchronized void stopPreview() {
    if (autoFocusManager != null) {
      autoFocusManager.stop();
      autoFocusManager = null;
    }
    if (camera != null && previewing) {
      camera.stopPreview();
      previewCallback.setHandler(null, 0);
      previewing = false;
    }
  }

  /**
   * Convenience method for {@link com.techshino.phoneface.ui.fragment.CameraFragment}
   */
  public synchronized void setTorch(boolean newSetting) {
    if (newSetting != configManager.getTorchState(camera)) {
      if (camera != null) {
        if (autoFocusManager != null) {
          autoFocusManager.stop();
        }
        configManager.setTorch(camera, newSetting);
        if (autoFocusManager != null) {
          autoFocusManager.start();
        }
      }
    }
  }

  /**
   * A single preview frame will be returned to the handler supplied. The data will arrive as byte[]
   * in the message.obj field, with width and height encoded as message.arg1 and message.arg2,
   * respectively.
   *
   * @param handler The handler to send the message to.
   * @param message The what field of the message to be sent.
   */
  public synchronized void requestPreviewFrame(Handler handler, int message) {
    Camera theCamera = camera;
    if (theCamera != null && previewing) {
      previewCallback.setHandler(handler, message);
      theCamera.setOneShotPreviewCallback(previewCallback);
    }
  }

  /**
   * Calculates the framing rect which the UI should draw to show the user where to place the
   * barcode. This target helps with alignment as well as forces the user to hold the device
   * far enough away to ensure the image will be in focus.
   *
   * @return The rectangle to draw on screen in window coordinates.
   */
  public synchronized RectF getFramingRect() {
    if (framingRect == null) {
      if (camera == null) {
        return null;
      }
//    Point screenResolution = configManager.getScreenResolution();
      Point screenResolution = configManager.getViewResolution();
      Log.d(TAG, "打印screenResolution : " + screenResolution);
      if (screenResolution == null) {
        // Called early, before init even finished
        return null;
      }

      int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
      int height = findDesiredDimensionInRange(screenResolution.y, MAX_FRAME_HEIGHT, MIN_FRAME_HEIGHT);

      int leftOffset = (screenResolution.x - width) / 2;
      int topOffset = (screenResolution.y - height) / 2;

      framingRect = new RectF(leftOffset, topOffset, leftOffset + width, topOffset + height);
      Log.d(TAG, "Calculated framing rect: " + framingRect);
    }

    return framingRect;
  }

  /**
   * Like {@link #getFramingRect} 锁定相机尺寸，显示分辨率为75% 然后映射到界面
   * not UI / screen.
   */
  public synchronized Rect getFramingRectInPreview() {
    if (framingRectInPreview == null) {
            /*Rect framingRect = getFramingRect();
            if (framingRect == null) {
				return null;
			}
			Rect rect = new Rect(framingRect);
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getViewResolution();

			if (cameraResolution == null || screenResolution == null) {
				// Called early, before init even finished
				return null;
			}

			 * 通过屏幕尺寸判断摄像头横竖屏 screenResolution.x>screenResolution.y 横屏 640 480
			 * screenResolution.x<screenResolution.y 竖屏 480 640

			Boolean isLandscape = (screenResolution.x > screenResolution.y);
			if (isLandscape) {

				 * rect.left = rect.left * cameraResolution.x /
				 * screenResolution.x; rect.right = rect.right *
				 * cameraResolution.x / screenResolution.x; rect.top = rect.top
				 * * cameraResolution.y / screenResolution.y; rect.bottom =
				 * rect.bottom * cameraResolution.y / screenResolution.y;

			} else {

				 * 将宽高进行变换，

				rect.left = rect.top;
				rect.top = rect.left;

				rect.right = rect.bottom;
				rect.bottom = rect.right;
			}*/
      if (camera == null) {
        return null;
      }
      Point cameraResolution = configManager.getCameraResolution();
      Log.d(TAG, "打印cameraResolution : " + cameraResolution);
      if (cameraResolution == null) {
        // Called early, before init even finished
        return null;
      }

      int width = findDesiredDimensionInRange(cameraResolution.x, 320, 640);
      int height = findDesiredDimensionInRange(cameraResolution.y, 240, 480);

      int leftOffset = (cameraResolution.x - width) / 2;
      int topOffset = (cameraResolution.y - height) / 2;

      framingRectInPreview = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
      Log.d(TAG, "Calculated framingRectInPreview rect: " + framingRectInPreview);
    }
    return framingRectInPreview;
  }

  /**
   * Allows third party apps to specify the scanning rectangle dimensions, rather than determine
   * them automatically based on screen resolution.
   *
   * @param width  The width in pixels to scan.
   * @param height The height in pixels to scan.
   */
  public synchronized void setManualFramingRect(int width, int height) {
    if (initialized) {
      Point screenResolution = configManager.getScreenResolution();
      if (width > screenResolution.x) {
        width = screenResolution.x;
      }
      if (height > screenResolution.y) {
        height = screenResolution.y;
      }
      int leftOffset = (screenResolution.x - width) / 2;
      int topOffset = (screenResolution.y - height) / 2;
      framingRect = new RectF(leftOffset, topOffset, leftOffset + width, topOffset + height);
      Log.d(TAG, "Calculated manual framing rect: " + framingRect);
      framingRectInPreview = null;
    } else {
      requestedFramingRectWidth = width;
      requestedFramingRectHeight = height;
    }
  }

  /**
   * Allows third party apps to specify the camera ID, rather than determine
   * it automatically based on available cameras and their orientation.
   *
   * @param cameraId camera ID of the camera to use. A negative value means
   *                 "no preference".
   */
  public synchronized void setManualCameraId(int cameraId) {
    requestedCameraId = cameraId;
  }

  public void setCameraDisplayOrientation(Context context,
                                          int cameraId) {
    Camera.CameraInfo info =
        new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    int rotation = windowManager.getDefaultDisplay()
        .getRotation();
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
      result = (360 - result) % 360;  // compensate the mirror
    } else {  // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
    mOrientation = result;
  }
}
