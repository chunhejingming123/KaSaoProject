package com.kasao.qintai.util;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.kasao.qintaiframework.base.MyApplication;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:16
 */

public class ContextComp {

    public static String[] getStringArray(int res_id) {
        return MyApplication.Companion.getApplicaton().getResources().getStringArray(res_id);
    }

    public static int[] getIntArray(int res_id) {
        return MyApplication.Companion.getApplicaton().getResources().getIntArray(res_id);
    }

    public static String getString(int res_id) {
        return MyApplication.Companion.getApplicaton().getResources().getString(res_id);
    }

    public static String getString(int res_id, Object... params) {
        return String.format(getString(res_id), params);
    }

    public static int getDimensionPixelOffset(int id) {
        return MyApplication.Companion.getApplicaton().getResources().getDimensionPixelOffset(id);
    }

    public static float getDimension(int id) {
        return MyApplication.Companion.getApplicaton().getResources().getDimension(id);
    }

    public static int getColor(int id) {
        return MyApplication.Companion.getApplicaton().getResources().getColor(id);
    }

    public static ColorStateList getColorStateList(int id) {
        return MyApplication.Companion.getApplicaton().getResources().getColorStateList(id);
    }

    public static void setBackgroundAttr(View view, int selectableItemBackground) {
        try {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = view.getContext().obtainStyledAttributes(attrs);
            Drawable drawableFromTheme = ta.getDrawable(0);
            ta.recycle();
            view.setBackgroundDrawable(drawableFromTheme);
        } finally {
        }
    }

    public static Drawable getDrawable(int resId) {
        return MyApplication.Companion.getApplicaton().getResources().getDrawable(resId);
    }
}
