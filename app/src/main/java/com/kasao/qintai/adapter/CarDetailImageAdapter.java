package com.kasao.qintai.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.base.BaseKSadapter;
import com.kasao.qintai.base.BaseViewHolder;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.CarImage;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintai.util.GlideUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/10.
 * 简述:详情页面的 汽贸展示
 */
public class CarDetailImageAdapter extends BaseKSadapter<CarImage> {
    public static final int CAR_OUT_TEXT = 101;
    public static final int CAR_OUT_IMG = 102;
    public static final int CAR_BOTTOM_TEXT = 103;
    public static final int CAR_BOTTOM_IMG = 104;
    private List<CarImage> surface;
    private List<CarImage> chassis;
    public static boolean isOutLook = true;
    public Drawable drawableDown;
    public Drawable drawableUp;

    public CarDetailImageAdapter() {
        setOnlyLoadingOne(false);
        drawableDown = ContextComp.getDrawable(R.drawable.icon_arrow_down);
        drawableUp = ContextComp.getDrawable(R.drawable.icon_arrow_up);
        drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(), drawableDown.getMinimumHeight()); //设置边界
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight()); //设置边界
    }

    @Override
    public int getHeaderItemCount() {
        if (isOutLook) {
            if (surface != null) {
                return surface.size();
            }
        } else {
            if (chassis != null) {
                return chassis.size();
            }
        }
        return 0;
    }

    @Override
    protected int getHeaderItemViewType(int position) {
        if (isOutLook) {
            if (position == 0) {
                return CAR_OUT_TEXT;
            } else if (position == getHeaderItemCount() - 1) {
                return CAR_BOTTOM_TEXT;
            } else {
                return CAR_OUT_IMG;
            }
        } else {
            if (position == 0) {
                return CAR_OUT_TEXT;
            } else if (position == 1) {
                return CAR_BOTTOM_TEXT;
            } else {
                return CAR_OUT_IMG;
            }
        }

    }


    @Override
    protected BaseViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        View view;
        switch (headerViewType) {
            case CAR_OUT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardetail_header_text, parent, false);
                return new CarHeadtOPViewText(view);
            case CAR_OUT_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardetail_header, parent, false);
                return new CarHeadViewImage(view);
            case CAR_BOTTOM_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardetail_header_text, parent, false);
                return new CarHeadBottomViewText(view);
            case CAR_BOTTOM_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardetail_header, parent, false);
                return new CarHeadViewImage(view);
        }
        return null;
    }


    @Override
    protected void onBindHeaderItemViewHolder(BaseViewHolder<CarImage> headerViewHolder, int position) {
        if (isOutLook) {
            headerViewHolder.rendView(surface.get(position), position);
        } else {
            headerViewHolder.rendView(chassis.get(position), position);
        }

    }


    // 车辆外观
    public class CarHeadtOPViewText extends BaseViewHolder<CarImage> implements View.OnClickListener {
        TextView tvArrow;
        int size = 0;

        public CarHeadtOPViewText(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvArrow = (TextView) itemView.findViewById(R.id.tv_title);
            size = surface.size() - 2;
            if (surface != null && size > 0) {
                tvArrow.setText(ContextComp.getString(R.string.caroutphoto) + "(" + size + ")");
                if (isOutLook) {
                    tvArrow.setCompoundDrawables(null, null, drawableUp, null);
                } else {
                    tvArrow.setCompoundDrawables(null, null, drawableDown, null);//画在右边
                }
            } else {
                tvArrow.setText(ContextComp.getString(R.string.caroutphoto));
                tvArrow.setCompoundDrawables(null, null, null, null);//画在右边
            }
        }

        @Override
        public void onClick(View view) {
            if (size < 1) {
                return;
            }
            if (isOutLook) {
                tvArrow.setCompoundDrawables(null, null, drawableUp, null);
            } else {
                tvArrow.setCompoundDrawables(null, null, drawableDown, null);//画在右边
            }
            isOutLook = !isOutLook;
            notifyDataSetChanged();
        }
    }

    // 车辆底盘
    public class CarHeadBottomViewText extends BaseViewHolder<CarImage> implements View.OnClickListener {
        TextView tvArrow;
        int size;

        public CarHeadBottomViewText(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvArrow = (TextView) itemView.findViewById(R.id.tv_title);
            size = chassis.size() - 2;
            if (null != chassis && size > 0) {
                tvArrow.setText(ContextComp.getString(R.string.carbottomphoto) + "(" + size + ")");
                if (isOutLook) {
                    tvArrow.setCompoundDrawables(null, null, drawableDown, null);
                } else {
                    tvArrow.setCompoundDrawables(null, null, drawableUp, null);//画在右边
                }
            } else {
                tvArrow.setText(ContextComp.getString(R.string.carbottomphoto));
                tvArrow.setCompoundDrawables(null, null, null, null);//画在右边
            }
        }

        @Override
        public void onClick(View view) {
            if (size < 1) {
                return;
            }
            if (isOutLook) {
                tvArrow.setCompoundDrawables(null, null, drawableDown, null);
            } else {
                tvArrow.setCompoundDrawables(null, null, drawableUp, null);//画在右边
            }
            isOutLook = !isOutLook;
            notifyDataSetChanged();
        }
    }

    // 车辆外观
    public class CarHeadViewImage extends BaseViewHolder<CarImage> {
        ImageView tv_car_style;
        int width;
        View view;

        public CarHeadViewImage(View itemView) {
            super(itemView);
            view = itemView;
            tv_car_style = (ImageView) itemView.findViewById(R.id.tv_car_style);
            ViewGroup.LayoutParams parmars = tv_car_style.getLayoutParams();
            width = ScreenUtil.getScreenW() - ContextComp.getDimensionPixelOffset(R.dimen.dimen_30);
            parmars.width = width;
            parmars.height = (int) (0.5f * width);//172 * width / 345;
            tv_car_style.setLayoutParams(parmars);
            tv_car_style.requestLayout();
        }

        @Override
        public void rendView(CarImage carImage, int position) {
            GlideUtil.into(view.getContext(), carImage.url, tv_car_style, width, (int) (0.5f * width), R.drawable.bg_default);

        }
    }

    public void setData(CarDetailEntity entity) {
        if (null != entity) {
            if (null != entity.chassis) {
                chassis = entity.chassis;
            }
            if (null == chassis) {
                chassis = new ArrayList<>();
            }
            CarImage img1 = new CarImage();
            CarImage img2 = new CarImage();
            chassis.add(0, img1);
            chassis.add(1, img2);

            if (null != entity.surface) {
                surface = entity.surface;
            }
            if (null == surface) {
                surface = new ArrayList<>();
            }
            CarImage img3 = new CarImage();
            CarImage img4 = new CarImage();
            surface.add(0, img3);
            surface.add(img4);
        }
        notifyDataSetChanged();
    }

}
