/*
 
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

package com.techshino.eyekeydemo.decode;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.techshino.eyekeydemo.utils.CustomUtil;
import com.techshino.eyekeydemo.utils.MessageManager;
import com.techshino.eyekeydemo.view.CameraSurfaceView;
import com.techshino.eyekeydemo.view.CameraSurfaceView.FaceCallback;

public final class DecodeHandler extends Handler implements IConstants {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final CameraSurfaceView mCameraSurfaceView;
    private FaceCallback mFaceCallback;

    private boolean running = true;

    // 超时30秒后返回,主要用于捕捉时超时

    Handler handler = null;
    Bitmap[] mBitmaps;

    DecodeHandler(CameraSurfaceView cameraSurfaceView) {
        this.mCameraSurfaceView = cameraSurfaceView;
        this.mFaceCallback = mCameraSurfaceView.getFaceCallback();
        handler = cameraSurfaceView.getHandler();
        mBitmaps = new Bitmap[3];
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case DECODE:
                decodeArray((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case QUIT:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    int index = 0;

    private void decodeArray(byte[] data, int width, int height) {
        Log.i(TAG, "width:" + width + " height:" + height);
        Bitmap bitmap = CustomUtil.getInstance(mCameraSurfaceView.getContext()).collectBitmap(data, width, height,
                mCameraSurfaceView.getCameraManager().getOrientation(), mCameraSurfaceView.getCameraId());
        if (bitmap == null)
            return;
        mBitmaps[index] = bitmap;

        index++;
        if (index < 3) {
            MessageManager.sendToTarget(handler, RESTART_PREVIEW);
        } else {
            index = 0;
            MessageManager.sendToTarget(handler, ON_RESULLT);
            if (mFaceCallback != null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFaceCallback.onResullt(mBitmaps);
                    }
                });
        }
    }
}
