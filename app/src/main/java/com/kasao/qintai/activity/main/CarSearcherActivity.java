package com.kasao.qintai.activity.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.CarSearchAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.dialoge.DialogeCarChose;
import com.kasao.qintai.model.CarBrand;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.domain.CarListdomain;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.widget.menu.BrandGridView;
import com.kasao.qintai.widget.menu.DropDownMenu;
import com.kasao.qintai.widget.menu.adapter.ListIconDropDownAdapter;
import com.kasao.qintai.widget.menu.adapter.PriceGridView;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.OnTimeClickDuring;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/7.
 * 简述:汽贸搜索页面
 */

public class CarSearcherActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditext;
    private RecyclerView recycleView;
    private DropDownMenu mDropDownMenu;
    private CarSearchAdapter searchAdapter;
    private List<View> popupViews = new ArrayList<>();
    private ListIconDropDownAdapter orderAdapter;
    private String headers[];
    private String order[];
    private boolean isItemStyle = true;
    private boolean isLoading = false;
    private String cid;
    private int currypage = 1;
    private Map<String, String> map;
    private boolean isFirst;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_carsearcher_layout;
    }

    @Override
    public void findView() {
        cid = getIntent().getStringExtra(ParmarsValue.KEY_CID);
        headers = ContextComp.getStringArray(R.array.array_head);
        order = ContextComp.getStringArray(R.array.array_order);
        mEditext = findViewById(R.id.et_editex);
        findViewById(R.id.iv_change_item).setBackgroundResource(R.drawable.icon_category_a);
        recycleView =findViewById(R.id.recycleView);
        mDropDownMenu = findViewById(R.id.dropDownMenu);
        initRecycle(recycleView, new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = getLlm().findLastVisibleItemPosition();
                int totalItemCount = getLlm().getItemCount();
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0 && !isLoading) {
                    isLoading = true;
                    onloadData();
                }
            }
        });
        searchAdapter = new CarSearchAdapter();
        recycleView.setAdapter(searchAdapter);
        searchAdapter.setCarSellAction(new CarSearchAdapter.ICarSellAction() {
            @Override
            public void onCarDetaile(CarDetailEntity entity) {
                Intent intent = new Intent(CarSearcherActivity.this, CarSeriesActivity.class);
                intent.putExtra(ParmarsValue.KEY_GOODID, entity.goods_id);
                startActivity(intent);
            }

            @Override
            public void onCarDel(@NotNull CarDetailEntity entity, int index) {

            }
        });
        //init order menu
        final ListView orderView = new ListView(this);
        orderAdapter = new ListIconDropDownAdapter(this, Arrays.asList(order));
        orderView.setDividerHeight(0);
        orderView.setAdapter(orderAdapter);
        final BrandGridView brandView = new BrandGridView(this);
        final PriceGridView priceView = new PriceGridView(this);
        final View constellationView = getLayoutInflater().inflate(R.layout.grid_drop_down_layout, null);
        popupViews.add(orderView);
        popupViews.add(brandView);
        popupViews.add(priceView);
        popupViews.add(constellationView);
        //add item click event
        orderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currypage = 1;
                isLoading = false;
                orderAdapter.setCheckItem(position);
                map = getSearchParmars();
                if (position == 0) {// 价格 默认
                    map.put(ParmarsValue.KEY_SORT, "1");
                    map.put(ParmarsValue.KEY_ORDER, "1");
                } else if (position == 1) {// 价格 最高
                    map.put(ParmarsValue.KEY_SORT, "1");
                } else {
                    map.put(ParmarsValue.KEY_SORT, "2");// 最新
                }
                searchByCondition(map);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : order[position]);
                mDropDownMenu.closeMenu();
            }
        });

        brandView.setItemBrand(new BrandGridView.IItemBrand() {
            @Override
            public void onBrand(CarBrand brands) {
                final DialogeCarChose chose = new DialogeCarChose(CarSearcherActivity.this, brands.name, brands.id);
                chose.showDialoge(new DialogeCarChose.IChoseSeries() {
                    @Override
                    public void onChoseSerise(String id) {
                        Intent intent = new Intent(CarSearcherActivity.this, CarSeriesActivity.class);
                        intent.putExtra(ParmarsValue.KEY_GOODID, id);
                        startActivity(intent);
                        chose.hide();
                    }
                });

            }
        });

        priceView.setItemPrice(new PriceGridView.IItemPrice() {
            @Override
            public void onPrice(String price, int position) {
                mDropDownMenu.setTabText(position == 0 ? headers[2] : price);
                mDropDownMenu.closeMenu();
                currypage = 1;
                isLoading = false;
                map = getSearchParmars();
                try {
                    price = URLDecoder.decode(price, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                map.put(ParmarsValue.KEY_PRICE, price);
                searchByCondition(map);
            }

            @Override
            public void onCustomPrice(String minprice, String maxprice) {
                mDropDownMenu.setTabText(minprice + "-" + maxprice + "万");
                mDropDownMenu.closeMenu();
                currypage = 1;
                isLoading = false;
                map = getSearchParmars();
                map.put(ParmarsValue.KEY_MIN_PRICE, minprice);
                map.put(ParmarsValue.KEY_MAX_PRICE, maxprice);
                searchByCondition(map);
            }
        });
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);
        mDropDownMenu.setChoseBanner(new DropDownMenu.IChoseBanner() {
            @Override
            public void onChoseItem(int index) {
                currypage = 1;
                isLoading = false;
                if (index == 3) {
                    map = getSearchParmars();
                    map.put(ParmarsValue.KEY_SORT, "3");//销量排序
                    searchByCondition(map);
                }
            }
        });

        mEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {

                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    keySearch();
                }
                return false;
            }
        });
        mEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != mDropDownMenu) {
                    mDropDownMenu.closeMenu();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        map = getSearchParmars();
        if (!TextUtils.isEmpty(cid)) {
            map.put(ParmarsValue.KEY_CID, cid);
        }

        rendView();
    }

    @Override
    public void rendView() {
        findViewById(R.id.view_search).setOnClickListener(this);
        findViewById(R.id.iv_view_change).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst && null != mDropDownMenu) {
            mDropDownMenu.openMenu(2);
            isFirst = true;
        }

    }


    @Override
    public void onloadData() {
        if (null == map) {
            return;
        }
        map.put("pageNum", currypage + "");
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCAR_LIST(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarListdomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarListdomain.class);
                    if (null != domain && null != domain.data) {
                        List<CarDetailEntity> list = domain.data;
                        if (null != list || !list.isEmpty()) {
                            searchAdapter.setDatas(list);
                            isLoading = false;
                            currypage++;
                        } else {
                            isLoading = true;
                        }
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

    private Map<String, String> getSearchParmars() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_view_change:
                onChangeItemStyle();
                break;
            case R.id.view_search:
                searchKeyWord();
                break;


        }
    }


    public void onChangeItemStyle() {
        if (OnTimeClickDuring.getInstance().onTickTimeChange(System.currentTimeMillis())) {
            searchAdapter.setItemStyle(isItemStyle);
            if (isItemStyle) {
                findViewById(R.id.iv_change_item).setBackgroundResource(R.drawable.icon_category_b);
            } else {
                findViewById(R.id.iv_change_item).setBackgroundResource(R.drawable.icon_category_a);
            }
            isItemStyle = !isItemStyle;
        }
    }


    public void searchKeyWord() {
        if (null != mDropDownMenu) {
            mDropDownMenu.closeMenu();
        }
        keySearch();
    }

    private void keySearch() {
        String keyWords = mEditext.getText().toString().trim();
        try {
            keyWords = URLDecoder.decode(keyWords, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(keyWords)) {
            return;
        }
        currypage = 1;
        isLoading = false;
        map = getSearchParmars();
        map.put(ParmarsValue.KEY_KEYWORD, keyWords);
        searchByCondition(map);
    }

    public void searchByCondition(Map<String, String> map) {
        if (null == map || map.isEmpty()) {
            return;
        }
        map.put("pageNum", currypage + "");

        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCAR_LIST(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarListdomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarListdomain.class);
                    if (null != domain && null != domain.data) {
                        searchAdapter.setFresh(domain.data);
                        currypage++;
                    } else {
                        searchAdapter.setNodata();
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
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        }
        if (mEditext != null) {
            mEditext.removeTextChangedListener(null);
        }
        if (null != searchAdapter) {
            searchAdapter.onDestroy();
        }
        super.onDestroy();
    }
}
