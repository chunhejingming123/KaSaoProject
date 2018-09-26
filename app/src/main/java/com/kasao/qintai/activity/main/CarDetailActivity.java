package com.kasao.qintai.activity.main;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.activity.shop.OnLineOrderActivity;
import com.kasao.qintai.activity.shop.ShopListActivity;
import com.kasao.qintai.adapter.CarDetailImageAdapter;
import com.kasao.qintai.adapter.CarDetailRecommenAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintai.dialoge.DialogeAskprice;
import com.kasao.qintai.dialoge.ShareDialog;
import com.kasao.qintai.model.BannerEntity;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.CarImage;
import com.kasao.qintai.model.CarParmeterKeyValue;
import com.kasao.qintai.model.RtnSuss;
import com.kasao.qintai.model.domain.Bannderdomain;
import com.kasao.qintai.model.domain.CarDetaildomain;
import com.kasao.qintai.model.domain.User;
import com.kasao.qintai.model.domain.UserCountdomain;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.DataTypeChange;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.widget.CareBaseParmarsView;
import com.kasao.qintai.widget.ObserveScrollView;
import com.kasao.qintai.widget.banner.BannerView;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.OnTimeClickDuring;
import com.kasao.qintaiframework.until.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/7.
 * 简述:汽贸详情页面
 */

public class CarDetailActivity extends BaseActivity implements View.OnClickListener {
    ObserveScrollView scrollview;
    ImageView ivBack;// 返回图标
    ImageView ivShare;// 分享
    ImageView ivStore;// 收藏
    BannerView mBanner;//轮播图
    TextView tvDescride, tvMoney;
    ImageView ivScrolltoTop;
    View viewTab;
    View viewTop;
    TextView tvCarPhoto;
    TextView tvDetail, tvRecommend, tvKnow, tvParmars, tvSpeed, tvStore, tv_button;
    View viewRealPhoto;
    View viewItemDetail;
    View viewAllTop;
    View viewParmars;
    View viewKnow;
    WebView mWebView;
    WebView noticeWebView;
    View viewBuy, viewCall;
    View viewRecommend;
    LinearLayout conterLayout;
    RecyclerView recycleview, recycleViewRecommend;
    CarDetailImageAdapter adapter;
    CarDetailRecommenAdapter adapterRecommend;
    int distance = 0;
    private String goodId;
    private Handler mHandler = new Handler();
    private CarDetailEntity carEntity;
    public int titleShowPosition = 1;
    public int tabtop = 0;
    private boolean isStore;
    private String uid;
    private TextView[] tabView;
    private int currentNum;
    ShareDialog share;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_cardetail_layout;
    }


    @Override
    public void findView() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            goodId = uri.getQueryParameter("goodsId");
        }
        if (TextUtils.isEmpty(goodId)) {
            goodId = getIntent().getExtras().getString(ParmarsValue.KEY_GOODID);
        }
        if (TextUtils.isEmpty(goodId)) {
            finish();
            return;
        }

        uid = BaseKasaoApplication.getUser().user_id;
        scrollview = findViewById(R.id.scrollview);
        ivBack = findViewById(R.id.ivback);
        ivShare = findViewById(R.id.iv_share);
        ivStore = findViewById(R.id.iv_store);
        mBanner = findViewById(R.id.banner);
        tvDescride = findViewById(R.id.tv_descride);
        tvMoney = findViewById(R.id.tv_money);
        ivScrolltoTop = findViewById(R.id.iv_to_top);
        viewTab = findViewById(R.id.view_tab);
        viewTop = findViewById(R.id.view_top);
        tvCarPhoto = findViewById(R.id.tv01);
        tvDetail = findViewById(R.id.tv02);
        tvRecommend = findViewById(R.id.tv03);
        tvKnow = findViewById(R.id.tv04);
        tvParmars = findViewById(R.id.tv05);
        tvSpeed = findViewById(R.id.tvSpeed);
        viewRealPhoto = findViewById(R.id.view_realPhoto);
        viewItemDetail = findViewById(R.id.view_item_detail);
        viewAllTop = findViewById(R.id.vie_all_top);
        viewParmars = findViewById(R.id.view_parmars);
        viewKnow = findViewById(R.id.viewKnow);
        mWebView = findViewById(R.id.webview);
        noticeWebView = findViewById(R.id.webViewNotice);
        viewBuy = findViewById(R.id.viewBuy);
        viewRecommend = findViewById(R.id.viewRecommend);
        conterLayout = findViewById(R.id.viewParmars);
        recycleview = findViewById(R.id.recycleview);
        recycleViewRecommend = findViewById(R.id.recycleviewRecommend);
        tv_button = findViewById(R.id.tv_button);
        viewCall = findViewById(R.id.viewCall);
        viewCall.setOnClickListener(this);
        tv_button.setOnClickListener(this);
        tvStore = findViewById(R.id.tvStore);
        tvStore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivStore.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        viewBuy.setOnClickListener(this);
        viewParmars.setOnClickListener(this);
        ivScrolltoTop.setOnClickListener(this);
        tvParmars.setOnClickListener(this);
        tvCarPhoto.setOnClickListener(this);
        tvDetail.setOnClickListener(this);
        tvKnow.setOnClickListener(this);
        tvRecommend.setOnClickListener(this);
        ivScrolltoTop.setOnClickListener(this);


        tabView = new TextView[]{tvParmars, tvCarPhoto, tvDetail, tvKnow, tvRecommend};
        changeTab(0);
        initRecycle(recycleview, null);
        initRecycle(recycleViewRecommend, null);
        recycleview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollview.fullScroll(ScrollView.FOCUS_UP);

            }
        });
        recycleViewRecommend.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollview.fullScroll(ScrollView.FOCUS_UP);

            }
        });
        adapterRecommend = new CarDetailRecommenAdapter(new CarDetailRecommenAdapter.ActionToCarDetail() {
            @Override
            public void toCareDetail(CarDetailEntity entity) {
                Bundle bundle = new Bundle();
                bundle.putString(ParmarsValue.KEY_GOODID, entity.goods_id);
                startActivity(CarDetailActivity.class, bundle);
            }
        });
        recycleViewRecommend.setAdapter(adapterRecommend);
        scrollview.setScrollListener(new ObserveScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                distance = scrollview.getScrollY();
                if (distance <= 0) {
                    viewTop.setAlpha(0);
                    viewTab.setAlpha(0);
                    ivBack.setBackgroundResource(R.drawable.icon_round_back);
                    if (isStore) {
                        ivStore.setBackgroundResource(R.drawable.selctor_stored);
                    } else {
                        ivStore.setBackgroundResource(R.drawable.selctor_store);
                    }
                    ivShare.setBackgroundResource(R.drawable.icon_round_share);
                    ivBack.setAlpha(1f);
                    ivStore.setAlpha(1f);
                    ivShare.setAlpha(1f);
                } else if (distance > 0 && distance <= titleShowPosition) {
                    float scale = (float) distance / titleShowPosition;
                    //  float alpha = (255 * scale);
                    viewTab.setAlpha(scale);
                    viewTop.setAlpha(scale);
                    if (scale > 0 && scale < 0.6) {
                        ivBack.setBackgroundResource(R.drawable.icon_round_back);
                        if (isStore) {
                            ivStore.setBackgroundResource(R.drawable.selctor_stored);
                        } else {
                            ivStore.setBackgroundResource(R.drawable.selctor_store);
                        }
                        ivShare.setBackgroundResource(R.drawable.icon_round_share);
                        ivBack.setAlpha(1 - scale);
                        ivStore.setAlpha(1 - scale);
                        ivShare.setAlpha(1 - scale);
                    } else {
                        ivBack.setBackgroundResource(R.drawable.icon_back_arrow_gray);
                        if (isStore) {
                            ivStore.setBackgroundResource(R.drawable.icon_detaile_store);
                        } else {
                            ivStore.setBackgroundResource(R.drawable.icon_detail_unstore);
                        }
                        ivShare.setBackgroundResource(R.drawable.icon_detail_share);
                        ivBack.setAlpha(scale);
                        ivStore.setAlpha(scale);
                        ivShare.setAlpha(scale);
                    }

                } else {
                    viewTab.setAlpha(1f);
                    viewTop.setAlpha(1f);
                    ivBack.setAlpha(1f);
                    ivStore.setAlpha(1f);
                    ivShare.setAlpha(1f);
                }
                if (distance > ivScrolltoTop.getTop()) {
                    ivScrolltoTop.setVisibility(View.VISIBLE);
                } else {
                    ivScrolltoTop.setVisibility(View.GONE);
                }
                int parmarsHeight = viewParmars.getHeight();
                int photoHeight = viewRealPhoto.getHeight();
                int detailHeight = viewItemDetail.getHeight();
                int guideHeight = viewKnow.getHeight();

                if (distance > 0 && distance < viewAllTop.getHeight() + parmarsHeight - tabtop) {
                    changeTab(0);
                } else if (distance >= viewAllTop.getHeight() + parmarsHeight - tabtop && distance < viewAllTop.getHeight() + photoHeight + parmarsHeight - tabtop) {
                    changeTab(1);
                } else if (distance >= viewAllTop.getHeight() + parmarsHeight + photoHeight - tabtop && distance < viewAllTop.getHeight() + parmarsHeight + photoHeight + detailHeight - tabtop) {
                    changeTab(2);
                } else if (distance >= viewAllTop.getHeight() + parmarsHeight + photoHeight + detailHeight - tabtop && distance < viewAllTop.getHeight() + +parmarsHeight + photoHeight + detailHeight + guideHeight - tabtop) {
                    changeTab(3);
                } else {
                    changeTab(4);
                }
            }
        });
        adapter = new CarDetailImageAdapter();
        recycleview.setAdapter(adapter);
        ivBack.setBackgroundResource(R.drawable.icon_round_back);
        if (isStore) {
            ivStore.setBackgroundResource(R.drawable.selctor_stored);
        } else {
            ivStore.setBackgroundResource(R.drawable.selctor_store);
        }
        ivShare.setBackgroundResource(R.drawable.icon_round_share);
        ivBack.setAlpha(1);
        ivStore.setAlpha(1);
        ivShare.setAlpha(1);
        initWebView();
    }

    private void changeTab(int index) {
        int size = tabView.length;
        for (int i = 0; i < size; i++) {
            if (index == i) {
                tabView[i].setTextColor(ContextComp.getColor(R.color.color_dd3838));
            } else {
                tabView[i].setTextColor(ContextComp.getColor(R.color.color_333333));
            }
        }
    }


    // 获取汽车详情
    @Override
    public void onloadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParmarsValue.KEY_GOODID, goodId);
        map.put(ParmarsValue.KEY_UID, uid);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCAR_DATAI(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {
            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarDetaildomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarDetaildomain.class);
                    if (null != domain && domain.data != null) {
                        carEntity = domain.data;
                        rendView();
                    }
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });

        getPrice();
    }

    @Override
    public void rendView() {
        if (null != carEntity) {
            tvDescride.setText(TextUtils.isEmpty(carEntity.name) ? "" : carEntity.name);
            String money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(carEntity.goods_price));//entity.goods_price;
            tvMoney.setText(money);
            isStore = carEntity.fav_type;
            if (isStore) {
                ivStore.setBackgroundResource(R.drawable.selctor_stored);
            } else {
                ivStore.setBackgroundResource(R.drawable.selctor_store);
            }
            tvSpeed.setText(carEntity.purpose);
            adapter.setData(carEntity);
            if (null != carEntity.recommend_list && !carEntity.recommend_list.isEmpty()) {
                adapterRecommend.setData(carEntity.recommend_list);
                viewRecommend.setVisibility(View.VISIBLE);
                tvRecommend.setVisibility(View.VISIBLE);
            } else {
                viewRecommend.setVisibility(View.GONE);
                tvRecommend.setVisibility(View.GONE);
            }
            Bannderdomain domain = buildBanner(carEntity.img_list);
            if (domain == null || domain.data == null || domain.data.isEmpty()) {
                mBanner.setBackgroundResource(R.drawable.bg_default);
            } else {
                mBanner.renderViewText(domain, 0.56f);
            }
            if (!carEntity.browse_type) {
                recorder();
            }
            if (!TextUtils.isEmpty(carEntity.notice)) {
                tvKnow.setVisibility(View.VISIBLE);
                viewKnow.setVisibility(View.VISIBLE);
                noticeWebView.loadDataWithBaseURL("", getNewContent(carEntity.notice), "text/html", "utf-8", null);
            } else {
                tvKnow.setVisibility(View.GONE);
                viewKnow.setVisibility(View.GONE);
            }
            viewTab.setAlpha(0);
            List<CarParmeterKeyValue> lists = carEntity.target;
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(lists.get(i).cname)) {
                    CareBaseParmarsView view = new CareBaseParmarsView(getApplicationContext());
                    view.rendView(lists.get(i));
                    conterLayout.addView(view);
                }
            }
            View view = LayoutInflater.from(this).inflate(R.layout.item_carparmars_more, conterLayout, false);
            conterLayout.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carParmars();
                }
            });

        }
    }

    private String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }


    private Bannderdomain buildBanner(List<CarImage> img_list) {
        if (img_list == null || img_list.isEmpty()) {
            return null;
        }
        Bannderdomain domain = new Bannderdomain();
        List<BannerEntity> listbanner = new ArrayList<>();
        for (CarImage car : img_list) {
            BannerEntity entity = new BannerEntity();
            entity.img = car.url;
            listbanner.add(entity);
        }
        domain.data = listbanner;
        return domain;
    }

    @Override
    protected void onResume() {
        super.onResume();
        User mUser = BaseKasaoApplication.getUser();
        if (null == mUser) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
                titleShowPosition = mBanner.getHeight();
                tabtop = viewTab.getHeight() + viewTop.getHeight();
            }
        }, 200);
        if (null != share) {
            share.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivback:
                finish();
                break;
            case R.id.iv_share:
                share();
                break;
            case R.id.iv_store:
                store();
                break;
            case R.id.viewBuy:
                onLineBuy();
                break;
            case R.id.tvStore:
                gotoStoreShop();
                break;
            case R.id.tv_button:
                liveMoble();
                break;
            case R.id.iv_to_top:
                backToTop();
                break;
            case R.id.tv05:
                tvParmars.setSelected(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.smoothScrollTo(0, viewAllTop.getHeight() - tabtop);
                    }
                }, 200);
                break;
            case R.id.tv04:
                tvKnow.setSelected(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.smoothScrollTo(0, viewAllTop.getHeight() + viewRealPhoto.getHeight() + viewItemDetail.getHeight() + viewParmars.getHeight() - tabtop);
                    }
                }, 200);
                break;
            case R.id.tv03:
                tvRecommend.setSelected(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.smoothScrollTo(0, viewAllTop.getHeight() + viewRealPhoto.getHeight() + viewItemDetail.getHeight() + viewParmars.getHeight() + viewKnow.getHeight() - tabtop);
                    }
                }, 200);
                break;
            case R.id.tv02:
                tvDetail.setSelected(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.smoothScrollTo(0, viewAllTop.getHeight() + viewRealPhoto.getHeight() + viewParmars.getHeight() - tabtop);
                    }
                }, 200);

                break;
            case R.id.tv01:
                tvCarPhoto.setSelected(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.smoothScrollTo(0, viewAllTop.getHeight() + viewParmars.getHeight() - tabtop);
                    }
                }, 200);
                break;
            case R.id.viewCall:
                if (null != carEntity) {
                    callPerssion(this, carEntity.inphone);
                }
                break;


        }
    }


    public void share() {
        String shareImg;
        if (null != carEntity) {
            if (null != carEntity.img_list && null != carEntity.img_list.get(0)) {
                shareImg = carEntity.img_list.get(0).url;
                share = ShareDialog.getInstanceShareDialoge(CarDetailActivity.this, TextUtils.isEmpty(carEntity.name) ? "卡车" : carEntity.name, ApiInterface.Companion.getRequestUrl(ApiInterface.Companion.getShareCarDetailUrl()) + goodId, shareImg, carEntity.goods_jingle);
                share.show();
            }
        }
    }


    public void store() {
        if (!OnTimeClickDuring.getInstance().onTickTimeChange(System.currentTimeMillis())) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(ParmarsValue.KEY_GOODID, goodId);
        map.put(ParmarsValue.KEY_UID, uid);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCAR_STORE(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    RtnSuss rtn = GsonUtil.Companion.GsonToBean(t.string(), RtnSuss.class);
                    isStore = rtn.code.equals("200");
                    if (isStore) {
                        ivStore.setBackgroundResource(R.drawable.selctor_stored);
                    } else {
                        ivStore.setBackgroundResource(R.drawable.selctor_store);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void _onError(@NotNull Throwable e) {
                ToastUtil.showAlter("收藏失败");
            }
        });

    }


    public void onLineBuy() {
        if (null != carEntity) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ParmarsValue.KEY_OBJ, carEntity);
            startActivity(OnLineOrderActivity.class, bundle);
        }

    }

    //参数配置
    private void carParmars() {
        Bundle bundle = new Bundle();
        bundle.putString(ParmarsValue.KEY_GOODID, goodId);
        startActivity(CarParmarsDeatailActivity.class, bundle);
    }


    public void gotoStoreShop() {
        startActivity(ShopListActivity.class, null);
    }

    // 询底价
    public void liveMoble() {
        final DialogeAskprice ask = new DialogeAskprice(this);
        ask.showDialoge(carEntity.goods_price, currentNum);
        ask.setSendPrice(new DialogeAskprice.SendPrice() {
            @Override
            public void sendPrice(String tel) {
                Map<String, String> map = new HashMap<>();
                map.put(ParmarsValue.KEY_GOODID, goodId);
                map.put(ParmarsValue.KEY_UID, uid);
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

    // 返回顶部
    public void backToTop() {
        scrollview.fullScroll(ScrollView.FOCUS_UP);
        ivScrolltoTop.setVisibility(View.GONE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                distance = 0;
            }
        }, 200);

    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(null);
        mHandler = null;
        distance = 0;
        if (mBanner != null) {
            mBanner.destory();
        }
        if (null != noticeWebView) {
            ViewParent parent = noticeWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(noticeWebView);
            }
            noticeWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            noticeWebView.getSettings().setJavaScriptEnabled(false);
            noticeWebView.clearHistory();
            noticeWebView.clearView();
            noticeWebView.removeAllViews();
            noticeWebView.destroy();
            noticeWebView = null;
        }
        if (null != mWebView) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        //settings.setUseWideViewPort(true);//调整到适合webview的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        mWebView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);//设置js可用
        mWebView.setWebViewClient(new WebViewClient());
        //noinspection deprecation
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        mWebView.loadUrl(ApiInterface.Companion.getRequestUrl(ApiInterface.Companion.getLINK_CAR_DETAI()) + goodId);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 在APP内部打开链接，不要调用系统浏览器
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings1 = noticeWebView.getSettings();
        settings1.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        noticeWebView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        //设置WebView属性，能够执行Javascript脚本
        settings1.setJavaScriptEnabled(true);//设置js可用
        noticeWebView.setWebViewClient(new WebViewClient());
        //    noticeWebView.setScrollContainer(false);
        settings1.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局


    }

    //浏览过汽车详情
    private void recorder() {
        Map<String, String> map = new HashMap<>();
        map.put(ParmarsValue.KEY_GOODID, goodId);
        map.put(ParmarsValue.KEY_UID, uid);
        map.put(ParmarsValue.KEY_BRAND, carEntity.brand_id);

        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCAR_BROWSE(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {

            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });

    }

    // 询底价
    public void getPrice() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goodId);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getXundijia(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    UserCountdomain domain = GsonUtil.Companion.GsonToBean(t.string(), UserCountdomain.class);
                    if (null != domain) {
                        currentNum = domain.count;
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
