/*  1:   */
package com.idcard;

/*  2:   */
/*  3:   */ public enum TParam
/*  4:   */ {
  /*  5:10 */   T_ONLY_CARD_NUM(
/*  6:11 */     1), T_SET_HEADIMG(
/*  7:14 */     2), T_SET_LOGPATH(
/*  8:   */   
/*  9:   */ 
/* 10:   */ 
/* 11:20 */     4), T_SET_HEADIMGBUFMODE(
/* 12:   */   
/* 13:   */ 
/* 14:   */ 
/* 15:26 */     6), T_SET_NDCORRECTION(
/* 16:29 */     7), T_SET_BANK_RECMODE(
/* 17:32 */     8), T_SET_BANK_AREA_LEFT(
/* 18:35 */     9), T_SET_BANK_AREA_TOP(
/* 19:38 */     16), T_SET_BANK_AREA_WIDTH(
/* 20:41 */     17), T_SET_BANK_AREA_HEIGHT(
/* 21:44 */     18), T_SET_BANK_LINE_STREAM(
/* 22:47 */     19);
  /* 23:   */
/* 24:   */   public int nValue;

  /* 25:   */
/* 26:   */
  private TParam(int nValue)
/* 27:   */ {
/* 28:51 */
    this.nValue = nValue;
/* 29:   */
  }

  /* 30:   */
/* 31:   */
  public static TParam valueOf(int nValue)
/* 32:   */ {
/* 33:62 */
    TParam[] values = values();
/* 34:63 */
    TParam ret = null;
/* 35:64 */
    for (TParam status : values) {
/* 36:65 */
      if (status.nValue == nValue)
/* 37:   */ {
/* 38:66 */
        ret = status;
/* 39:67 */
        break;
/* 40:   */
      }
/* 41:   */
    }
/* 42:70 */
    return ret;
/* 43:   */
  }
/* 44:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TParam

 * JD-Core Version:    0.7.0.1

 */