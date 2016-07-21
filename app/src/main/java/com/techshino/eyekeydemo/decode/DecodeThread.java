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

package com.techshino.eyekeydemo.decode;

import android.os.Handler;
import android.os.Looper;

import com.techshino.eyekeydemo.view.CameraSurfaceView;

import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class DecodeThread extends Thread {

  private final CameraSurfaceView mCameraSurfaceView;
  private final CountDownLatch handlerInitLatch;
  private Handler handler;

  public DecodeThread(CameraSurfaceView cameraSurfaceView) {
    this.mCameraSurfaceView = cameraSurfaceView;
    handlerInitLatch = new CountDownLatch(1);
  }

  public Handler getHandler() {
    try {
      handlerInitLatch.await();
    } catch (InterruptedException ie) {
      // continue?
    }
    return handler;
  }

  @Override
  public void run() {
    Looper.prepare();
    handler = new DecodeHandler(mCameraSurfaceView);
    handlerInitLatch.countDown();
    Looper.loop();
  }
}
