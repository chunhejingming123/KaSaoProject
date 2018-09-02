package com.kasao.qintai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kasao.qintai.R;
import com.kasao.qintai.base.BaseKSadapter;
import com.kasao.qintai.base.BaseViewHolder;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.widget.ItemCarDisplayReommend;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/10.
 * 简述:详情页面的 汽贸展示
 */

public class CarDetailRecommenAdapter extends BaseKSadapter<CarDetailEntity> {
    public static final int FOOTTITLE = 105;
    public static final int FOOTITEM = 106;
    private List<CarDetailEntity> list;

    public CarDetailRecommenAdapter(ActionToCarDetail actionToCareDetail) {
        setOnlyLoadingOne(false);
        this.mActionToCareDetail = actionToCareDetail;

    }

    @Override
    public int getHeaderItemCount() {
        return null == list ? 0 : (list.size() > 5 ? 5 : list.size());
    }

    @Override
    protected int getHeaderItemViewType(int position) {
        if (position == 0) {
            return FOOTTITLE;

        } else {
            return FOOTITEM;
        }

    }


    @Override
    protected BaseViewHolder<CarDetailEntity> onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        View view;
        switch (headerViewType) {
            case FOOTTITLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardetail_recommend_text, parent, false);
                return new CarFootTitleViewText(view);
            case FOOTITEM:
                return new CarFootItemViewText(new ItemCarDisplayReommend(parent.getContext()));
        }
        return null;
    }


    @Override
    protected void onBindHeaderItemViewHolder(BaseViewHolder<CarDetailEntity> headerViewHolder, int position) {
        headerViewHolder.rendView(list.get(position), position);

    }


    public void setData(List<CarDetailEntity> recommend_list) {
        this.list = recommend_list;
        list.add(0, new CarDetailEntity());
        notifyDataSetChanged();
    }


    public class CarFootTitleViewText extends BaseViewHolder implements View.OnClickListener {
        public CarFootTitleViewText(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class CarFootItemViewText extends BaseViewHolder<CarDetailEntity> implements View.OnClickListener {
        private ItemCarDisplayReommend viewRecommend;
        private CarDetailEntity mEntity;

        public CarFootItemViewText(View itemView) {
            super(itemView);
            viewRecommend = (ItemCarDisplayReommend) itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void rendView(CarDetailEntity entity, int position) {
            if (entity != null) {
                mEntity = entity;
                viewRecommend.rendView(entity, false);
            }
        }

        @Override
        public void onClick(View view) {
            if (null != mActionToCareDetail) {
                mActionToCareDetail.toCareDetail(mEntity);
            }
        }
    }

    private ActionToCarDetail mActionToCareDetail;

    private void setActionToCareDetail(ActionToCarDetail actionToCareDetail) {
        this.mActionToCareDetail = actionToCareDetail;
    }

    public interface ActionToCarDetail {
        void toCareDetail(CarDetailEntity entity);
    }

}
