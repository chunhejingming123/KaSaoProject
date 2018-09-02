package com.kasao.qintai.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: DateUtils
 * @说明:时间工具类
 * @author dxf
 * @date 2015-7-15 上午10:12:12
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class DateUtils {

	public static final String FORMAT_YYYYMMDD_HH24 = "yyyyMMddHHmmss";
	public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String FORMAT_YYMMdd = "yyMMdd";
	public static final String FORMAT_YYYYMMDD_HZ = "yyyy年MM月dd日";
	public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_TIMESTAMP_I = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_HHMMSS = "HH:mm:ss";
	public static final String FORMAT_HHMM = "HH:mm";

	
	public static String getNowDate(String formate) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
		return dateFormat.format(date);
	}

	public static String getFormatDate(Date date, String formate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
		return dateFormat.format(date);
	}
	public static String getGapDate(long time, String formate) {
		Date date = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
		return dateFormat.format(date);
	}
	/**
	 * 得到当前时间
	 * 
	 * @param dateFormat
	 *            时间格式
	 * @return 转换后的时间格式
	 */
	public static String getStringToday(String dateFormat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将字符串型日期转换成日期
	 * 
	 * @param dateStr
	 *            字符串型日期
	 * @param dateFormat
	 *            日期格式
	 * @return
	 */
	public static Date stringToDate(String dateStr, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String dateToString(Date date, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(date);
	}

	/**
	 * 两个时间点的间隔时长（分钟）
	 * 
	 * @param before
	 *            开始时间
	 * @param after
	 *            结束时间
	 * @return 两个时间点的间隔时长（分钟）
	 */
	public static long compareMin(Date before, Date after) {
		if (before == null || after == null) {
			return 0l;
		}
		long dif = 0;
		if (after.getTime() >= before.getTime()) {
			dif = after.getTime() - before.getTime();
		} else if (after.getTime() < before.getTime()) {
			dif = after.getTime() + 86400000 - before.getTime();
		}
		dif = Math.abs(dif);
		return dif / 60000;
	}

	/**
	 * 获取指定时间间隔分钟后的时间
	 * 
	 * @param date
	 *            指定的时间
	 * @param min
	 *            间隔分钟数
	 * @return 间隔分钟数后的时间
	 */
	public static Date addMinutes(Date date, int min) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}

	/**
	 * 根据时间返回指定术语
	 * 
	 * @param hourday
	 *            小时
	 * @return
	 */
	public static String showTimeView(int hourday) {
		if (hourday >= 22 && hourday <= 24) {
			return "晚上";
		} else if (hourday >= 0 && hourday <= 6) {
			return "凌晨";
		} else if (hourday > 6 && hourday <= 12) {
			return "上午";
		} else if (hourday > 12 && hourday < 22) {
			return "下午";
		}
		return null;
	}

	/**
	 * 
	 * @Title: daysBetween
	 * @说 明: 计算两个日期之间相差的天数
	 * @参 数: @param date1
	 * @参 数: @param date2
	 * @参 数: @return
	 * @return int 返回类型
	 * @throws
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 比较时间大小
	 * @param time1
	 * @param time2
	 * @param format
	 * @return
	 */
	public static int TimeComparison(String time1, String time2, String format){
		java.text.DateFormat df = new java.text.SimpleDateFormat(format);
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(time1));
			c2.setTime(df.parse(time2));
		} catch (java.text.ParseException e) {
			return 0;
		}
		return c1.compareTo(c2);
	}


	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}

	public static String getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case 1:
			return "星期天";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";
		default:
			return "";
		}
	}

	public static Date getDeta(String data){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			try {
				date = dateFormat.parse(data);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (android.net.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDeta(String data, String formart){
		SimpleDateFormat dateFormat = new SimpleDateFormat(formart);
		Date date = null;
		try {
			try {
				date = dateFormat.parse(data);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (android.net.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
