package com.kasao.qintai.model;

import android.os.Environment;

import java.io.File;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/27 0027:14
 */

public class Info {

    public final static String PATH = Environment.getExternalStorageDirectory() + "/kasao/";
    public final static String GSBCACHE = "CaChe";
    public final static String GSBDOWN = "Download";
    public final static String GSBXUTILS = "xBitmapCache";
    public final static File PHOTO_DIR = new File(PATH + File.separator + GSBCACHE);
    public static int widthPixels = 0;// 机型的屏幕分辨率的宽
    public static int heightPixels = 0;// 机型的屏幕分辨率的高
    public static int ImageSize = 1000; // 图片上传统一压缩大小
}
