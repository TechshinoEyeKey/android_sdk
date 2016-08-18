/*  1:   */
package com.idcard;

/*  2:   */
/*  3:   */ public enum BankTypeID
/*  4:   */ {
  /*  5: 9 */   T_GET_BANK_NUM(
/*  6:10 */     1), T_GET_BANK_NAME(
/*  7:12 */     2), T_GET_BANK_OrganizeCode(
/*  8:14 */     3), T_GET_BANK_CardClass(
/*  9:16 */     4), T_GET_CARD_NAME(
/* 10:18 */     5);
  /* 11:   */
/* 12:   */   public int nValue;

  /* 13:   */
/* 14:   */
  private BankTypeID(int nValue)
/* 15:   */ {
/* 16:23 */
    this.nValue = nValue;
/* 17:   */
  }

  /* 18:   */
/* 19:   */
  public static BankTypeID valueOf(int nValue)
/* 20:   */ {
/* 21:34 */
    BankTypeID[] values = values();
/* 22:35 */
    BankTypeID ret = null;
/* 23:36 */
    for (BankTypeID status : values) {
/* 24:37 */
      if (status.nValue == nValue)
/* 25:   */ {
/* 26:38 */
        ret = status;
/* 27:39 */
        break;
/* 28:   */
      }
/* 29:   */
    }
/* 30:42 */
    return ret;
/* 31:   */
  }
/* 32:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.BankTypeID

 * JD-Core Version:    0.7.0.1

 */