package com.kasao.qintai.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.util.ContextComp;

/**
 * 作者 :created  by suochunming
 * 日期：2018/9/11 0011:16
 */

public class EmptyView extends BaseView {
    private ImageView iv;
    private TextView tv;

    public EmptyView(@NonNull Context context) {
        super(context);
        initView();
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, this);
        iv = (ImageView) itemView.findViewById(R.id.iv);
        tv = (TextView) itemView.findViewById(R.id.tv_msg);
//        LinearLayout.LayoutParams parmars = new LinearLayout.LayoutParams(3 * 242 / 2, 3 * 194 / 2);
//        parmars.gravity = Gravity.CENTER_HORIZONTAL;
//        parmars.topMargin = ContextComp.getDimensionPixelOffset(R.dimen.dimen_120);
//        iv.setLayoutParams(parmars);
        tv.setText(ContextComp.getString(R.string.search_no_more));

    }
}
