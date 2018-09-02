package com.kasao.qintai.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.R;

import java.util.List;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:17
 */

public class ViewPageCarRankAdapter extends PagerAdapter {
    public List<CarDetailEntity> mAdList;
    private Context context;
    private TextView tvIndex;
    private View viewBg;

    public ViewPageCarRankAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mAdList == null ? 0 : (mAdList.size() > 3 ? 3 : mAdList.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final CarDetailEntity exponent = mAdList.get(position);
        View view = LayoutInflater.from(context).inflate(
                R.layout.itempage_car_rank, null);
        viewBg = view.findViewById(R.id.viewBg);
        tvIndex = view.findViewById(R.id.tvTitle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mIClickPager) {
                    mIClickPager.onClickPager(exponent);
                }
            }
        });
        tvIndex.setText(exponent.name);
        switch (position) {
            case 0:
                viewBg.setBackgroundResource(R.drawable.icon_rank01);
                break;
            case 1:
                viewBg.setBackgroundResource(R.drawable.icon_rank02);
                break;
            case 2:
                viewBg.setBackgroundResource(R.drawable.icon_rank03);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    /**
     * 页面宽度所占ViewPager测量宽度的权重比例，默认为1
     */
    @Override
    public float getPageWidth(int position) {
        if (null != mAdList) {
            if (mAdList.size() > 2) {
                return (float) 0.33;
            } else if (mAdList.size() == 2) {
                return (float) 0.50;
            } else {
                return (float) 1.0;
            }
        }
        return 0;
    }

    private IClickPager mIClickPager;

    public void setmIClickPager(IClickPager mIClickPager) {
        this.mIClickPager = mIClickPager;
    }

    public void setData(List<CarDetailEntity> exponent) {
        this.mAdList = exponent;
        notifyDataSetChanged();
    }

    public interface IClickPager {
        void onClickPager(CarDetailEntity exponent);
    }
}
