package com.kasao.qintaiframework.until;

import android.text.TextUtils;
import android.widget.Toast;

import com.kasao.qintaiframework.base.MyApplication;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:17
 */

public class ToastUtil {


    private static void showBaseStr(String var) {
        if (TextUtils.isEmpty(var)) {
            return;
        }
        Toast.makeText(MyApplication.Companion.getApplicaton(), var, Toast.LENGTH_SHORT).show();
    }

    public static void showAlter(String var) {
        showBaseStr(var);
    }
}
