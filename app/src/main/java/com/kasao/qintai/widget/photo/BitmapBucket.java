package com.kasao.qintai.widget.photo;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/31 0031:15
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBucket {
    public static int max = 4;
    public static List<Bitmap> bitlist = new ArrayList<>();
    public static List<String> pathList = new ArrayList<>();

    public static void clear() {
        bitlist.clear();
        pathList.clear();
    }

    public static Bitmap revitionImageSize(String path, int size)
            throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        @SuppressWarnings("unused")
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inJustDecodeBounds = false;
                options.inSampleSize = (int) Math.pow(2.0D, i);
                bitmap = BitmapFactory.decodeStream(in, null, options);
                in.close();
            }
            i += 1;
        }
    }
}
