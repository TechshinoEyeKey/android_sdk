/*   1:    */
package com.idcard;
/*   2:    */ 
/*   3:    */

import android.content.Context;
import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;

/*   4:    */
/*   5:    */

/*   6:    */
/*   7:    */ public class Demo
/*   8:    */ {
  /*   9:    */
  public static native int RECOCRBoot(Context paramContext);

  /*  10:    */
/*  11:    */
  public static native int SetSupportEngine(int paramInt);

  /*  12:    */
/*  13:    */
  public static native int LoadImage(String paramString);

  /*  14:    */
/*  15:    */
  public static native int RECOCR();

  /*  16:    */
/*  17:    */
  public static native int FreeImage();

  /*  18:    */
/*  19:    */
  public static native byte[] GetOCRStringBuf();

  /*  20:    */
/*  21:    */
  public static native byte[] GetCopyrightInfo();

  /*  22:    */
/*  23:    */
  public static native byte[] GetVersion();

  /*  24:    */
/*  25:    */
  public static native byte[] GetUseTimeString();

  /*  26:    */
/*  27:    */
  public static native byte[] GetOCRFieldStringBuf(int paramInt);

  /*  28:    */
/*  29:    */
  public static native int TerminateOCRHandle();

  /*  30:    */
/*  31:    */
  public static native int LoadMemBitMap(Bitmap paramBitmap);

  /*  32:    */
/*  33:    */
  public static native int SaveImage(String paramString);

  /*  34:    */
/*  35:    */
  public static native int SetLOGPath(String paramString);

  /*  36:    */
/*  37:    */
  public static native byte[] GetHeadImgBuf();

  /*  38:    */
/*  39:    */
  public static native int GetHeadImgBufSize();

  /*  40:    */
/*  41:    */
  public static native int SetParam(int paramInt1, int paramInt2);

  /*  42:    */
/*  43:    */
  public static native int BankJudgeExist4Margin(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  /*  44:    */
/*  45:    */
  public static native byte[] GetPublicBankInfo(int paramInt, String paramString);

  /*  46:    */
/*  47:    */
  public static native int GetLineRect(int paramInt);

  /*  48:    */
/*  49:    */
  public int initengine(Context context)
/*  50:    */ {
/*  51: 72 */
    int ret = RECOCRBoot(context);
/*  52: 73 */
    return ret;
/*  53:    */
  }

  /*  54:    */
/*  55:    */
  public int RunOCR(Bitmap map)
/*  56:    */ {
/*  57: 77 */
    int ret = 0;
/*  58: 78 */
    ret = LoadMemBitMap(map);
/*  59: 79 */
    if (ret != 1) {
/*  60: 80 */
      return 0;
/*  61:    */
    }
/*  62: 82 */
    ret = RECOCR();
/*  63: 83 */
    FreeImage();
/*  64: 84 */
    return ret;
/*  65:    */
  }

  /*  66:    */
/*  67:    */
  public String GetAllInfoString()
/*  68:    */ {
/*  69: 89 */
    byte[] buf = GetOCRStringBuf();
/*  70: 90 */
    String str = null;
/*  71:    */
    try
/*  72:    */ {
/*  73: 92 */
      str = new String(buf, "GB2312");
/*  74:    */
    }
/*  75:    */ catch (UnsupportedEncodingException e)
/*  76:    */ {
/*  77: 95 */
      e.printStackTrace();
/*  78:    */
    }
/*  79: 97 */
    return str;
/*  80:    */
  }

  /*  81:    */
/*  82:    */
  public String GetFieldString(int field)
/*  83:    */ {
/*  84:102 */
    byte[] buf = GetOCRFieldStringBuf(field);
/*  85:103 */
    String str = null;
/*  86:    */
    try
/*  87:    */ {
/*  88:105 */
      str = new String(buf, "GB2312");
/*  89:    */
    }
/*  90:    */ catch (UnsupportedEncodingException e)
/*  91:    */ {
/*  92:108 */
      e.printStackTrace();
/*  93:    */
    }
/*  94:110 */
    return str;
/*  95:    */
  }

  /*  96:    */
/*  97:    */
  public int uinitengine()
/*  98:    */ {
/*  99:115 */
    int ret = 0;
/* 100:116 */
    ret = TerminateOCRHandle();
/* 101:117 */
    return ret;
/* 102:    */
  }

  /* 103:    */
/* 104:    */
  public String GetTheIDcardEngineCopyrightInfo()
/* 105:    */ {
/* 106:121 */
    byte[] buf = GetCopyrightInfo();
/* 107:122 */
    String str = null;
/* 108:    */
    try
/* 109:    */ {
/* 110:124 */
      str = new String(buf, "GB2312");
/* 111:    */
    }
/* 112:    */ catch (UnsupportedEncodingException e)
/* 113:    */ {
/* 114:127 */
      e.printStackTrace();
/* 115:    */
    }
/* 116:129 */
    return str;
/* 117:    */
  }

  /* 118:    */
/* 119:    */
  public String GetEngineVersion()
/* 120:    */ {
/* 121:133 */
    byte[] szVersion = GetVersion();
/* 122:134 */
    String version = null;
/* 123:    */
    try
/* 124:    */ {
/* 125:136 */
      version = new String(szVersion, "GB2312");
/* 126:    */
    }
/* 127:    */ catch (UnsupportedEncodingException e)
/* 128:    */ {
/* 129:139 */
      e.printStackTrace();
/* 130:    */
    }
/* 131:141 */
    return version;
/* 132:    */
  }
/* 133:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.Demo

 * JD-Core Version:    0.7.0.1

 */