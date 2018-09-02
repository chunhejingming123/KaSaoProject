package com.kasao.qintaiframework.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kasao.qintaiframework.R;
import com.kasao.qintaiframework.http.UriConsts;
import com.kasao.qintaiframework.until.FrameParmars;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/17.
 * 简述:
 */
public class WebActivity extends Activity implements OnClickListener {
    private ProgressBar progressBar;
    public WebShareInterface mWebShareInterface;
    public boolean isShare;
    public String url = "";
    public String title = "";
    private String id;
    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webview);
        initparmars();
        initView();
        initDate();
    }

    public void initparmars() {
        title = getIntent().getStringExtra(FrameParmars.KEY_TITLE);
        url = getIntent().getStringExtra(FrameParmars.KEY_URL);
        if (TextUtils.isEmpty(title)) {
            title = "资讯详情";
        }
        if (TextUtils.isEmpty(url)) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                id = uri.getQueryParameter("Id");
            }
            if (TextUtils.isEmpty(id)) {
                finish();
                return;
            }
            url = UriConsts.Companion.getBASE_URL() + UriConsts.Companion.getNEWdETAIL() + id;
        }
    }

    private void initView() {
        //在这里接收BannerPageAdapterGongneng中的intent数据 直接设置到title
        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        View ivShare = findViewById(R.id.iv_share);
        if (isShare) {
            ivShare.setVisibility(View.VISIBLE);
            ivShare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mWebShareInterface) {
                        mWebShareInterface.webShare();
                    }
                }
            });
        } else {
            ivShare.setVisibility(View.GONE);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE);
                } else {
                    // 加载中
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }


    private void initDate() {

    }

    public void setmWebShareInterface(WebShareInterface webShareInterface) {
        this.mWebShareInterface = webShareInterface;
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (null != webview) {
            webview.removeAllViews();
            webview.destroy();
        }

        super.onDestroy();
    }
}
