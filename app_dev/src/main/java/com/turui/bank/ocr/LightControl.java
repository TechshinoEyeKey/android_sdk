/*  1:   */
package com.turui.bank.ocr;
/*  2:   */ 
/*  3:   */

import android.hardware.Camera;

/*  4:   */

/*  5:   */
/*  6:   */ public class LightControl
/*  7:   */ {
  /*  8:   */ boolean m_isOn;
  /*  9:   */ Camera m_Camera;

  /* 15:   */
/* 16:   */
  public LightControl()
/* 17:   */ {
/* 18:19 */
    this.m_isOn = false;
/* 19:   */
  }

  /* 10:   */
/* 11:   */
  public boolean getIsOn()
/* 12:   */ {
/* 13:15 */
    return this.m_isOn;
/* 14:   */
  }

  /* 20:   */
/* 21:   */
  public void turnOn()
/* 22:   */ {
/* 23:   */
    try
/* 24:   */ {
/* 25:26 */
      this.m_Camera = CameraManager.get().getCamera();
/* 26:   */       
/* 27:28 */
      Camera.Parameters mParameters = this.m_Camera.getParameters();
/* 28:29 */
      mParameters.setFlashMode("torch");
/* 29:30 */
      this.m_Camera.setParameters(mParameters);
/* 30:   */
    }
/* 31:   */ catch (Exception localException) {
    }
/* 32:   */
  }

  /* 33:   */
/* 34:   */
  public void turnOff()
/* 35:   */ {
/* 36:   */
    try
/* 37:   */ {
/* 38:39 */
      this.m_Camera = CameraManager.get().getCamera();
/* 39:   */       
/* 40:41 */
      Camera.Parameters mParameters = this.m_Camera.getParameters();
/* 41:42 */
      mParameters.setFlashMode("off");
/* 42:43 */
      this.m_Camera.setParameters(mParameters);
/* 43:   */
    }
/* 44:   */ catch (Exception localException) {
    }
/* 45:   */
  }
/* 46:   */
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.turui.bank.ocr.LightControl

 * JD-Core Version:    0.7.0.1

 */