package com.kasao.qintai.version;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.util.FileUtils;
import com.kasao.qintai.util.ParmarsValue;
import com.kasao.qintai.util.PermissionsUtils;
import com.kasao.qintai.util.SharedPreferencesHelper;
import com.kasao.qintaiframework.http.Api;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/2/9.
 */

public class VersionUpMananger {
    private Activity mContent;

    public VersionUpMananger() {
    }

    public VersionUpMananger(Activity mContent) {
        this.mContent = mContent;
    }

    public void setVersionInfo(Version version) {//isUp 是用来判断设置自启管理权限
        String versionCode = MobileUtil.getVersionName(mContent);
        if (versionCode.equals(version.version)) {
            if (isRequers) {
                setting();
            } else {
               // ToastUtil.showAlter("已最新版本");
            }
        } else {
            if (ContextCompat.checkSelfPermission(mContent, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContent, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionsUtils.REQUEST_EXTERNAL_STORAGE);
            } else {
                showUpdateDialog(version);
            }
        }

    }

    //开启权限管理
    private void setting() {
        boolean issetting = (Boolean) SharedPreferencesHelper.getInstance(mContent).getSharedPreference(ParmarsValue.KEY_SETTING, false);
//        if (!issetting) {
//            boolean isToday = DateUtil.isInToday(MyApplication.getInstance().getAppConfig().getLong(TraminsParmars.KEY_System_Time, 0l));
//            if (!isToday) {
//                DialogeStartPermission perssion = new DialogeStartPermission(mContent);
//                perssion.showDialoge();
//            }
//        }
    }

    private void showUpdateDialog(final Version version) {
        DialogeVersionUp dialoge = new DialogeVersionUp(mContent);
        dialoge.rendView(version.upgrade_point);
        dialoge.showDialoge(new DialogeVersionUp.ODialogeVersionUp() {
            @Override
            public void onUpDate() {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(version.url);     //在下面的代码段  "http://www.qtmeike.com/Public/site/app/user.apk"
                } else {
                    ToastUtil.showAlter("SD卡不可用，请插入SD卡");
                }
            }
        });
    }

    private ProgressDialog pBar;

    void downFile(final String strurl) {
        pBar = new ProgressDialog(mContent);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.setCanceledOnTouchOutside(false);
        pBar.show();
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(strurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    int length = conn.getContentLength();   //获取文件大小
                    pBar.setMax(length);                            //设置进度条的总长度
                    InputStream is = conn.getInputStream();
                    FileOutputStream fileOutputStream = null;
                    File file = null;
                    if (is != null) {
                        file = new File(
                                Environment.getExternalStorageDirectory(),
                                "kasao.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process);       //这里就是关键的实时更新进度了！
                        }
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    is.close();
                    if (pBar != null) {
                        pBar.cancel();
                    }
                    FileUtils.installApk(file, mContent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    boolean isRequers;

    public void checkUp(boolean isRequer) {
        isRequers = isRequer;
        Map<String, String> map = new HashMap<String, String>();
        map.put("app_id", "2");
        ApiManager.Companion.getGetInstance().getDataByParmars(ApiInterface.Companion.getUPVERSION(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                VersionDomain domain = null;
                try {
                    domain = GsonUtil.Companion.GsonToBean(t.string(), VersionDomain.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null != domain) {
                    setVersionInfo(domain.data);
                } else {
                    //  ToastUtil.showAlter("已最新版本");
                }
            }

            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });

    }

}
