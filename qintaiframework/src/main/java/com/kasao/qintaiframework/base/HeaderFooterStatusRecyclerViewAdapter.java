package com.kasao.qintaiframework.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasao.qintaiframework.R;

/**
 * footer带有数据加载状态
 */
public abstract class HeaderFooterStatusRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends HeaderFooterRecyclerViewAdapter<VH> {

    /**
     * 0 加载更多
     * 1 加载中...
     * 2 没有更多数据了
     */
    private int mFooterStatus = -1;

    /**
     * 隐藏footer
     */
    public final void hideFooter() {
        try {
            if (mFooterStatus != -1) {
                mFooterStatus = -1;
                notifyFooterItemRemoved(0);
            }
        } catch (Exception e) {
        }
    }

    /**
     * footer 正常状态
     */
    public final void setFooterNormal() {
        try {
            if (mFooterStatus == -1) {
                mFooterStatus = 0;
                notifyFooterItemInserted(0);
            } else {
                mFooterStatus = 0;
                notifyFooterItemChanged(0);
            }
        } catch (Exception e) {
        }
    }

    /**
     * footer loading 状态
     */
    public final void setFooterLoading() {
        try {
            if (mFooterStatus == -1) {
                mFooterStatus = 1;
                notifyFooterItemInserted(0);
            } else {
                mFooterStatus = 1;
                notifyFooterItemChanged(0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * footer 没有更多了
     */
    public final void setFooterNoMore() {
        try {
            if (mFooterStatus == -1) {
                mFooterStatus = 2;
                notifyFooterItemInserted(0);
            } else {
                mFooterStatus = 2;
                notifyFooterItemChanged(0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected final int getFooterItemCount() {
        return mFooterStatus == -1 ? 0 : 1;
    }

    @Override
    protected final int getFooterItemViewType(int position) {
        return mFooterStatus;
    }

    @Override
    protected final VH onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        View footerView = null;
        TextView tipView;
        try {
            switch (footerViewType) {
                case 0://加载更多
                    footerView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.footer_list_tip, parent, false);
                    tipView = (TextView) footerView.findViewById(R.id.footer_item_hint);
                    tipView.setText(R.string.footer_list_normal);
                    break;
                case 1://加载中...
                    footerView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.footer_list_loading, parent, false);
                    break;
                case 2://没有更多数据了
                    footerView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.footer_empty, parent, false);
                    break;
            }
        } catch (Exception e) {

        }
        return createFooterStatusViewHolder(footerView);
    }

    public abstract VH createFooterStatusViewHolder(View footerView);

    @Override
    protected final void onBindFooterItemViewHolder(VH footerViewHolder, int position) {
    }
}