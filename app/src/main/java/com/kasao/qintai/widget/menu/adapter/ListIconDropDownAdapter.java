package com.kasao.qintai.widget.menu.adapter;

import android.content.Context;
import android.widget.TextView;

import com.kasao.qintai.R;

import java.util.List;

/**
 * Created by HuangYuGuang on 2016/4/14.
 */
public class ListIconDropDownAdapter extends DropDownAdapter {
    private int imgResource;

    public ListIconDropDownAdapter(Context context, List<String> list) {
        super(context,list);
    }

    @Override
    protected int inflateItemView() {
        return R.layout.item_list_icon_drop_down;
    }

    @Override
    protected void actionNotSelect(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    protected void actionSelect(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(imgResource>0?imgResource:R.drawable.drop_down_checked), null);
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}