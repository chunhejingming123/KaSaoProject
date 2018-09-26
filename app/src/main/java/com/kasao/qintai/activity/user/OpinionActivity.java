package com.kasao.qintai.activity.user;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import okhttp3.ResponseBody;


public class OpinionActivity extends BaseActivity implements OnClickListener {
    private EditText et_title;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_opinion;
    }

    @Override
    public void findView() {

        (findViewById(R.id.ivBack)).setBackgroundResource(R.drawable.icon_return_black);
        View ll_back = findViewById(R.id.viewBack);
        ll_back.setOnClickListener(this);
        findViewById(R.id.tvRight).setOnClickListener(this);
        TextView tv_title = findViewById(R.id.tvTitle);
        tv_title.setText("意见反馈");
        ((TextView) findViewById(R.id.tvRight)).setText("发送");
        et_title = findViewById(R.id.et_title);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewBack:
                finish();
                break;
            case R.id.tvRight:
                if (TextUtils.isEmpty(et_title.getText().toString())) {
                    ToastUtil.showAlter("请输入内容");
                    break;
                }
                sumbit();
                break;
        }
    }

    private void sumbit() {
        HashMap<String, String> param = new HashMap<>();
        param.put("u_id", BaseKasaoApplication.getUser().user_id);
        param.put("content", et_title.getText().toString());
        param.put("type", "2");
        param.put("applies", "android");
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getOPINION(), param, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                finish();
            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });
//		Observable<RtnSuss> call=RetrofitUtils.getInstance().posCommenRtn(ApiInterface.getRequesBaseUrl(ApiInterface.OPINION),param);
//		call.subscribeOn(Schedulers.io())
//				.compose(this.<RtnSuss>bindToLifecycle())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Action1<RtnSuss>() {
//					@Override
//					public void call(RtnSuss suss) {
//						finish();
//					}
//				});
    }
}
