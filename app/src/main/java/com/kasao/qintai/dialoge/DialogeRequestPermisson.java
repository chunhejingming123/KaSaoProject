package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.kasao.qintai.R;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 2018/1/15.
 */

public class DialogeRequestPermisson implements View.OnClickListener {
    private Dialog mDialoge;
    private TextView tvTitle;
    private TextView tvContent;
    private SoftReference<Activity> soft = null;

    public DialogeRequestPermisson(Activity context) {
        soft = new SoftReference<Activity>(context);
        if (soft.get() == null) {
            return;
        }
        if (null == mDialoge) {
            mDialoge = new Dialog(soft.get(), R.style.dialog);
            initView();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_request_permission, null);
        mDialoge.setContentView(view);
        Button btnCannel = (Button) view.findViewById(R.id.btnCannel);
        Button btnQuery = (Button) view.findViewById(R.id.btnQuery);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        btnCannel.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (int) (0.8 * ScreenUtil.getScreenW());
        // lp.height = (int) (0.3 * ScreenUtil.getScreenH());
        window.setGravity(Gravity.CENTER);
        mDialoge.getWindow().setAttributes(lp);
        mDialoge.setCanceledOnTouchOutside(false);
        mDialoge.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDialoge.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCannel:
                break;
            case R.id.btnQuery:
                if (null != mSetting) {
                    mSetting.toSetting();
                }
                break;
        }
        hide();
    }

    public void hide() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    public void showDialoge() {
        if (mDialoge != null) {
            mDialoge.show();
        }
    }

    public void showDialoge(String title, String content) {
        if (mDialoge != null) {
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
            if (!TextUtils.isEmpty(content)) {
                tvContent.setText(content);
            }
            mDialoge.show();

        }
    }

    private ISettings mSetting;

    public void setmSetting(ISettings mSetting) {
        this.mSetting = mSetting;
    }

    public interface ISettings {
        void toSetting();
    }
}
