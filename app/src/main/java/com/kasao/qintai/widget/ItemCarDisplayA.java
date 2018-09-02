package com.kasao.qintai.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.util.DataTypeChange;
import com.kasao.qintai.util.GlideUtil;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:09
 */

public class ItemCarDisplayA extends BaseView {
    ImageView ivIcon;
    TextView tvName;
    TextView tvMoney;

    public ItemCarDisplayA(@NonNull Context context) {
        super(context);
        initView();
    }

    public ItemCarDisplayA(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemCarDisplayA(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cardisplay_a, this);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvName = view.findViewById(R.id.tv_car_name);


    }

    public void rendView(CarDetailEntity entity, boolean isShow, boolean isDrive) {
        if (null != entity) {
            tvName.setText(entity.name.trim());
//            String money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(entity.goods_price));//entity.goods_price;
//            tvMoney.setText(money);
            GlideUtil.into(getContext(), entity.goods_img, ivIcon, R.drawable.bg_default);


        }
    }
}
