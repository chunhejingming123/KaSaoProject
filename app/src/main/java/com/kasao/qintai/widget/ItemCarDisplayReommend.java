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
import com.kasao.qintai.util.GlideUtil;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/6.
 * 简述:卡车展示样式 a 推荐
 */

public class ItemCarDisplayReommend extends BaseView {
    ImageView ivIcon;
    TextView tvName;
    TextView tvMoney;
    TextView tvButton;

    public ItemCarDisplayReommend(@NonNull Context context) {
        super(context);
        initView();
    }

    public ItemCarDisplayReommend(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemCarDisplayReommend(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cardisplay_recommend, this);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvName = view.findViewById(R.id.tv_car_name);
        tvMoney = view.findViewById(R.id.tv_car_money);
        tvButton = view.findViewById(R.id.tv_button);

    }

    public void rendView(CarDetailEntity entity, boolean isShow) {
        if (null != entity) {
            tvName.setText(entity.name.trim());
//            String money= DataTypeChange.getF10000(DataTypeChange.stringToDounble(entity.goods_price));//entity.goods_price;
//            tvMoney.setText(money);
            GlideUtil.into(getContext(),entity.goods_img,ivIcon,R.drawable.bg_default);
            if (!isShow) {
                tvButton.setVisibility(INVISIBLE);
            } else {
                tvButton.setVisibility(VISIBLE);
            }
        }
    }
}
