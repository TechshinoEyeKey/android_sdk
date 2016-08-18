/*  1:   */
package com.idcard;
/*  2:   */ 
/*  3:   */

import android.graphics.Bitmap;

import java.io.Serializable;

/*  4:   */

/*  5:   */
/*  6:   */ public class CardInfo
/*  7:   */ implements Serializable
/*  8:   */ {
  /*  9:   */ String allinfo;
  /* 10:   */ String[] allocrstrings;
  /* 11:   */ String headPath;
  /* 12:   */ String smallHeadPath;
  /* 13:   */ Bitmap HeadImgbitmap;
  /* 14:   */ boolean isShow;
  /* 15:   */ boolean isSelect;
  /* 16:   */ boolean isUpdate;

  /* 17:   */
/* 18:   */
  public CardInfo()
/* 19:   */ {
/* 20:17 */
    int max = TFieldID.TMAX.nValue;
/* 21:18 */
    this.allocrstrings = new String[max];
/* 22:19 */
    setNull();
/* 23:   */
  }

  /* 24:   */
/* 25:   */
  public boolean isSelect()
/* 26:   */ {
/* 27:22 */
    return this.isSelect;
/* 28:   */
  }

  /* 29:   */
/* 30:   */
  public void setSelect(boolean isSelect)
/* 31:   */ {
/* 32:25 */
    this.isSelect = isSelect;
/* 33:   */
  }

  /* 34:   */
/* 35:   */
  public boolean isShow()
/* 36:   */ {
/* 37:28 */
    return this.isShow;
/* 38:   */
  }

  /* 48:   */
/* 49:   */
  public void setShow(boolean isShow)
/* 50:   */ {
/* 51:40 */
    this.isShow = isShow;
/* 52:   */
  }

  /* 39:   */
/* 40:   */
  public void setNull()
/* 41:   */ {
/* 42:32 */
    this.allinfo = "";
/* 43:33 */
    for (int i = 0; i < TFieldID.TMAX.nValue; i++) {
/* 44:35 */
      this.allocrstrings[i] = "";
/* 45:   */
    }
/* 46:37 */
    this.HeadImgbitmap = null;
/* 47:   */
  }

  /* 53:   */
/* 54:   */
  public String getSmallHeadPath()
/* 55:   */ {
/* 56:43 */
    return this.smallHeadPath;
/* 57:   */
  }

  /* 58:   */
/* 59:   */
  public void setSmallHeadPath(String smallHeadPath)
/* 60:   */ {
/* 61:46 */
    this.smallHeadPath = smallHeadPath;
/* 62:   */
  }

  /* 63:   */
/* 64:   */
  public boolean isUpdate()
/* 65:   */ {
/* 66:50 */
    return this.isUpdate;
/* 67:   */
  }

  /* 68:   */
/* 69:   */
  public void setUpdate(boolean isUpdate)
/* 70:   */ {
/* 71:53 */
    this.isUpdate = isUpdate;
/* 72:   */
  }

  /* 73:   */
/* 74:   */
  public String getAllinfo()
/* 75:   */ {
/* 76:56 */
    return this.allinfo;
/* 77:   */
  }

  /* 78:   */
/* 79:   */
  public void setAllinfo(String info)
/* 80:   */ {
/* 81:59 */
    this.allinfo = info;
/* 82:   */
  }

  /* 83:   */
/* 84:   */
  public String getFieldString(TFieldID tFieldID)
/* 85:   */ {
/* 86:62 */
    return this.allocrstrings[tFieldID.nValue];
/* 87:   */
  }

  /* 88:   */
/* 89:   */
  public void setFieldString(TFieldID tFieldID, String info)
/* 90:   */ {
/* 91:65 */
    this.allocrstrings[tFieldID.nValue] = info;
/* 92:   */
  }

  /* 93:   */
/* 94:   */
  public String getHeadPath()
/* 95:   */ {
/* 96:69 */
    return this.headPath;
/* 97:   */
  }

  /* 98:   */
/* 99:   */
  public void setHeadPath(String headPath)
/* :0:   */ {
/* :1:72 */
    this.headPath = headPath;
/* :2:   */
  }

  /* ;1:   */
/* ;2:   */
  public Bitmap getHeadBit()
/* ;3:   */ {
/* ;4:84 */
    return this.HeadImgbitmap;
/* ;5:   */
  }

  /* :3:   */
/* :4:   */
  public void setHeadBit(Bitmap bitmap)
/* :5:   */ {
/* :6:76 */
    if (this.HeadImgbitmap != null) {
/* :7:78 */
      this.HeadImgbitmap.recycle();
/* :8:   */
    }
/* :9:80 */
    this.HeadImgbitmap = bitmap;
/* ;0:   */
  }
/* ;6:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.CardInfo

 * JD-Core Version:    0.7.0.1

 */