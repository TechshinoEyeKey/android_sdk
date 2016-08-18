/*   1:    */
package com.idcard;
/*   2:    */ 
/*   3:    */

import android.graphics.Bitmap;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/*   4:    */
/*   5:    */

/*   6:    */
/*   7:    */ public class TRECAPIImpl
/*   8:    */ implements TRECAPI, Serializable
/*   9:    */ {
  /*  10:    */   static
/*  11:    */ {
/*  12: 14 */
    System.loadLibrary("IDCARDDLL");
/*  13:    */
  }

  /*  14:    */
/*  15:    */
  public TStatus TR_StartUP()
/*  16:    */ {
/*  17: 20 */
    return TStatus.valueOf(Demo.RECOCRBoot(null));
/*  18:    */
  }

  /*  19:    */
/*  20:    */
  public TStatus TR_SetSupportEngine(TengineID tEngineTypeID)
/*  21:    */ {
/*  22: 26 */
    int id = tEngineTypeID.nValue;
/*  23: 27 */
    return TStatus.valueOf(Demo.SetSupportEngine(tEngineTypeID.nValue));
/*  24:    */
  }

  /*  25:    */
/*  26:    */
  public TStatus TR_LoadImage(String path)
/*  27:    */ {
/*  28: 33 */
    return TStatus.valueOf(Demo.LoadImage(path));
/*  29:    */
  }

  /*  30:    */
/*  31:    */
  public TStatus TR_RECOCR()
/*  32:    */ {
/*  33: 39 */
    return TStatus.valueOf(Demo.RECOCR());
/*  34:    */
  }

  /*  35:    */
/*  36:    */
  public TStatus TR_FreeImage()
/*  37:    */ {
/*  38: 45 */
    return TStatus.valueOf(Demo.FreeImage());
/*  39:    */
  }

  /*  40:    */
/*  41:    */
  public String TR_GetOCRStringBuf()
/*  42:    */ {
/*  43: 51 */
    return Byte2String(Demo.GetOCRStringBuf());
/*  44:    */
  }

  /*  45:    */
/*  46:    */
  public String TR_GetCopyrightInfo()
/*  47:    */ {
/*  48: 57 */
    return Byte2String(Demo.GetCopyrightInfo());
/*  49:    */
  }

  /*  50:    */
/*  51:    */
  public String TR_GetVersion()
/*  52:    */ {
/*  53: 63 */
    return Byte2String(Demo.GetVersion());
/*  54:    */
  }

  /*  55:    */
/*  56:    */
  public String TR_GetUseTimeString()
/*  57:    */ {
/*  58: 69 */
    return Byte2String(Demo.GetUseTimeString());
/*  59:    */
  }

  /*  60:    */
/*  61:    */
  public String TR_GetOCRFieldStringBuf(TFieldID field)
/*  62:    */ {
/*  63: 75 */
    return Byte2String(Demo.GetOCRFieldStringBuf(field.nValue));
/*  64:    */
  }

  /*  65:    */
/*  66:    */
  public TStatus TR_ClearUP()
/*  67:    */ {
/*  68: 81 */
    return TStatus.valueOf(Demo.TerminateOCRHandle());
/*  69:    */
  }

  /*  70:    */
/*  71:    */
  public TStatus TR_LoadMemBitMap(Bitmap map)
/*  72:    */ {
/*  73: 87 */
    return TStatus.valueOf(Demo.LoadMemBitMap(map));
/*  74:    */
  }

  /*  75:    */
/*  76:    */
  public TStatus TR_SaveImage(String path)
/*  77:    */ {
/*  78: 93 */
    return TStatus.valueOf(Demo.SaveImage(path));
/*  79:    */
  }

  /*  80:    */
/*  81:    */
  public TStatus TR_SetLOGPath(String path)
/*  82:    */ {
/*  83: 99 */
    return TStatus.valueOf(Demo.SetLOGPath(path));
/*  84:    */
  }

  /*  85:    */
/*  86:    */
  public byte[] TR_GetHeadImgBuf()
/*  87:    */ {
/*  88:105 */
    return Demo.GetHeadImgBuf();
/*  89:    */
  }

  /*  90:    */
/*  91:    */
  public int TR_GetHeadImgBufSize()
/*  92:    */ {
/*  93:111 */
    return Demo.GetHeadImgBufSize();
/*  94:    */
  }

  /*  95:    */
/*  96:    */
  public TStatus TR_SetParam(TParam Param, int value)
/*  97:    */ {
/*  98:117 */
    return TStatus.valueOf(Demo.SetParam(Param.nValue, value));
/*  99:    */
  }

  /* 100:    */
/* 101:    */
  public int TR_BankJudgeExist4Margin(int x1, int y1, int x2, int y2)
/* 102:    */ {
/* 103:124 */
    return Demo.BankJudgeExist4Margin(x1, y1, x2, y2);
/* 104:    */
  }

  /* 105:    */
/* 106:    */
  public String TR_GetPublicBankInfo(BankTypeID bankTypeID, String BankCardNumString)
/* 107:    */ {
/* 108:130 */
    return Byte2String(Demo.GetPublicBankInfo(bankTypeID.nValue, BankCardNumString));
/* 109:    */
  }

  /* 110:    */
/* 111:    */
  public int TR_GetLineRect(int val)
/* 112:    */ {
/* 113:136 */
    return Demo.GetLineRect(val);
/* 114:    */
  }

  /* 115:    */
/* 116:    */
  public String Byte2String(byte[] info)
/* 117:    */ {
/* 118:141 */
    String str = null;
/* 119:142 */
    if (info != null) {
/* 120:    */
      try
/* 121:    */ {
/* 122:144 */
        str = new String(info, "GB2312");
/* 123:    */
      }
/* 124:    */ catch (UnsupportedEncodingException e)
/* 125:    */ {
/* 126:147 */
        e.printStackTrace();
/* 127:    */
      }
/* 128:    */
    }
/* 129:150 */
    return str;
/* 130:    */
  }
/* 131:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TRECAPIImpl

 * JD-Core Version:    0.7.0.1

 */