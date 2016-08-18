/*  1:   */
package com.turui.bank.ocr;
/*  2:   */ 
/*  3:   */

import android.app.Activity;
import android.content.DialogInterface;

/*  4:   */
/*  5:   */
/*  6:   */

/*  7:   */
/*  8:   */ public final class FinishListener
/*  9:   */ implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable
/* 10:   */ {
  /* 11:   */   private final Activity activityToFinish;

  /* 12:   */
/* 13:   */
  public FinishListener(Activity activityToFinish)
/* 14:   */ {
/* 15:32 */
    this.activityToFinish = activityToFinish;
/* 16:   */
  }

  /* 17:   */
/* 18:   */
  public void onCancel(DialogInterface dialogInterface)
/* 19:   */ {
/* 20:36 */
    run();
/* 21:   */
  }

  /* 22:   */
/* 23:   */
  public void onClick(DialogInterface dialogInterface, int i)
/* 24:   */ {
/* 25:40 */
    run();
/* 26:   */
  }

  /* 27:   */
/* 28:   */
  public void run()
/* 29:   */ {
/* 30:44 */
    this.activityToFinish.finish();
/* 31:   */
  }
/* 32:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.FinishListener

 * JD-Core Version:    0.7.0.1

 */