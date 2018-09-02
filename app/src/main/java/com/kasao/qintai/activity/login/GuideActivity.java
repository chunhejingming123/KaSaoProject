package com.kasao.qintai.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kasao.qintai.MainActivity;
import com.kasao.qintai.R;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.util.SharedPreferencesHelper;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.util.ArrayList;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:11
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private View ll_bottom;
    // 引导图片资源
    private int[] pics = {R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3};
    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPageAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_guide;
    }

    @Override
    public void findView() {
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_bottom.setOnClickListener(this);
        views = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ScreenUtil.initScreen(this);
        rendView();
    }

    @Override
    public void rendView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new ViewPageAdapter();
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            GlideUtil.into(this, pics[i], iv, ScreenUtil.getScreenW(), ScreenUtil.getScreenH(), 0);
            views.add(iv);
        }
        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom:
                SharedPreferencesHelper.getInstance(this).put(ParmarsValue.KEY_GUIDE, true);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            ll_bottom.setVisibility(View.VISIBLE);
        }
    }

    class ViewPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pics.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return (view == o);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }
    }
}
