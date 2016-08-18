/*  1:   */
package com.idcard;

/*  2:   */
/*  3:   */ public enum TStatus
/*  4:   */ {
  /*  5: 9 */   TR_FAIL(
/*  6:   */   
/*  7:   */ 
/*  8:12 */     0), TR_OK(1), TR_TIME_OUT(
/*  9:   */   
/* 10:18 */     100);
  /* 11:   */
/* 12:   */   public int nValue;

  /* 13:   */
/* 14:   */
  private TStatus(int nValue)
/* 15:   */ {
/* 16:23 */
    this.nValue = nValue;
/* 17:   */
  }

  /* 18:   */
/* 19:   */
  public static TStatus valueOf(int nValue)
/* 20:   */ {
/* 21:29 */
    TStatus[] values = values();
/* 22:30 */
    TStatus ret = null;
/* 23:31 */
    for (TStatus status : values) {
/* 24:32 */
      if (status.nValue == nValue)
/* 25:   */ {
/* 26:33 */
        ret = status;
/* 27:34 */
        break;
/* 28:   */
      }
/* 29:   */
    }
/* 30:37 */
    return ret;
/* 31:   */
  }
/* 32:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TStatus

 * JD-Core Version:    0.7.0.1

 */