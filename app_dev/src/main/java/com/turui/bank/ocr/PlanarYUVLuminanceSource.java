/*   1:    */
package com.turui.bank.ocr;
/*   2:    */ 
/*   3:    */

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
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
/*  12:    */ public final class PlanarYUVLuminanceSource
/*  13:    */ {
  /*  16:    */   private final int top;
  /*  17:    */   private final int left;
  /*  18:    */   private final int dataHeight;
  /*  19:    */   private final int dataWidth;
  /*  20:    */   private final byte[] yuvData;
  /*  14: 44 */   private int height = 0;
  /*  15: 44 */   private int width = 0;

  /*  21:    */
/*  22:    */
  public PlanarYUVLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, int left, int top, int width, int height)
/*  23:    */ {
/*  24: 48 */
    this.width = width;
/*  25: 49 */
    this.height = height;
/*  26: 50 */
    if ((left + width > dataWidth) || (top + height > dataHeight)) {
/*  27: 51 */
      throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
/*  28:    */
    }
/*  29: 54 */
    this.yuvData = yuvData;
/*  30: 55 */
    this.dataWidth = dataWidth;
/*  31: 56 */
    this.dataHeight = dataHeight;
/*  32: 57 */
    this.left = left;
/*  33: 58 */
    this.top = top;
/*  34:    */
  }

  /*  35:    */
/*  36:    */
  public int getDataWidth()
/*  37:    */ {
/*  38: 63 */
    return this.dataWidth;
/*  39:    */
  }

  /*  40:    */
/*  41:    */
  public int getDataHeight()
/*  42:    */ {
/*  43: 67 */
    return this.dataHeight;
/*  44:    */
  }

  /*  45:    */
/*  46:    */
  public void saveJpeg(Bitmap bm)
/*  47:    */ {
/*  48: 70 */
    String jpegName = "";
/*  49: 71 */
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/AATurec/img/";
/*  50: 72 */
    File folder = new File(savePath);
/*  51: 73 */
    if (!folder.exists()) {
/*  52: 75 */
      folder.mkdir();
/*  53:    */
    }
/*  54: 77 */
    long dataTake = System.currentTimeMillis();
/*  55: 78 */
    jpegName = savePath + dataTake + ".jpg";
/*  56:    */
    try
/*  57:    */ {
/*  58: 80 */
      FileOutputStream fout = new FileOutputStream(jpegName);
/*  59: 81 */
      BufferedOutputStream bos = new BufferedOutputStream(fout);
/*  60: 82 */
      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
/*  61: 83 */
      bos.flush();
/*  62: 84 */
      bos.close();
/*  63:    */
    }
/*  64:    */ catch (IOException e)
/*  65:    */ {
/*  66: 86 */
      e.printStackTrace();
/*  67:    */
    }
/*  68:    */
  }

  /*  69:    */
/*  70:    */
  public Bitmap renderCroppedGreyscaleBitmap()
/*  71:    */ {
/*  72: 90 */
    int width = this.width;
/*  73: 91 */
    int height = this.height;
/*  74: 92 */
    int[] pixels = new int[width * height];
/*  75: 93 */
    byte[] yuv = this.yuvData;
/*  76: 94 */
    int inputOffset = this.top * this.dataWidth + this.left;
/*  77: 96 */
    for (int y = 0; y < height; y++)
/*  78:    */ {
/*  79: 97 */
      int outputOffset = y * width;
/*  80: 98 */
      for (int x = 0; x < width; x++)
/*  81:    */ {
/*  82: 99 */
        int grey = yuv[(inputOffset + x)] & 0xFF;
/*  83:100 */
        pixels[(outputOffset + x)] = (0xFF000000 | grey * 65793);
/*  84:    */
      }
/*  85:102 */
      inputOffset += this.dataWidth;
/*  86:    */
    }
/*  87:105 */
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
/*  88:106 */
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
/*  89:    */     
/*  90:108 */
    return bitmap;
/*  91:    */
  }
/*  92:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.PlanarYUVLuminanceSource

 * JD-Core Version:    0.7.0.1

 */