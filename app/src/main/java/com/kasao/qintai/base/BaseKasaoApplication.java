package com.kasao.qintai.base;

import android.content.Intent;
import android.location.Location;

import com.kasao.qintai.activity.login.LoginActivity;
import com.kasao.qintai.model.domain.User;
import com.kasao.qintai.util.SharedPreferencesHelper;
import com.kasao.qintaiframework.base.MyApplication;
import com.kasao.qintaiframework.until.MyUncaughtExceptionHandler;
import com.kasao.qintaiframework.until.ScreenUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/23 0023:11
 */

public class BaseKasaoApplication extends MyApplication {
    private static User mUser;

    public static void setUser(User user) {
        if (null != user) {
            JPushInterface.setAlias(MyApplication.Companion.getApplicaton(), 1, user.push_alias);
        }
        mUser = user;
    }

    public static User getUser() {
        if (null == mUser) {
            mUser = SharedPreferencesHelper.getInstance(MyApplication.Companion.getApplicaton()).
                    getObject(User.class);
        }
        return mUser;
    }

    public static Location mLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        MyUncaughtExceptionHandler.with(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        ScreenUtil.initScreen(this);
    }
}
