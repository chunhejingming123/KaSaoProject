package com.kasao.qintai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.base.BaseKSadapter;
import com.kasao.qintai.base.BaseViewHolder;
import com.kasao.qintai.model.CarBrand;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:16
 */

public class CarViewpageGridAdapter extends BaseKSadapter<CarBrand> {
    private List<CarBrand> listBrand;
    private int itemWidth;
    private Context mContext;

    public CarViewpageGridAdapter(int demen) {
        itemWidth = (ScreenUtil.getScreenW() - ContextComp.getDimensionPixelOffset(demen)) / 4;
    }

    public CarViewpageGridAdapter(List<CarBrand> listBrand) {
        this.listBrand = listBrand;
    }

    public CarViewpageGridAdapter(Context content) {
       setOnlyLoadingOne(false);
        mContext = content;
        itemWidth = ScreenUtil.getScreenW() / 4;
    }

    @Override
    protected int getFooterItemCount() {
        return listBrand == null || listBrand.isEmpty() ? 1 : 0;
    }


    @Override
    protected int getContentItemCount() {
        return listBrand == null || listBrand.isEmpty() ? 0 : (listBrand.size() > 8 ? 8 : listBrand.size());
    }

    @Nullable
    @Override
    protected BaseViewHolder<CarBrand> onCreateFooterItemViewHolder(@Nullable ViewGroup parent, int footerViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_nodata_display, parent, false);
        return new FootView(view);
    }


    @Override
    protected BaseViewHolder<CarBrand> onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homecar_cellview, parent, false);
        return new BrandViewHolder(view);
    }


    @Override
    protected void onBindContentItemViewHolder(BaseViewHolder<CarBrand> contentViewHolder, int position) {
        if (listBrand != null) {
            contentViewHolder.rendView(listBrand.get(position), position);
        }
    }


    public void setData(List<CarBrand> data) {
        listBrand = data;
        notifyDataSetChanged();
    }

    public void setNodata() {
        notifyDataSetChanged();
    }

    public class BaseViewHold extends BaseViewHolder<CarBrand> implements View.OnClickListener {
        public BaseViewHold(View itemView) {
            super(itemView);
        }

        @Override
        public void rendView(CarBrand carBrand, int position) {
            super.rendView(carBrand, position);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class FootView extends BaseViewHolder {
        TextView tvMsg;

        public FootView(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams item = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            item.width = ScreenUtil.getScreenW();
            itemView.setLayoutParams(item);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            tvMsg.setText("敬请期待(＾－＾)");
        }
    }

    public class BrandViewHolder extends BaseViewHold {
        private ImageView ivBrand;
        private TextView tvBrandName;
        private CarBrand mCarBrand;
        private View item;

        public BrandViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            RecyclerView.LayoutParams item = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            item.width = itemWidth;
            itemView.setLayoutParams(item);
            ivBrand = (ImageView) itemView.findViewById(R.id.iv_car_brand);
            tvBrandName = (TextView) itemView.findViewById(R.id.tv_car_name);
            ivBrand.getLayoutParams();
            ViewGroup.LayoutParams params = ivBrand.getLayoutParams();
            params.width = (int) (0.9 * itemWidth);
            params.height = (int) (0.55 * itemWidth);
            ivBrand.setLayoutParams(params);
            ivBrand.requestLayout();
            itemView.setOnClickListener(this);
        }

        @Override
        public void rendView(CarBrand carBrand, int position) {
            this.mCarBrand = carBrand;
            GlideUtil.into(mContext, carBrand.brand_img, ivBrand, R.drawable.bg_default);
            tvBrandName.setText(carBrand.name);
        }


        @Override
        public void onClick(View view) {
            if (null != mCarCategory) {
                mCarCategory.choseBrand(mCarBrand);
            }
        }
    }

    private CarCategory mCarCategory;

    public void setCategory(CarCategory category) {
        this.mCarCategory = category;
    }

    public interface CarCategory {
        void choseBrand(CarBrand brand);
    }
}
