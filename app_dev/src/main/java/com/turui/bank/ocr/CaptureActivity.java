package com.turui.bank.ocr;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idcard.TParam;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;

import java.io.IOException;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
  private static final float BEEP_VOLUME = 0.1F;
  private static final long VIBRATE_DURATION = 200L;
  public static Bitmap TakeBitmap = null;
  public static Bitmap SmallBitmap = null;
  private static boolean isLight = false;
  private static boolean isShowBack = false;
  private static String showText = "请将银行卡置于此区域尝试对齐边缘";
  private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
    public void onCompletion(MediaPlayer mediaPlayer) {
      mediaPlayer.seekTo(0);
    }
  };
  public TRECAPIImpl engineDemo = null;
  private CaptureActivityHandler handler;
  private ViewfinderView viewfinderView;
  private boolean hasSurface;
  private String characterSet;
  private InactivityTimer inactivityTimer;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;
  private boolean vibrate;
  private boolean isShow = false;
  private ProgressBar pg;
  private ImageView iv_pg_bg_grey;
  private SurfaceView surfaceView;
  private TextView logtxt;

  public static void SetDisplayLight(boolean init) {
    isLight = init;
  }

  public static void SetShowBackBotton(boolean init) {
    isShowBack = init;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.engineDemo = ((TRECAPIImpl) getIntent().getSerializableExtra(
        "engine"));
    if (this.engineDemo.TR_SetSupportEngine(TengineID.TIDBANK) == TStatus.TR_FAIL) {
      Toast.makeText(getBaseContext(), "引擎不支持", 0).show();

      Intent zhuceIntent = new Intent();
      Bundle myBundle = new Bundle();
      myBundle.putString("result", "");
      myBundle.putParcelable("image", null);

      zhuceIntent.putExtras(myBundle);
      setResult(1, zhuceIntent);
      Capfinish();
    }
    TStatus tStatus = this.engineDemo.TR_SetParam(
        TParam.T_SET_BANK_RECMODE, 0);
    initLayout();

    CameraManager.init(getApplication());
    getWindow().addFlags(128);

    this.hasSurface = false;
    this.inactivityTimer = new InactivityTimer(this);
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

    this.viewfinderView = new ViewfinderView(this);
    LinearLayout.LayoutParams viewfindlayoutParams = new LinearLayout.LayoutParams(
        -2, -2);
    main.addView(this.viewfinderView);

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
    this.logtxt.setText(showText);
    this.logtxt.setGravity(17);
    this.logtxt.setTextSize(16.0F);
    this.logtxt.setTextColor(-1);
    this.logtxt.setLayoutParams(Params);
    relativeLayout.addView(this.logtxt);
    if (isShowBack) {
      Button button = new Button(getBaseContext());
      RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(
          -2, -2);
      bParams.addRule(9);
      button.setText("返回");
      button.setTag("back");
      button.setBackgroundColor(Color.parseColor("#00000000"));
      button.setOnClickListener(initClickListener());
      button.setLayoutParams(bParams);
      relativeLayout.addView(button);
    }
    main.addView(relativeLayout);
    setContentView(main);
  }

  protected void onResume() {
    super.onResume();

    SurfaceHolder surfaceHolder = this.surfaceView.getHolder();
    if (this.hasSurface) {
      initCamera(surfaceHolder);
    } else {
      surfaceHolder.addCallback(this);
      surfaceHolder.setType(3);
    }
    this.characterSet = null;

    this.playBeep = true;
    AudioManager audioService = (AudioManager) getSystemService("audio");
    if (audioService.getRingerMode() != 2) {
      this.playBeep = false;
    }
    this.vibrate = true;
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == 4) && (event.getRepeatCount() == 0)) {
      Intent zhuceIntent = new Intent();
      Bundle myBundle = new Bundle();
      myBundle.putString("result", "");
      myBundle.putParcelable("image", null);

      zhuceIntent.putExtras(myBundle);
      setResult(1, zhuceIntent);
      Capfinish();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  protected void onPause() {
    super.onPause();
    if (this.handler != null) {
      this.handler.quitSynchronously();
      this.handler = null;
    }
    CameraManager.get().closeDriver();
  }

  protected void Capfinish() {
    finish();
  }

  protected void onDestroy() {
    this.inactivityTimer.shutdown();
    super.onDestroy();
  }

  private View.OnClickListener initClickListener() {
    return new View.OnClickListener() {
      public void onClick(View v) {
        if (v.getTag().equals("back")) {
          Intent zhuceIntent = new Intent();
          Bundle myBundle = new Bundle();
          myBundle.putString("result", "");
          myBundle.putParcelable("image", null);

          zhuceIntent.putExtras(myBundle);
          CaptureActivity.this.setResult(1, zhuceIntent);
          CaptureActivity.this.Capfinish();
        }
      }
    };
  }

  public void handleDecode(String result, Bitmap barcode) {
    this.inactivityTimer.onActivity();
    playBeepSoundAndVibrate();
    String resultString = result;
    if (resultString.equals("")) {
      Toast.makeText(this, "Scan failed!", 0).show();
    } else {
      if ((this.pg != null) && (this.pg.isShown())) {
        this.pg.setVisibility(8);
        this.iv_pg_bg_grey.setVisibility(0);
      }
      Intent zhuceIntent = new Intent();
      Bundle myBundle = new Bundle();
      myBundle.putString("result", resultString);
      myBundle.putParcelable("image", barcode);

      zhuceIntent.putExtras(myBundle);
      setResult(1, zhuceIntent);

      Capfinish();
    }
  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    try {
      CameraManager.get().openDriver(surfaceHolder);
    } catch (IOException ioe) {
      return;
    } catch (RuntimeException e) {
      String scanResult = "您已禁止应用程序调用摄像头！请在安全助手中授权！";
      this.logtxt.setText(scanResult);

      return;
    }
    if (this.handler == null) {
      LightControl mLightControl = new LightControl();
      if (isLight) {
        mLightControl.turnOn();
      } else {
        mLightControl.turnOff();
      }
      this.handler = new CaptureActivityHandler(this, this.characterSet);
    }
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {
  }

  public void surfaceCreated(SurfaceHolder holder) {
    if (!this.hasSurface) {
      this.hasSurface = true;
      initCamera(holder);
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    this.hasSurface = false;
  }

  public ViewfinderView getViewfinderView() {
    return this.viewfinderView;
  }

  public Handler getHandler() {
    return this.handler;
  }

  public void drawViewfinder() {
    this.viewfinderView.drawViewfinder();
  }

  private void playBeepSoundAndVibrate() {
    if ((this.playBeep) && (this.mediaPlayer != null)) {
      this.mediaPlayer.start();
    }
    if (this.vibrate) {
      Vibrator vibrator = (Vibrator) getSystemService("vibrator");
      vibrator.vibrate(200L);
    }
  }
}
