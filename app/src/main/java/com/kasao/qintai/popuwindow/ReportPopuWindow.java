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

public class ReportPopuWindow implements View.OnClickListener {
    PopupWindow pop = null;
    private View locationView;
    private Activity activity;
    private TextView tvTakeCare;
    private TextView tvReport;
    private TextView tvDel;

    public ReportPopuWindow(Activity activity, View location) {
        this.activity = activity;
        this.locationView = location;

    }

    public void showPopuWindow(OnReport onReport) {
        this.mOnReport = onReport;
        LayoutInflater mLayoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.popuwindow_oper_report, null);// R.layout.pop为 PopupWindow 的布局文件
        pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // pop.setBackgroundDrawable(new BitmapDrawable(ContextComp.getDrawable(R.color.activity_bg)));
        tvTakeCare = contentView.findViewById(R.id.tvTakeCare);
        tvReport = contentView.findViewById(R.id.tvReport);
        tvDel = contentView.findViewById(R.id.tvDel);
        TextView tvCannel = contentView.findViewById(R.id.tvCancle);
        tvTakeCare.setOnClickListener(this);
        tvReport.setOnClickListener(this);
        tvDel.setOnClickListener(this);
        tvCannel.setOnClickListener(this);
        pop.setAnimationStyle(R.style.mypopuwindow_bootom_in);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pop.dismiss();
                return true;
            }
        });
    }

    public void show(boolean isSelf) {
        if (isSelf) {
            tvReport.setVisibility(View.GONE);
            tvTakeCare.setVisibility(View.GONE);
            tvDel.setVisibility(View.VISIBLE);
        } else {
            tvReport.setVisibility(View.VISIBLE);
            tvTakeCare.setVisibility(View.VISIBLE);
            tvDel.setVisibility(View.GONE);
        }
        pop.showAtLocation(locationView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTakeCare:
                if (mOnReport != null) {
                    mOnReport.takeCare();
                    dismisPop();
                }
                break;
            case R.id.tvReport:
                if (mOnReport != null) {
                    mOnReport.onReport();
                    dismisPop();
                }
                break;
            case R.id.tvDel:
                if (mOnReport != null) {
                    mOnReport.del();
                    dismisPop();
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

    private OnReport mOnReport;

    public interface OnReport {
        void takeCare();

        void del();

        void onReport();
    }
}
