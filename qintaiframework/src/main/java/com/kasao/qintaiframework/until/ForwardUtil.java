package com.kasao.qintaiframework.until;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.kasao.qintaiframework.activity.WebActivity;


/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/16.
 * 简述:descride
 */

public class ForwardUtil {

    /**
     * 点击跳转地址的时候解析跳转地址
     *
     * @param url
     * @param context
     */
    public static void startUri(String url, Context context) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        startUriWithOutFilter(url, context);
    }
    /**
     * 没有经过filter过滤打开地址
     *
     * @param url
     * @param activity
     */
    private static void startUriWithOutFilter(String url, Context activity) {
        if (UrlCheckUtil.isMySchemeUri(url)) {
            startSchemeUri(url, activity);
        } else if (UrlCheckUtil.isCompleteUrl(url) || !UrlCheckUtil.isSchemeUri(url)) {
            startWebInner(url, activity);
        }
    }
    /**
     * 跳转到 SchemeUri
     *
     * @param url
     * @param context
     */
    private static void startSchemeUri(String url, Context context) {
        try {
            Intent i;
            Uri uri = Uri.parse(url);
            if ("tel".equals(uri.getScheme()) || "wtai".equals(uri.getScheme())) {
                i = new Intent(Intent.ACTION_CALL, uri);
            } else {
                i = new Intent();
            }
//            if (!(context instanceof Activity)) {
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(uri);
            context.startActivity(i);
        } catch (ActivityNotFoundException e2) {
             LogUtil.e(e2);
//            if (url.startsWith("wtai://wp/mc;")) {
//                String url2 = url.replace("wtai://wp/mc;", "tel://");
//                startSchemeUri(url2, context);
//                return;
//            }

        } catch (Throwable e) {
            LogUtil.e(e);
        }
    }

    /**
     * 打开web页面
     *
     * @param url
     * @param activity
     */
    private static void startWebInner(String url, Context activity) {
        try {
            Intent intent = new Intent(activity, WebActivity.class);
            intent.putExtra("url", url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Throwable e) {
            LogUtil.e(e);
        }
    }
}
