package com.ui.card;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idcard.CardInfo;
import com.idcard.GlobalData;
import com.idcard.TFieldID;
import com.idcard.TParam;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class TRCardScan extends Activity implements SurfaceHolder.Callback {
  public static final int only_auto_focus = 110;
  private static final String tag = "yan";
  private static final int auto_focus = 111;
  private static final int take_pic_ok = 222;
  private static final int get_data_ok = 333;
  private static final int closeview = 555;
  public static boolean isPreview = false;
  public static Bitmap TakeBitmap = null;
  public static Bitmap HeadImgBitmap = null;
  private static TengineID tengineID = TengineID.TUNCERTAIN;
  private final Handler mHandler = new MyHandler(this);
  private final Thread MyThread = new MyThread(this);
  private final Thread MyOpenThread = new MyOpenThread(this);
  public int isGetdataok = -1;
  List<Camera.Size> mSupportedPreviewSizes;
  int issuccessfocus = 0;
  int nfocusNum = 0;
  CardInfo cardInfo = new CardInfo();
  Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {
    public void onShutter() {
      Log.i("yan", "myShutterCallback:onShutter...");
    }
  };
  Camera.PictureCallback myRawCallback = new Camera.PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.i("yan", "myRawCallback:onPictureTaken...");
    }
  };
  private SurfaceView surfaceView;
  private SurfaceHolder mySurfaceHolder = null;
  private Camera myCamera = null;
  Camera.PictureCallback myJpegCallback = new Camera.PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.i("yan", "myJpegCallback:onPictureTaken...");
      if (data != null) {
        TRCardScan.this.myCamera.stopPreview();
        Message msg_data = TRCardScan.this.mHandler.obtainMessage();
        msg_data.what = 333;
        msg_data.obj = data;
        TRCardScan.this.mHandler.sendMessage(msg_data);
        TRCardScan.isPreview = false;
      }
    }
  };
  private Camera.AutoFocusCallback myAutoFocusCallback = null;
  private Camera.AutoFocusCallback myAutoFocusCallback1 = null;
  private TextView logtxt;
  private WindowManager manager;
  private Display display;
  private RelativeLayout.LayoutParams Params;
  private int process = 0;
  private byte[] data = null;
  private boolean isGetWidth = false;
  private boolean cameraErr = false;
  private boolean isFlash = false;
  private int drawHeight = 0;
  private int drawWidth = 0;
  private int drawPerConff = 4;
  private TRECAPIImpl engineDemo = null;

  public static void SetEngineType(TengineID tID) {
    tengineID = tID;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.engineDemo = ((TRECAPIImpl) getIntent().getSerializableExtra(
        "engine"));
    TStatus ret = this.engineDemo.TR_SetSupportEngine(tengineID);
    if (ret != TStatus.TR_OK) {
      Toast.makeText(getBaseContext(), "引擎不支持", 0).show();
      CloseView();
    }
    this.engineDemo.TR_SetParam(TParam.T_SET_HEADIMG, 1);
    requestWindowFeature(1);
    int flag = 1024;
    Window myWindow = getWindow();
    myWindow.setFlags(flag, flag);

    this.isGetdataok = -1;
    isPreview = false;
    this.issuccessfocus = 0;
    this.nfocusNum = 0;

    this.MyOpenThread.start();
    try {
      this.MyOpenThread.join();
    } catch (Exception e) {
      this.cameraErr = true;
    }
    if (this.cameraErr) {
      Toast.makeText(getBaseContext(), "请允许拍照权限,或者相机打开异常", 0).show();
      finish();
      return;
    }
    if (TakeBitmap != null) {
      TakeBitmap.recycle();
      TakeBitmap = null;
    }
    if (HeadImgBitmap != null) {
      HeadImgBitmap.recycle();
      HeadImgBitmap = null;
    }
    this.manager = ((WindowManager) getBaseContext().getSystemService(
        "window"));
    this.display = this.manager.getDefaultDisplay();

    initLayout();

    this.surfaceView.setZOrderOnTop(false);
    this.mySurfaceHolder = this.surfaceView.getHolder();
    this.mySurfaceHolder.setFormat(-2);

    this.mySurfaceHolder.addCallback(this);
    this.mySurfaceHolder.setType(3);

    this.myAutoFocusCallback = new Camera.AutoFocusCallback() {
      public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
          Log.i("yan", "myAutoFocusCallback: success...");
          TRCardScan.this.mHandler.sendEmptyMessageDelayed(222, 100L);
        } else {
          Toast.makeText(TRCardScan.this.getApplicationContext(),
              "对焦失败", 0).show();
          TRCardScan.this.mHandler.sendEmptyMessageDelayed(222, 300L);
          Log.i("yan", "myAutoFocusCallback: 失败");
        }
      }
    };
    this.myAutoFocusCallback1 = new Camera.AutoFocusCallback() {
      public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
          TRCardScan.this.issuccessfocus += 1;
          if (TRCardScan.this.issuccessfocus <= 1) {
            TRCardScan.this.mHandler.sendEmptyMessage(110);
          }
          Log.i("yan", "myAutoFocusCallback1: success..."
              + TRCardScan.this.issuccessfocus);
        } else {
          TRCardScan.this.nfocusNum += 1;
          if (TRCardScan.this.nfocusNum <= 2) {
            TRCardScan.this.mHandler.sendEmptyMessage(110);
          }
          Log.i("yan", "myAutoFocusCallback1: 失败...");
        }
      }
    };
    this.mHandler.sendEmptyMessageDelayed(110, 100L);
  }

  @SuppressLint({"NewApi"})
  public void initLayout() {
    FrameLayout main = new FrameLayout(this);
    main.setLayoutParams(new ActionBar.LayoutParams(-1, -1));

    this.surfaceView = new SurfaceView(this);
    LinearLayout.LayoutParams layoutParamsRoot = new LinearLayout.LayoutParams(
        -2, -2);
    layoutParamsRoot.gravity = 17;
    this.surfaceView.setLayoutParams(layoutParamsRoot);
    main.addView(this.surfaceView);

    RelativeLayout relativeLayout = new RelativeLayout(this);
    RelativeLayout.LayoutParams relayoutParams = new RelativeLayout.LayoutParams(
        -1, -1);
    relayoutParams.addRule(13);
    relativeLayout.setLayoutParams(relayoutParams);

    WindowManager manager = (WindowManager) getBaseContext()
        .getSystemService("window");
    Display display = manager.getDefaultDisplay();
    int textWidth = display.getWidth() / 2;
    this.logtxt = new TextView(getBaseContext());
    RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(
        textWidth, -2);
    Params.addRule(13);
    if (tengineID == TengineID.TIDCARD2) {
      this.logtxt.setText("请将身份证置于预览区中,尽量使身份证区域足够大!");
    } else if (tengineID == TengineID.TIDLPR) {
      this.logtxt.setText("请将车牌置于预览区中,尽量使识别区域足够大!");
    } else if (tengineID == TengineID.TIDJSZCARD) {
      this.logtxt.setText("请将驾驶证置于预览区中,尽量使识别区域足够大!");
    } else if (tengineID == TengineID.TIDXSZCARD) {
      this.logtxt.setText("请将行驶证置于预览区中,尽量使识别区域足够大!");
    } else {
      this.logtxt.setText("请将证件图片置于预览区中,尽量使身份证区域足够大!");
    }
    this.logtxt.setGravity(17);
    this.logtxt.setTextSize(16.0F);
    this.logtxt.setTextColor(-1);
    this.logtxt.setLayoutParams(Params);
    relativeLayout.addView(this.logtxt);

    main.addView(relativeLayout);

    Button tack_button = new Button(this);
    tack_button.setText(" 拍照 ");
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);

    lp.gravity = 5;
    tack_button.setLayoutParams(lp);

    tack_button.setGravity(17);
    tack_button.setTag("takepic");
    tack_button.setOnClickListener(initClickListener());

    Button back_button = new Button(this);
    back_button.setText(" 返回 ");
    back_button.setLayoutParams(lp);

    back_button.setGravity(17);
    back_button.setTag("back");
    back_button.setOnClickListener(initClickListener());

    LinearLayout linear = new LinearLayout(this);

    linear.setOrientation(1);
    linear.setGravity(17);
    linear.addView(tack_button);
    linear.addView(back_button);
    main.addView(linear);

    setContentView(main);
  }

  private View.OnClickListener initClickListener() {
    return new View.OnClickListener() {
      public void onClick(View v) {
        if (v.getTag().equals("takepic")) {
          if ((TRCardScan.isPreview)
              && (TRCardScan.this.myCamera != null)) {
            TRCardScan.isPreview = false;
            TRCardScan.this.mHandler.sendEmptyMessage(111);
          }
        } else if (v.getTag().equals("back")) {
          TRCardScan.this.CloseView();
        }
      }
    };
  }

  public boolean onTouchEvent(MotionEvent event) {
    if (this.myCamera != null) {
      this.myCamera.autoFocus(this.myAutoFocusCallback1);
    }
    return true;
  }

  public boolean openFlashlight() {
    Camera.Parameters parameters = this.myCamera.getParameters();
    parameters.setFlashMode("torch");
    try {
      this.myCamera.setParameters(parameters);
    } catch (Exception localException) {
    }
    return true;
  }

  public boolean closeFlashlight() {
    Camera.Parameters parameters = this.myCamera.getParameters();
    parameters.setFlashMode("off");
    try {
      this.myCamera.setParameters(parameters);
    } catch (Exception localException) {
    }
    return true;
  }

  public void CloseView() {
    Intent it = new Intent();
    it.putExtra("cardinfo", this.cardInfo);
    Bundle myBundle = new Bundle();
    myBundle.putParcelable("image", null);
    it.putExtras(myBundle);
    setResult(2, it);
    finish();
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {
    Log.i("yan", "SurfaceHolder.Callback:surfaceChanged!");
    initCamera();
  }

  public void surfaceCreated(SurfaceHolder holder) {
    if (this.myCamera == null) {
      this.myCamera = Camera.open();
      if (this.myCamera == null) {
        this.mHandler.sendEmptyMessage(555);
        return;
      }
    }
    try {
      this.myCamera.setPreviewDisplay(this.mySurfaceHolder);
      Log.i("yan", "SurfaceHolder.Callback: surfaceCreated!");
    } catch (IOException e) {
      if (this.myCamera != null) {
        this.myCamera.release();
        this.myCamera = null;
      }
      e.printStackTrace();
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    Log.i("yan", "SurfaceHolder.Callback：Surface Destroyed");
    if (this.myCamera != null) {
      this.myCamera.setPreviewCallback(null);
      this.myCamera.stopPreview();
      isPreview = false;
      this.myCamera.release();
      this.myCamera = null;
    }
  }

  public void initCamera() {
    if (isPreview) {
      this.myCamera.stopPreview();
    }
    if (this.myCamera != null) {
      Camera.Parameters myParam = this.myCamera.getParameters();

      myParam.setPictureFormat(256);
      myParam.setJpegQuality(100);

      DisplayMetrics displaysMetrics = new DisplayMetrics();

      getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
      int width = 0;
      int height = 0;
      width = displaysMetrics.widthPixels;
      height = displaysMetrics.heightPixels;
      myParam.setPictureSize(width, height);
      myParam.setPreviewSize(width, height);
      myParam.set("rotation", 0);
//			this.myCamera.setDisplayOrientation(0);
      setCameraDisplayOrientation(this, 0, myCamera);
      try {
        this.myCamera.setParameters(myParam);
      } catch (Exception e) {
        e.printStackTrace();
      }
      this.myCamera.startPreview();
      this.mHandler.sendEmptyMessageDelayed(110, 100L);
      isPreview = true;
    }
  }

  public void saveJpeg(Bitmap bm) {
    String jpegName = "";
    String savePath = GlobalData.SDCARD_ROOT_PATH + "/img/";
    File folder = new File(savePath);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    long dataTake = System.currentTimeMillis();
    jpegName = savePath + dataTake + ".jpg";
    try {
      FileOutputStream fout = new FileOutputStream(jpegName);
      BufferedOutputStream bos = new BufferedOutputStream(fout);
      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
      bos.flush();
      bos.close();
      Log.i("yan", "saveJpeg：存储完毕！");
    } catch (IOException e) {
      Log.i("yan", "saveJpeg:存储失败！");
      e.printStackTrace();
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 3) {
      setResult(2);
      finish();
    }
  }

  public void onBackPressed() {
    Intent it = new Intent();
    this.cardInfo.setNull();
    it.putExtra("cardinfo", this.cardInfo);
    setResult(1, it);
    finish();
    super.onBackPressed();
  }

  protected void onDestroy() {
    if (this.myCamera != null) {
      this.myCamera.stopPreview();
      isPreview = false;
      this.myCamera.release();
      this.myCamera = null;
    }
    this.mHandler.removeCallbacksAndMessages(null);
    super.onDestroy();
  }

  @SuppressLint("NewApi")
  public void setCameraDisplayOrientation(Context context, int cameraId,
                                          android.hardware.Camera camera) {
    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    android.hardware.Camera.getCameraInfo(cameraId, info);
    WindowManager windowManager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    int rotation = windowManager.getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
  }

  private static class MyThread extends Thread {
    WeakReference<TRCardScan> mThreadActivityRef;

    public MyThread(TRCardScan activity) {
      this.mThreadActivityRef = new WeakReference(activity);
    }

    public void run() {
      super.run();
      if (this.mThreadActivityRef == null) {
        return;
      }
      if (this.mThreadActivityRef.get() == null) {
        return;
      }
      if ((((TRCardScan) this.mThreadActivityRef.get()).isGetdataok == 1) && (((TRCardScan) this.mThreadActivityRef.get()).data != null)) {
        if (((TRCardScan) this.mThreadActivityRef.get()).data == null) {
          Toast.makeText(((TRCardScan) this.mThreadActivityRef.get()).getApplicationContext(), "拍照数据获取失败，请手动对焦", 0).show();
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera.startPreview();
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera.autoFocus(((TRCardScan) this.mThreadActivityRef.get()).myAutoFocusCallback1);
          return;
        }
        TStatus isRecSucess = TStatus.TR_FAIL;
        long beforeTime = 0L;
        long afterTime = 0L;

        Bitmap mBitmap = null;

        beforeTime = System.currentTimeMillis();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        mBitmap = BitmapFactory.decodeByteArray(((TRCardScan) this.mThreadActivityRef.get()).data, 0, ((TRCardScan) this.mThreadActivityRef.get()).data.length, options);
        TRCardScan.TakeBitmap = mBitmap;

        ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_LoadMemBitMap(TRCardScan.TakeBitmap);

        isRecSucess = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_RECOCR();
        ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_FreeImage();
        if (isRecSucess == TStatus.TR_OK) {
          if (TRCardScan.tengineID == TengineID.TIDCARD2) {
            String name = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.NAME);
            String sex = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.SEX);
            String folk = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.FOLK);
            String BirthDay = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.BIRTHDAY);
            String Address = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.ADDRESS);
            String CardNum = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.NUM);
            String issue = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.ISSUE);
            String period = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.PERIOD);
            String allinfo = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRStringBuf();
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.NAME, name);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.SEX, sex);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.FOLK, folk);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.BIRTHDAY, BirthDay);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.ADDRESS, Address);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.NUM, CardNum);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.ISSUE, issue);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.PERIOD, period);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setAllinfo(allinfo);
            byte[] hdata = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetHeadImgBuf();
            int size = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetHeadImgBufSize();
            if ((size > 0) && (hdata != null) && (hdata.length > 0)) {
//              ((TRCardScan)this.mThreadActivityRef.get());
              TRCardScan.HeadImgBitmap = BitmapFactory.decodeByteArray(hdata, 0, size);
            }
          } else if (TRCardScan.tengineID == TengineID.TIDLPR) {
            String CardNum = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.NUM);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.LPRNUM, CardNum);
            String allinfo = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRStringBuf();
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setAllinfo(allinfo);
          } else if (TRCardScan.tengineID == TengineID.TIDXSZCARD) {
            String DP_PLATENO = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_PLATENO);
            String DP_TYPE = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_TYPE);
            String DP_OWNER = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_OWNER);
            String DP_ADDRESS = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_ADDRESS);
            String DP_USECHARACTER = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_USECHARACTER);
            String DP_MODEL = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_MODEL);
            String DP_VIN = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_VIN);
            String DP_ENGINENO = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_ENGINENO);
            String DP_REGISTER_DATE = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_REGISTER_DATE);
            String DP_ISSUE_DATE = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DP_ISSUE_DATE);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_PLATENO, DP_PLATENO);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_TYPE, DP_TYPE);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_OWNER, DP_OWNER);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_ADDRESS, DP_ADDRESS);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_USECHARACTER, DP_USECHARACTER);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_MODEL, DP_MODEL);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_VIN, DP_VIN);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_ENGINENO, DP_ENGINENO);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_REGISTER_DATE, DP_REGISTER_DATE);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DP_ISSUE_DATE, DP_ISSUE_DATE);
            String allinfo = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRStringBuf();
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setAllinfo(allinfo);
          } else if (TRCardScan.tengineID == TengineID.TIDJSZCARD) {
            String DL_NUM = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_NUM);
            String DL_NAME = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_NAME);
            String DL_SEX = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_SEX);
            String DL_COUNTRY = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_COUNTRY);
            String DL_ADDRESS = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_ADDRESS);
            String DL_BIRTHDAY = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_BIRTHDAY);
            String DL_ISSUE_DATE = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_ISSUE_DATE);
            String DL_CLASS = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_CLASS);
            String DL_VALIDFROM = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_VALIDFROM);
            String DL_VALIDFOR = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRFieldStringBuf(TFieldID.DL_VALIDFOR);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_NUM, DL_NUM);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_NAME, DL_NAME);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_SEX, DL_SEX);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_COUNTRY, DL_COUNTRY);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_ADDRESS, DL_ADDRESS);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_BIRTHDAY, DL_BIRTHDAY);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_ISSUE_DATE, DL_ISSUE_DATE);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_CLASS, DL_CLASS);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_VALIDFROM, DL_VALIDFROM);
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setFieldString(TFieldID.DL_VALIDFOR, DL_VALIDFOR);
            String allinfo = ((TRCardScan) this.mThreadActivityRef.get()).engineDemo.TR_GetOCRStringBuf();
            ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setAllinfo(allinfo);
          }
        } else {
          ((TRCardScan) this.mThreadActivityRef.get()).cardInfo.setNull();
        }
        afterTime = System.currentTimeMillis();
        long timeDistance = afterTime - beforeTime;
        ((TRCardScan) this.mThreadActivityRef.get()).data = null;
        if (((TRCardScan) this.mThreadActivityRef.get()).myCamera != null) {
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera.setPreviewCallback(null);
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera.stopPreview();
//          ((TRCardScan)this.mThreadActivityRef.get());
          TRCardScan.isPreview = false;
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera.release();
          ((TRCardScan) this.mThreadActivityRef.get()).myCamera = null;
        }
        ((TRCardScan) this.mThreadActivityRef.get()).isGetdataok = 0;
        ((TRCardScan) this.mThreadActivityRef.get()).mHandler.sendEmptyMessage(555);
      }
    }
  }

  private static class MyOpenThread extends Thread {
    WeakReference<TRCardScan> mThreadActivityRef;

    public MyOpenThread(TRCardScan activity) {
      this.mThreadActivityRef = new WeakReference(activity);
    }

    public void run() {
      super.run();
      if (this.mThreadActivityRef == null) {
        return;
      }
      if (this.mThreadActivityRef.get() == null) {
        return;
      }
      try {
        ((TRCardScan) this.mThreadActivityRef.get()).myCamera = Camera
            .open();
      } catch (Exception e) {
        Log.i("yan", "Camera open fail!");
        ((TRCardScan) this.mThreadActivityRef.get()).cameraErr = true;
      }
      if ((((TRCardScan) this.mThreadActivityRef.get()).myCamera != null)
          && (((TRCardScan) this.mThreadActivityRef.get()).isFlash)) {
        ((TRCardScan) this.mThreadActivityRef.get()).openFlashlight();
      }
    }
  }

  private static class MyHandler extends Handler {
    private final WeakReference<TRCardScan> mActivity;

    public MyHandler(TRCardScan activity) {
      this.mActivity = new WeakReference(activity);
    }

    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 110:
          if ((((TRCardScan) this.mActivity.get()).myCamera != null)
              && (((TRCardScan) this.mActivity.get()).isGetdataok == -1)) {
            ((TRCardScan) this.mActivity.get()).myCamera
                .autoFocus(((TRCardScan) this.mActivity.get()).myAutoFocusCallback1);
          }
          break;
        case 111:
          if (((TRCardScan) this.mActivity.get()).myCamera != null) {
            ((TRCardScan) this.mActivity.get()).myCamera
                .autoFocus(((TRCardScan) this.mActivity.get()).myAutoFocusCallback);
          }
          break;
        case 222:
          ((TRCardScan) this.mActivity.get()).logtxt
              .setText("请稍后,正在识别中...");
          if (((TRCardScan) this.mActivity.get()).myCamera != null) {
            ((TRCardScan) this.mActivity.get()).myCamera
                .takePicture(
                    ((TRCardScan) this.mActivity.get()).myShutterCallback,
                    null,
                    ((TRCardScan) this.mActivity.get()).myJpegCallback);
          }
          break;
        case 333:
          ((TRCardScan) this.mActivity.get()).data = ((byte[]) msg.obj);
          if ((((TRCardScan) this.mActivity.get()).myCamera != null)
              && (((TRCardScan) this.mActivity.get()).isFlash)) {
            ((TRCardScan) this.mActivity.get()).closeFlashlight();
          }
          ((TRCardScan) this.mActivity.get()).isGetdataok = 1;
          ((TRCardScan) this.mActivity.get()).MyThread.start();
          break;
        case 555:
          if (((TRCardScan) this.mActivity.get()).isGetdataok == 0) {
            ((TRCardScan) this.mActivity.get()).CloseView();
          }
          break;
      }
    }
  }
}
