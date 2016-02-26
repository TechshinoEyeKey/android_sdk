package com.techshino.sample_compare;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeysdk.entity.FaceAttrs;
import com.techshino.eyekeysdk.entity.MatchCompare;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * eyekey人脸比对示例(matchCompare)
 *
 * @author wangzhi
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE1 = 11;
    private static final int REQUEST_CODE2 = 12;

    private String mFaceId1;
    private String mFaceId2;
    private String mImgBase641;
    private String mImgBase642;

    ImageView mImageView1;
    ImageView mImageView2;
    Button mCompareBtn;
    TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化eyekey接口 （需在AndroidManifest.xml中添加appid和appkey）
        CheckAPI.init(this);

        mImageView1 = (ImageView) findViewById(R.id.img1);
        mImageView2 = (ImageView) findViewById(R.id.img2);
        mCompareBtn = (Button) findViewById(R.id.compareBtn);
        mResultText = (TextView) findViewById(R.id.resultBtn);

        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlbumActivity(REQUEST_CODE1);
            }
        });
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlbumActivity(REQUEST_CODE2);
            }
        });
        mCompareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCompare();
            }
        });
    }

    private void startCompare() {
        if ("".equals(mImgBase641) || mImgBase641 == null || "".equals(mImgBase642) || mImgBase642 == null) {
            Toast.makeText(this, "请选择图片再比对", Toast.LENGTH_SHORT).show();
            return;
        }
        mResultText.setText("比对中...");
        getFaceId1();
    }

    void getFaceId1() {
        CheckAPI.checkingImageData(mImgBase641, null, null).enqueue(new Callback<FaceAttrs>() {
            @Override
            public void onResponse(Call<FaceAttrs> call, Response<FaceAttrs> response) {
                FaceAttrs faceAttrs = response.body();
                if (faceAttrs != null && "0000".equals(faceAttrs.getRes_code())) {
                    mFaceId1 = faceAttrs.getFace().get(0).getFace_id();
                    getFaceId2();
                } else {
                    mResultText.setText("人脸检测失败...");
                }
            }

            @Override
            public void onFailure(Call<FaceAttrs> call, Throwable t) {
                mResultText.setText("网络出错...");
                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getFaceId2() {
        CheckAPI.checkingImageData(mImgBase642, null, null).enqueue(new Callback<FaceAttrs>() {
            @Override
            public void onResponse(Call<FaceAttrs> call, Response<FaceAttrs> response) {
                FaceAttrs faceAttrs = response.body();
                if (faceAttrs != null && "0000".equals(faceAttrs.getRes_code())) {
                    mFaceId2 = faceAttrs.getFace().get(0).getFace_id();
                    compare();
                } else {
                    mResultText.setText("人脸检测失败...");
                }
            }

            @Override
            public void onFailure(Call<FaceAttrs> call, Throwable t) {
                mResultText.setText("网络出错...");
                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void compare() {
        CheckAPI.matchCompare(mFaceId1, mFaceId2).enqueue(new Callback<MatchCompare>() {
            @Override
            public void onResponse(Call<MatchCompare> call, Response<MatchCompare> response) {
                MatchCompare compare = response.body();
                if (compare != null && "0000".equals(compare.getRes_code())) {
                    mResultText.setText(compare.toString());
                } else {
                    mResultText.setText("比对失败...");
                }
            }

            @Override
            public void onFailure(Call<MatchCompare> call, Throwable t) {
                mResultText.setText("网络出错...");
                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startAlbumActivity(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        Uri uri = data.getData();
        Log.e("uri", uri.toString());
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
        } catch (FileNotFoundException e) {
            Log.e("Exception", e.getMessage(), e);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE1) {
            mImageView1.setImageBitmap(bitmap);
            mImgBase641 = bitmapToBase64(bitmap);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE2) {
            mImageView2.setImageBitmap(bitmap);
            mImgBase642 = bitmapToBase64(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bStream);
        return Base64.encodeToString(bStream.toByteArray(), 0);
    }
}
