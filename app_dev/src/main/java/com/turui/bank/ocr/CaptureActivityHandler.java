/*   1:    */
package com.turui.bank.ocr;
/*   2:    */ 
/*   3:    */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */

/*   9:    */
/*  10:    */ public final class CaptureActivityHandler
/*  11:    */ extends Handler
/*  12:    */ {
  /*  13: 18 */   private static final String TAG = CaptureActivityHandler.class.getSimpleName();
  /*  14:    */   private final CaptureActivity activity;
  /*  15:    */   private final DecodeThread decodeThread;
  /*  16:    */   private State state;

  /*  22:    */
/*  23:    */
  public CaptureActivityHandler(CaptureActivity activity, String characterSet)
/*  24:    */ {
/*  25: 32 */
    this.activity = activity;
/*  26: 33 */
    this.decodeThread = new DecodeThread(activity, characterSet);
/*  27: 34 */
    this.decodeThread.start();
/*  28: 35 */
    this.state = State.SUCCESS;
/*  29:    */
/*  30: 37 */
    CameraManager.get().startPreview();
/*  31: 38 */
    restartPreviewAndDecode();
/*  32:    */
  }

  /*  33:    */
/*  34:    */
  public void handleMessage(Message message)
/*  35:    */ {
/*  36: 43 */
    switch (message.what)
/*  37:    */ {
/*  38:    */
      case 0:
/*  39: 48 */
        if (this.state == State.PREVIEW) {
/*  40: 49 */
          CameraManager.get().requestAutoFocus(this, 0);
/*  41:    */
        }
/*  42: 51 */
        break;
/*  43:    */
      case 8:
/*  44: 53 */
        Log.d(TAG, "Got restart preview message");
/*  45: 54 */
        restartPreviewAndDecode();
/*  46: 55 */
        break;
/*  47:    */
      case 3:
/*  48: 57 */
        Log.d(TAG, "Got decode succeeded message");
/*  49: 58 */
        this.state = State.SUCCESS;
/*  50: 59 */
        Bundle bundle = message.getData();
/*  51:    */
/*  52:    */
/*  53:    */
/*  54:    */
/*  55:    */
/*  56: 65 */
        this.activity.handleDecode((String) message.obj, null);
/*  57: 66 */
        break;
/*  58:    */
      case 2:
/*  59: 69 */
        this.state = State.PREVIEW;
/*  60: 70 */
        CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), 1);
/*  61: 71 */
        break;
/*  62:    */
      case 9:
/*  63: 73 */
        Log.d(TAG, "Got return scan result message");
/*  64: 74 */
        this.activity.setResult(-1, (Intent) message.obj);
/*  65: 75 */
        this.activity.finish();
/*  66: 76 */
        break;
/*  67:    */
      case 6:
/*  68: 78 */
        Log.d(TAG, "Got product query message");
/*  69: 79 */
        String url = (String) message.obj;
/*  70: 80 */
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
/*  71: 81 */
        intent.addFlags(524288);
/*  72: 82 */
        this.activity.startActivity(intent);
/*  73: 83 */
        break;
/*  74:    */
      case 12:
/*  75: 85 */
        this.activity.drawViewfinder();
/*  76:    */
    }
/*  77:    */
  }

  /*  78:    */
/*  79:    */
  public void quitSynchronously()
/*  80:    */ {
/*  81: 91 */
    this.state = State.DONE;
/*  82: 92 */
    CameraManager.get().stopPreview();
/*  83: 93 */
    Message quit = Message.obtain(this.decodeThread.getHandler(), 7);
/*  84: 94 */
    quit.sendToTarget();
/*  85:    */
    try
/*  86:    */ {
/*  87: 96 */
      this.decodeThread.join();
/*  88:    */
    }
/*  89:    */ catch (InterruptedException localInterruptedException) {
    }
/*  90:102 */
    removeMessages(3);
/*  91:103 */
    removeMessages(2);
/*  92:    */
  }

  /*  93:    */
/*  94:    */
  private void restartPreviewAndDecode()
/*  95:    */ {
/*  96:107 */
    if (this.state == State.SUCCESS)
/*  97:    */ {
/*  98:108 */
      this.state = State.PREVIEW;
/*  99:109 */
      CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), 1);
/* 100:110 */
      CameraManager.get().requestAutoFocus(this, 0);
/* 101:111 */
      this.activity.drawViewfinder();
/* 102:    */
    }
/* 103:    */
  }

  /*  17:    */
/*  18:    */   private static enum State
/*  19:    */ {
    /*  20: 25 */     PREVIEW, SUCCESS, DONE;
/*  21:    */
  }
/* 104:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.CaptureActivityHandler

 * JD-Core Version:    0.7.0.1

 */