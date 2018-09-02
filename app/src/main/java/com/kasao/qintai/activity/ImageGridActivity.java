package com.kasao.qintai.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.ImageGridAdapter;
import com.kasao.qintai.dialoge.DialogeRequestPermisson;
import com.kasao.qintai.popuwindow.PhotoBucketWindow;
import com.kasao.qintai.util.ImageTools;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.util.PermissionsUtils;
import com.kasao.qintai.util.SharedPreferencesHelper;
import com.kasao.qintai.widget.photo.AlbumHelper;
import com.kasao.qintai.widget.photo.BitmapBucket;
import com.kasao.qintai.widget.photo.ImageBucket;
import com.kasao.qintai.widget.photo.ImageItem;
import com.kasao.qintaiframework.base.BaseActivity;
import com.kasao.qintaiframework.until.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageGridActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener, PhotoBucketWindow.OnBucketSelectedListener {
    public final String TAG = getClass().getSimpleName();
    public final static String IMAGE_MAP = "image_map";
    public static Bitmap def;
    private List<ImageItem> dataList = new ArrayList<>();
    private GridView gridView;
    private ImageGridAdapter adapter;
    private AlbumHelper helper;
    private Button btn;
    private PhotoBucketWindow bucketWindow;
    private String col_id;
    public static final int TAKE_PHOTO = 1;//拍照
    public static final int PICTURE_CUT = 3;//剪切图片
    private Uri outputUri;//裁剪万照片保存地址
    private View popView;
    private List<ImageBucket> list;

    @Override
    public int onLayoutLoad() {
        return R.layout.activity_image_system;
    }

    @Override
    public void findView() {
        def = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
        helper = AlbumHelper.getHelper();
        helper.init(this);
        btn = (Button) findViewById(R.id.btn_sel);
        Button bucketBtn = findViewById(R.id.sel_bucket);
        ImageView back = findViewById(R.id.space);
        gridView = findViewById(R.id.image_grid);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        popView = LayoutInflater.from(this).inflate(R.layout.select_picture_pop_list, null);
        list = helper.getImageBucketList(false);
        bucketWindow = new PhotoBucketWindow(this, popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, list,
                mHandler);
        bucketWindow.setSelectedListener(this);

        btn.setOnClickListener(this);
        back.setOnClickListener(this);
        bucketBtn.setOnClickListener(this);
        btn.setText("完成(" + BitmapBucket.bitlist.size() + "/" + BitmapBucket.max + ")");
        col_id = getIntent().getStringExtra("col_id");
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showAlter("最多只能" + BitmapBucket.max + "张");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sel && adapter.map.size() != 0) {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_MAP, (Serializable) adapter.map);
            intent.putExtra("col_id", col_id);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (id == R.id.sel_bucket) {
            if (null == list || list.isEmpty()) {
                ToastUtil.showAlter("暂无图片,赶紧去拍照吧");
                return;
            }
            if (null != bucketWindow && bucketWindow.isShowing()) {
                bucketWindow.dismiss();
            } else {
                bucketWindow.showAsDropDown(popView);
            }

        } else if (id == R.id.space) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        dataList.addAll(helper.getAllImageList(true));
        adapter = new ImageGridAdapter(this, dataList, mHandler);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        adapter.setTextCallBack(new ImageGridAdapter.TextCallBack() {

            @Override
            public void onListen(int count) {
                btn.setText("完成(" + count + "/" + BitmapBucket.max + ")");
            }

            @Override
            public void onCamere() {
                takePhote();
            }
        });
        super.onResume();
    }

    private void takePhote() {
        boolean isFlag = ActivityCompat.shouldShowRequestPermissionRationale(ImageGridActivity.this, Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(ImageGridActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            boolean isAllow = (Boolean) SharedPreferencesHelper.getInstance(this).getSharedPreference(ParmarsValue.PETSION_CAMER, false);
            if (isAllow && !isFlag) {
                DialogeRequestPermisson dialoge = new DialogeRequestPermisson(ImageGridActivity.this);
                dialoge.setmSetting(new DialogeRequestPermisson.ISettings() {
                    @Override
                    public void toSetting() {
                        Intent in = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        in.setData(uri);
                        startActivityForResult(in, PermissionsUtils.REQUEST_PHOTE_PERSION);
                    }
                });
                dialoge.showDialoge(getString(R.string.permission_camare), getString(R.string.permission_request_camare));
            } else {
                SharedPreferencesHelper.getInstance(this).put(ParmarsValue.PETSION_CAMER, ActivityCompat.shouldShowRequestPermissionRationale(ImageGridActivity.this, Manifest.permission.CAMERA));
                ActivityCompat.requestPermissions(ImageGridActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PermissionsUtils.REQUEST_PHOTE_PERSION);
            }
        } else {
            //有权限，直接拍照
            openCamera();
        }

    }

    private Uri imageUri;//相机拍照图片保存地址

    protected void openCamera() {
        try {
            File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT < 24) {
                imageUri = Uri.fromFile(outputImage);
            } else {
                //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                //参数二:fileprovider绝对路径 com.qintai.meike.fileprovider：项目包名
                imageUri = FileProvider.getUriForFile(this, "com.kasao.qintai.fileprovider", outputImage);
            }
            // 启动相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showAlter("Photo NotFound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBucketSelected(List<ImageItem> imageList) {
        if (null != dataList) {
            dataList.clear();
            dataList.addAll(imageList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PHOTO:
                startPhotoZoom(imageUri);
                break;
            case PICTURE_CUT:
                if (resultCode == RESULT_OK) {
                    try {
                        if (intent == null) {
                            ToastUtil.showAlter("取消上传");
                            return;
                        } else {
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && intent.getData() != null) {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
                            } else {
                                bitmap = intent.getParcelableExtra("data");
                            }
                            if (bitmap != null) {
                                String path = ImageTools.saveBitmapToSDcard(this, bitmap);
                                Intent data = new Intent();
                                data.putExtra("path", path);
                                data.putExtra("col_id", col_id);
                                setResult(5, data);
                                ImageGridActivity.this.finish();
                            } else {
                                ToastUtil.showAlter("获取裁剪照片错误");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        try {
            startActivityForResult(intent, PICTURE_CUT);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showAlter("您的设备不支持照片剪裁");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_PHOTE_PERSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，可以拍照
                    openCamera();
                } else {
                    ToastUtil.showAlter("CAMERA PERMISSION DENIED");
                }
                break;
            default:
        }
    }

}
