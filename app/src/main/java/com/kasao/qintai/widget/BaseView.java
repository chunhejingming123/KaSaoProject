package com.kasao.qintai.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.kasao.qintai.model.CarDetailEntity;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:09
 */

public class BaseView extends FrameLayout {

    public BaseView(@NonNull Context context) {
        super(context);
        setParmars();
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setParmars();
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setParmars();
    }

    public void init() {
    }

    public void rendView(CarDetailEntity entitiy) {
    }

    public void setParmars() {
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lytp.gravity = Gravity.CENTER;
        setLayoutParams(lytp);
    }

}
