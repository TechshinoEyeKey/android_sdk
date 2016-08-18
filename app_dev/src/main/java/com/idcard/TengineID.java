/*  1:   */
package com.idcard;

/*  2:   */
/*  3:   */ public enum TengineID
/*  4:   */ {
  /*  5: 9 */   TUNCERTAIN(
/*  6:10 */     0), TIDCARD2(
/*  7:13 */     17), TIDBANK(
/*  8:16 */     21), TIDLPR(
/*  9:19 */     22), TIDJSZCARD(
/* 10:22 */     23), TIDXSZCARD(
/* 11:25 */     24);
  /* 12:   */
/* 13:   */   public int nValue;

  /* 14:   */
/* 15:   */
  private TengineID(int nValue)
/* 16:   */ {
/* 17:30 */
    this.nValue = nValue;
/* 18:   */
  }

  /* 19:   */
/* 20:   */
  public static TengineID valueOf(int nValue)
/* 21:   */ {
/* 22:41 */
    TengineID[] values = values();
/* 23:42 */
    TengineID ret = null;
/* 24:43 */
    for (TengineID status : values) {
/* 25:44 */
      if (status.nValue == nValue)
/* 26:   */ {
/* 27:45 */
        ret = status;
/* 28:46 */
        break;
/* 29:   */
      }
/* 30:   */
    }
/* 31:49 */
    return ret;
/* 32:   */
  }
/* 33:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TengineID

 * JD-Core Version:    0.7.0.1

 */