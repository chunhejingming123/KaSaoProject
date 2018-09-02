package com.kasao.qintaiframework.until;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.kasao.qintaiframework.base.MyApplication;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:18
 */

public class LogUtil {
    public static void d(String tag, String msg) {
        if (MyApplication.Companion.isDebug())
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (MyApplication.Companion.isDebug())
            Log.d(tag, msg, t);
    }

    public static void i(String tag, String msg) {
        if (MyApplication.Companion.isDebug())
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (MyApplication.Companion.isDebug())
            Log.i(tag, msg, t);
    }

    public static void w(String tag, String msg) {
        if (MyApplication.Companion.isDebug())
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (MyApplication.Companion.isDebug())
            Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        if  (MyApplication.Companion.isDebug())
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (MyApplication.Companion.isDebug())
        Log.e(tag, msg, t);
    }

    /**
     * 打印error级别信息
     *
     * @param msg
     */
    public static void e(Object msg) {
        if (MyApplication.Companion.isDebug()){
            Log.e("QintaiFramework:", msg == null ? "" : msg.toString());
        }
    }

//    @SuppressLint("SimpleDateFormat")
//    public static void write(String str) {
//        if  (MyApplication.Companion.isDebug()){
//            StringBuilder sb = new StringBuilder();
//            sb.append(str);
//            try {
//              String path = Info.PATH;
//                String fileName = "LOG.txt";
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    File dir = new File(path);
//                    if (!dir.exists()) {
//                        dir.mkdirs();
//                    }
//                    File pathname = new File(path + fileName);
//                    if (!pathname.exists()) {
//                        pathname.createNewFile();
//                    }
//                    FileOutputStream fos = new FileOutputStream(pathname);
//                    fos.write(sb.toString().getBytes());
//                    fos.close();
//                }
//            } catch (Exception e) {
//                Log.e("write", "an error occured while writing file...", e);
//            }
//        }
//    }
}
