package com.kasao.qintai.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.kasao.qintai.base.BaseKasaoApplication;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * 调用地图导航功能
 */
public class MapNativeUtil {
    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // 手机是否安装百度地图
    public static boolean isInstallBaiduMap() {
        if (checkApkExist(BaseKasaoApplication.Companion.getApplicaton(), "com.baidu.BaiduMap")) {
            return true;
        }
        return false;
    }

    // 手机是否安装高德地图
    public static boolean isInstallGodeMap() {
        if (checkApkExist(BaseKasaoApplication.Companion.getApplicaton(), "com.autonavi.minimap")) {
            return true;
        }
        return false;
    }

    // 优先高德地图
    public static void nativegation(Context nContext, double latitude, double lngtitude) {
        if (isInstallGodeMap()) {
            try {
                AMapUtils.getLatestAMapApp(BaseKasaoApplication.Companion.getApplicaton());
                // 构造导航参数
                NaviPara naviPara = new NaviPara();
                // 设置终点位置
                naviPara.setTargetPoint(new LatLng(latitude, lngtitude));
                // 设置导航策略，这里是避免拥堵
                naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
                // 调起高德地图导航
                AMapUtils.openAMapNavi(naviPara,BaseKasaoApplication.Companion.getApplicaton());
            } catch (com.amap.api.maps.AMapException e) {
            }
            return;
        }
        if (isInstallBaiduMap()) {
            try {
                double lat = BaseKasaoApplication.mLocation.getLatitude();
                double lng = BaseKasaoApplication.mLocation.getLongitude();
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?origin=" + lat + "," + lng + "&destination=" + latitude + "," + lngtitude + "&mode=driving"));//方案
                // i1.setData(Uri.parse("baidumap://map/navi?location=" + latitude + "," + lngtitude));//方案
                nContext.startActivity(i1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // 如果没安装会进入异常，调起下载页面
        AMapUtils.getLatestAMapApp(BaseKasaoApplication.Companion.getApplicaton());

    }

    // 优先高德地图
    public static void nativegationLatLng(Context nContext, String start, String end, LatLng latStart, LatLng latend) {
        if (isInstallGodeMap()) {
            try {
                Intent i1 = new Intent();
                i1.setAction("android.intent.action.VIEW");
                String date = "amapuri://route/plan/?sid=BGVIS1&slat=" + latStart.latitude + "+&slon=" + latStart.longitude + "&sname=" + start + "&did=BGVIS2&dlat=" + latend.latitude + "&dlon=" + latend.longitude + "&dname=" + end + "&dev=0&t=0";
                i1.setData(Uri.parse(date));
                i1.setPackage("com.autonavi.minimap");
                nContext.startActivity(i1);
            } catch (Throwable e) {
            }
            return;
        }
        if (isInstallBaiduMap()) {
            try {
                latend = bd_encrypt(latend.latitude, latend.longitude);
                double lat;
                double lng;
                if (null == latStart) {
                    lat = BaseKasaoApplication.mLocation.getLatitude();
                    lng =BaseKasaoApplication.mLocation.getLongitude();
                } else {
                    latStart = bd_encrypt(latStart.latitude, latStart.longitude);
                    lat = latStart.latitude;
                    lng = latStart.longitude;
                }
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?origin=" + lat + "," + lng + "&destination=" + latend.latitude + "," + latend.longitude + "&mode=driving"));//方案
                // i1.setData(Uri.parse("baidumap://map/navi?location=" + latitude + "," + lngtitude));//方案
                nContext.startActivity(i1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        // 如果没安装会进入异常，调起下载页面
        AMapUtils.getLatestAMapApp(BaseKasaoApplication.Companion.getApplicaton());

    }

    //高德转百度
    private static LatLng bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * Math.PI);
        double theta = atan2(y, x) + 0.000003 * cos(x * Math.PI);
        double bd_lon = z * cos(theta) + 0.0065;
        double bd_lat = z * sin(theta) + 0.006;
        return new LatLng(bd_lat, bd_lon);
    }

    //百度转高德
    void bd_decrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * Math.PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * Math.PI);
        double gg_lon = z * cos(theta);
        double gg_lat = z * sin(theta);
    }

}
