package com.kasao.qintai.activity.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.activity.login.LoginActivity;
import com.kasao.qintai.activity.shop.ShopListActivity;
import com.kasao.qintai.adapter.CarSeriesAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintai.dialoge.DialogeAskprice;
import com.kasao.qintai.model.BannerEntity;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.CarImage;
import com.kasao.qintai.model.CarSeriesDetail;
import com.kasao.qintai.model.RtnSuss;
import com.kasao.qintai.model.domain.Bannderdomain;
import com.kasao.qintai.model.domain.CarSeriesedomain;
import com.kasao.qintai.model.domain.User;
import com.kasao.qintai.model.domain.UserCountdomain;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.widget.FlowLayout;
import com.kasao.qintai.widget.banner.BannerView;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.base.MyApplication;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CarSeriesActivity extends BaseActivity implements View.OnClickListener {
    View ivBack;
    BannerView banner;
    TextView tvDes;
    TextView tvHot;
    TextView tvRank;
    FlowLayout flowLayout;
    RecyclerView recycleViewFind;
    View viewBuy;
    View viewCall;
    TextView tvNum;
    View tvCall;
    String temple = "";
    private Drawable drawable_mormal;
    private Drawable drawable_select;
    private CarSeriesAdapter adapterFind;
    private String goodId;
    private CarSeriesDetail mCarSeriesDetail;
    private Map<String, List<CarDetailEntity>> map = new HashMap<>();
    int carNum;
    DialogeAskprice ask;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_carseries;
    }

    @Override
    public void findView() {
        goodId = getIntent().getStringExtra(ParmarsValue.KEY_GOODID);
        ivBack = findViewById(R.id.ivback);
        banner = findViewById(R.id.banner);
        tvDes = findViewById(R.id.tv_descride);
        tvHot = findViewById(R.id.tvHot);
        tvRank = findViewById(R.id.tvRank);
        flowLayout = findViewById(R.id.flowLayout);
        recycleViewFind = findViewById(R.id.recycleViewFind);
        viewBuy = findViewById(R.id.viewBuy);
        viewCall = findViewById(R.id.viewCall);
        tvNum = findViewById(R.id.tvNum);
        tvCall = findViewById(R.id.viewCall);

        ivBack.setOnClickListener(this);
        viewBuy.setOnClickListener(this);
        tvHot.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvRank.setOnClickListener(this);

        drawable_mormal = ContextComp.getDrawable(R.drawable.icon_arrow_down_mormal);
        drawable_select = ContextComp.getDrawable(R.drawable.icon_arrow_down_select);
        tvRank.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_mormal, null);
        tvRank.setTextColor(getResources().getColor(R.color.color_33333350));
        tvHot.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_select, null);
        tvHot.setTextColor(getResources().getColor(R.color.color_ee2e3b));

        adapterFind = new CarSeriesAdapter();
        initRecycle(recycleViewFind, null);
        recycleViewFind.setAdapter(adapterFind);

        adapterFind.setmIonclick(new CarSeriesAdapter.IOnClik() {
            @Override
            public void goToDetail(String goodsId) {
                User mUser = BaseKasaoApplication.getUser();
                if (null == mUser) {
                    startActivity(LoginActivity.class, null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(ParmarsValue.KEY_GOODID, goodsId);
                startActivity(CarDetailActivity.class, bundle);
            }


            @Override
            public void goToConsult(CarDetailEntity carDetailEntity) {
                User mUser = BaseKasaoApplication.getUser();
                if (null == mUser) {
                    startActivity(LoginActivity.class, null);
                    return;
                }
                if (ask != null && ask.showing()) {
                    ask.hide();
                } else {
                    getPrice(carDetailEntity);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivback:
                finish();
                break;
            case R.id.viewBuy:
                startActivity(ShopListActivity.class, null);
                break;
            case R.id.viewCall:
                callPerssion(this, mCarSeriesDetail.item.inphone);
                break;
            case R.id.tvHot:
                tvRank.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_mormal, null);
                tvRank.setTextColor(getResources().getColor(R.color.color_33333350));
                tvHot.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_select, null);
                tvHot.setTextColor(getResources().getColor(R.color.color_ee2e3b));
                break;
            case R.id.tvRank:
                tabMl();
                break;
        }
    }

    // 询底价
    public void getPrice(final CarDetailEntity car) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", car.goods_id);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getXundijia(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    UserCountdomain domain = GsonUtil.Companion.GsonToBean(t.string(), UserCountdomain.class);
                    if (null != domain) {
                        if (null == ask) {
                            ask = new DialogeAskprice(CarSeriesActivity.this);
                        }
                        ask.showDialoge(car.goods_price, domain.count);
                        ask.setSendPrice(new DialogeAskprice.SendPrice() {
                            @Override
                            public void sendPrice(String tel) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(ParmarsValue.KEY_GOODID, car.goods_id);
                                map.put(ParmarsValue.KEY_UID, BaseKasaoApplication.getUser().user_id);
                                map.put(ParmarsValue.KEY_PHONE, tel);
                                ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getLeavePhone(), map, new HttpRespnse() {
                                    @Override
                                    public void _onComplete() {

                                    }

                                    @Override
                                    public void _onNext(@NotNull ResponseBody t) {
                                        try {
                                            RtnSuss rtn = GsonUtil.Companion.GsonToBean(t.string(), RtnSuss.class);
                                            if (null != rtn && rtn.code.equals("200")) {
                                                ToastUtil.showAlter("您的信息已提交成功");
                                            } else {
                                                ToastUtil.showAlter("提交失败,重新提交");
                                            }
                                            ask.hide();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void _onError(@NotNull Throwable e) {
                                        ToastUtil.showAlter("提交失败,请检查网络信息");
                                        ask.hide();
                                    }
                                });

                            }
                        });

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


    @Override
    public void onloadData() {
        Map<String, String> map = new HashMap<>();
        map.put("set_id", goodId);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCarSystemDetail(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarSeriesedomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarSeriesedomain.class);
                    if (null != domain && null != domain.data) {
                        mCarSeriesDetail = domain.data;
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

    private Bannderdomain buildBanner(List<CarImage> img_list) {
        if (img_list == null || img_list.isEmpty()) {
            return null;
        }
        Bannderdomain domain = new Bannderdomain();
        List<BannerEntity> listbanner = new ArrayList<>();
        for (CarImage car : img_list) {
            BannerEntity entity = new BannerEntity();
            if (!TextUtils.isEmpty(car.url)) {
                LogUtil.e("Tag", "-------url=" + car.url);
                entity.img = car.url;
                listbanner.add(entity);
            }

        }
        domain.data = listbanner;
        return domain;
    }


    @Override
    public void rendView() {
        if (null != mCarSeriesDetail.item) {
            tvDes.setText(mCarSeriesDetail.item.name);
            banner.renderViewText(buildBanner(mCarSeriesDetail.item.images), 0.56f);
        }
        if (null != mCarSeriesDetail.setdata && !mCarSeriesDetail.setdata.isEmpty()) {
            int size = mCarSeriesDetail.setdata.size();
            for (int i = 0; i < size; i++) {
                map.put(mCarSeriesDetail.setdata.get(i).driver, mCarSeriesDetail.setdata.get(i).carlists);
                carNum += mCarSeriesDetail.setdata.get(i).carlists.size();
            }
            adapterFind.setMlist(mCarSeriesDetail.setdata.get(0).carlists);
            temple = mCarSeriesDetail.setdata.get(0).driver;
            Set<String> sets = map.keySet();
            for (final String key : sets) {
                LinearLayout view = (LinearLayout) LayoutInflater.from(CarSeriesActivity.this).inflate(R.layout.item_car_flow_tv, flowLayout, false);
                final TextView tv = view.findViewById(R.id.tvValue);

                if (temple.equals(key)) {
                    tv.setBackgroundResource(R.drawable.bg_border2_fbe0e2);
                } else {
                    tv.setBackgroundResource(R.drawable.bg_border6_dedede);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ss = view.getTag().toString();
                        temple = ss;
                        int n = flowLayout.getChildCount();
                        for (int i = 0; i < n; i++) {
                            View child = flowLayout.getChildAt(i);
                            if (child instanceof ViewGroup) {
                                if (((ViewGroup) child).getChildAt(0).getTag().toString().equals(temple)) {
                                    ((ViewGroup) child).getChildAt(0).setBackgroundResource(R.drawable.bg_border2_fbe0e2);
                                } else {
                                    ((ViewGroup) child).getChildAt(0).setBackgroundResource(R.drawable.bg_border6_dedede);
                                }
                            }
                        }
                        adapterFind.setMlist(map.get(key));
                    }
                });
                tv.setText(key);
                tv.setTag(key);
                flowLayout.addView(view);
                if (carNum > 0) {
                    tvNum.setText("共有" + carNum + "种车型");
                }
            }

        }
    }

    public void tabMl() {
        tvHot.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_mormal, null);
        tvHot.setTextColor(getResources().getColor(R.color.color_33333350));
        tvRank.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable_select, null);
        tvRank.setTextColor(getResources().getColor(R.color.color_ee2e3b));

        if (TextUtils.isEmpty(temple) || null == map) {
            return;
        }
        List<CarDetailEntity> list = map.get(temple);
        Collections.sort(list, new Comparator<CarDetailEntity>() {
            @Override
            public int compare(CarDetailEntity o1, CarDetailEntity o2) {
                return o1.zdml.compareTo(o2.zdml);
            }
        });
        Collections.reverse(list);
        adapterFind.setMlist(list);

    }
}

