package com.kasao.qintai.popuwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kasao.qintai.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by suochunming
 * <p>
 * on 2017/8/20.
 * des:
 */

public class TakePhotoPopuWindow implements View.OnClickListener {
    PopupWindow pop = null;
    private View locationView;
    private Activity activity;
    private TextView tvPhoteWall;
    private TextView tvCannel;
    private TextView tvTakePhoto;

    public TakePhotoPopuWindow(Activity activity, View location) {
        this.activity = activity;
        this.locationView = location;

    }

    public void showPopuWindow(OnTakePhotoStyle mOnTakePhotoStyle) {
        this.mOnTakePhotoStyle = mOnTakePhotoStyle;
        LayoutInflater mLayoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.popuwindow_oper_photo, null);// R.layout.pop为 PopupWindow 的布局文件
        pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // pop.setBackgroundDrawable(new BitmapDrawable(ContextComp.getDrawable(R.color.activity_bg)));
        tvTakePhoto = (TextView) contentView.findViewById(R.id.tvTakePhoto);
        tvPhoteWall = (TextView) contentView.findViewById(R.id.tvPhoteWall);
        tvCannel = (TextView) contentView.findViewById(R.id.tvCancle);

        tvTakePhoto.setOnClickListener(this);
        tvPhoteWall.setOnClickListener(this);
        tvCannel.setOnClickListener(this);
        pop.setAnimationStyle(R.style.mypopuwindow_bootom_in);
        pop.showAtLocation(locationView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pop.dismiss();
                return true;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTakePhoto:
                if (mOnTakePhotoStyle != null) {
                    mOnTakePhotoStyle.takePhoto();
                }
                break;
            case R.id.tvPhoteWall:
                if (mOnTakePhotoStyle != null) {
                    mOnTakePhotoStyle.ChosePhotoWall();
                }
                break;
            default:
                dismisPop();
                break;
        }

    }

    public void dismisPop() {
        if (pop != null) {
            pop.dismiss();
        }
    }

    private OnTakePhotoStyle mOnTakePhotoStyle;

    public interface OnTakePhotoStyle {
        void takePhoto();

        void ChosePhotoWall();
    }
}
