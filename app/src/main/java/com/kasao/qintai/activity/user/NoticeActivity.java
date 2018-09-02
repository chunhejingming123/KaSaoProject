package com.kasao.qintai.activity.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.NoticeAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.model.NoticeEntity;
import com.kasao.qintai.model.domain.Noticedomain;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.ForwardUtil;
import com.kasao.qintaiframework.until.GsonUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class NoticeActivity extends BaseActivity implements OnClickListener {
    private RecyclerView listview;
    private int currypage = 0;
    private NoticeAdapter adapter;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_notice;
    }

    @Override
    public void findView() {
        findViewById(R.id.ll_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listview = (RecyclerView) findViewById(R.id.listview);
        initRecycle(listview, null);
        adapter = new NoticeAdapter();
        adapter.setMAction(new NoticeAdapter.INoticeAction() {
            @Override
            public void goToDetal(@NotNull NoticeEntity entity) {
                ForwardUtil.startUri(entity.notice_url, NoticeActivity.this);
            }
        });
        listview.setAdapter(adapter);

    }


    @Override
    public void onloadData() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("page_num", currypage + "");
        param.put("type", 2 + "");
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getNOTICELIST(), param, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    Noticedomain domain = GsonUtil.Companion.GsonToBean(t.string(), Noticedomain.class);
                    if (null != domain && null != domain.data) {
                        adapter.setMutableList(domain.data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != adapter) {
            adapter = null;
        }
        super.onDestroy();
    }
}
