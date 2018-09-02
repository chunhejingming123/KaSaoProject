package com.kasao.qintai.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.model.CarParmeterKeyValue;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/6.
 * 简述:卡车展示样式 a 推荐
 */
public class CareBaseParmarsView extends BaseView {

    TextView tvKey;

    TextView tvValue;

    public CareBaseParmarsView(@NonNull Context context) {
        super(context);
        initView();
    }

    public CareBaseParmarsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CareBaseParmarsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_care_baseparmars_view, this);
        tvKey = findViewById(R.id.tvKey);
        tvValue = findViewById(R.id.tvValue);
    }

    public void rendView(CarParmeterKeyValue entity) {
        if (null != entity) {
            tvKey.setText(entity.name.trim());
            tvValue.setText(entity.cname.trim());
        }
    }
}
