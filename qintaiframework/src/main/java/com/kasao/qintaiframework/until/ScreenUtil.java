package com.kasao.qintaiframework.until;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 屏幕分辨率工具类
 * ScreenUtil
 */
public class ScreenUtil {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    /**
     * 初始化屏幕分辨率工具
     * @param context
     */
    public static void initScreen(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenW() {
        return screenW;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenH() {
        return screenH;
    }

    /**
     * 获取屏幕密度
     * @return
     */
    public static float getScreenDensity() {
        return screenDensity;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    public static String getDisplayMetrics(Context cx) {
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm =cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth =dm.widthPixels;
        int screenHeight =dm.heightPixels;
        float density =dm.density;
        float xdpi =dm.xdpi;
        float ydpi =dm.ydpi;
        str += "The absolute width:"+ String.valueOf(screenWidth) + "pixels\n";
        str += "The absoluteheightin:" + String.valueOf(screenHeight)+ "pixels\n";

        str += "The logical densityof the display.:" + String.valueOf(density)+ "\n";
        str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
        return str;
    }
}
