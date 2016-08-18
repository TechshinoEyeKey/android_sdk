/*   1:    */
package com.turui.bank.ocr;
/*   2:    */ 
/*   3:    */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.idcard.TFieldID;
import com.idcard.TStatus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */
/*   9:    */
/*  10:    */
/*  11:    */
/*  12:    */
/*  13:    */
/*  14:    */
/*  15:    */
/*  16:    */
/*  17:    */
/*  18:    */
/*  19:    */
/*  20:    */
/*  21:    */
/*  22:    */
/*  23:    */

/*  24:    */
/*  25:    */ final class DecodeHandler
/*  26:    */ extends Handler
/*  27:    */ {
  /*  28: 47 */   private static final String TAG = DecodeHandler.class.getSimpleName();
  /*  29: 49 */   static int x1 = 0;
  /*  30: 50 */   static int y1 = 0;
  /*  31: 51 */   static int x2 = 0;
  /*  32: 52 */   static int y2 = 0;
  /*  33:    */   private final CaptureActivity activity;

  /*  34:    */
/*  35:    */   DecodeHandler(CaptureActivity activity)
/*  36:    */ {
/*  37: 55 */
    this.activity = activity;
/*  38:    */
  }

  /*  39:    */
/*  40:    */
  public void handleMessage(Message message)
/*  41:    */ {
/*  42: 60 */
    switch (message.what)
/*  43:    */ {
/*  44:    */
      case 1:
/*  45: 63 */
        decode((byte[]) message.obj, message.arg1, message.arg2);
/*  46: 64 */
        break;
/*  47:    */
      case 7:
/*  48: 66 */
        Looper.myLooper().quit();
/*  49:    */
    }
/*  50:    */
  }

  /*  51:    */
/*  52:    */
  public void saveJpeg(Bitmap bm)
/*  53:    */ {
/*  54: 85 */
    String jpegName = "";
/*  55: 86 */
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/AATurec/img/";
/*  56: 87 */
    String savePathRoot = Environment.getExternalStorageDirectory().getPath() + "/AATurec/";
/*  57: 88 */
    File folder = new File(savePath);
/*  58: 89 */
    if (!folder.exists())
/*  59:    */ {
/*  60: 91 */
      folder = new File(savePathRoot);
/*  61: 92 */
      folder.mkdir();
/*  62:    */       
/*  63: 94 */
      folder = new File(savePath);
/*  64: 95 */
      folder.mkdir();
/*  65:    */
    }
/*  66: 98 */
    long dataTake = System.currentTimeMillis();
/*  67: 99 */
    jpegName = savePath + dataTake + ".jpg";
/*  68:    */
    try
/*  69:    */ {
/*  70:101 */
      FileOutputStream fout = new FileOutputStream(jpegName);
/*  71:102 */
      BufferedOutputStream bos = new BufferedOutputStream(fout);
/*  72:103 */
      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
/*  73:104 */
      bos.flush();
/*  74:105 */
      bos.close();
/*  75:    */
    }
/*  76:    */ catch (IOException e)
/*  77:    */ {
/*  78:107 */
      e.printStackTrace();
/*  79:    */
    }
/*  80:    */
  }

  /*  81:    */
/*  82:    */
  public void saveJpegSig(Bitmap bm, String filename)
/*  83:    */ {
/*  84:115 */
    String jpegName = "";
/*  85:116 */
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/AATurec/img/";
/*  86:117 */
    String savePathRoot = Environment.getExternalStorageDirectory().getPath() + "/AATurec/";
/*  87:118 */
    File folder = new File(savePath);
/*  88:119 */
    if (!folder.exists())
/*  89:    */ {
/*  90:121 */
      folder = new File(savePathRoot);
/*  91:122 */
      folder.mkdir();
/*  92:    */       
/*  93:124 */
      folder = new File(savePath);
/*  94:125 */
      folder.mkdir();
/*  95:    */
    }
/*  96:128 */
    long dataTake = System.currentTimeMillis();
/*  97:129 */
    jpegName = savePath + filename;
/*  98:    */
    try
/*  99:    */ {
/* 100:131 */
      FileOutputStream fout = new FileOutputStream(jpegName);
/* 101:132 */
      BufferedOutputStream bos = new BufferedOutputStream(fout);
/* 102:133 */
      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
/* 103:134 */
      bos.flush();
/* 104:135 */
      bos.close();
/* 105:    */
    }
/* 106:    */ catch (IOException e)
/* 107:    */ {
/* 108:137 */
      e.printStackTrace();
/* 109:    */
    }
/* 110:    */
  }

  /* 111:    */
/* 112:    */
  private void decode(byte[] data, int width, int height)
/* 113:    */ {
/* 114:142 */
    long start = System.currentTimeMillis();
/* 115:143 */
    String rawResult = null;
/* 116:    */     
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:165 */
    long beforeTime = 0L;
/* 138:166 */
    long afterTime = 0L;
/* 139:    */     
/* 140:168 */
    beforeTime = System.currentTimeMillis();
/* 141:169 */
    if (CaptureActivity.TakeBitmap != null)
/* 142:    */ {
/* 143:170 */
      CaptureActivity.TakeBitmap.recycle();
/* 144:171 */
      CaptureActivity.TakeBitmap = null;
/* 145:    */
    }
/* 146:173 */
    if (CaptureActivity.SmallBitmap != null)
/* 147:    */ {
/* 148:174 */
      CaptureActivity.SmallBitmap.recycle();
/* 149:175 */
      CaptureActivity.SmallBitmap = null;
/* 150:    */
    }
/* 151:178 */
    YuvImage yuvimage = new YuvImage(data, 17, width,
/* 152:179 */       height, null);
/* 153:180 */
    ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
/* 154:181 */
    yuvimage.compressToJpeg(new Rect(0, 0, width, height), 100, outputSteam);
/* 155:182 */
    BitmapFactory.Options options = new BitmapFactory.Options();
/* 156:183 */
    options.inPreferredConfig = Bitmap.Config.RGB_565;
/* 157:184 */
    CaptureActivity.TakeBitmap = BitmapFactory.decodeByteArray(outputSteam.toByteArray(), 0, outputSteam.toByteArray().length, options);
/* 158:    */
    try
/* 159:    */ {
/* 160:186 */
      outputSteam.flush();
/* 161:    */
    }
/* 162:    */ catch (IOException e)
/* 163:    */ {
/* 164:188 */
      e.printStackTrace();
/* 165:    */
    }
/* 166:191 */
    Rect rect = CameraManager.get().getFramingRectInPreview();
/* 167:192 */
    this.activity.engineDemo.TR_LoadMemBitMap(CaptureActivity.TakeBitmap);
/* 168:    */     
/* 169:194 */
    int ret = this.activity.engineDemo.TR_BankJudgeExist4Margin(rect.left, rect.top, rect.right, rect.bottom);
/* 170:    */     
/* 171:196 */
    yuvimage = null;
/* 172:197 */
    afterTime = System.currentTimeMillis();
/* 173:198 */
    long timeDistance = afterTime - beforeTime;
/* 174:199 */
    ViewfinderView viewfinderView = this.activity.getViewfinderView();
/* 175:200 */
    viewfinderView.SetEdgeVal(ret);
/* 176:201 */
    Message message1 = Message.obtain(this.activity.getHandler(), 12);
/* 177:202 */
    message1.sendToTarget();
/* 178:204 */
    if ((ret == 3) || (ret == 5) || (ret > 9))
/* 179:    */ {
/* 180:207 */
      TStatus isRecSucess = this.activity.engineDemo.TR_RECOCR();
/* 181:208 */
      if (isRecSucess != TStatus.TR_FAIL)
/* 182:    */ {
/* 183:210 */
        x1 = this.activity.engineDemo.TR_GetLineRect(1);
/* 184:211 */
        y1 = this.activity.engineDemo.TR_GetLineRect(2);
/* 185:212 */
        x2 = this.activity.engineDemo.TR_GetLineRect(3);
/* 186:213 */
        y2 = this.activity.engineDemo.TR_GetLineRect(4);
/* 187:    */
      }
/* 188:216 */
      this.activity.engineDemo.TR_FreeImage();
/* 189:217 */
      if (isRecSucess == TStatus.TR_FAIL)
/* 190:    */ {
/* 191:219 */
        Message message = Message.obtain(this.activity.getHandler(), 2);
/* 192:220 */
        message.sendToTarget();
/* 193:    */
      }
/* 194:222 */
      else if (isRecSucess == TStatus.TR_TIME_OUT)
/* 195:    */ {
/* 196:223 */
        Message message = Message.obtain(this.activity.getHandler(), 3, "时间过期，请call 0592-5588468");
/* 197:224 */
        Bundle bundle = new Bundle();
/* 198:225 */
        bundle.putParcelable("image", null);
/* 199:226 */
        message.setData(bundle);
/* 200:    */         
/* 201:228 */
        message.sendToTarget();
/* 202:    */
      }
/* 203:    */
      else
/* 204:    */ {
/* 205:231 */
        String CardNum = this.activity.engineDemo.TR_GetOCRFieldStringBuf(TFieldID.TBANK_NUM);
/* 206:232 */
        String BankName = this.activity.engineDemo.TR_GetOCRFieldStringBuf(TFieldID.TBANK_NAME);
/* 207:233 */
        String BANK_OrganizeCode = this.activity.engineDemo.TR_GetOCRFieldStringBuf(TFieldID.TBANK_ORGCODE);
/* 208:234 */
        String BANK_CardClass = this.activity.engineDemo.TR_GetOCRFieldStringBuf(TFieldID.TBANK_CLASS);
/* 209:235 */
        String CARD_NAME = this.activity.engineDemo.TR_GetOCRFieldStringBuf(TFieldID.TBANK_CARD_NAME);
/* 210:237 */
        if (CardNum != null)
/* 211:    */ {
/* 212:238 */
          long end = System.currentTimeMillis();
/* 213:    */           
/* 214:    */ 
/* 215:241 */
          rawResult = "银行卡号: " + CardNum + "\n" +
/* 216:242 */             "发卡行    : " + BankName + "\n" + 
/* 217:243 */             "机构代码: " + BANK_OrganizeCode + "\n" + 
/* 218:244 */             "卡种         : " + BANK_CardClass + "\n" + 
/* 219:245 */             "卡名         : " + CARD_NAME + "\n";
/* 220:246 */
          Log.d(TAG, "Found barcode (" + (end - start) + " ms)\n" + rawResult.toString());
/* 221:247 */
          Message message = Message.obtain(this.activity.getHandler(), 3, rawResult);
/* 222:248 */
          Bundle bundle = new Bundle();
/* 223:249 */
          if ((CaptureActivity.TakeBitmap != null) && (width != 0) && (height != 0))
/* 224:    */ {
/* 225:251 */
            bundle.putParcelable("image", null);
/* 226:252 */
            CaptureActivity.SmallBitmap = Bitmap.createBitmap(CaptureActivity.TakeBitmap, x1, y1, x2 - x1, y2 - y1);
/* 227:    */
          }
/* 228:    */
          else
/* 229:    */ {
/* 230:255 */
            bundle.putParcelable("image", null);
/* 231:    */
          }
/* 232:256 */
          message.setData(bundle);
/* 233:    */           
/* 234:258 */
          message.sendToTarget();
/* 235:    */
        }
/* 236:    */
        else
/* 237:    */ {
/* 238:261 */
          Message message = Message.obtain(this.activity.getHandler(), 2);
/* 239:262 */
          message.sendToTarget();
/* 240:    */
        }
/* 241:    */
      }
/* 242:    */
    }
/* 243:    */
    else
/* 244:    */ {
/* 245:268 */
      this.activity.engineDemo.TR_FreeImage();
/* 246:269 */
      Message message = Message.obtain(this.activity.getHandler(), 2);
/* 247:270 */
      message.sendToTarget();
/* 248:    */
    }
/* 249:    */
  }
/* 250:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.DecodeHandler

 * JD-Core Version:    0.7.0.1

 */