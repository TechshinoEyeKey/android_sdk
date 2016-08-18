/*   1:    */
package com.idcard;

/*   2:    */
/*   3:    */ public enum TFieldID
/*   4:    */ {
  /*   5: 10 */   LPRNUM(
/*   6:    */   
/*   7: 12 */     5), NAME(
/*   8:    */   
/*   9:    */ 
/*  10: 16 */     0), SEX(
/*  11: 19 */     1), FOLK(
/*  12: 22 */     2), BIRTHDAY(
/*  13: 25 */     3), ADDRESS(
/*  14: 28 */     4), NUM(
/*  15: 31 */     5), ISSUE(
/*  16: 34 */     6), PERIOD(
/*  17: 37 */     7), DP_PLATENO(
/*  18:    */   
/*  19:    */ 
/*  20: 42 */     10), DP_TYPE(
/*  21: 45 */     11), DP_OWNER(
/*  22: 48 */     12), DP_ADDRESS(
/*  23: 51 */     13), DP_USECHARACTER(
/*  24: 54 */     14), DP_MODEL(
/*  25: 57 */     15), DP_VIN(
/*  26: 60 */     16), DP_ENGINENO(
/*  27: 63 */     17), DP_REGISTER_DATE(
/*  28: 66 */     18), DP_ISSUE_DATE(
/*  29: 69 */     19), DL_NUM(
/*  30:    */   
/*  31:    */ 
/*  32: 74 */     20), DL_NAME(
/*  33: 77 */     21), DL_SEX(
/*  34: 80 */     22), DL_COUNTRY(
/*  35: 83 */     23), DL_ADDRESS(
/*  36: 86 */     24), DL_BIRTHDAY(
/*  37: 89 */     25), DL_ISSUE_DATE(
/*  38: 92 */     26), DL_CLASS(
/*  39: 95 */     27), DL_VALIDFROM(
/*  40: 98 */     28), DL_VALIDFOR(
/*  41:101 */     29), TIC_START(
/*  42:    */   
/*  43:    */ 
/*  44:106 */     30), TIC_NUM(
/*  45:109 */     31), TIC_END(
/*  46:112 */     32), TIC_TIME(
/*  47:115 */     33), TIC_SEAT(
/*  48:118 */     34), TIC_NAME(
/*  49:121 */     35), TBANK_NUM(
/*  50:    */   
/*  51:125 */     36), TBANK_NAME(
/*  52:128 */     37), TBANK_ORGCODE(
/*  53:131 */     38), TBANK_CLASS(
/*  54:134 */     39), TBANK_CARD_NAME(
/*  55:137 */     40), TBANK_NUM_REGION(
/*  56:140 */     41), TBANK_NUM_CHECKSTATUS(
/*  57:143 */     42), TBANK_IMG_STREAM(
/*  58:146 */     43), TBANK_LENTH_IMGSTREAM(
/*  59:149 */     44), TMAX(
/*  60:152 */     45);
  /*  61:    */
/*  62:    */   public int nValue;

  /*  63:    */
/*  64:    */
  private TFieldID(int nValue)
/*  65:    */ {
/*  66:157 */
    this.nValue = nValue;
/*  67:    */
  }

  /*  68:    */
/*  69:    */
  public static TFieldID valueOf(int nValue)
/*  70:    */ {
/*  71:168 */
    TFieldID[] values = values();
/*  72:169 */
    TFieldID ret = null;
/*  73:170 */
    for (TFieldID status : values) {
/*  74:171 */
      if (status.nValue == nValue)
/*  75:    */ {
/*  76:172 */
        ret = status;
/*  77:173 */
        break;
/*  78:    */
      }
/*  79:    */
    }
/*  80:176 */
    return ret;
/*  81:    */
  }
/*  82:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TFieldID

 * JD-Core Version:    0.7.0.1

 */