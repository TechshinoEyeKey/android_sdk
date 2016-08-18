/*   1:    */
package com.turui.bank.ocr;
/*   2:    */ 
/*   3:    */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

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
/*  14:    */ public final class ViewfinderView
/*  15:    */ extends View
/*  16:    */ {
  /*  17: 39 */   private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  /*  18:    */   private static final long ANIMATION_DELAY = 100L;
  /*  19:    */   private static final int OPAQUE = 255;
  /*  23: 46 */   public int isleft = 0;
  /*  24: 46 */   public int isright = 0;
  /*  25: 46 */   public int istop = 0;
  /*  26: 46 */   public int isbottom = 0;
  /*  22: 45 */ int linewidth = 8;
  /*  27:    */ Paint paint;
  /*  28:    */ Paint paint1;
  /*  20:    */   private Bitmap resultBitmap;
  /*  21:    */   private int scannerAlpha;

  /*  29:    */
/*  30:    */
  public ViewfinderView(Context context, AttributeSet attrs)
/*  31:    */ {
/*  32: 49 */
    super(context, attrs);
/*  33:    */     
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:    */ 
/*  46: 63 */
    this.paint = new Paint();
/*  47:    */     
/*  48: 65 */
    this.paint.setAntiAlias(true);
/*  49: 66 */
    this.paint.setColor(16315652);
/*  50: 67 */
    this.paint.setStyle(Paint.Style.STROKE);
/*  51: 68 */
    this.paint.setStrokeWidth(this.linewidth);
/*  52: 69 */
    this.paint.setAlpha(255);
/*  53:    */     
/*  54: 71 */
    this.paint1 = new Paint();
    Resources resources = getResources();
    this.scannerAlpha = 0;
/*  55:    */
  }

  /*  56:    */
/*  57:    */
  public ViewfinderView(Context context)
/*  58:    */ {
/*  59: 56 */
    super(context);
/*  60:    */     
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66: 63 */
    this.paint = new Paint();
/*  67:    */     
/*  68: 65 */
    this.paint.setAntiAlias(true);
/*  69: 66 */
    this.paint.setColor(16315652);
/*  70: 67 */
    this.paint.setStyle(Paint.Style.STROKE);
/*  71: 68 */
    this.paint.setStrokeWidth(this.linewidth);
/*  72: 69 */
    this.paint.setAlpha(255);
/*  73:    */     
/*  74: 71 */
    this.paint1 = new Paint();
    Resources resources = getResources();
    this.scannerAlpha = 0;
/*  75:    */
  }

  /*  76:    */
/*  77:    */
  public void onDraw(Canvas canvas)
/*  78:    */ {
/*  79: 74 */
    Rect rect = CameraManager.get().getFramingRect();
/*  80: 75 */
    if (rect == null) {
/*  81: 76 */
      return;
/*  82:    */
    }
/*  83: 78 */
    int width = canvas.getWidth();
/*  84: 79 */
    int height = canvas.getHeight();
/*  85: 82 */
    if (this.istop == 1)
/*  86:    */ {
/*  87: 83 */
      this.paint.setAlpha(255);
/*  88: 84 */
      canvas.drawLine(rect.left + height / 15, rect.top, rect.right - height / 15, rect.top, this.paint);
/*  89:    */
    }
/*  90:    */
    else
/*  91:    */ {
/*  92: 87 */
      this.paint.setAlpha(0);
/*  93: 88 */
      canvas.drawLine(rect.left + height / 15, rect.top, rect.right - height / 15, rect.top, this.paint);
/*  94:    */
    }
/*  95: 91 */
    if (this.isleft == 2)
/*  96:    */ {
/*  97: 92 */
      this.paint.setAlpha(255);
/*  98: 93 */
      canvas.drawLine(rect.left, rect.top + height / 15, rect.left, rect.bottom - height / 15, this.paint);
/*  99:    */
    }
/* 100:    */
    else
/* 101:    */ {
/* 102: 96 */
      this.paint.setAlpha(0);
/* 103: 97 */
      canvas.drawLine(rect.left, rect.top + height / 15, rect.left, rect.bottom - height / 15, this.paint);
/* 104:    */
    }
/* 105: 99 */
    if (this.isbottom == 4)
/* 106:    */ {
/* 107:100 */
      this.paint.setAlpha(255);
/* 108:101 */
      canvas.drawLine(rect.left + height / 15, rect.bottom, rect.right - height / 15, rect.bottom, this.paint);
/* 109:    */
    }
/* 110:    */
    else
/* 111:    */ {
/* 112:104 */
      this.paint.setAlpha(0);
/* 113:105 */
      canvas.drawLine(rect.left + height / 15, rect.bottom, rect.right - height / 15, rect.bottom, this.paint);
/* 114:    */
    }
/* 115:107 */
    if (this.isright == 8)
/* 116:    */ {
/* 117:108 */
      this.paint.setAlpha(255);
/* 118:109 */
      canvas.drawLine(rect.right, rect.top + height / 15, rect.right, rect.bottom - height / 15, this.paint);
/* 119:    */
    }
/* 120:    */
    else
/* 121:    */ {
/* 122:112 */
      this.paint.setAlpha(0);
/* 123:113 */
      canvas.drawLine(rect.right, rect.top + height / 15, rect.right, rect.bottom - height / 15, this.paint);
/* 124:    */
    }
/* 125:115 */
    this.paint.setAlpha(255);
/* 126:116 */
    this.paint1.setColor(this.resultBitmap != null ? Color.parseColor("#b0000000") : Color.parseColor("#60000000"));
/* 127:117 */
    canvas.drawRect(0.0F, 0.0F, width, rect.top - this.linewidth / 2, this.paint1);
/* 128:118 */
    canvas.drawRect(0.0F, rect.top - this.linewidth / 2, rect.left - this.linewidth / 2, rect.bottom + this.linewidth / 2, this.paint1);
/* 129:119 */
    canvas.drawRect(rect.right + this.linewidth / 2, rect.top - this.linewidth / 2, width, rect.bottom + this.linewidth / 2, this.paint1);
/* 130:120 */
    canvas.drawRect(0.0F, rect.bottom + this.linewidth / 2, width, height, this.paint1);
/* 131:    */     
/* 132:    */ 
/* 133:123 */
    canvas.drawLine(rect.left, rect.top, rect.left, rect.top + height / 15, this.paint);
/* 134:124 */
    canvas.drawLine(rect.left, rect.bottom - height / 15, rect.left, rect.bottom, this.paint);
/* 135:125 */
    canvas.drawLine(rect.left - this.linewidth / 2, rect.top, rect.left + height / 15, rect.top, this.paint);
/* 136:126 */
    canvas.drawLine(rect.left - this.linewidth / 2, rect.bottom, rect.left + height / 15, rect.bottom, this.paint);
/* 137:    */     
/* 138:128 */
    canvas.drawLine(rect.right, rect.top, rect.right, rect.top + height / 15, this.paint);
/* 139:129 */
    canvas.drawLine(rect.right, rect.bottom - height / 15, rect.right, rect.bottom, this.paint);
/* 140:130 */
    canvas.drawLine(rect.right - height / 15, rect.top, rect.right + this.linewidth / 2, rect.top, this.paint);
/* 141:131 */
    canvas.drawLine(rect.right - height / 15, rect.bottom, rect.right + this.linewidth / 2, rect.bottom, this.paint);
/* 142:    */
  }

  /* 143:    */
/* 144:    */
  public void SetEdgeVal(int val)
/* 145:    */ {
/* 146:136 */
    this.istop = (val & 0x1);
/* 147:137 */
    this.isleft = (val & 0x2);
/* 148:138 */
    this.isbottom = (val & 0x4);
/* 149:139 */
    this.isright = (val & 0x8);
/* 150:    */
  }

  /* 151:    */
/* 152:    */
  public void drawViewfinder()
/* 153:    */ {
/* 154:143 */
    this.resultBitmap = null;
/* 155:144 */
    invalidate();
/* 156:    */
  }

  /* 157:    */
/* 158:    */
  public void drawResultBitmap(Bitmap barcode)
/* 159:    */ {
/* 160:153 */
    this.resultBitmap = barcode;
/* 161:154 */
    invalidate();
/* 162:    */
  }
/* 163:    */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.ViewfinderView

 * JD-Core Version:    0.7.0.1

 */