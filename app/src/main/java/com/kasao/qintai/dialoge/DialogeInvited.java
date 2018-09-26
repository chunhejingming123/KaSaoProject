package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
 * 日期 on 2017/10/30.
 * 简述:邀请注册
 */

public class DialogeInvited implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;

    public DialogeInvited(Activity mActivity) {
        soft = new SoftReference<Activity>(mActivity);
        if (null == soft.get()) {
            return;
        }
        if (null == mDialoge) {
            mDialoge = new Dialog(soft.get(), R.style.dialog);
            initView();
        }

    }

    // 初始化
    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_action_invited, null);
        mDialoge.setContentView(view);

        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (ScreenUtil.getScreenW());
        lp.height = ScreenUtil.getScreenH();
        window.setGravity(Gravity.CENTER);
        mDialoge.getWindow().setAttributes(lp);
        mDialoge.setCanceledOnTouchOutside(true);
        mDialoge.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDialoge.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {


    }

    public boolean showing() {
        return null != mDialoge && mDialoge.isShowing();
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

    private SendPrice mSendPrice;

    public void setSendPrice(SendPrice sendPrice) {
        this.mSendPrice = sendPrice;
    }


    public interface SendPrice {
        void sendPrice(String tel);
    }
}
