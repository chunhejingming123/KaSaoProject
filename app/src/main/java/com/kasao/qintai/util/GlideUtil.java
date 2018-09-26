package com.kasao.qintai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:10
 */

public class GlideUtil {
    private static GlideUtil mInstance;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (mInstance == null) {
            synchronized (GlideUtil.class) {
                if (mInstance == null) {
                    mInstance = new GlideUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 20; //圆角
        public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
    }

    ///////////////////////////////////////  具 体 方 法  ////////////////////////////////////////////

    /**
     * 默认glide，不做任何处理，glide 从字符串中加载图片（网络地址或者本地地址）
     */
    public static void intoDefault(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    /**
     * 默认glide，不做任何处理，加载资源图片
     */
    public static void intoDefault(Context context, int id, ImageView view) {
        Glide.with(context).load(id).into(view);
    }

    /**
     * glide 从文件中加载图片
     */
    public static void into(Context context, File file, ImageView view, int defaultId) {
        Glide.with(context).load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultId)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }

    /**
     * glide 从URI中加载图片
     */
    public static void into(Context context, Uri uri, ImageView view, int defaultId) {
        Glide.with(context).load(uri)
                .placeholder(defaultId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }
    /**
     * glide 通过指定的大小从字符串中加载图片（网络地址或者本地地址）
     */
    public static void intoFitCenter(Context context, String url, ImageView view, int width, int height, int defaultId) {
        DrawableTypeRequest<String> request = Glide.with(context).load(url);

        if (width > 0 && height > 0) {
            request.override(width, height);
        }

        request.placeholder(defaultId)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .fitCenter() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }
    /**
     * glide 通过指定的大小从字符串中加载图片（网络地址或者本地地址）
     */
    public static void into(Context context, String url, ImageView view, int width, int height, int defaultId) {
        DrawableTypeRequest<String> request = Glide.with(context).load(url);

        if (width > 0 && height > 0) {
            request.override(width, height);
        }

        request.placeholder(defaultId)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }


    /**
     * glide 通过指定的大小从资源ID中加载图片
     */
    public static void into(Context context, int resourceId, ImageView view, int width, int height, int defaultId) {
        DrawableTypeRequest<Integer> request = Glide.with(context).load(resourceId);

        if (width > 0 && height > 0) {
            request.override(width, height);
        }

        request.placeholder(defaultId)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .fitCenter() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }

    /**
     * glide 通过指定的大小从文件中加载图片
     */
    public static void into(Context context, File file, ImageView view, int width, int height, int defaultId) {
        DrawableTypeRequest<File> request = Glide.with(context).load(file);

        if (width > 0 && height > 0) {
            request.override(width, height);
        }

        request.placeholder(defaultId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }

    /**
     * glide 通过指定的大小从Uri中加载图片
     */
    public static void into(Context context, Uri uri, ImageView view, int width, int height, int defaultId) {
        DrawableTypeRequest<Uri> request = Glide.with(context).load(uri);
        if (width > 0 && height > 0) {
            request.override(width, height);
        }
        request.placeholder(defaultId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }

    /**
     * 高斯模糊图片处理
     */
//    public static void intoBlur(Context context, String url, ImageView view, int defaultId) {
//        Glide.with(context).load(url)
//                .placeholder(defaultId)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
//                .transform(new BlurTransformation(context))
//                .centerCrop() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
//                .priority(Priority.NORMAL) //下载的优先级
//                .into(view);
//    }
    /**
     * glide 从字符串中加载图片（网络地址或者本地地址）,
     */
    public static void into(Context context, String url, ImageView view, int defaultId) {
        Glide.with(context).load(url)
                .placeholder(defaultId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()//去掉glide 自带的效果，防止加载自定义控件时只显示替换图
                .fitCenter() // 缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分
                .priority(Priority.NORMAL) //下载的优先级
                .into(view);
    }

    /**
     * 加载gif
     *
     * @param context
     * @param imageView
     * @param imgUrl
     * @param placeholder 图片资源ID
     */
    public void loadGifImage(Context context, ImageView imageView, String imgUrl, int placeholder) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .error(placeholder)
                .into(imageView);
    }

    /**
     * 加载gif的缩略图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     * @param placeholder 图片资源ID
     */
    public void loadGifThumbnailImage(Context context, ImageView imageView, String imgUrl, int placeholder) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .error(placeholder)
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    /**
     * 设置要加载的内容
     *
     * @param mContext
     * @param path
     * @param simpleTarget
     */
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }


    /**
     * 恢复请求，一般在停止滚动的时候
     *
     * @param context
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 暂停请求 正在滚动的时候
     *
     * @param context
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 清理磁盘缓存
     *
     * @param mContext
     */
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清理内存缓存
     *
     * @param mContext
     */
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }


    /////////////////////////////  加载圆角图片   加载圆形图片  第二种方法 ////////////////////////////////


    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImage(final Context context, String url, int placeholder,
                                      final int radius, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(radius); //设置圆角弧度
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCirclePic(final Context context, String url, int placeholder,
                                     final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
