package com.techshino.eyekeydemo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.techshino.eyekeydemo.R;
import com.techshino.eyekeydemo.utils.CustomUtil;
import com.techshino.eyekeydemo.utils.Logs;
import com.techshino.eyekeydemo.utils.SnackBarUtils;
import com.techshino.eyekeydemo.utils.StringUtils;
import com.techshino.eyekeydemo.utils.ToastUtils;
import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeysdk.conn.Constant;
import com.techshino.eyekeysdk.entity.PeopleDelete;
import com.techshino.eyekeysdk.entity.PeopleGet;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangzhi on 2015/12/25.
 * <p>
 * 首页
 */
public class IndexActivity extends BaseAppcompatActivity {

    private static final String TAG = IndexActivity.class.getSimpleName();

    private static final String OFFCIAL_WEB_URL = "http://www.eyekey.com";

    private static final int ACTION_VERIFY = 1;
    private static final int ACTION_REGISTER = 2;

    private int mAction;

    private int mCameraId = 1;
    private String mName;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbarTitle)
    TextView mTitleText;

    @Bind(R.id.container)
    CoordinatorLayout mContainer;

    @Bind(R.id.nameEdit)
    EditText mNameEdit;

    ProgressDialog mProgressDialog;

    @Override
    public void initData() {
        mProgressDialog = new ProgressDialog(this);
        mTitleText.setText(R.string.app_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    @OnTextChanged(callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED, value = R.id.nameEdit)
    void etNameEdit(Editable editable) {
        mName = editable.toString();
    }

    /**
     * 验证
     */
    @OnClick(R.id.takeImg)
    void verifyIconClick() {
        mAction = ACTION_VERIFY;
        peopleGet();
    }

    /**
     * 注册
     */
    @OnClick(R.id.registerIcon)
    void registerIconClick() {
        mAction = ACTION_REGISTER;
        peopleGet();
    }

    /**
     * 重置
     */
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

    /**
     * 官网
     */
    @OnClick(R.id.offcialIcon)
    void offcialIconClick() {
        Uri uri = Uri.parse(OFFCIAL_WEB_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * 切换摄像头
     */
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

    private void peopleDelete() {
        mProgressDialog.setMessage(CustomUtil.getString(IndexActivity.this, R.string.text_deleting));
        mProgressDialog.show();
        Call<PeopleDelete> call = CheckAPI.peopleDelete(mName);
        call.enqueue(new Callback<PeopleDelete>() {

            public void onFinish() {
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call<PeopleDelete> call, Response<PeopleDelete> response) {
                onFinish();
                handleDelete(response.body());
            }

            @Override
            public void onFailure(Call<PeopleDelete> call, Throwable t) {
                onFinish();
                ToastUtils.show(IndexActivity.this, R.string.toast_network_error);
            }
        });
    }

    private void peopleGet() {
        if (StringUtils.isBlank(mName)) {
            ToastUtils.show(this, R.string.text_name_not_empty);
            return;
        }

        mProgressDialog.setMessage(CustomUtil.getString(IndexActivity.this, R.string.text_test));
        mProgressDialog.show();
        Call<PeopleGet> call = CheckAPI.peopleGet(mName);
        call.enqueue(new Callback<PeopleGet>() {

            public void onFinish() {
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call<PeopleGet> call, Response<PeopleGet> response) {
                onFinish();
                handleCheckData(response.body());
            }

            @Override
            public void onFailure(Call<PeopleGet> call, Throwable t) {
                onFinish();
                ToastUtils.show(IndexActivity.this, R.string.toast_network_error);
            }
        });
    }

    private void handleCheckData(PeopleGet data) {
        Logs.d(TAG, data == null ? "认证失败" : data.toString());

        if (mAction == ACTION_VERIFY) {
            handleVerify(data);
        } else {
            handleRegister(data);
        }
    }

    private void handleRegister(PeopleGet data) {
        if (data == null) {
            SnackBarUtils.showError(mTitleText, "注册失败，请检查是否填写appid和appkey");
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
            SnackBarUtils.showError(mTitleText, "认证失败，请检查是否填写appid和appkey");
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
            SnackBarUtils.showError(mContainer, "删除失败，请检查是否填写appid和appkey");
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
}
