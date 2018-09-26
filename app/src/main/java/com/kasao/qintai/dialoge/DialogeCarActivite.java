package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kasao.qintai.R;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

/**
 * 作者 Created by suochunming
 * 日期 on 2018/2/7.
 * 简述:红包弹窗
 */

public class DialogeCarActivite implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;
    private View viewClose;
    private View viewClick;

    public DialogeCarActivite(Activity mActivity) {
        soft = new SoftReference(mActivity);
        if (null == soft.get()) {
            return;
        }
        mDialoge = new Dialog(soft.get(), R.style.dialog);
        initView();
    }

    // 初始化
    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_caractivite, null);
        mDialoge.setContentView(view);
        viewClose = view.findViewById(R.id.ivClose);
        viewClick = view.findViewById(R.id.ivbig);
        viewClose.setOnClickListener(this);
        viewClick.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = ScreenUtil.getScreenW();
        lp.height = ScreenUtil.getScreenH();
        window.setGravity(Gravity.CENTER);
        mDialoge.getWindow().setAttributes(lp);
        mDialoge.setCanceledOnTouchOutside(false);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                break;
            case R.id.ivbig:
                if (null != mActivite) {
                    mActivite.onJoinActivite();
                }
                break;
        }
        dimiss();

    }

    public void show() {
        if (mDialoge != null) {
            mDialoge.show();
        }
    }

    private void dimiss() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    private IJoinActivite mActivite;

    public void setOnActivite(IJoinActivite activite) {
        this.mActivite = activite;
    }

    public interface IJoinActivite {
        void onJoinActivite();

    }

}
