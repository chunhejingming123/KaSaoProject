package com.kasao.qintai.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kasao.qintai.R;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/31 0031:17
 */

public class LodingStateView extends BaseView {
    private Typeface TEXT_TYPE;
    private GradientShaderTextView tv;

    public LodingStateView(Context context) {
        super(context);
        initView();
    }

    public LodingStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LodingStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void setParmars() {
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lytp.bottomMargin = (int) getResources().getDimension(R.dimen.dimen_150);
        lytp.gravity = Gravity.CENTER;
        setLayoutParams(lytp);
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_loading_state, this);
        tv = (GradientShaderTextView) findViewById(R.id.tvloading);
        // 加载自定义字体
        try {
            //  TEXT_TYPE = Typeface.createFromAsset(getContext().getAssets(), "hhh.otf");

        } catch (Exception e) {
            TEXT_TYPE = null;
        }
        if (TEXT_TYPE != null) {
            tv.setTypeface(TEXT_TYPE);
        }
    }
}