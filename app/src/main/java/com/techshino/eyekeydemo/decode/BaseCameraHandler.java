package com.techshino.eyekeydemo.decode;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.techshino.eyekeydemo.camera.CameraManager;
import com.techshino.eyekeydemo.utils.MessageManager;
import com.techshino.eyekeydemo.view.CameraSurfaceView;


/**
 * Fragment接收message并处理的handler
 *
 * @author James
 *         Created by James on 10/15/13
 */
public class BaseCameraHandler extends Handler implements IConstants {

    private static final String TAG = BaseCameraHandler.class.getSimpleName();

    private DecodeThread decodeThread; //用于识别的真正线程
    private final CameraManager cameraManager; //摄像头管理器
    public final CameraSurfaceView mSurfaceView; //摄像头碎片的引用

    private Handler mHandler;
    private State state;  //当前状态 枚举类型
    Bitmap[] tempBitmap;

    /**
     * @author James
     *         枚举类型的状态值，
     *         预览，成功，完成
     */
    private enum State {
        PREVIEW, SUCCESS, DONE, PAUSE, ERROR
    }

    /**
     * 默认构造，只允许包内调用
     *
     * @param fragment
     * @param mQueue
     * @param cameraManager
     */
    public BaseCameraHandler(CameraSurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
        this.cameraManager = mSurfaceView.getCameraManager();
        //传入fragment和显示回调

        state = State.SUCCESS;//将状态置为success
        mHandler = this;

    }

    /**
     * 启动解析线程并开始拍照
     */
    public void startDecodeThread() {
        cameraManager.startPreview();//这里才开始启动摄像头，并显示内容
        decodeThread = new DecodeThread(mSurfaceView);
        decodeThread.start();
    }

    public void startCaptrue() {
        state = State.SUCCESS;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {

        switch (message.what) {
            case RESTART_PREVIEW:
                state = State.SUCCESS;
                restartPreviewAndDecode();
                break;
            case ON_RESULLT:
                pauseCamera();
                break;
            default:
                Log.v(TAG, "Unknown message: " + message.what);
                break;
        }
    }

    /**
     * 停止线程并退出
     */
    public void quitSynchronously() {
        Log.d(TAG, "停止线程并退出");
        state = State.DONE;
        cameraManager.stopPreview();
        removeMessages(RESTART_PREVIEW);

        if (decodeThread == null)
            return;
        MessageManager.sendToTarget(decodeThread.getHandler(), QUIT);
        try {
            decodeThread.join(500L);//等待500毫秒，然后将线程停止
        } catch (InterruptedException e) {
            // continue
        }
    }

    /**
     * 暂停摄像头并阻止消息，不停止线程
     */
    public void pauseCamera() {
        Log.d(TAG, "暂停摄像头并阻止消息");
        state = State.PAUSE;
        cameraManager.stopPreview();
        removeMessages(RESTART_PREVIEW);
    }

    /**
     * 重启摄像头并开始寻找人脸
     */
    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), DECODE);
        }
    }
}
