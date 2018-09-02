package com.kasao.qintai.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;

public class MobileUtil {
    public MobileUtil() {
    }



    public static String getMobileMode() {
        return Build.MODEL;
    }

    /** @deprecated */
    @Deprecated
    public static Integer getVersion(PackageManager pm, String packageName) throws NameNotFoundException {
        return pm.getPackageInfo(packageName, 0).versionCode;
    }

    public static Integer getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getPackageName(Context context) {
        String name = context.getPackageName();
        String[] names = name.split("\\.");
        return names[names.length - 1];
    }




    public static int getDeviceSDK() {
        return VERSION.SDK_INT;
    }



    /** @deprecated */
    @Deprecated
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /** @deprecated */
    @Deprecated
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
