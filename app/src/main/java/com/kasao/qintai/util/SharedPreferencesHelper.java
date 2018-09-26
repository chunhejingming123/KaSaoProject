package com.kasao.qintai.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.kasao.qintaiframework.base.MyApplication;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.LogUtil;

import java.util.Map;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:13
 */

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper mHelp;
    private SharedPreferences sharedPreferences;
    /*
     * 保存手机里面的名字
     */private SharedPreferences.Editor editor;

    private SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("kasao",
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (null == mHelp) {
            mHelp = new SharedPreferencesHelper(context);
        }
        return mHelp;
    }

    public SharedPreferencesHelper(Context context, String FILE_NAME) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 存储
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 获取保存的数据
     */
    public Object getSharedPreference(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    public <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        String json = getSharedPreference(key, "").toString();
        LogUtil.e("----key1=" + key + "----json2=" + json);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return GsonUtil.Companion.GsonToBean(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void putObject(Object object) {
        String key = getKey(object.getClass());

        String json = GsonUtil.Companion.GsonString(object);
        LogUtil.e("--------key--" + key + "--json=" + json);
        put(key, json);
    }

    public void removeObject(Context context, Class<?> clazz) {
        remove(getKey(clazz));
    }

    public String getKey(Class<?> clazz) {
        return clazz.getName();
    }


    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}
