package com.techshino.eyekeysample;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techshino.eyekeysdk.api.CheckAPI;
import com.techshino.eyekeysdk.entity.FaceAttrs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * eyekey人脸检测示例
 *
 * @author wangzhi
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button mChoiceBtn;
    Button mCheckingBtn;
    TextView mPathText;
    TextView mResultText;
    String mImgBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckAPI.init(this);
        mChoiceBtn = (Button) findViewById(R.id.choiceImgBtn);
        mCheckingBtn = (Button) findViewById(R.id.checkingImgBtn);
        mPathText = (TextView) findViewById(R.id.pathText);
        mResultText = (TextView) findViewById(R.id.resultText);

        mChoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
        mCheckingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(mImgBase64) || null == mImgBase64) {
                    Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkingImg();
            }
        });
    }

    private void checkingImg() {
        mResultText.setText("加载中...");
        Call<FaceAttrs> call = CheckAPI.checkingImageData(mImgBase64, null, null);
        call.enqueue(new Callback<FaceAttrs>() {

            @Override
            public void onResponse(Call<FaceAttrs> call, Response<FaceAttrs> response) {
                Log.i(TAG,"response:" + response.body());
                mResultText.setText(response.body() == null ? null : response.body().toString());
            }

            @Override
            public void onFailure(Call<FaceAttrs> call, Throwable t) {
                mResultText.setText("网络出错，请检查网络连接!");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            mPathText.setText(uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                mImgBase64 = bitmapToBase64(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bStream);
        return Base64.encodeToString(bStream.toByteArray(), 0);
    }
}
