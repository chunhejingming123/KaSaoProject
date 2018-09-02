package com.kasao.qintai.activity.active;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kasao.qintai.R;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.widget.CameraSurfaceView;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.until.LogUtil;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/27 0027:14
 */

public class RectCameraActivity extends BaseActivity {
    @Override
    public int onLayoutLoad() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_custum_camere;
    }

    private Button button;
    private Button buttonCancel;
    private CameraSurfaceView mCameraSurfaceView;
    private String name = "";
    @Override
    public void findView() {
        name = getIntent().getStringExtra(ParmarsValue.KEY_KEYWORD);
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        button = (Button) findViewById(R.id.takePic);
        buttonCancel = (Button) findViewById(R.id.takeCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraHardware(RectCameraActivity.this)) {
                    mCameraSurfaceView.takePicture(name);
                    mCameraSurfaceView.setmPicturePath(new CameraSurfaceView.OnPicturePath() {
                        @Override
                        public void getPicturePath(String path) {
                            LogUtil.e("-----------path----------=" + path);
                            Intent intent = new Intent();
                            intent.putExtra(ParmarsValue.KEY_KEYWORD, path);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                }
            }
        });
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a
            return true;
        } else {
            return false;
        }
    }

}
