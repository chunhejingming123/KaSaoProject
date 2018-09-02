package com.kasao.qintai.widget.menu.adapter;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.model.CarPriceDuring;
import com.kasao.qintai.model.domain.CarPriceDuringdomain;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.widget.BaseView;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;

import com.kasao.qintai.R;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/8.
 * 简述:搜索中 价格排列
 */

public class PriceGridView extends BaseView {
    private GridView gridview;
    private EditText minPrice;
    private EditText maxPrice;
    private TextView tvEditerOk;
    private List<String> pricemoney = new ArrayList<>();
    private GridDropDownAdapter constellationAdapter;

    public PriceGridView(@NonNull Context context) {
        super(context);
        initVew();
    }

    public PriceGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVew();
    }

    public PriceGridView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVew();
    }

    private void initVew() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_pricr_gridview, this);
        gridview = (GridView) view.findViewById(R.id.recyclView);
        minPrice = (EditText) view.findViewById(R.id.et_min_price);
        maxPrice = (EditText) view.findViewById(R.id.et_max_price);
        tvEditerOk = (TextView) view.findViewById(R.id.tv_confirm);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                if (null != iItemPrice) {
                    iItemPrice.onPrice(pricemoney.get(position), position);
                }
            }
        });

        tvEditerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != iItemPrice) {

                    String min = minPrice.getText().toString().trim();
                    String max = maxPrice.getText().toString().trim();
                    if (TextUtils.isEmpty(min)) {
                        ToastUtil.showAlter(getResources().getString(R.string.inputminprice));
                        return;
                    }
                    if (TextUtils.isEmpty(max)) {
                        ToastUtil.showAlter(getResources().getString(R.string.inputmaxprice));
                        return;
                    }
                    iItemPrice.onCustomPrice(min, max);
                }
            }
        });

        // 获取车间 间断
        ApiManager.Companion.getGetInstance().getDataByUrl(ApiInterface.Companion.getCAR_MONEY_DURING(), new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarPriceDuringdomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarPriceDuringdomain.class);
                    if (null != domain && null != domain.data) {
                        List<CarPriceDuring> list = domain.data;
                        for (CarPriceDuring carprice : list) {
                            pricemoney.add(carprice.money);
                        }
                        constellationAdapter = new GridDropDownAdapter(getContext(), pricemoney);
                        gridview.setAdapter(constellationAdapter);

                    } else {
                        pricemoney = Arrays.asList(ContextComp.getStringArray(R.array.array_price));
                        constellationAdapter = new GridDropDownAdapter(getContext(), pricemoney);
                        gridview.setAdapter(constellationAdapter);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void _onError(@NotNull Throwable e) {
                pricemoney = Arrays.asList(ContextComp.getStringArray(R.array.array_price));
                constellationAdapter = new GridDropDownAdapter(getContext(), pricemoney);
                gridview.setAdapter(constellationAdapter);
            }
        });
        rendView();
    }

    // 获取车价信息
    private void rendView() {
    }

    private IItemPrice iItemPrice;

    public void setItemPrice(IItemPrice iItemBrand) {
        this.iItemPrice = iItemBrand;
    }

    public interface IItemPrice {
        void onPrice(String price, int position);

        void onCustomPrice(String minprice, String maxprice);
    }
}
