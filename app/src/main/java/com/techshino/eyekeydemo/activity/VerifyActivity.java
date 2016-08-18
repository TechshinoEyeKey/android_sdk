package com.techshino.eyekeydemo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techshino.eyekeydemo.R;
import com.techshino.eyekeydemo.utils.CustomUtil;
import com.techshino.eyekeydemo.utils.Logs;
import com.techshino.eyekeydemo.utils.MeasureUtil;
import com.techshino.eyekeydemo.utils.StringUtils;
import com.techshino.eyekeydemo.utils.ToastUtils;
import com.techshino.eyekeydemo.view.CameraSurfaceView;
import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeysdk.conn.Constant;
import com.techshino.eyekeysdk.entity.Face;
import com.techshino.eyekeysdk.entity.FaceAttrs;
import com.techshino.eyekeysdk.entity.MatchVerify;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangzhi on 2016/1/12.
 * 验证
 */
public class VerifyActivity extends BaseAppcompatActivity implements CameraSurfaceView.FaceCallback {

  public static final String ARG_CAMERA_ID = VerifyActivity.class.getSimpleName() + ".camera_id";
  public static final String ARG_NAME = VerifyActivity.class.getSimpleName() + ".name";
  private static final String TAG = VerifyActivity.class.getSimpleName();
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
  @Bind(R.id.verifyLayout)
  LinearLayout mVerifyLayout;
  @Bind(R.id.verifyImg)
  ImageView mVerifyImg;
  @Bind(R.id.verifyText)
  TextView mVerifyText;
  @Bind(R.id.bgFrame)
  ImageView mBgFrame;
  ProgressDialog mProgressDialog;
  private int mCameraId;
  private String mName;
  private boolean isRefresh = true;
  private SoundPool mSoundPool;

  @Override
  public void initData() {
    Intent intent = getIntent();
    mCameraId = intent.getIntExtra(ARG_CAMERA_ID, 1);
    mName = intent.getStringExtra(ARG_NAME);

    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);

    mSurfaceView.setCameraId(mCameraId);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
    mToolbar.setBackgroundColor(Color.TRANSPARENT);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        VerifyActivity.this.finish();
      }
    });

    // 设置拍照回掉接口
    mSurfaceView.setFaceCallback(this);
    setLayoutParams();
  }

  private void setLayoutParams() {
    int screenWidth = MeasureUtil.getScreenSize(this)[0];
    int mSurfaceViewHeight = screenWidth / 3 * 4;
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
    lp.height = mSurfaceViewHeight;
    mSurfaceView.setLayoutParams(lp);

    mImg1.post(new Runnable() {
      @Override
      public void run() {
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
      }
    });

    FrameLayout.LayoutParams bgLp = (FrameLayout.LayoutParams) mBgFrame.getLayoutParams();
    bgLp.height = mSurfaceViewHeight / 5 * 3;
    bgLp.width = screenWidth / 5 * 3;
    mBgFrame.setLayoutParams(bgLp);
    mTakeBtn.setAdjustViewBounds(true);
    mVerifyImg.setAdjustViewBounds(true);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_verify;
  }

  /**
   * 拍照
   */
  @OnClick(R.id.takeBtn)
  void takeBtnClick() {
    mSurfaceView.startCapture();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mSurfaceView.onResume();
    mTakeBtn.setScaleX(0f);
    mTakeBtn.setScaleY(0f);

    mTakeBtn.postDelayed(new Runnable() {
      @Override
      public void run() {
        startTakeAnim();
      }
    }, 600);

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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_refresh) {
      actionRefresh();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void actionRefresh() {
    if (isRefresh)
      return;
    isRefresh = true;
    mSurfaceView.getCameraManager().startPreview();
    ObjectAnimator takeAnim1 = ObjectAnimator.ofFloat(mTakeBtn, "translationY", 300f, 0f);
    ObjectAnimator takeAnim2 = ObjectAnimator.ofFloat(mTakeBtn, "alpha", 0f, 1f);

    ObjectAnimator animator1 = ObjectAnimator.ofFloat(mVerifyLayout, "translationY", 0f, -300f);
    ObjectAnimator animator2 = ObjectAnimator.ofFloat(mVerifyLayout, "alpha", 1f, 0f);

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    animSet.setInterpolator(new OvershootInterpolator());
    //两个动画同时执行
    animSet.playTogether(animator1, animator2, takeAnim1, takeAnim2);
    animSet.start();
    mImg1.setImageBitmap(null);
  }

  @Override
  public void onResullt(Bitmap[] bitmaps) {
    mImg1.setImageBitmap(bitmaps[0]);
    mImg2.setImageBitmap(bitmaps[1]);
    mImg3.setImageBitmap(bitmaps[2]);

    checkImageData(bitmaps);
  }

  private void checkImageData(Bitmap[] bitmaps) {
    if (bitmaps == null) {
      ToastUtils.show(this, "检测失败");
      return;
    }

    String dataImage = CustomUtil.bitmapToBase64(bitmaps[1]);
    mProgressDialog.setMessage("验证中...");
    mProgressDialog.show();
    Call<FaceAttrs> call = CheckAPI.checkingImageByBase64(dataImage, null, null);
    call.enqueue(new Callback<FaceAttrs>() {
      @Override
      public void onResponse(Call<FaceAttrs> call, Response<FaceAttrs> response) {
        FaceAttrs faceAttrs = response.body();
        Log.i(TAG, "response:" + faceAttrs == null ? null : faceAttrs.toString());
        handlerCheck(faceAttrs);
      }

      @Override
      public void onFailure(Call<FaceAttrs> call, Throwable t) {
        Log.e(TAG, "onFailure:" + (t == null ? null : t.getMessage()));
        mProgressDialog.dismiss();
        ToastUtils.show(VerifyActivity.this, R.string.toast_network_error);
        exit();
      }
    });
  }

  private void handlerCheck(FaceAttrs data) {
    Logs.i(TAG, data.toString());
    if (data == null) {
      ToastUtils.show(this, "认证失败");
      return;
    }
    List<Face> faces = data.getFace();
    if (!StringUtils.isEquals(Constant.RES_CODE_0000, data.getRes_code()) || faces == null) {
      mProgressDialog.dismiss();
      ToastUtils.show(this, "人脸检测失败");
      verifyError();
      return;
    }
    if (!faces.isEmpty() && !StringUtils.isBlank(faces.get(0).getFace_id())) {
      matchVerify(faces.get(0).getFace_id());
    } else {
      mProgressDialog.dismiss();
      ToastUtils.show(this, "认证失败");
      verifyError();
    }
  }

  /**
   * 比对
   */
  private void matchVerify(String faceId) {
    Call<MatchVerify> call = CheckAPI.matchVerify(faceId, mName);
    call.enqueue(new Callback<MatchVerify>() {

      public void onFinish() {
        mProgressDialog.dismiss();
      }

      @Override
      public void onResponse(Call<MatchVerify> call, Response<MatchVerify> response) {
        onFinish();
        handleVerify(response.body());
      }

      @Override
      public void onFailure(Call<MatchVerify> call, Throwable t) {
        onFinish();
        ToastUtils.show(VerifyActivity.this, "认证失败，请检查网络连接");
      }
    });
  }

  private void handleVerify(MatchVerify data) {
    Logs.i(TAG, "matchVerify=" + data);
    if (data == null) {
      ToastUtils.show(this, "认证失败");
      verifyError();
      return;
    }
    mVerifyLayout.setVisibility(View.VISIBLE);
    if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.isResult() == true) {
      ToastUtils.show(this, "认证成功");
      verifySuc(data.getSimilarity());
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.isResult() == false) {
      ToastUtils.show(this, "认证失败，不是同一个人");
      verifyFail(data.getSimilarity());
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_1025)) {
      ToastUtils.show(this, "用户不存在");
      verifyError();
    } else {
      ToastUtils.show(this, "认证失败");
      verifyError();
    }
  }

  private void verifyError() {
    mVerifyLayout.setVisibility(View.VISIBLE);
    mVerifyImg.setImageResource(R.drawable.verify_fail);
    mVerifyText.setText("");
    startVerifyAnim();
  }

  private void verifyFail(double similarity) {
    mVerifyText.setText(similarity + "");
    mVerifyImg.setImageResource(R.drawable.verify_fail);

    startVerifyAnim();
  }

  private void verifySuc(double similarity) {
    mVerifyText.setText(similarity + "");
    mVerifyImg.setImageResource(R.drawable.verify_suc);

    startVerifyAnim();
  }

  private void startVerifyAnim() {
    isRefresh = false;
    ObjectAnimator takeAnim1 = ObjectAnimator.ofFloat(mTakeBtn, "translationY", 0f, 300f);
    ObjectAnimator takeAnim2 = ObjectAnimator.ofFloat(mTakeBtn, "alpha", 1f, 0f);

    ObjectAnimator animator1 = ObjectAnimator.ofFloat(mVerifyLayout, "translationY", -300f, 0f);
    ObjectAnimator animator2 = ObjectAnimator.ofFloat(mVerifyLayout, "alpha", 0f, 1f);

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(300);
    animSet.setInterpolator(new OvershootInterpolator());
    //两个动画同时执行
    animSet.playTogether(animator1, animator2, takeAnim1, takeAnim2);
    animSet.start();
  }

  private void exit() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        finish();
      }
    }, 1000);
  }
}
