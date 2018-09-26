package com.kasao.qintai.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.base.BaseKasaoApplication;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ToastUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.tencent.mm.opensdk.openapi.WXAPIFactory.*;

public class UtilsTool {
    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannel() {
        String channel = getMetaData("UMENG_CHANNEL");
        if (TextUtils.isEmpty(channel)) {
            return getChannelFromMetainfo(BaseKasaoApplication.Companion.getApplicaton());
        } else {
            return channel;
        }
    }

    /**
     * 从metainfo 读取 zhiyun168channel_xxx 文件名来获取渠道
     *
     * @param context context
     * @return 默认返回 common
     */
    private static String getChannelFromMetainfo(Context context) {
        try {
            ApplicationInfo appinfo = context.getApplicationInfo();
            String sourceDir = appinfo.sourceDir;
            String ret = "";
            ZipFile zipfile = null;
            try {
                zipfile = new ZipFile(sourceDir);
                Enumeration<?> entries = zipfile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.startsWith("META-INF/zhiyun168channel") || entryName.startsWith("zhiyun168channel")) {
                        ret = entryName;
                        break;
                    }
                }
            } catch (IOException e) {

            } finally {
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e) {
                        LogUtil.e("Tag", e.getMessage());
                    }
                }
            }

            String[] split = ret.split("_");
            if (split != null && split.length >= 2) {
                return ret.substring(split[0].length() + 1);
            } else {
                return "common";
            }
        } catch (Throwable e) {
            LogUtil.e("Tag", e.getMessage());
            return "common";
        }
    }

    /**
     * 获取meta data
     *
     * @param metaKey
     * @return
     */
    public static String getMetaData(String metaKey) {
        try {
            ApplicationInfo appInfo = BaseKasaoApplication.Companion.getApplicaton().getPackageManager()
                    .getApplicationInfo(BaseKasaoApplication.Companion.getApplicaton().getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(metaKey);
        } catch (Throwable e) {
            return null;
        }
    }


    /**
     * 获取版本号
     *
     * @param context
     * @param
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取版本code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        Context appContext = context.getApplicationContext();

        try {
            PackageManager pm = appContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(appContext.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getDevice() {
        return Build.VERSION.RELEASE + "_" + Build.BRAND + " " + Build.MODEL;
    }

    // 品牌  型号  版本号
    public static String getDevice2() {
        return Build.BRAND + "," + Build.MODEL + "," + Build.VERSION.RELEASE;
    }

    /**
     * 设置文字高亮
     *
     * @param tv
     * @param baseText
     * @param highlightText
     */
    public static void showTextHighlight(TextView tv, String baseText, String highlightText) {
        if ((null == tv) || (null == baseText) || (null == highlightText)) {
            return;
        }

        int index = baseText.indexOf(highlightText);
        if (index < 0) {
            tv.setText(baseText);
            return;
        }

        int len = highlightText.length();
        Spanned spanned = Html.fromHtml(baseText.substring(0, index) + "<font color=#FF8C00 >"
                + baseText.substring(index, index + len) + "</font>"
                + baseText.substring(index + len, baseText.length()));

        tv.setText(spanned);
    }


    public static String SetLengthToKm(double length) {
        String b;
        if (length > 1000) {
            double c = length / 1000;
            String a = c + "";
            b = a.substring(0, a.indexOf(".") + 2) + "公里";
        } else {
            b = length + "米";

        }
        return b;
    }

    public static String getLengthm(double length) {
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(length / 1000);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 用来判断服务是否运行.        
     *
     * @param context
     * @param className 判断的服务名字          
     * @return true 在运行 false 不在运行         
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
            if (!(serviceList.size() > 0)) {
                return false;
            }
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(className) == true) {
                    isRunning = serviceList.get(i).started;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRunning;
    }

    public static boolean isApkInstall(Context mcontext,String appid) {
        IWXAPI msgApi = createWXAPI(mcontext, appid);
        msgApi.registerApp(appid);
        boolean isapk = msgApi.isWXAppInstalled();
        if (!isapk) {
            ToastUtil.showAlter("请安装最新版本微信");
            return false;
        } else {
            return true;
        }
    }
}
