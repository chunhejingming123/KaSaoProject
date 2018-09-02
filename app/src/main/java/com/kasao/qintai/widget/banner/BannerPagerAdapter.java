package com.kasao.qintai.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kasao.qintai.R;
import com.kasao.qintai.model.BannerEntity;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintaiframework.base.MyApplication;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * banner广告位置轮播图adapter
 *
 * @auth lipf on 2015/1/29.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private List<BannerEntity> mBannerNodeList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnClickBannerListener mSelfOnClickBannerListener;
    private GlideUtil mGlide;

    public BannerPagerAdapter(Context context, OnClickBannerListener onClickBannerListener) {
        mSelfOnClickBannerListener = onClickBannerListener;
        this.mBannerNodeList = new ArrayList<>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGlide = GlideUtil.getInstance();
    }

    /**
     * 替换banner数据源
     *
     * @param bannerNodeList
     */
    public void replaceBanner(List<BannerEntity> bannerNodeList) {
        synchronized (mBannerNodeList) {
            mBannerNodeList.clear();
            mBannerNodeList.addAll(bannerNodeList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (mBannerNodeList.size() > 0) {
            if (mBannerNodeList.size() == 1) {
                return 1;
            } else {
                return mBannerNodeList.size() > 0 ? Integer.MAX_VALUE : 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final BannerEntity node = mBannerNodeList.get(position % mBannerNodeList.size());
        View view = mLayoutInflater.inflate(R.layout.view_banner_item, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelfOnClickBannerListener != null) {
                    mSelfOnClickBannerListener.onClickBanner(node);
                }
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.banner_item_image);
//        final TextView descTextView = (TextView) view.findViewById(R.id.banner_item_desc);
//        final TextView titleTextView = (TextView) view.findViewById(R.id.banner_item_title);
//        if (TextUtils.isEmpty(node.description)) {
//            descTextView.setVisibility(View.GONE);
//        } else {
//            descTextView.setVisibility(View.VISIBLE);
//            descTextView.setText(node.description);
//        }
//        if (TextUtils.isEmpty(node.title)) {
//            titleTextView.setVisibility(View.GONE);
//        } else {
//            titleTextView.setVisibility(View.VISIBLE);
//            titleTextView.setText(node.title);
//          //  titleTextView.setBackgroundDrawable(FeelUtils.getShapeDrawable(mContext.getResources().getColor(R.color.subject_button_bg), ScreenUtil.dp2px(2)));
//        }
        if (!TextUtils.isEmpty(node.img)) {
            GlideUtil.into(mContext, node.img, imageView,R.drawable.bg_default);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    public interface OnClickBannerListener {
        public void onClickBanner(BannerEntity node);
    }
}
