/* 1:  */
package com.idcard;
/* 2:  */ 
/* 3:  */

import android.os.Environment;

/* 4:  */

/* 5:  */
/* 6:  */ public class GlobalData
/* 7:  */ {
  /* 8:7 */   public static String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
  /* 9:8 */   public static String LOGPath = SDCARD_ROOT_PATH + "/IDImage/slog.txt";
/* ::  */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.GlobalData

 * JD-Core Version:    0.7.0.1

 */