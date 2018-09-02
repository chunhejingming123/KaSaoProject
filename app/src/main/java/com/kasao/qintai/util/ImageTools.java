package com.kasao.qintai.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.kasao.qintai.model.Info;
import com.kasao.qintaiframework.until.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public final class ImageTools {

    private static final int MIN_STORAGE_AVAILABLE_SIZE = 5;
    public static String image_path;
    public static String filepath;
    public static String filename;

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int pictureDegree = readPictureDegree(srcPath);
        if (pictureDegree > 0) {
            bitmap = rotaingImageView(pictureDegree, bitmap);
        }
        if (null == bitmap) {
            return null;
        }
        return compressImage(bitmap, Info.ImageSize);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap bitmap, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于size,大于继续压缩
            LogUtil.e("------compress=" + baos.toByteArray().length / 1024);
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }

        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = 1;
        // 计算缩放比例
        if (width > height && width > newWidth) {// 如果宽度大的话根据宽度固定大小缩放
            scale = ((float) newWidth) / width;
        } else if (height > width && height > 0.8 * newWidth) {// 如果高度高的话根据宽度固定大小缩放
            scale = ((float) 0.8 * newWidth) / height;
        } else if (height > newWidth) {
            scale = ((float) height / newWidth);
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

//	public static Bitmap resizeImage2(byte[] bs, int width, int height) {
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;// 不加载bitmap到内存中
//		BitmapFactory.decodeByteArray(bs, 0, bs.length, options);
//		options.inDither = false;
//		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		options.inSampleSize = 1;
//		options.inSampleSize = calculateInSampleSize(options, width, height);
//		options.inJustDecodeBounds = false;
//		return BitmapFactory.decodeByteArray(bs, 0, bs.length, options);
//	}

    /**
     * 对图片进行二次采样
     *
     * @param
     * @param reqWidth  要压缩的宽度
     * @param reqHeight 要压缩的高度
     * @return
     */
    public static Bitmap getSmallBitmap(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 获取图片压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 保存图片到指定的目录中
     *
     * @param bitmap
     */
    public static String saveBitmapToSDcard(Context context, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        File file = null;
        image_path = createImageFile(context).replaceAll(" ", "");
        try {
            file = new File(filepath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(filepath + File.separator + filename);
            outputStream = new FileOutputStream(file);
            boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (flag) {
                return image_path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 创建图片的存储路径，包含文件名
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String imageFileName = null;
        if (checkSDCardAvailable()) {
            imageFileName = getExternalPath(context) + File.separator + timeStamp + ".png";
            filepath = getExternalPath(context);
            filename = timeStamp + ".png";
        } else {
            String FilePath = context.getCacheDir() + File.separator + Info.GSBXUTILS;
            imageFileName = FilePath + File.separator + timeStamp + ".png";
            filepath = FilePath;
            filename = timeStamp + ".png";
        }
        return imageFileName;
    }

    /**
     * 获得指定的存储路径：例如Picture
     *
     * @return
     */
    public static String getExternalPath(Context context) {
        String cachePath;
        if (checkSDCardAvailable() || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath() + File.separator + Info.GSBXUTILS;
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    /**
     * Drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Input stream to bitmap
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Bitmap inputStreamToBitmap(InputStream inputStream)
            throws Exception {
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Byte transfer to bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Byte transfer to drawable
     *
     * @param byteArray
     * @return
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    /**
     * Bitmap transfer to bytes
     *
     * @param
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Drawable transfer to bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);

        return bytes;
    }


    /**
     * Bitmap to hex String
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String BitmapToHexString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 40, baos);
        byte[] bArray = baos.toByteArray();
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * Create reflection images
     *
     * @param bitmap
     * @return
     */
//	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
//		final int reflectionGap = 4;
//		int w = bitmap.getWidth();
//		int h = bitmap.getHeight();
//
//		Matrix matrix = new Matrix();
//		matrix.preScale(1, -1);
//
//		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
//				h / 2, matrix, false);
//
//		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
//				Config.ARGB_8888);
//
//		Canvas canvas = new Canvas(bitmapWithReflection);
//		canvas.drawBitmap(bitmap, 0, 0, null);
//		Paint deafalutPaint = new Paint();
//		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);
//
//		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);
//
//		Paint paint = new Paint();
//		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
//				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
//				0x00ffffff, TileMode.CLAMP);
//		paint.setShader(shader);
//		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
//				+ reflectionGap, paint);
//
//		return bitmapWithReflection;
//	}

    /**
     * Get rounded corner images
     *
     * @param bitmap
     * @param roundPx 5 10
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * Resize the drawable
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * Get images from SD card by path and the name of image
     *
     * @param photoName
     * @return
     */
    public static Bitmap getPhotoFromSDCard(String path, String photoName) {
        Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
        if (photoBitmap == null) {
            return null;
        } else {
            return photoBitmap;
        }
    }


    /**
     * Get image from SD card by path and the name of image
     *
     * @param
     * @return
     */
    public static boolean findPhotoFromSDCard(String path, String photoName) {
        boolean flag = false;
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (dir.exists()) {
                File folders = new File(path);
                File photoFile[] = folders.listFiles();
                for (int i = 0; i < photoFile.length; i++) {
                    String fileName = photoFile[i].getName();
                    if (fileName.equals(photoName)) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }

        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * Save the same file automatic sorting
     */
    private static Random sRandom = new Random(SystemClock.uptimeMillis());

    public static String ChooseUniqueFilename(String filename, String extension) {
        String fullFilename = filename + extension;
        if (!new File(fullFilename).exists()) {
            return fullFilename;
        }

        int sequence = 1;
        for (int magnitude = 1; magnitude < 1000000000; magnitude *= 10) {
            for (int iteration = 0; iteration < 9; ++iteration) {
                fullFilename = filename + "(" + sequence + ")" + extension;
                if (!new File(fullFilename).exists()) {
                    return fullFilename;
                }
                sequence += sRandom.nextInt(magnitude) + 1;
            }
        }

        return fullFilename;
    }


    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(dir, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete the image from SD card
     *
     * @param
     * @param path file:///sdcard/temp.jpg
     */
    public static void deleteAllPhoto(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    /**
     * Delete the image from fileName
     *
     * @param path
     * @param fileName
     */
    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }


    /**
     * get SdCard StorageSize
     */
    @SuppressWarnings("deprecation")
    public static long getSdCardAvailableStorageSize() {
        long availabledSize = 0L;
        if (checkSDCardAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long blockSize = statFs.getBlockSize();
            long availableBlocks = statFs.getAvailableBlocks();
            availabledSize = availableBlocks * blockSize;
        }
        return availabledSize;
    }

    /**
     * check SdCard Storage
     *
     * @return
     */
    public static boolean isSdCardStorageAvailable() {
        long minStorageSizeByte = MIN_STORAGE_AVAILABLE_SIZE * 1024;
        long availableSizeByte = getSdCardAvailableStorageSize();
        return availableSizeByte > minStorageSizeByte;
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 4.4
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
