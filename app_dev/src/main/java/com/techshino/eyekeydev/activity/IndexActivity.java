package com.techshino.eyekeydev.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.idcard.CardInfo;
import com.idcard.TFieldID;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.techshino.eyekeydev.R;
import com.techshino.eyekeydev.api.CheckAPI;
import com.techshino.eyekeydev.conn.Constant;
import com.techshino.eyekeydev.entity.PeopleDelete;
import com.techshino.eyekeydev.entity.PeopleGet;
import com.techshino.eyekeydev.utils.CustomUtil;
import com.techshino.eyekeydev.utils.Logs;
import com.techshino.eyekeydev.utils.SnackBarUtils;
import com.techshino.eyekeydev.utils.StringUtils;
import com.techshino.eyekeydev.utils.ToastUtils;
import com.ui.card.TRCardScan;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangzhi on 2015/12/25.
 */
public class IndexActivity extends BaseAppcompatActivity {

  private static final String TAG = IndexActivity.class.getSimpleName();
  private static final String OFFCIAL_WEB_URL = "http://www.eyekey.com";
  private static final int ACTION_VERIFY = 1;
  private static final int ACTION_REGISTER = 2;
  public int RESULT_GET_CARD_OK = 1;
  @Bind(R.id.toolbar)
  Toolbar mToolbar;
  @Bind(R.id.toolbarTitle)
  TextView mTitleText;
  @Bind(R.id.container)
  CoordinatorLayout mContainer;
  @Bind(R.id.nameEdit)
  EditText mNameEdit;
  ProgressDialog mProgressDialog;
  private TRECAPIImpl engineDemo = new TRECAPIImpl();
  private TengineID tengineID = TengineID.TUNCERTAIN;
  private int mAction;
  private int mCameraId = 1;
  private String mName;

  @Override
  public void initData() {
    mProgressDialog = new ProgressDialog(this);
    mTitleText.setText(R.string.app_title);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("");

    TStatus tStatus = engineDemo.TR_StartUP();
    if (tStatus == TStatus.TR_TIME_OUT) {
      Toast.makeText(getBaseContext(), "引擎过期", Toast.LENGTH_SHORT).show();
    } else if (tStatus == TStatus.TR_FAIL) {
      Toast.makeText(getBaseContext(), "引擎初始化失败", Toast.LENGTH_SHORT).show();
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        startAnimation();
      }
    }, 1000);
  }

  private void startAnimation() {
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_index;
  }

  @OnTextChanged(callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED, value = R.id.nameEdit)
  void etNameEdit(Editable editable) {
    mName = editable.toString();
  }

  @OnClick(R.id.ocrBtn)
  void ocrBtnClick() {
    TRCardScan.SetEngineType(TengineID.TIDCARD2);
    tengineID = TengineID.TIDCARD2;

    Intent intent = new Intent(IndexActivity.this, TRCardScan.class);
    intent.putExtra("engine", engineDemo);
    startActivityForResult(intent, RESULT_GET_CARD_OK);

  }

  @OnClick(R.id.takeImg)
  void verifyIconClick() {
    mAction = ACTION_VERIFY;
    peopleGet();
  }

  @OnClick(R.id.registerIcon)
  void registerIconClick() {
    mAction = ACTION_REGISTER;
    peopleGet();
  }

  @OnClick(R.id.resetIcon)
  void resetIconClick() {
    if (StringUtils.isBlank(mName)) {
      ToastUtils.show(this, R.string.text_name_not_empty);
      return;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(CustomUtil.getString(this, R.string.text_delete_confirm))
        .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            peopleDelete();
          }
        })
        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).show();
  }

  private void peopleDelete() {
    CheckAPI.peopleDelete(mName)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<PeopleDelete>() {

          @Override
          public void onStart() {
            super.onStart();
            mProgressDialog.setMessage(CustomUtil.getString(IndexActivity.this, R.string.text_deleting));
            mProgressDialog.show();
          }

          @Override
          public void onCompleted() {
            mProgressDialog.dismiss();
          }

          @Override
          public void onError(Throwable e) {
            ToastUtils.show(IndexActivity.this, R.string.toast_network_error);
            mProgressDialog.dismiss();
          }

          @Override
          public void onNext(PeopleDelete peopleDelete) {
            handleDelete(peopleDelete);
          }
        });
  }

  @OnClick(R.id.offcialIcon)
  void offcialIconClick() {
    Uri uri = Uri.parse(OFFCIAL_WEB_URL);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    startActivity(intent);
  }

  @OnClick(R.id.changeCamera)
  void changeCameraClick() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    if (mCameraId == 0) {
      builder.setMessage(R.string.text_is_change_front_preview);
    } else {
      builder.setMessage(R.string.text_is_change_back_preview);
    }
    builder
        .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            changeCamera();
            dialog.dismiss();
          }
        })
        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .show();
  }

  private void peopleGet() {
    if (StringUtils.isBlank(mName)) {
      ToastUtils.show(this, R.string.text_name_not_empty);
      return;
    }

    CheckAPI.peopleGet(mName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<PeopleGet>() {

          @Override
          public void onStart() {
            super.onStart();
            mProgressDialog.setMessage(CustomUtil.getString(IndexActivity.this, R.string.text_test));
            mProgressDialog.show();
          }

          @Override
          public void onCompleted() {
            mProgressDialog.dismiss();
          }

          @Override
          public void onError(Throwable e) {
            onCompleted();
            ToastUtils.show(IndexActivity.this, R.string.toast_network_error);
          }

          @Override
          public void onNext(PeopleGet peopleGet) {
            handleCheckData(peopleGet);
          }
        });
  }

  private void handleCheckData(PeopleGet data) {
    Logs.d(TAG, data.toString());

    if (mAction == ACTION_VERIFY) {
      handleVerify(data);
    } else {
      handleRegister(data);
    }
  }

  private void handleRegister(PeopleGet data) {
    if (data == null) {
      SnackBarUtils.showError(mTitleText, R.string.toast_enroll_failed);
      return;
    }
    if (data.getRes_code() != null && Constant.RES_CODE_1025.equals(data.getRes_code())) {
      startCaptureActivity(false);
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.getFace_count() > 0) {
      SnackBarUtils.show(mTitleText, R.string.text_user_exist);
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.getFace_count() == 0) {
      startCaptureActivity(true);
    } else {
      SnackBarUtils.showError(mTitleText, R.string.toast_enroll_failed);
    }
  }

  private void handleVerify(PeopleGet data) {
    if (data == null) {
      SnackBarUtils.showError(mTitleText, R.string.toast_verify_failed);
      return;
    }
    if (data.getRes_code() != null && Constant.RES_CODE_1025.equals(data.getRes_code())) {
      SnackBarUtils.show(mTitleText, R.string.text_user_unregister);
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.getFace_count() > 0) {
      Intent intent = new Intent(this, VerifyActivity.class);
      intent.putExtra(VerifyActivity.ARG_CAMERA_ID, mCameraId);
      intent.putExtra(VerifyActivity.ARG_NAME, mName);
      startActivity(intent);
    } else if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_0000) && data.getFace_count() == 0) {
      SnackBarUtils.show(mTitleText, R.string.text_user_unregister);
    } else {
      SnackBarUtils.showError(mTitleText, R.string.toast_verify_failed);
    }
  }


  private void startCaptureActivity(boolean created) {
    Intent intent = new Intent(this, RegisterActivity.class);
    intent.putExtra(RegisterActivity.ARG_CAMERA_ID, mCameraId);
    intent.putExtra(RegisterActivity.ARG_NAME, mName);
    intent.putExtra(RegisterActivity.ARG_IS_CREATED, created);
    startActivity(intent);
  }

  private void handleDelete(PeopleDelete data) {
    if (data == null) {
      SnackBarUtils.showError(mContainer, R.string.toast_delete_failed);
      return;
    }
    if (data.getSuccess()) {
      SnackBarUtils.show(mContainer, R.string.toast_delete_success);
    } else {
      if (StringUtils.isEquals(data.getRes_code(), Constant.RES_CODE_1025)) {
        SnackBarUtils.showError(mContainer, R.string.toast_delete_failed_no_user);
      } else {
        SnackBarUtils.showError(mContainer, R.string.toast_delete_failed);
      }
    }
  }

  private void changeCamera() {
    if (mCameraId == 0) {
      mCameraId = 1;
    } else {
      mCameraId = 0;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "resultCode:" + resultCode);
    Log.i(TAG, "requestCode:" + requestCode);
    if (requestCode == RESULT_GET_CARD_OK) {
      //身份证正面时， 能获取到人头像图片
      // 获取人头像
//            Bitmap headimg = TRCardScan.HeadImgBitmap;// 人头像
//            Bitmap Takeimg = TRCardScan.TakeBitmap;  // 全图
//            ImageView HeadImageView = (ImageView)findViewById(R.id.imageViewHead);
//            HeadImageView.setImageBitmap(headimg);
      // 处理身份证识别信息（在界面上显示）
      if (data == null) {
        Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
        return;
      }
      CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
//            TextView textView = (TextView) findViewById(R.id.textViewResult);
      //获取单个栏目识别信息
      //textView.setText(cardInfo.getFieldString(TFieldID.NUM));// 以此类推

      // 获取全部识别信息， 为了演示， 这里采用展示所有信息
      mNameEdit.setText(cardInfo.getFieldString(TFieldID.NUM));

      Log.i(TAG, "name:" + cardInfo.getFieldString(TFieldID.NAME));
      Log.i(TAG, "num:" + cardInfo.getFieldString(TFieldID.NUM));
      Log.i(TAG, "ADDRESS:" + cardInfo.getFieldString(TFieldID.ADDRESS));
    }
  }
}
