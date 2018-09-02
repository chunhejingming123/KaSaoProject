package com.kasao.qintaiframework.until;

import android.text.TextUtils;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/16.
 * 简述:descride
 */

public class UrlCheckUtil {
    private final static String HTTP_PREFIX = "http://";
    private final static String HTTPS_PREFIX = "https://";
    /**
     * 是否是自定义scheme
     * @param url
     * @return
     */
    public static boolean isSchemeUri(String url){
        if(TextUtils.isEmpty(url) || isCompleteUrl(url) ){
            return false;
        }
        return url.contains("://");
    }

    public static boolean isMySchemeUri(String url){
        if(TextUtils.isEmpty(url) || isCompleteUrl(url) ){
            return false;
        }
        return url.contains("://");
    }
    /**
     * 检测是否是完整路径
     *
     * @param url
     * @return
     */
    public static boolean isCompleteUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX);
    }
    /**
     * 获取scheme
     * @param url
     * @return
     */
    public static String getScheme(String url) {
        if( TextUtils.isEmpty(url) ){
            return null;
        }
        int ssi = url.indexOf(':');
        return ssi == -1 ? null : url.substring(0, ssi);
    }

}
