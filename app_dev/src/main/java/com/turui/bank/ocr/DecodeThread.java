/*  1:   */
package com.turui.bank.ocr;
/*  2:   */ 
/*  3:   */

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/*  4:   */
/*  5:   */

/*  6:   */
/*  7:   */ final class DecodeThread
/*  8:   */ extends Thread
/*  9:   */ {
  /* 10:   */   public static final String BARCODE_BITMAP = "image";
  /* 11:   */   private final CaptureActivity activity;
  /* 13:   */   private final CountDownLatch handlerInitLatch;
  /* 12:   */   private Handler handler;

  /* 14:   */
/* 15:   */   DecodeThread(CaptureActivity activity, String characterSet)
/* 16:   */ {
/* 17:40 */
    this.activity = activity;
/* 18:41 */
    this.handlerInitLatch = new CountDownLatch(1);
/* 19:   */
  }

  /* 20:   */
/* 21:   */   Handler getHandler()
/* 22:   */ {
/* 23:   */
    try
/* 24:   */ {
/* 25:47 */
      this.handlerInitLatch.await();
/* 26:   */
    }
/* 27:   */ catch (InterruptedException localInterruptedException) {
    }
/* 28:51 */
    return this.handler;
/* 29:   */
  }

  /* 30:   */
/* 31:   */
  public void run()
/* 32:   */ {
/* 33:56 */
    Looper.prepare();
/* 34:57 */
    this.handler = new DecodeHandler(this.activity);
/* 35:58 */
    this.handlerInitLatch.countDown();
/* 36:59 */
    Looper.loop();
/* 37:   */
  }
/* 38:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.DecodeThread

 * JD-Core Version:    0.7.0.1

 */