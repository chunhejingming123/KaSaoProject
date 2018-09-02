package com.kasao.qintai.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Administrator on 2017/12/14.
 */

public class PermissionsUtils {
    public static boolean hasSdCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    public static final int REQUEST_LOCATION=3;
    //Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    //photo persion
    public static final int REQUEST_PHOTE_PERSION=2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_PHOTO = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //动态获取内存存储权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //动态获取拍照权限
    public static void verifyPhotoPermissions(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission= ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_PHOTO,
                      REQUEST_PHOTE_PERSION
                );
            }

        }


    }
}
