package com.kasao.qintai.activity.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.SimpleAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.model.CarParameterEntity;
import com.kasao.qintai.model.CarParmeterKeyValue;
import com.kasao.qintai.model.domain.CarParameterdomain;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/13.
 * 简述:汽车参数详情页面
 */

public class CarParmarsDeatailActivity extends BaseActivity {
    private RecyclerView recycleview;
    private SimpleAdapter adapter;
    private List<CarParmeterKeyValue> childer;
    private List<CarParmeterKeyValue> all = new ArrayList<>();
    List<CarParameterEntity> list;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_car_parmars;
    }


    @Override
    public void findView() {
        recycleview = findViewById(R.id.recycleview);
        initRecycle(recycleview, null);
        adapter = new SimpleAdapter();
        recycleview.setAdapter(adapter);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void rendView() {
        int size = list.size();
        childer = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CarParameterEntity entity = list.get(i);
            CarParmeterKeyValue value = new CarParmeterKeyValue();
            if (entity != null) {
                value.name = entity.name;
                value.aliasvalue = (i + 1) + "";
                value.index = i + 1;
                childer.add(value);
                if (null != entity.child) {
                    childer.addAll(entity.child);
                }
            }
        }

        adapter.setMlist(childer);
    }

    @Override
    public void onloadData() {
        String goodId = getIntent().getExtras().getString(ParmarsValue.KEY_GOODID);
        Map map = new HashMap();
        map.put(ParmarsValue.KEY_GOODID, goodId);
        ApiManager.Companion.getGetInstance().getDataByParmars(ApiInterface.Companion.getCAR_PARAMETER(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarParameterdomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarParameterdomain.class);
                    if (null != domain && null != domain.data) {
                        list = domain.data;
                        rendView();
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

}
