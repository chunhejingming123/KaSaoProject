package com.kasao.qintai.popuwindow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kasao.qintai.util.BitmapCache;
import com.kasao.qintai.widget.photo.ImageBucket;
import com.kasao.qintai.R;
import com.kasao.qintai.widget.photo.ImageItem;

import java.util.List;

public class PhotoBucketWindow extends PopupWindow implements OnItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private View view;
    private View anchor;
    private ListView listView;
    private List<ImageBucket> bucketList;
    private BitmapCache bitmapCache;
    private OnBucketSelectedListener selectedListener;
    private ImageBucket selectedBucket;

    public PhotoBucketWindow(Context context, View contentView, int width, int height, List<ImageBucket> bucketList, Handler mHandler) {
        super(contentView, width, height);
        this.context = context;
        this.view = contentView;
        this.bucketList = bucketList;
        this.mHandler = mHandler;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        setContentView(view);
        listView = (ListView) view.findViewById(R.id.lv_select_picture);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.AnimBottom);
        bitmapCache = new BitmapCache();
        ImageBucketAdapter adapter = new ImageBucketAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (null != bucketList && !bucketList.isEmpty()) {
            selectedBucket = bucketList.get(0);
        }

    }

    public void setSelectedListener(OnBucketSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    Handler mHandler;
    public BitmapCache.ImageCallBack callBack = new BitmapCache.ImageCallBack() {

        @Override
        public void doImageLoad(ImageView iv, Bitmap bitmap, Object... params) {
            if (iv != null && bitmap != null) {
                iv.setImageBitmap(bitmap);
//                if (url != null && iv.getTag() != null && url.equals(iv.getTag().toString())) {
//                    iv.setImageBitmap(bitmap);
//                } else {
//                    Log.e(TAG, "photo not same");
//                }
            } else {
                Log.e(TAG, "bitmap is null");
            }

        }
    };

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    public void show() {
        showAsDropDown(anchor);
    }

    class ImageBucketAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            return bucketList == null || bucketList.isEmpty() ? 0 : bucketList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageBucket bucket = bucketList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.select_picture_pop_item, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
//            bitmapCache.dispBitmap(imageView, bucket.imageList.get(0).imagePath, bucket.imageList.get(0).thumbnailPath, callBack);
            if ("我的图片".equals(bucket.bucketName) && null != bucket.imageList && !bucket.imageList.isEmpty()) {
                bitmapCache.dispBitmap(imageView, bucket.imageList.get(bucket.imageList.size() - 1).imagePath,
                        bucket.imageList.get(bucket.imageList.size() - 1).thumbnailPath, callBack);
            } else {
                if (null != bucket.imageList && !bucket.imageList.isEmpty()) {
                    bitmapCache.dispBitmap(imageView, bucket.imageList.get(0).imagePath, bucket.imageList.get(0).thumbnailPath, callBack);
                }

            }
            ;
            if (bucket.isSelected)
                imageView2.setImageResource(R.drawable.checkbox_pressed);
            else
                imageView2.setImageBitmap(null);

            TextView tv1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView tv2 = (TextView) convertView.findViewById(R.id.textView2);
            tv1.setText(bucket.bucketName);
            tv2.setText(bucket.count + "张");

            return convertView;
        }

    }

    public interface OnBucketSelectedListener {
        void onBucketSelected(List<ImageItem> imageList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageBucket curBucket = bucketList.get(position);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        List<ImageItem> imageList = curBucket.imageList;
        if (selectedListener != null) {
            selectedListener.onBucketSelected(imageList);
        }
        imageView2.setImageResource(R.drawable.checkbox_pressed);
        curBucket.isSelected = true;
        if (curBucket != selectedBucket)
            selectedBucket.isSelected = false;
        selectedBucket = curBucket;
        dismiss();
    }
}
