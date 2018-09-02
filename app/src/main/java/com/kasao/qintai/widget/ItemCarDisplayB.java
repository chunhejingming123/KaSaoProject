package com.kasao.qintai.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.util.DataTypeChange;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintaiframework.until.ScreenUtil;
/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/7.
 * 简述:卡车展示样式 B 搜索 样式
 */

public class ItemCarDisplayB extends BaseView {
    ImageView iv_bigimg;
    TextView tvName;
    TextView tvMoney;
    public ItemCarDisplayB(@NonNull Context context) {
        super(context);
        initView();
    }

    public ItemCarDisplayB(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemCarDisplayB(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.item_cardisplay_b,this);
         iv_bigimg=view.findViewById(R.id.iv_bigimg);
         tvName=view.findViewById(R.id.tv_descride);
         tvMoney=view.findViewById(R.id.tv_money);
        ViewGroup.LayoutParams params= iv_bigimg.getLayoutParams();
        params.width= ScreenUtil.getScreenW();
        params.height=196*ScreenUtil.getScreenW()/345;
        iv_bigimg.setLayoutParams(params);
        iv_bigimg.requestLayout();
        iv_bigimg.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    @Override
    public void rendView(CarDetailEntity entity){
        if(null!=entity){
            GlideUtil.into(BaseKasaoApplication.Companion.getApplicaton(),entity.goods_img,iv_bigimg,R.drawable.bg_default);
            tvName.setText(entity.name);
            String money= DataTypeChange.getF10000(DataTypeChange.stringToDounble(entity.goods_price));//entity.goods_price;
            tvMoney.setText(money);
        }
    }
}
