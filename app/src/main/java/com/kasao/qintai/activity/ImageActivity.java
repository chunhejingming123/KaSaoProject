package com.kasao.qintai.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends BaseActivity {
    private int position;
    private TextView page;
    private int count;

    private String[] imageArray, nameArray;
    private int index;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_image;
    }

    @Override
    public void findView() {
        rendView();
    }

    @Override
    public void rendView() {
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        String picurl = intent.getStringExtra("picurl");
        imageArray = picurl.split(",");
        count = (imageArray.length);
        initViews();
    }

    private void initViews() {
        page = (TextView) findViewById(R.id.text_page);
        if (count <= 1) {
            page.setVisibility(View.GONE);
        } else {
            page.setVisibility(View.VISIBLE);
            page.setText((position + 1) + "/" + count);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.gestureimage_viewpager);
        viewPager.setPageMargin(20);
        viewPager.setAdapter(new ImagePagerAdapter(getWebImageViews()));
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                index = arg0;
                page.setText((arg0 + 1) + "/" + count);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private List<View> getWebImageViews() {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View view = View.inflate(ImageActivity.this, R.layout.item_gallery_view, null);
            ImageView iv = view.findViewById(R.id.iv);
            GlideUtil.intoFitCenter(this, imageArray[i], iv, ScreenUtil.getScreenW(), ScreenUtil.getScreenH(), R.drawable.bg_default);
            list.add(view);
        }
        return list;
    }


    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            showpop();
            return false;
        }
    };


    private void showpop() {
        View view = View.inflate(ImageActivity.this, R.layout.pop_email_file, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.5f);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        });
        ;
        Button button2 = (Button) view.findViewById(R.id.button2);
        Button button3 = (Button) view.findViewById(R.id.button3);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mPopupWindow.dismiss();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
            }
        });
    }


    /**
     * 弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<View> views;

        public ImagePagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}

