package com.kasao.qintai.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kasao.qintai.util.FileUtils;
import com.kasao.qintaiframework.until.ToastUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by Administrator on 2018/1/11.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {
    private static final String TAG = "CameraSurfaceView";
    private SurfaceHolder holder;
    private Camera mCamera;

    private int mScreenWidth;
    private int mScreenHeight;
    private CameraTopRectView topView;
    private String imgName;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        topView = new CameraTopRectView(context, attrs);
        initView();


    }

    //拿到手机屏幕大小
    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;

    }

    private void initView() {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e) {
                mCamera = Camera.open(Camera.getNumberOfCameras() - 1);//开启相机
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera Camera) {
        if (success) {
            Log.i(TAG, "onAutoFocus success=" + success);
            mCamera.takePicture(null, null, jpeg);
        }
    }


    private void setCameraParams(Camera camera, int width, int height) {
        Log.i(TAG, "setCameraParams  width=" + width + "  height=" + height);
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizeList) {
            Log.i(TAG, "pictureSizeList size.width=" + size.width + "  size.height=" + size.height);
        }
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
        if (null == picSize) {
            Log.i(TAG, "null == picSize");
            picSize = parameters.getPictureSize();
        }
        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
        // 根据选出的PictureSize重新设置SurfaceView大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (height * (h / w)), height));

        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

        for (Camera.Size size : previewSizeList) {
            Log.i(TAG, "previewSizeList size.width=" + size.width + "  size.height=" + size.height);
        }
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
        if (null != preSize) {
            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }
        mCamera.cancelAutoFocus();//自动对焦。
        mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        mCamera.setParameters(parameters);

    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Log.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }

        return result;
    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        private Bitmap bitmap;

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {

            topView.draw(new Canvas());
            BufferedOutputStream bos = null;
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (data != null) {
                if (data.length / 1024 > 200
                        && data.length / 1024 < 300) {
                    options.inSampleSize = 1;
                } else if (data.length / 1024 > 300 && data.length / 1024 < 500) {
                    options.inSampleSize = 1;
                } else if (data.length / 1024 > 500 && data.length / 1024 < 1000) {
                    options.inSampleSize = 1;
                } else {
                    options.inSampleSize = 2;
                }
                options.inPurgeable = true;//内存不足时可被回收
                options.inInputShareable = true;
            }
            try {
                // 获得图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // String filePath = "/sdcard/temp.jpg";//照片保存路径
                    String filePath = FileUtils.sdCardDir + imgName;
//                    //图片存储前旋转
                    Matrix m = new Matrix();
                    int height = bm.getHeight();
                    int width = bm.getWidth();
                    m.setRotate(90);
                    //旋转后的图片
                    bitmap = Bitmap.createBitmap(bm, 0, 0, width, height, m, true);
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        file.delete();
                        file.createNewFile();
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    Bitmap sizeBitmap = Bitmap.createScaledBitmap(bitmap,
                            topView.getViewWidth(), topView.getViewHeight(), true);
                    bm = Bitmap.createBitmap(sizeBitmap, topView.getRectLeft(),
                            topView.getRectTop(),
                            topView.getRectRight() - topView.getRectLeft(),
                            topView.getRectBottom() - topView.getRectTop());// 截取

                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
                    if (null != mPicturePath) {
                        mPicturePath.getPicturePath(filePath);
                    }
                } else {
                    ToastUtil.showAlter("没有检测到内存卡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != bos) {
                        bos.flush();//输出
                        bos.close();//关闭
                        bm.recycle();// 回收bitmap空间
                    }
                    if (null != mCamera) {
                        mCamera.stopPreview();// 关闭预览
                        mCamera.startPreview();// 开启预览
                    }
                    if (null != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void takePicture(String name) {
        this.imgName = name;
        //设置参数,并拍照
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(null, null, jpeg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private OnPicturePath mPicturePath;

    public void setmPicturePath(OnPicturePath mPicturePath) {
        this.mPicturePath = mPicturePath;
    }

    public interface OnPicturePath {
        void getPicturePath(String path);
    }
}
