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

import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.R;
import com.kasao.qintai.util.DataTypeChange;
import com.kasao.qintai.util.GlideUtil;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/6.
 * 简述:卡车展示样式 a 推荐
 */

public class ItemCarDisplayDel extends BaseView implements View.OnClickListener {
    ImageView ivIcon;
    TextView tvName;
    TextView tvEngen;
    TextView tvMoney;
    TextView tvButton;
    View delView;
    TextView tvCancel;
    TextView tvDel;
    CarDetailEntity tempt;

    public ItemCarDisplayDel(@NonNull Context context) {
        super(context);
        initView();
    }

    public ItemCarDisplayDel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemCarDisplayDel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cardisplay_del, this);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvName = view.findViewById(R.id.tv_car_name);
        tvEngen = view.findViewById(R.id.tv_car_engen);
        tvMoney = view.findViewById(R.id.tv_car_money);
        tvButton = view.findViewById(R.id.tv_button);
        delView = view.findViewById(R.id.ll_delect);
        tvCancel = view.findViewById(R.id.positiveButton);
        tvDel = view.findViewById(R.id.negativeButton);
        tvDel.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public void rendView(CarDetailEntity entity, boolean isShow, boolean isDrive) {
        if (null != entity) {
            tempt = entity;
            tvName.setText(entity.name.trim());
            String money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(entity.goods_price));//entity.goods_price;
            tvMoney.setText(money);
            GlideUtil.into(BaseKasaoApplication.Companion.getApplicaton(), entity.goods_img, ivIcon, R.drawable.bg_default);
            tvButton.setVisibility(VISIBLE);

            if (isDrive) {
                tvEngen.setText(entity.drive + " " + entity.purpose);
            } else {
                tvEngen.setText(entity.goods_jingle);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                delView.setVisibility(GONE);
                break;
            case R.id.negativeButton:
                delView.setVisibility(GONE);
                if (null != mIcarDel) {
                    mIcarDel.onDelStoreCar(tempt, 0);
                }
                break;
        }
    }

    public void setState(boolean state) {
        if (state) {
            delView.setVisibility(VISIBLE);
        } else {
            delView.setVisibility(GONE);
        }
    }

    private ICarDel mIcarDel;

    public void setmIcarDel(ICarDel mIcarDel) {
        this.mIcarDel = mIcarDel;
    }

    public interface ICarDel {
        void onDelStoreCar(CarDetailEntity entity, int index);
    }
}
