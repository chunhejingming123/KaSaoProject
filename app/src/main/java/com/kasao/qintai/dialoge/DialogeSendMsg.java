package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.SoftKeybordUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/10/30.
 * 简述:删除发布的动态
 */

public class DialogeSendMsg implements View.OnClickListener {
    private Dialog mDialoge;
    private EditText mEditext;
    TextView tvReply;
    private SoftReference<Activity> soft = null;

    public DialogeSendMsg(Activity mActivity) {
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
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_send_msg, null);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvReply = view.findViewById(R.id.tvReply);
        TextView tvSend = view.findViewById(R.id.tvSend);
        mEditext = view.findViewById(R.id.etMsg);
        tvCancel.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        mDialoge.setContentView(view);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (ScreenUtil.getScreenW());
        window.setGravity(Gravity.BOTTOM);
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
            case R.id.tvCancel:
                hide();
                break;
            case R.id.tvSend:
                mSendPrice.send(mEditext.getText().toString());
                break;

        }

    }

    public void diss(){
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }
    public void hide() {
        if (mDialoge != null) {
            SoftKeybordUtil.keyBoradDelay(mEditext, false, 150);
            mDialoge.dismiss();
        }
    }

    public void showDialoge(String nickName, boolean isReply) {
        if (mDialoge != null) {
            mDialoge.show();

            if (isReply) {
                tvReply.setText(ContextComp.getString(R.string.reply));
                mEditext.setHint("回复 "+nickName);
            } else {
                tvReply.setText(ContextComp.getString(R.string.pinglun));
            }
            mEditext.requestFocus();
           SoftKeybordUtil.keyBoradDelay(mEditext, true, 100);
        }


    }

    private ReplyOrSend mSendPrice;

    public void setSendPrice(ReplyOrSend sendPrice) {
        this.mSendPrice = sendPrice;
    }


    public interface ReplyOrSend {
        void send(String msg);
    }
}
