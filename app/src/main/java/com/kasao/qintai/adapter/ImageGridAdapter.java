package com.kasao.qintai.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.kasao.qintai.util.BitmapCache;
import com.kasao.qintai.util.ImageTools;
import com.kasao.qintai.widget.photo.BitmapBucket;
import com.kasao.qintai.widget.photo.ImageItem;
import com.kasao.qintai.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageGridAdapter extends BaseAdapter {
    private TextCallBack textCasllBack = null;
    private final String TAG = getClass().getSimpleName();
    private List<ImageItem> dataList;
    private Activity act;
    public Map<String, String> map = new HashMap<String, String>();
    private BitmapCache cache;
    private Handler mHandler;
    private int selectedTotal = 0;
    int count = 0;
    public BitmapCache.ImageCallBack callBack = new BitmapCache.ImageCallBack() {

        @Override
        public void doImageLoad(ImageView iv, Bitmap bitmap, Object... params) {
            if (iv != null && bitmap != null) {
                String url = params[0].toString();
                int degree = ImageTools.readPictureDegree(url);
                if (degree > 0) {
                    bitmap = ImageTools.rotaingImageView(degree, bitmap);
                }
                if (url != null && iv.getTag() != null && url.equals(iv.getTag().toString())) {
                    iv.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "picture does not same");
                }
            } else {
                Log.e(TAG, "picture is null");
            }

        }
    };

    public void setTextCallBack(TextCallBack textCallBack) {
        this.textCasllBack = textCallBack;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> dataList, Handler mHandler) {
        this.act = act;
        this.dataList = dataList;
        this.mHandler = mHandler;
        this.cache = new BitmapCache();
    }

    @Override
    public int getCount() {
        if (dataList != null&&!dataList.isEmpty()) {
            return dataList.size() + 1;
        }
        return 1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.img_grid_item);
            holder.selected = (ImageView) convertView.findViewById(R.id.check_img);
            holder.text = (TextView) convertView.findViewById(R.id.img_grid_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position == 0) {
            holder.selected.setVisibility(View.GONE);
            holder.iv.setImageResource(R.drawable.camera);
            holder.iv.setScaleType(ScaleType.FIT_XY);
            holder.iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (textCasllBack != null) {
                        textCasllBack.onCamere();
                    }
                }
            });

        } else {
            holder.selected.setVisibility(View.VISIBLE);
            final ImageItem item = dataList.get(position - 1);
            holder.iv.setTag(item.imagePath);
            holder.iv.setScaleType(ScaleType.CENTER_CROP);
            cache.dispBitmap(holder.iv, item.imagePath, item.thumbnailPath, callBack);
            if (item.isSelected) {
                holder.selected.setImageResource(R.drawable.checkbox_pressed);
                holder.text.setBackgroundColor(0xb2293334);
            } else {
                holder.selected.setImageResource(R.drawable.checkbox_normal);
                holder.text.setBackgroundColor(0x00000000);
            }
            holder.iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String path = dataList.get(position - 1).imagePath;
                    if (selectedTotal + BitmapBucket.bitlist.size() < BitmapBucket.max) {
                        item.isSelected = !item.isSelected;
                        if (item.isSelected) {
                            holder.selected.setImageResource(R.drawable.checkbox_pressed);
                            holder.text.setBackgroundColor(0xb2293334);
                            selectedTotal += 1;
                            if (textCasllBack != null) {
                                textCasllBack.onListen(selectedTotal + BitmapBucket.bitlist.size());
                            }
                            map.put(path, path);
                        } else {
                            holder.selected.setImageResource(R.drawable.checkbox_normal);
                            holder.text.setBackgroundColor(0x00000000);
                            selectedTotal -= 1;
                            if (textCasllBack != null) {
                                textCasllBack.onListen(selectedTotal + BitmapBucket.bitlist.size());
                            }
                            map.remove(path);
                        }
                    } else if (selectedTotal + BitmapBucket.bitlist.size() >= BitmapBucket.max) {
                        if (item.isSelected) {
                            item.isSelected = !item.isSelected;
                            holder.selected.setImageResource(R.drawable.checkbox_normal);
                            holder.text.setBackgroundColor(0x00000000);
                            selectedTotal--;
                            if (textCasllBack != null) {
                                textCasllBack.onListen(selectedTotal + BitmapBucket.bitlist.size());
                            }
                            map.remove(path);
                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }
        return convertView;

    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    public interface TextCallBack {
         void onListen(int count);
        void onCamere();
    }
}
