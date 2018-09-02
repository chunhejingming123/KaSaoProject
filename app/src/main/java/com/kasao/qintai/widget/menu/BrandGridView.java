package com.kasao.qintai.widget.menu;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.CarBrandGridAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintai.model.CarBrand;
import com.kasao.qintai.model.domain.CarBranddomain;
import com.kasao.qintai.widget.BaseView;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/8.
 * 简述:descride
 */

public class BrandGridView extends BaseView {
    private RecyclerView recycleView;
    private CarBrandGridAdapter adapter;

    public BrandGridView(@NonNull Context context) {
        super(context);
        initVew();
    }

    public BrandGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVew();
    }

    public BrandGridView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVew();
    }

    private void initVew() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_gridview, this);
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        GridLayoutManager gide = new GridLayoutManager(BaseKasaoApplication.Companion.getApplicaton(), 4);
        recycleView.setLayoutManager(gide);
        // 设置ItemAnimator
        recycleView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        recycleView.setHasFixedSize(true);
        adapter = new CarBrandGridAdapter();
        recycleView.setAdapter(adapter);
        adapter.setChoseBrand(new CarBrandGridAdapter.IChoseBrand() {
            @Override
            public void choseBrand(CarBrand brand) {
                if (null != iItemBrand) {
                    iItemBrand.onBrand(brand);
                }
            }
        });
        initLoadData();
    }

    private void initLoadData() {
        ApiManager.Companion.getGetInstance().getDataByUrl(ApiInterface.Companion.getCAR_BRAND(), new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarBranddomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarBranddomain.class);
                    if (null != domain && null != domain.data) {
                        adapter.setListBrands(domain.data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });
    }


    private IItemBrand iItemBrand;

    public void setItemBrand(IItemBrand iItemBrand) {
        this.iItemBrand = iItemBrand;
    }

    public interface IItemBrand {
        void onBrand(CarBrand brand);
    }
}
