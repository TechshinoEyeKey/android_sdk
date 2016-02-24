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
import com.techshino.eyekeydemo.api.CheckAPI;
import com.techshino.eyekeydemo.conn.Constant;
import com.techshino.eyekeydemo.entity.Face;
import com.techshino.eyekeydemo.entity.FaceAttrs;
import com.techshino.eyekeydemo.entity.MatchVerify;
import com.techshino.eyekeydemo.utils.CustomUtil;
import com.techshino.eyekeydemo.utils.Logs;
import com.techshino.eyekeydemo.utils.MeasureUtil;
import com.techshino.eyekeydemo.utils.StringUtils;
import com.techshino.eyekeydemo.utils.ToastUtils;
import com.techshino.eyekeydemo.view.CameraSurfaceView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wangzhi on 2016/1/12.
 */
public class VerifyActivity extends BaseAppcompatActivity implements CameraSurfaceView.FaceCallback {

    private static final String TAG = VerifyActivity.class.getSimpleName();

    public static final String ARG_CAMERA_ID = VerifyActivity.class.getSimpleName() + ".camera_id";
    public static final String ARG_NAME = VerifyActivity.class.getSimpleName() + ".name";

    private int mCameraId;
    private String mName;
    private boolean isRefresh = true;

    private SoundPool mSoundPool;

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

    ProgressDialog mProgressDialog;

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
        mToolbar.setNavigationOnClickListener(v -> {
            VerifyActivity.this.finish();
        });

        setLayoutParams();
    }

    private void setLayoutParams() {
        int screenWidth = MeasureUtil.getScreenSize(this)[0];
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
        lp.height = screenWidth / 3 * 4;
        mSurfaceView.setLayoutParams(lp);
        mSurfaceView.setFaceCallback(this);

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
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify;
    }

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
        animSet.setDuration(300);
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

        final String dataImage = CustomUtil.bitmapToBase64(bitmaps[1]);
        Map<String, String> params = CustomUtil.getMapParams();
        params.put("img", dataImage);
        CheckAPI.checkingImageData(dataImage, null, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FaceAttrs>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mProgressDialog.setMessage("验证中...");
                        mProgressDialog.show();
                    }

                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onFailure:" + (e == null ? null : e.getMessage()));
                        mProgressDialog.dismiss();
                        ToastUtils.show(VerifyActivity.this, R.string.toast_network_error);
                        exit();
                    }

                    @Override
                    public void onNext(FaceAttrs faceAttrs) {
                        Log.i(TAG, "response:" + faceAttrs == null ? null : faceAttrs.toString());
                        handlerCheck(faceAttrs);
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
            ToastUtils.show(this, "人脸检测失败");
            verifyError();
            return;
        }
        if (!faces.isEmpty() && !StringUtils.isBlank(faces.get(0).getFace_id())) {
            matchVerify(faces.get(0).getFace_id());
        } else {
            ToastUtils.show(this, "认证失败");
            verifyError();
        }
    }

    /**
     * 比对
     */
    private void matchVerify(String faceId) {
        CheckAPI.matchVerify(faceId, mName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MatchVerify>() {
                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleted();
                        ToastUtils.show(VerifyActivity.this, "认证失败，请检查网络连接");
                    }

                    @Override
                    public void onNext(MatchVerify matchVerify) {
                        handleVerify(matchVerify);
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
