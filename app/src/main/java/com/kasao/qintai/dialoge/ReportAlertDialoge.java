package com.kasao.qintai.dialoge;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.kasao.qintai.R;
import com.kasao.qintai.model.domain.Reportdomain;
import com.kasao.qintai.util.ContextComp;
import com.kasao.qintaiframework.until.ScreenUtil;

import java.lang.ref.SoftReference;

public class ReportAlertDialoge implements View.OnClickListener {
    private Dialog mDialoge;
    private TextView tvMsg;
    private TextView tvRemark;
    private TextView tvReson;
    private SoftReference<Activity> soft = null;
    public ReportAlertDialoge(Activity mActivity) {
        soft = new SoftReference(mActivity);
        if (null == soft.get()) {
            return;
        }
        if (null == mDialoge) {
            mDialoge = new Dialog(soft.get(), R.style.dialog);
            initView();
        }
    }

    // 初始化
    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_report, null);
        mDialoge.setContentView(view);
        tvMsg=view.findViewById(R.id.tvMsg);
        tvRemark=view.findViewById(R.id.tvRemark);
        tvReson=view.findViewById(R.id.tvReson);
        view.findViewById(R.id.btnCannel).setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (int) (0.8* ScreenUtil.getScreenW());
        window.setGravity(Gravity.CENTER);
        mDialoge.getWindow().setAttributes(lp);
        mDialoge.setCanceledOnTouchOutside(false);
        mDialoge.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDialoge.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCannel:
                break;
        }
        hide();
    }

    public void hide() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    public void showDialoge(Reportdomain entity) {
        if(null==entity){
            return;
        }
        if (mDialoge != null) {
            mDialoge.show();
            if(null!=entity.data){
                if(!TextUtils.isEmpty(entity.data.time)&&!TextUtils.isEmpty(entity.data.articleinfo)){
                    String s=entity.data.articleinfo.replaceAll("\\s*|\t|\r|\n","");
                    tvMsg.setText(ContextComp.getString(R.string.reportalert,entity.data.time,s));
                }
               if(!TextUtils.isEmpty(entity.data.reportinfo)){
                   tvReson.setText(ContextComp.getString(R.string.reportReson,entity.data.reportinfo));
               }
            }
            tvRemark.setText(entity.msg);
        }
    }

}
