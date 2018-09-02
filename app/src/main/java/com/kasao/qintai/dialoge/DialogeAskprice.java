package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.DataTypeChange;
import com.kasao.qintaiframework.until.ScreenUtil;
import com.kasao.qintaiframework.until.ToastUtil;

import java.lang.ref.SoftReference;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/10/30.
 * 简述:删除发布的动态
 */

public class DialogeAskprice implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;
    private TextView tvCurrentprice;
    private EditText mEditext;
    private TextView tvCurrentUser;

    public DialogeAskprice(Activity mActivity) {
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
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_ask_price, null);
        mDialoge.setContentView(view);
        View ivClose = view.findViewById(R.id.iv_close);
        TextView tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvCurrentprice = (TextView) view.findViewById(R.id.tv_current_price);
        tvCurrentUser = (TextView) view.findViewById(R.id.tv_curent_user);
        mEditext = (EditText) view.findViewById(R.id.etmoble);
        ivClose.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (ScreenUtil.getScreenW());
        window.setGravity(Gravity.BOTTOM);
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
            case R.id.iv_close:
                hide();
                break;
            case R.id.tvOk:
                if (null != mSendPrice) {
                    String moble = mEditext.getText().toString().trim();
                    if (!TextUtils.isEmpty(moble) && moble.length() == 11) {
                        mSendPrice.sendPrice(mEditext.getText().toString().trim());
                    } else {
                        ToastUtil.showAlter(ContextComp.getString(R.string.input_moble));
                    }
                }
                break;
        }

    }

    public boolean showing() {
        return null != mDialoge && mDialoge.isShowing();
    }

    public void hide() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    public void showDialoge(String price, int num) {
        if (mDialoge != null) {
            mDialoge.show();
        }

        String money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(price));
        tvCurrentprice.setText(Html.fromHtml("<font color=#494747>" + "当前报价" + "<font/>" +
                "<font color=#ee2e3b>" + money + "</font>"));
        tvCurrentUser.setText(Html.fromHtml("<font color=#494747>" + "目前已有</font>" + "<font color=#ee2e3b>" + num + "</font>" + "<font color=#494747>个用户通过此功能查询到底价</font>"));
    }

    private SendPrice mSendPrice;

    public void setSendPrice(SendPrice sendPrice) {
        this.mSendPrice = sendPrice;
    }


    public interface SendPrice {
        void sendPrice(String tel);
    }
}
