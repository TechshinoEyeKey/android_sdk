package com.idcard;

import android.graphics.Bitmap;

public abstract interface TRECAPI {
  public abstract TStatus TR_StartUP();

  public abstract TStatus TR_SetSupportEngine(TengineID paramTengineID);

  public abstract TStatus TR_LoadImage(String paramString);

  public abstract TStatus TR_RECOCR();

  public abstract TStatus TR_FreeImage();

  public abstract String TR_GetOCRStringBuf();

  public abstract String TR_GetCopyrightInfo();

  public abstract String TR_GetVersion();

  public abstract String TR_GetUseTimeString();

  public abstract String TR_GetOCRFieldStringBuf(TFieldID paramTFieldID);

  public abstract TStatus TR_ClearUP();

  public abstract TStatus TR_LoadMemBitMap(Bitmap paramBitmap);

  public abstract TStatus TR_SaveImage(String paramString);

  public abstract TStatus TR_SetLOGPath(String paramString);

  public abstract byte[] TR_GetHeadImgBuf();

  public abstract int TR_GetHeadImgBufSize();

  public abstract TStatus TR_SetParam(TParam paramTParam, int paramInt);

  public abstract int TR_BankJudgeExist4Margin(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract String TR_GetPublicBankInfo(BankTypeID paramBankTypeID, String paramString);

  public abstract int TR_GetLineRect(int paramInt);

  public abstract String Byte2String(byte[] paramArrayOfByte);
}



/* Location:           D:\workplace-eclipse\引擎识别SDK安卓版\API\TCardApi.jar

 * Qualified Name:     com.idcard.TRECAPI

 * JD-Core Version:    0.7.0.1

 */