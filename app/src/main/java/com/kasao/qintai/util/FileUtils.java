package com.kasao.qintai.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.kasao.qintai.model.Info;
import com.kasao.qintaiframework.base.BaseFragment;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Administrator on 2017/12/15.
 * 文件工具类使用
 */

public class FileUtils {

    public final static int TAKE_PIC = 124;
    public static final int REQUEST_CODE_PICK_IMAGE = 123;
    public final static int CROP_PIC = 125;
    public static String sdCardDir = Environment.getExternalStorageDirectory() + "/"; //+MEIKE+"/"

    /**
     * 打开选择图片的界面
     */
    public static void choosePhoto(Activity activity, int REQUEST_CODE_PICK_IMAGE, String path, String name) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        try {
            if (!PermissionsUtils.hasSdCard()) {
                ToastUtil.showAlter("no read sdk");
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, name);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开选择图片的界面
     */
    public static void choosePhoto(BaseFragment fragment, int REQUEST_CODE_PICK_IMAGE, String name) {
        try {
            if (!PermissionsUtils.hasSdCard()) {
                ToastUtil.showAlter("no read sdk");
            }
//            File dir = new File(path);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            File photoFile = new File(dir, photoName);

            File file = new File(sdCardDir, name);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            fragment.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拍照
    public static File takePhotos(BaseFragment fragment, String name, int requestCode) {
        File file = null;
        try {
            if (!PermissionsUtils.hasSdCard()) {
                ToastUtil.showAlter("no read sdk");
                return null;
            }
            file = new File(sdCardDir, name);

            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            fragment.startActivityForResult(intent, requestCode);

        } catch (Exception e) {
        }
        return file;
    }

    // 裁剪
    public static void cropImageUri(BaseFragment fragment, Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 400);
        intent.putExtra("aspectY", 240);
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 294);
//        intent.putExtra("aspectY", 184);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 240);
        intent.putExtra("scale", true);
        // intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", true);
        try {
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            ToastUtil.showAlter("您的设备不支持照片剪裁");
        }

    }

    /**
     * Compress image by size, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public Bitmap ratio(Bitmap image, float pixelW, float pixelH, String name) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        saveBitmap(bitmap, name);
        return bitmap;
    }

    // 保存bitmap 到文件
    public static void saveBitmap(Bitmap mBitmap, String name) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下
            // 压缩小于1M
            Bitmap bitmap = compressImage(mBitmap);
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }

            File fold = new File(sdCardDir, name);//+ ".jpg"
            if (fold.exists()) {
                File[] files = fold.listFiles();
                if (null != files) {
                    for (File filed : files) {
                        if (null != filed && filed.getName().contains(name)) {
                            filed.delete();
                        }
                    }
                }
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(fold);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showAlter("没有sd卡不能存储");
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > Info.ImageSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            LogUtil.e("Tag", "-------size=" + baos.toByteArray().length / 1024);
        }
        LogUtil.e("Tag", "-------sizess=" + baos.toByteArray().length / 1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static Bitmap getBitmap(String name) {
        //读取本地图片
        Bitmap bitmap = null;
        try {
            File avaterFile = new File(sdCardDir, name + ".jpg");
            if (avaterFile.exists()) {
                bitmap = BitmapFactory.decodeFile(sdCardDir + name + ".jpg");
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(BaseFragment ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = null;
        Bitmap bitmap = null;
        if (null != uri) {
            input = ac.getActivity().getContentResolver().openInputStream(uri);
            try {
                File file = new File(new URI(uri.toString()));
                long content = file.length() / 1024;
                if (content > 1000) {
                    BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
                    onlyBoundsOptions.inJustDecodeBounds = true;
                    onlyBoundsOptions.inDither = true;//optional
                    onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                    BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
                    input.close();
                    int originalWidth = onlyBoundsOptions.outWidth;
                    int originalHeight = onlyBoundsOptions.outHeight;
                    if ((originalWidth == -1) || (originalHeight == -1))
                        return null;
                    //图片分辨率以480x800为标准
                    float hh = 800f;//这里设置高度为800f
                    float ww = 480f;//这里设置宽度为480f
                    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                    int be = 1;//be=1表示不缩放
                    if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                        be = (int) (originalWidth / ww);
                    } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
                        be = (int) (originalHeight / hh);
                    }
                    if (be <= 0)
                        be = 1;
                    //比例压缩
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = be;//设置缩放比例
                    bitmapOptions.inDither = true;//optional
                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                    input = ac.getActivity().getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                } else {
                    input = ac.getActivity().getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(input, null, null);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
                input.close();
            }
            input.close();
        }


        return compressImage(bitmap);//再进行质量压缩
    }

    // 加载本地图片 防止内存溢出
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    //进行复制的函数
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File file = new File(newPath);
            if (!file.exists()) {
                LogUtil.e("Tag", "---------copy");
                file.createNewFile();
            }
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件不存在时
                LogUtil.e("Tag", "---------copy111111111111");
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            } else {
                LogUtil.e("Tag", "---------copy222222222222222222");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap compressImageByte(byte[] data, float size) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap == null || getSizeOfBitmap(bitmap) <= size) {
            return null;// 如果图片本身的大小已经小于指定大小，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        int quality = 100;
        while ((baos.toByteArray().length / 1024f) > size) {
            quality -= 5;// 每次都减少5(如果这里-=10，有时候循环次数会提前结束)
            baos.reset();// 重置baos即清空baos
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        return bitmap;
    }

    private static long getSizeOfBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100的话表示不压缩质量
        long length = baos.toByteArray().length / 1024;//读出图片的kb大小
        return length;
    }

    public static void installApk(File apkFile, Context context) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(context.getApplicationContext(), "com.qintai.meike.fileprovider", apkFile), "application/vnd.android.package-archive");
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(installApkIntent);
//        if (context.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
//            context.startActivity(installApkIntent);
//        }
    }

    /**
     * 网络图片转换为Bitmap
     *
     * @Author: ChengBin
     * @Time: 16/4/5 下午2:41
     */
    public static Bitmap netPicToBmp(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            //设置固定大小
            //需要的大小
            float newWidth = 100f;
            float newHeigth = 120f;

            //图片大小
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();

            //缩放比例
            float scaleWidth = newWidth / width;
            float scaleHeigth = newHeigth / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeigth);

            Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}
