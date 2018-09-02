package com.kasao.qintai.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.model.BannerEntity;
import com.kasao.qintai.model.domain.Bannderdomain;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * banner view 封装
 * Created by pengfeili on 15/9/12.
 */
public class BannerView extends FrameLayout implements BannerPagerAdapter.OnClickBannerListener {
   private float scale=0.0f;
    private Context mContext;
    private OnBannerViewActionListener mOnBannerViewActionListener;
    private ViewPager mBannerViewPager;
    private BannerPagerAdapter mBannerPagerAdapter;
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private List<View> dotViewsList;

    public BannerView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    /**
     * 设置banner action listener
     *
     * @param onBannerViewActionListener
     */
    public void setOnBannerViewActionListener(OnBannerViewActionListener onBannerViewActionListener) {
        mOnBannerViewActionListener = onBannerViewActionListener;
    }

    /**
     * 渲染view
     *
     * @param
     */
    public void renderView(Bannderdomain banner) {
        calculateLayout(banner);
        resetDotView(banner);
        List<BannerEntity> nodes = banner.data;
        mBannerPagerAdapter.replaceBanner(nodes);
        size = banner.data.size();
        if (nodes != null && !nodes.isEmpty()) {
            mBannerViewPager.setCurrentItem(nodes.size() * 1000);
            startImageTimerTask();
        } else {
            mBannerViewPager.setCurrentItem(0);
        }

    }

    public void renderView(Bannderdomain banner,float scale) {
        this.scale=scale;
        calculateLayout(banner);
        resetDotView(banner);
        List<BannerEntity> nodes = banner.data;
        mBannerPagerAdapter.replaceBanner(nodes);
        size = banner.data.size();
        if (nodes != null && !nodes.isEmpty()) {
            mBannerViewPager.setCurrentItem(nodes.size() * 1000);
            startImageTimerTask();
        } else {
            mBannerViewPager.setCurrentItem(0);
        }

    }
    public void renderViewText(Bannderdomain banner,float scales) {
        this.scale=scales;
        calculateLayout(banner);
        resetDotView(banner);
        List<BannerEntity> nodes = banner.data;
        mBannerPagerAdapter.replaceBanner(nodes);
        size = banner.data.size();
        if (nodes != null) {
            mBannerViewPager.setCurrentItem(nodes.size() * 1000);
            mLinearLayout.setVisibility(GONE);
            mTextView.setVisibility(VISIBLE);
            mTextView.setText("1/"+size);
         startImageTimerTask();
        } else {
            mBannerViewPager.setCurrentItem(0);
        }

    }
    private int size;
    /**
     * 初始化布局以及view
     */
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.widget_banner_viewpager, this,
                true);
        mBannerViewPager = (ViewPager) rootView.findViewById(R.id.widget_banner_item_viewpager);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.widget_banner_viewpager_dot_list);
        mTextView=(TextView) rootView.findViewById(R.id.widge_banner_textshow);
        mTextView.setVisibility(GONE);
        mBannerPagerAdapter = new BannerPagerAdapter(mContext, this);
        mBannerViewPager.setAdapter(mBannerPagerAdapter);
        dotViewsList = new ArrayList<>();
        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int position1 = position % dotViewsList.size();
                for (int i = 0, len = dotViewsList.size(); i < len; i++) {
                    if (i == position1) {//current
                        dotViewsList.get(i).setBackgroundResource(R.drawable.dot_item_current);
                    } else {
                        dotViewsList.get(i).setBackgroundResource(R.drawable.dot_item);
                    }
                }
                if(size>0){
                    int index=1+(position % size);
                    mTextView.setText(index+"/"+size);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnBannerViewActionListener != null) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {//在拖拽
                        mOnBannerViewActionListener.onHorizontalImageListScrollStart();
                    } else {//动作完成
                        mOnBannerViewActionListener.onHorizontalImageListScrollStop();
                    }
                }
            }
        });
//        mBannerViewPager.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        // 开始图片滚动
//                        startImageTimerTask();
//                        break;
//                    default:
//                        // 停止图片滚动
//                        stopImageTimerTask();
//                        break;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onClickBanner(BannerEntity bannerNode) {
        if (mOnBannerViewActionListener != null) {
            mOnBannerViewActionListener.onClickBanner(bannerNode);
        }
    }

    /**
     * 计算布局宽高
     *
     * @param banner
     */
    private void calculateLayout(Bannderdomain banner) {
//        int height = banner.height,
//                width = banner.width;
//        if (width <= 0) {
//            width = ScreenUtil.getScreenW();
//        }
     //   height = (int) (((float) height / width) * ScreenUtil.getScreenW());
        int  height= (int) (scale* ScreenUtil.getScreenW());
        if(height<=0){
            height = getResources().getDimensionPixelSize(R.dimen.banner_default_height);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        mBannerViewPager.setLayoutParams(layoutParams);
    }



    /**
     * 重置dot view
     *
     * @param banner
     */
    private void resetDotView(Bannderdomain banner) {
        List<BannerEntity> nodes = banner.data;
        //多个dot
        for (View v : dotViewsList) {
            try {
                mLinearLayout.removeView(v);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        dotViewsList.clear();
        final int size = nodes.size();
        int wh = getResources().getDimensionPixelSize(R.dimen.dimen_7);
        for (int i = 0; i < size; i++) {
            View dot = new View(mLinearLayout.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(wh, wh);
            layoutParams1.setMargins(wh / 3, 0, wh / 3, 0);
            dot.setLayoutParams(layoutParams1);
            if (i > 0) {
                dot.setBackgroundResource(R.drawable.dot_item);
            } else {
                dot.setBackgroundResource(R.drawable.dot_item_current);
            }
            mLinearLayout.addView(dot);
            dotViewsList.add(dot);
        }
        if (size < 2) {
            mLinearLayout.setVisibility(GONE);
        } else {
            mLinearLayout.setVisibility(VISIBLE);
        }
    }

    /** 轮播状态 */
    private boolean isRunning = false;

    /** 轮播间隔 */
    private int mWaitMillisecond = 5000;

    private static final int AUTO_START = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AUTO_START:
                    if (isRunning) {
                        if (size != 0) {
                            if (mBannerViewPager.getCurrentItem() == size - 1) {
                                mBannerViewPager.setCurrentItem(0);
                            } else {
                                mBannerViewPager.setCurrentItem((mBannerViewPager.getCurrentItem() + 1));
                            }
                            mHandler.sendEmptyMessageDelayed(AUTO_START, mWaitMillisecond);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 开始轮播
     */
    public void startImageTimerTask() {
        if (size > 1) {
            isRunning = true;
            if(null!=mHandler){
                mHandler.removeMessages(AUTO_START);
                mHandler.sendEmptyMessageDelayed(AUTO_START, mWaitMillisecond);
            }

        }
    }


    /**
     * 停止轮播
     */
    public void stopImageTimerTask() {
        if(null!=mHandler){
        mHandler.removeMessages(AUTO_START);
        isRunning = false;}
    }

    public void destory() {
        if(null!=mHandler){
            stopImageTimerTask();
            mHandler.removeCallbacks(null);
            mHandler=null;
        }
    }

    /**
     * 处理banner相关的动作
     */
    public interface OnBannerViewActionListener {
        /**
         * banner横向滑动开始
         */
        void onHorizontalImageListScrollStart();

        /**
         * banner横向滑动结束
         */
        void onHorizontalImageListScrollStop();

        /**
         * 点击banner node
         *
         * @param node
         */
        void onClickBanner(BannerEntity node);
    }
}
