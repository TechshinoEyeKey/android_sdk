package com.techshino.eyekeydemo.activity;

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
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.techshino.eyekeydemo.R;
import com.techshino.eyekeydemo.utils.CustomUtil;
import com.techshino.eyekeydemo.utils.Logs;
import com.techshino.eyekeydemo.utils.MeasureUtil;
import com.techshino.eyekeydemo.utils.SnackBarUtils;
import com.techshino.eyekeydemo.utils.StringUtils;
import com.techshino.eyekeydemo.view.CameraSurfaceView;
import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeysdk.conn.Constant;
import com.techshino.eyekeysdk.entity.Face;
import com.techshino.eyekeysdk.entity.FaceAttrs;
import com.techshino.eyekeysdk.entity.PeopleAdd;
import com.techshino.eyekeysdk.entity.PeopleCreate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>
 * Created by wangzhi on 2015/12/28.
 * 注册
 */
public class RegisterActivity extends BaseAppcompatActivity implements CameraSurfaceView.FaceCallback {

  public static final String ARG_CAMERA_ID = RegisterActivity.class.getSimpleName() + ".camera_id";
  public static final String ARG_NAME = RegisterActivity.class.getSimpleName() + ".name";
  public static final String ARG_IS_CREATED = RegisterActivity.class.getSimpleName() + ".created";
  private static final String TAG = RegisterActivity.class.getSimpleName();
  @BindView(R.id.cameraSurface)
  CameraSurfaceView mSurfaceView;
  @BindView(R.id.img1)
  ImageView mImg1;
  @BindView(R.id.img2)
  ImageView mImg2;
  @BindView(R.id.img3)
  ImageView mImg3;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.takeBtn)
  ImageView mTakeBtn;
  @BindView(R.id.bgFrame)
  ImageView mBgFrame;
  ProgressDialog mProgressDialog;
  private String mName;
  /**
   * 是否已经创建people
   */
  private boolean isCreated;
  /**
   * 是否有faceId
   */
  private boolean hasFaceId = false;
  private List<String> mBase64List = new ArrayList<String>();
  private SoundPool mSoundPool;
  private String mFaceAll = "";

  @Override
  public void initData() {
    Intent intent = getIntent();
    int cameraId = intent.getIntExtra(ARG_CAMERA_ID, 1);
    mName = intent.getStringExtra(ARG_NAME);
    isCreated = intent.getBooleanExtra(ARG_IS_CREATED, false);

    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);

    mSurfaceView.setCameraId(cameraId);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
    mToolbar.setBackgroundColor(Color.TRANSPARENT);
    mToolbar.setNavigationOnClickListener(v -> RegisterActivity.this.finish());
    mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
    try {
      mSoundPool.load(getAssets().openFd("prompt.wav"), 1);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 设置拍照回掉接口
    mSurfaceView.setFaceCallback(this);

    if (CustomUtil.isLandScape(this)) {
      setLandLayoutParams();
    } else {
      setPortraitLayoutParams();
    }
  }

  private void setLandLayoutParams() {
    mToolbar.setVisibility(View.GONE);

    int screenWidth = MeasureUtil.getScreenSize(this)[0];
    int screenHeight = MeasureUtil.getScreenSize(this)[1];
    Logs.i(TAG, "screenWidth:" + screenWidth);
    Logs.i(TAG, "screenHeight:" + screenHeight);
    int mSurfaceViewWidth = screenHeight / 3 * 4;
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
    lp.width = mSurfaceViewWidth;
    mSurfaceView.setLayoutParams(lp);

    mImg1.post(() -> {
      LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mImg1.getLayoutParams();
      lp1.width = mImg1.getMeasuredHeight();
      Log.i(TAG, "img1 width:" + mImg1.getWidth());
      mImg1.setLayoutParams(lp1);

      LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mImg2.getLayoutParams();
      lp2.width = mImg2.getMeasuredHeight();
      mImg2.setLayoutParams(lp2);

      LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) mImg3.getLayoutParams();
      lp3.width = mImg3.getMeasuredHeight();
      mImg3.setLayoutParams(lp3);
    });

    mTakeBtn.setAdjustViewBounds(true);

    FrameLayout.LayoutParams bgLp = (FrameLayout.LayoutParams) mBgFrame.getLayoutParams();
    bgLp.height = screenHeight / 5 * 3;
    bgLp.width = bgLp.height / 4 * 3;
    mBgFrame.setLayoutParams(bgLp);
  }

  private void setPortraitLayoutParams() {
    int screenWidth = MeasureUtil.getScreenSize(this)[0];
    int mSurfaceViewHeight = screenWidth / 3 * 4;
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
    lp.height = mSurfaceViewHeight;
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

    FrameLayout.LayoutParams bgLp = (FrameLayout.LayoutParams) mBgFrame.getLayoutParams();
    bgLp.height = mSurfaceViewHeight / 5 * 3;
    bgLp.width = screenWidth / 5 * 3;
    mBgFrame.setLayoutParams(bgLp);
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

    mTakeBtn.postDelayed(this::startTakeAnim, 600);
  }

  private void startTakeAnim() {
    ObjectAnimator takeAnim1 = ObjectAnimator.ofFloat(mTakeBtn, "scaleY", 0f, 1f);
    ObjectAnimator takeAnim2 = ObjectAnimator.ofFloat(mTakeBtn, "scaleX", 0f, 1f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
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
    Call<FaceAttrs> call = CheckAPI.checkingImageByBase64(base64, null, null);
    call.enqueue(new Callback<FaceAttrs>() {

      void onFinish() {
        if (!mBase64List.isEmpty()) {
          mBase64List.remove(0);
        }
        if (!mBase64List.isEmpty()) {
          getImageData(mBase64List.get(0));
        } else {
          addFaceId();
        }
      }

      @Override
      public void onResponse(Call<FaceAttrs> call, Response<FaceAttrs> response) {
        handleFaceData(response.body());
        onFinish();
      }

      @Override
      public void onFailure(Call<FaceAttrs> call, Throwable t) {
        onFinish();
      }
    });
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
      createPeople();
      return;
    }
    Call<PeopleAdd> call = CheckAPI.peopleAddByName(mFaceAll, mName);
    call.enqueue(new Callback<PeopleAdd>() {

      void onFinish() {
        mProgressDialog.dismiss();
      }

      @Override
      public void onResponse(Call<PeopleAdd> call, Response<PeopleAdd> response) {
        onFinish();
        handleAdd(response.body());
      }

      @Override
      public void onFailure(Call<PeopleAdd> call, Throwable t) {
        onFinish();
        SnackBarUtils.showError(mSurfaceView, R.string.toast_enroll_failed);
        exit();
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
  private void createPeople() {
    Logs.i(TAG, mFaceAll);
    Call<PeopleCreate> call = CheckAPI.peopleCreate(mFaceAll, mName, null, null);
    call.enqueue(new Callback<PeopleCreate>() {

      private void onFinish() {
        mProgressDialog.dismiss();
      }

      @Override
      public void onResponse(Call<PeopleCreate> call, Response<PeopleCreate> response) {
        onFinish();
        handleCreate(response.body());
      }

      @Override
      public void onFailure(Call<PeopleCreate> call, Throwable t) {
        onFinish();
        SnackBarUtils.showError(mSurfaceView, R.string.toast_network_error);
        exit();
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
    new Handler().postDelayed(this::finish, 1000);
  }
}
