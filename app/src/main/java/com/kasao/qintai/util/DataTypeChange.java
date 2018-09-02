package com.kasao.qintai.util;

import android.text.TextUtils;

/**
 * 作者 Created by suochunming
 * 日期 on 2017/11/7.
 * 简述:数据转化处理类
 */

public class DataTypeChange {
    public static Float stringToDounble(String data){
        if(!TextUtils.isEmpty(data)&&data.contains(".")){
            return Float.valueOf(data);
        }
        return 0.0f;
    }

    // 数据大于10000的时候进行处理
    public static String getF10000(float f1){
        String str = "";
        double d1 = 0.0d;
        if(f1>10000.0f&&f1<100000000.0d){// 数据在10万到1亿之间的数据
            d1 = f1/10000.0f;
            str = dealFianldata(DataTypeChange.save1bit(d1))+"万";
        }else if(f1>100000000.0d){
            d1 = (double)f1/100000000.0d;
            str =dealFianldata(DataTypeChange.save1bit(d1))+"亿";
        }else{
            d1 = f1;
            str = dealFianldata(DataTypeChange.save1bit(d1))+"";
        }
        return str;
    }

    // 保留2位有效数字
    public static double save2bit(double t1){
        double result = (double) (int) ((t1 + 0.005) * 100.0) / 100.0;
        return result;
    }
    // 保留1位有效数字
    public static double save1bit(double t1){
        double result = (double) (int) ((t1 + 0.005) * 10.0) / 10.0;
        return result;
    }
    // 数值最后处理
    public static String dealFianldata(double t){
        if(String.valueOf(t).contains(".0")){
            return String.valueOf((int)t);
        }
        return String.valueOf(t);
    }
}
