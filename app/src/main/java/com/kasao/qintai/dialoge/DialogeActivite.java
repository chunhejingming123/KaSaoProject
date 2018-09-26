package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.activity.active.CarActionActivity;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/10/30.
 * 简述: 活动弹窗
 */

public class DialogeActivite {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;

    public DialogeActivite(Activity mActivity) {
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
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_action, null);
        mDialoge.setContentView(view);

        view.findViewById(R.id.btnJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soft.get().startActivity(new Intent(soft.get(), CarActionActivity.class));
            }
        });
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
