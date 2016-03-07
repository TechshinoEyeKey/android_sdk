package com.techshino.eyekeydemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout.LayoutParams;

import com.techshino.eyekeydemo.camera.CameraManager;
import com.techshino.eyekeydemo.decode.BaseCameraHandler;
import com.techshino.eyekeydemo.decode.IConstants;

import java.io.IOException;

/**
 * 拍照预览SurfaceView
 *
 * @author Xiaozhi
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, IConstants {

    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private boolean hasSurface; // 是否存在摄像头显示层
    private boolean isPortrait = true;
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private CameraManager mCameraManager;
    private BaseCameraHandler handler; // 这个是解码的回调句柄
    private int mCameraId = 1;

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        mCameraManager = CameraManager.newInstance(context, this);
        mCameraManager.setManualCameraId(mCameraId);
        initOrientation();
    }

    public void setCameraId(int cameraId) {
        mCameraId = cameraId;
        mCameraManager.setManualCameraId(mCameraId);
    }

    public int getCameraId() {
        return mCameraId;
    }

    private void initOrientation() {
    }

    public void setIsPortrait(boolean isPortrait) {
        this.isPortrait = isPortrait;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "获取显示区域参数");
        int desiredWidth = 480;
        int desiredHeight = 640;
        /**
         * 每个MeasureSpec均包含两种数据，尺寸和设定类型，需要通过 MeasureSpec.getMode和getSize进行提取
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 参考值竖屏 800 1214
        // 参考值横屏 1280 734
        int width;
        int height;
        LayoutParams params;// 子布局的参数
        // 竖屏
        if (isPortrait) {
            // 测量宽度
            if (widthMode == MeasureSpec.EXACTLY) {
                // 精确值情况
                width = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                // 范围值情况，哪个小取哪个？
                width = Math.min(desiredWidth, widthSize);
            } else {
                // 没设定就是默认的了，呵呵
                width = desiredWidth;
            }

            // 高度设定同上
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                // height = Math.min(desiredHeight, heightSize);
                height = Math.min(desiredWidth, widthSize) * 4 / 3;
            } else {
                height = desiredHeight;
            }
            Log.i(TAG, "显示表明摄像头为竖屏");
            // 竖屏，宽度匹配，高度为宽度缩放
            //params = new LayoutParams((int) (width), (int) (height));

        } else {
            desiredWidth = 640;
            desiredHeight = 480;
            // 测量宽度
            if (widthMode == MeasureSpec.EXACTLY) {
                // 精确值情况
                width = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                // 范围值情况，哪个小取哪个？
                // width = Math.min(desiredWidth, widthSize);
                width = Math.min(desiredHeight, heightSize) * 4 / 3;
            } else {
                // 没设定就是默认的了，呵呵
                width = desiredWidth;
            }

            // 高度设定同上
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desiredHeight, heightSize);
            } else {
                height = desiredHeight;
            }
            // 横屏，高度匹配，宽度包裹
            //params = new LayoutParams((int) (width), (int) (height));
        }
        //viewfinderView.setLayoutParams(params);
        //surfaceView.setLayoutParams(params);
        setMeasuredDimension(width, height);
        Log.e(TAG, "CSV设定宽度:" + widthSize + "  设定高度:" + heightSize);// 让我们来输出他们
        Log.e(TAG, "CSV实际宽度:" + width + "  实际高度:" + height);// 让我们来输出他们
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface && holder != null) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeDriver();
        hasSurface = false;
    }

    public void onResume() {
        handler = null;// 清空handler
        if (hasSurface) {
            // 当activity暂停，但是并未停止的时候，surface仍然存在，所以 surfaceCreated()
            // 并不会调用，需要在此处初始化摄像头
            initCamera(mSurfaceHolder);
        } else {
            // 设置回调，等待 surfaceCreated() 初始化摄像头
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        mCameraManager.closeDriver();
        if (!hasSurface) {
            mSurfaceHolder.removeCallback(this);
        }
    }


    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    /**
     * 获取消息句柄
     *
     * @return
     */
    public BaseCameraHandler getHandler() {
        return handler;
    }

    public void startCapture() {
        if (handler == null)
            return;
        handler.startCaptrue();
    }

    /**
     * 初始化摄像头，较为关键的内容
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("SurfaceHolder is null");
        }
        if (mCameraManager.isOpen()) {
            Log.w(TAG, "Camera is opened！");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new BaseCameraHandler(this);
                handler.startDecodeThread();
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            Log.w(TAG, "摄像头初始化失败", e);
        }
    }

    public interface FaceCallback {
        void onResullt(Bitmap[] bitmaps);
    }

    private FaceCallback mFaceCallback;

    public void setFaceCallback(FaceCallback faceCallback) {
        mFaceCallback = faceCallback;
    }

    public FaceCallback getFaceCallback() {
        return mFaceCallback;
    }
}
