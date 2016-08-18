package com.techshino.eyekeydev.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.techshino.eyekeydev.R;
import com.techshino.eyekeydev.api.CheckAPI;
import com.techshino.eyekeydev.conn.Constant;
import com.techshino.eyekeydev.entity.Face;
import com.techshino.eyekeydev.entity.FaceAttrs;
import com.techshino.eyekeydev.entity.PeopleAdd;
import com.techshino.eyekeydev.entity.PeopleCreate;
import com.techshino.eyekeydev.utils.CustomUtil;
import com.techshino.eyekeydev.utils.Logs;
import com.techshino.eyekeydev.utils.MeasureUtil;
import com.techshino.eyekeydev.utils.SnackBarUtils;
import com.techshino.eyekeydev.utils.StringUtils;
import com.techshino.eyekeydev.view.CameraSurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * camera预览Activity
 * <p>
 * Created by wangzhi on 2015/12/28.
 */
public class RegisterActivity extends BaseAppcompatActivity implements CameraSurfaceView.FaceCallback {

  public static final String ARG_CAMERA_ID = RegisterActivity.class.getSimpleName() + ".camera_id";
  public static final String ARG_NAME = RegisterActivity.class.getSimpleName() + ".name";
  public static final String ARG_IS_CREATED = RegisterActivity.class.getSimpleName() + ".created";
  private static final String TAG = RegisterActivity.class.getSimpleName();
  @Bind(R.id.cameraSurface)
  CameraSurfaceView mSurfaceView;
  @Bind(R.id.img1)
  ImageView mImg1;
  @Bind(R.id.img2)
  ImageView mImg2;
  @Bind(R.id.img3)
  ImageView mImg3;
  @Bind(R.id.toolbar)
  Toolbar mToolbar;
  @Bind(R.id.takeBtn)
  ImageView mTakeBtn;
  ProgressDialog mProgressDialog;
  private int mCameraId;
  private String mName;
  /**
   * 是否已经创建people
   */
  private boolean isCreated;
  private List<String> mBase64List = new ArrayList<String>();
  private SoundPool mSoundPool;
  /**
   * 是否有faceId
   */
  private boolean hasFaceId = false;
  private String mFaceAll = "";

  @Override
  public void initData() {
    Intent intent = getIntent();
    mCameraId = intent.getIntExtra(ARG_CAMERA_ID, 1);
    mName = intent.getStringExtra(ARG_NAME);
    isCreated = intent.getBooleanExtra(ARG_IS_CREATED, false);

    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);

    mSurfaceView.setCameraId(mCameraId);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
    mToolbar.setBackgroundColor(Color.TRANSPARENT);
    mToolbar.setNavigationOnClickListener(v -> {
      RegisterActivity.this.finish();
    });
    mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
    try {
      mSoundPool.load(getAssets().openFd("prompt.wav"), 1);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mSurfaceView.setFaceCallback(this);
    setLayoutParams();
  }

  private void setLayoutParams() {
    int screenWidth = MeasureUtil.getScreenSize(this)[0];
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
    lp.height = screenWidth / 3 * 4;
    mSurfaceView.setLayoutParams(lp);

    mImg1.post(() -> {
      LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mImg1.getLayoutParams();
      lp1.height = mImg1.getMeasuredWidth();
      Log.i(TAG, "img1 width:" + mImg1.getWidth());
      mImg1.setLayoutParams(lp1);

      LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mImg2.getLayoutParams();
      lp2.height = mImg2.getMeasuredWidth();
      mImg2.setLayoutParams(lp2);

      LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) mImg3.getLayoutParams();
      lp3.height = mImg3.getMeasuredWidth();
      mImg3.setLayoutParams(lp3);
    });
    mTakeBtn.setAdjustViewBounds(true);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_register;
  }

  @OnClick(R.id.takeBtn)
  void takeBtnClick() {
    mSurfaceView.startCapture();
    mTakeBtn.setClickable(false);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mSurfaceView.onResume();

    mTakeBtn.setScaleX(0f);
    mTakeBtn.setScaleY(0f);

    mTakeBtn.postDelayed(() -> {
      startTakeAnim();
    }, 500);
  }

  private void startTakeAnim() {
    ObjectAnimator takeAnim1 = ObjectAnimator.ofFloat(mTakeBtn, "scaleY", 0f, 1f);
    ObjectAnimator takeAnim2 = ObjectAnimator.ofFloat(mTakeBtn, "scaleX", 0f, 1f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(300);
    animSet.setInterpolator(new OvershootInterpolator());
    //两个动画同时执行
    animSet.playTogether(takeAnim1, takeAnim2);
    animSet.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mSurfaceView.onPause();
  }

  @Override
  public void onResullt(Bitmap[] bitmaps) {
    if (bitmaps == null)
      return;
    mImg1.setImageBitmap(bitmaps[0]);
    mImg2.setImageBitmap(bitmaps[1]);
    mImg3.setImageBitmap(bitmaps[2]);

    String bmp0 = CustomUtil.bitmapToBase64(bitmaps[0]);
    String bmp1 = CustomUtil.bitmapToBase64(bitmaps[1]);
    String bmp2 = CustomUtil.bitmapToBase64(bitmaps[2]);

    mBase64List.add(bmp0);
    mBase64List.add(bmp1);
    mBase64List.add(bmp2);

    mProgressDialog.setMessage(CustomUtil.getString(this, R.string.text_enroll));
    mProgressDialog.show();
    getImageData(mBase64List.get(0));
  }

  /**
   * 获取单张人脸信息
   *
   * @param base64
   */
  private void getImageData(String base64) {
    CheckAPI.checkingImageData(base64, null, null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<FaceAttrs>() {

          @Override
          public void onStart() {
            super.onStart();
          }

          @Override
          public void onCompleted() {
            testNextImg();
          }

          @Override
          public void onError(Throwable e) {
            testNextImg();
          }

          @Override
          public void onNext(FaceAttrs faceAttrs) {
            handleFaceData(faceAttrs);
          }
        });
  }

  private void testNextImg() {
    if (!mBase64List.isEmpty()) {
      mBase64List.remove(0);
    }
    if (!mBase64List.isEmpty()) {
      getImageData(mBase64List.get(0));
    } else {
      addFaceId();
    }
  }

  private void handleFaceData(FaceAttrs data) {
    Logs.i(TAG, data.toString());
    List<Face> faces = data.getFace();
    if (StringUtils.isEquals("0000", data.getRes_code()) && faces != null) {
      hasFaceId = true;
      mFaceAll += faces.get(0).getFace_id() + ",";
    }
  }

  /**
   * 添加faceId
   */
  private void addFaceId() {
    if (!hasFaceId) {
      mProgressDialog.dismiss();
      SnackBarUtils.showError(mSurfaceView, R.string.toast_no_faceId);
      exit();
      return;
    }
    if (!isCreated) {
      createPeoplle();
      return;
    }
    CheckAPI.peopleAdd(mFaceAll, mName)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<PeopleAdd>() {
          @Override
          public void onCompleted() {
            mProgressDialog.dismiss();
          }

          @Override
          public void onError(Throwable e) {
            onCompleted();
            SnackBarUtils.showError(mSurfaceView, R.string.toast_enroll_failed);
            exit();
          }

          @Override
          public void onNext(PeopleAdd peopleAdd) {
            handleAdd(peopleAdd);
          }
        });
  }

  private void handleAdd(PeopleAdd data) {
    Logs.i(TAG, "添加faceId:" + data.toString());
    if (StringUtils.isEquals(Constant.RES_CODE_0000, data.getRes_code())) {
      SnackBarUtils.show(mSurfaceView, R.string.toast_enroll_success);
    } else {
      SnackBarUtils.show(mSurfaceView, R.string.toast_enroll_failed);
    }
    exit();
  }

  /**
   * 创建用户
   */
  private void createPeoplle() {
    CheckAPI.peopleCreate(mFaceAll, mName, null, null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<PeopleCreate>() {
          @Override
          public void onCompleted() {
            mProgressDialog.dismiss();
          }

          @Override
          public void onError(Throwable e) {
            onCompleted();
            SnackBarUtils.showError(mSurfaceView, R.string.toast_network_error);
            exit();
          }

          @Override
          public void onNext(PeopleCreate peopleCreate) {
            handleCreate(peopleCreate);
          }
        });
  }

  private void handleCreate(PeopleCreate data) {
    Logs.i(TAG, data.toString());
    if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000)) {
      SnackBarUtils.show(mSurfaceView, R.string.toast_enroll_success);
      exit();
    } else {
      mProgressDialog.dismiss();
      SnackBarUtils.showError(mSurfaceView, R.string.toast_enroll_failed);
      exit();
    }
  }

  private void exit() {
    new Handler().postDelayed(() -> {
      finish();
    }, 1000);
  }
}
