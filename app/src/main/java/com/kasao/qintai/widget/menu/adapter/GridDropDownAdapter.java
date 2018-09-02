package com.kasao.qintai.widget.menu.adapter;

import android.content.Context;
import android.widget.TextView;
import com.kasao.qintai.util.ContextComp;

import java.util.List;
import com.kasao.qintai.R;

/**
 * Created by HuangYuGuang on 2016/4/14.
 */
public class GridDropDownAdapter extends DropDownAdapter {


    public GridDropDownAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected int inflateItemView() {
        return R.layout.item_grid_drop_down;
    }

    @Override
    protected void actionSelect(TextView textView) {
        textView.setBackgroundResource(R.drawable.selector_conner_f72937tofe7bbf);
        textView.setTextColor(ContextComp.getColor(R.color.white));
    }

    @Override
    protected void actionNotSelect(TextView textView) {
        textView.setBackgroundResource(R.drawable.shape_round_f5f5f5);
        textView.setTextColor(ContextComp.getColor(R.color.color_333333));
    }
}