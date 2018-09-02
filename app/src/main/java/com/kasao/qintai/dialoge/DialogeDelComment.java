package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
 * 简述:删除发布的动态
 */

public class DialogeDelComment implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;

    public DialogeDelComment(Activity mActivity) {
        soft = new SoftReference<Activity>(mActivity);
        if (null == soft.get()) {
            return;
        }
        mDialoge = new Dialog(soft.get(), R.style.dialog);
        initView();
    }

    // 初始化
    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_del_comment, null);
        mDialoge.setContentView(view);
        View ivClose = view.findViewById(R.id.btnCannel);
        TextView tvOk = (TextView) view.findViewById(R.id.btnQuery);
        ivClose.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (int) (0.7 * ScreenUtil.getScreenW());
        // lp.height = (int) (0.3 * ScreenUtil.getScreenH());
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
        switch (view.getId()) {
            case R.id.btnCannel:
                break;
            case R.id.btnQuery:
                if (null != mODialoge) {
                    mODialoge.del();
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

    public void showDialoge(ODialogeDel dialogeDel) {
        if (mDialoge != null) {
            mDialoge.show();
            this.mODialoge = dialogeDel;
        }
    }

    private ODialogeDel mODialoge;

    public interface ODialogeDel {
        void del();
    }
}
