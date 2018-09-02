package com.kasao.qintai.base;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;

import com.kasao.qintai.activity.login.LoginActivity;
import com.kasao.qintai.model.domain.User;
import com.kasao.qintaiframework.base.MyApplication;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/23 0023:11
 */

public class BaseKasaoApplication extends MyApplication {
    private static User mUser;

    public static void setUser(User user) {
        mUser = user;
    }

    public static User getUser() {
        return mUser;
    }

    public static String getUserId() {
        return mUser.user_id;
    }

    public static String getUserImg() {
        return mUser.user_img;
    }
    public static String getUserName() {
        return mUser.user_name;
    }

    public static Location mLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.initScreen(this);
    }

    private void initLocation() {
        mLocation = new Location(new Location("山西"));
    }
}
