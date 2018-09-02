package com.kasao.qintaiframework.until;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/16.
 * 简述:descride
 */

public class UriFilter {
    public enum UriOpenStatus {
        open_http_inner,
        open_http_out,
        open_http_forbidden,
        open_scheme,
        open_scheme_forbidden,
    }

    //http 黑名单
    public List<String> http_black_list = new ArrayList<>();
    //http 白名单
    public Map<String, Boolean> http_white_list = new HashMap<>();
    //自定义scheme 白名单
    public Map<String, Boolean> third_party_app_white_list = new HashMap<>();
    /**
     * framework 默认支持的 白名单
     */
    private transient Map<String, Boolean> mFrameworkSchemeWhiteList = new HashMap<String, Boolean>() {
        {
            put("tel", true);
            put("sms", true);
            put("market", true);
            put("qintai", true);
        }
    };

    /**
     * 获取uri的打开方式
     *
     * @param uri
     * @return
     */
    public UriOpenStatus getUriOpenStatus(String uri) {
        if (UrlCheckUtil.isSchemeUri(uri)) {
            return canOpenScheme(uri) ? UriOpenStatus.open_scheme : UriOpenStatus.open_scheme_forbidden;
        } else {
            if (!UrlCheckUtil.isCompleteUrl(uri)) {
                return UriOpenStatus.open_http_inner;
            } else {
                if (isInBlackList(uri)) {
                    return UriOpenStatus.open_http_forbidden;
                } else {
                    try {
                        Uri _uri = Uri.parse(uri);
                        String forceBrowser = _uri.getQueryParameter("force_browser");
                        if ("true".equals(forceBrowser)) {
                            return UriOpenStatus.open_http_out;
                        } else {
                            return isInHttpWhiteList(uri) ? UriOpenStatus.open_http_out : UriOpenStatus.open_http_inner;
                        }
                    } catch (Throwable e) {
                        return isInHttpWhiteList(uri) ? UriOpenStatus.open_http_out : UriOpenStatus.open_http_inner;
                    }
                }
            }
        }
    }

    /**
     * 是否可以打开scheme
     *
     * @param uri
     * @return
     */
    public boolean canOpenScheme(String uri) {
        String scheme = UrlCheckUtil.getScheme(uri);
        return !TextUtils.isEmpty(scheme) && inWhiteSchemeList(scheme);
    }
    /**
     * 在scheme白名单里面
     *
     * @param scheme
     * @return
     */
    private boolean inWhiteSchemeList(String scheme) {
        Boolean inDefaultWhite = mFrameworkSchemeWhiteList.get(scheme);
        if (inDefaultWhite != null && inDefaultWhite) {
            return true;
        }
        Boolean inWhite = third_party_app_white_list.get(scheme);
        return inWhite != null && inWhite;
    }

    /**
     * 是否在黑名单
     *
     * @param uri
     * @return
     */
    public boolean isInBlackList(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return false;
        }
        for (String black : http_black_list) {
            if (uri.contains(black)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否在白名单中
     *
     * @param uri
     * @return
     */
    public boolean isInHttpWhiteList(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return false;
        }
        Uri _uri = Uri.parse(uri);
        String host = _uri.getHost();
        Boolean inWhite = http_white_list.get(host);
        return inWhite != null && inWhite;
    }

}
