package com.kasao.qintai.version;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.kasao.qintai.R;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/10/30.
 * 简述:版本更新弹窗
 */

public class DialogeVersionUp implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;
    private TextView tvContent;

    public DialogeVersionUp(Activity mActivity) {
        soft = new SoftReference<Activity>(mActivity);
        if (null == soft.get()) {
            return;
        }
        mDialoge = new Dialog(soft.get(), R.style.dialog);
        initView();
    }

    // 初始化
    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_version_up, null);
        mDialoge.setContentView(view);
        View tvOk = view.findViewById(R.id.btnQuery);
        View tvCannel = view.findViewById(R.id.btnCancel);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvCannel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        //lp.width = (int) (0.8 * ScreenUtil.getScreenW());
//       lp.height = (int) (0.3 * ScreenUtil.getScreenH());
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

    public void rendView(String strContent) {
        if (!TextUtils.isEmpty(strContent)) {
            tvContent.setText(strContent);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                break;
            case R.id.btnQuery:
                if (null != mODialoge) {
                    mODialoge.onUpDate();
                }
                break;
        }
        dimiss();

    }

    private void dimiss() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    public void showDialoge(ODialogeVersionUp dialogeDel) {
        if (mDialoge != null) {
            mDialoge.show();
            this.mODialoge = dialogeDel;
        }
    }

    private ODialogeVersionUp mODialoge;

    public interface ODialogeVersionUp {
        void onUpDate();
    }
}
