package com.kasao.qintai.activity.kayou;

import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.fragment.social.HistoryLookFragment;
import com.kasao.qintai.fragment.social.HistoryThumbleFragment;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.base.BaseFragment;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/29 0029:15
 */

public class VisitorActivity extends BaseActivity implements View.OnClickListener {
    public TextView tvLook;
    public TextView tvZan;
    View viewLine;
    private FragmentManager mFragmentManager;
    private BaseFragment mFragment;
    private int translationX = 0;
    private String id;
    private int textWidth;
    private String currenttype = "2";

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_visitor;
    }

    @Override
    public void findView() {
        tvLook = findViewById(R.id.tv_look);
        tvZan = findViewById(R.id.tv_zan);
        viewLine = findViewById(R.id.viewanimal);
        rendView();
    }


    private void initViews() {
        mFragmentManager = getSupportFragmentManager();
        HistoryLookFragment fragment = HistoryLookFragment.Companion.newInstance(id);
        replaceFragment(fragment, ParmarsValue.HISTORY_LOOK, R.id.fl_connter);
    }

    private void replaceFragment(BaseFragment fragment, String name, int fl_connter) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (null != mFragment) {
            fragmentTransaction.detach(mFragment);
        }
        mFragment = fragment;
        fragmentTransaction.add(fl_connter, fragment, name).commitAllowingStateLoss();
    }

    @Override
    public void rendView() {
        id = getIntent().getStringExtra("id");
        int zan = getIntent().getIntExtra("zan", 0);
        int browse = getIntent().getIntExtra("history", 0);

        View back = findViewById(R.id.viewBack);
        TextView tvtitle = (TextView) findViewById(R.id.tvTitle);
        tvtitle.setText(ContextComp.getString(R.string.title_vistor));
        back.setOnClickListener(this);
        tvLook.setOnClickListener(this);
        tvZan.setOnClickListener(this);
        tvLook.setSelected(true);
        tvZan.setSelected(false);

        String str1 = "谁看过(" + browse + ")";
        int length1 = str1.length();
        SpannableString styledText1 = new SpannableString(str1);
        styledText1.setSpan(new TextAppearanceSpan(this, R.style.span1), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText1.setSpan(new TextAppearanceSpan(this, R.style.span2), 3, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLook.setText(styledText1, TextView.BufferType.SPANNABLE);

        String str2 = "谁赞过(" + zan + ")";
        int length2 = str2.length();
        SpannableString styledText2 = new SpannableString(str2);
        styledText2.setSpan(new TextAppearanceSpan(this, R.style.span1), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText2.setSpan(new TextAppearanceSpan(this, R.style.span2), 3, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvZan.setText(styledText2, TextView.BufferType.SPANNABLE);
        initViews();
        Paint mPaint = new Paint();
        textWidth = (int) mPaint.measureText(str1) / 2;
        transferx(true, 0, ScreenUtil.getScreenW() / 4 - textWidth);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewBack:
                onBackPressed();
                break;
            case R.id.tv_look:
                changeToTab("history_look");
                if (tvLook.isSelected()) {
                } else {
                    transferx(false, 3 * ScreenUtil.getScreenW() / 4 - textWidth, (ScreenUtil.getScreenW() / 4 - textWidth));
                }
                tvLook.setSelected(true);
                tvZan.setSelected(false);
                break;
            case R.id.tv_zan:
                changeToTab("history_zan");
                if (tvZan.isSelected()) {
                } else {
                    transferx(false, ScreenUtil.getScreenW() / 4 - textWidth, 3 * ScreenUtil.getScreenW() / 4 - textWidth);
                }
                tvLook.setSelected(false);
                tvZan.setSelected(true);
                break;
        }
    }

    private void changeToTab(String tagname) {
        String name = makeFragmentName(tagname, 0);
        BaseFragment fragment = getFragmentByContext(name);
        if (null == fragment) {
            LogUtil.e("------nanem=" + tagname);
            switch (tagname) {
                case "history_look":
                    fragment = HistoryLookFragment.Companion.newInstance(id);
                    break;
                case "history_zan":
                    fragment = HistoryThumbleFragment.Companion.newInstance(id);
                    break;
            }
            replaceFragment(fragment, name, R.id.fl_connter);
        } else {

            LogUtil.e("------nanem=" + tagname);
            attachFragment(fragment);
        }
        mFragment = fragment;
    }

    private void attachFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (null != mFragment) {
            fragmentTransaction.detach(mFragment);
        }
        mFragment = fragment;
        fragmentTransaction.attach(fragment).commit();
    }

    private String makeFragmentName(String tagname, int id) {
        return "kasao:" + tagname + ":" + id;
    }

    // 从缓存中获取fragment
    public BaseFragment getFragmentByContext(String name) {
        BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(name);
        return fragment;
    }


    //viewline 移动动画
    private void transferx(boolean rm, int start, int end) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewLine, "translationX", start, end);
        if (rm) {
            objectAnimator.setDuration(200).start();
        } else {
            int temp = (int) viewLine.getTranslationX();
            if (translationX == temp) {
                return;
            }
            objectAnimator.setDuration(200).start();
            translationX = (int) viewLine.getTranslationX();
        }
    }
}

