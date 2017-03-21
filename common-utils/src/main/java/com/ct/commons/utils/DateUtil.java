package com.ct.commons.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * 
 * @author xhl
 *
 */
public class DateUtil {
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateFormatzh = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat datetmFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 获取当前时间戳---long型
	 * 
	 * @return
	 */
	public static long getCurrentMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得日期前几天
	 *
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return Date
	 */
	public static Date getDateBefore(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 格式化日期时间
	 *
	 * @param date
	 *            日期
	 * @param pattern
	 *            pattern
	 * @return Datetime
	 */
	public static String formatDatetime(Date date, String pattern) {
		SimpleDateFormat customFormat = (SimpleDateFormat) dateFormat.clone();
		customFormat.applyPattern(pattern);
		return customFormat.format(date);
	}

	/**
	 * 获得当前时间
	 *
	 * @param date
	 *            日期
	 * @return Time HH:mm:ss
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 格式化日期
	 *
	 * @param date
	 *            日期
	 * @return Date
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * 格式化日期，中文
	 *
	 * @param date
	 *            日期
	 * @return Date
	 */
	public static String formatZHDate(Date date) {
		return dateFormatzh.format(date);
	}

	/**
	 * 比较日期
	 *
	 * @param String
	 *            startDate , String endDate
	 * 
	 * @return boolean true:表示 开始日期大于结束日期 、false:表示 开始日期小于等于结束日期
	 */
	public static boolean comparDate(String startDate, String endDate) {
		boolean flag = false;
		try {
			Date start_date = datetimeFormat.parse(startDate);
			Date end_date = datetimeFormat.parse(endDate);
			flag = start_date.after(end_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 获取当前时间戳---long型
	 * 
	 * @return
	 */
	public static long dateTime(String date) {
		try {
			return datetimeFormat.parse(date).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前日期 不包括的时分秒
	 * 
	 * @return String
	 */
	public static String currentDate() {
		Date currDate = new Date();
		return dateFormat.format(currDate);
	}

	// 获得当天0点时间
	public static int getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	// 获得当天24点时间
	public static int getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}
	public static Date getYesterDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 获取当天的前[-1]或后[1]多少天的时间
	 * @param af
	 * @return
	 */
	public static Date getDate(int af) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, af); 
		Date date = calendar.getTime();
	    return date;
	}
	/**
	 * 获取当天时间的第一毫秒
	 * 【2016-10-12 00:00:00.001】
	 * @return
	 */
	public static Date getDayBegin() {
		  Calendar cal = Calendar.getInstance();
		  cal.set(Calendar.HOUR_OF_DAY, 0);
		  cal.set(Calendar.SECOND, 0);
		  cal.set(Calendar.MINUTE, 0);
		  cal.set(Calendar.MILLISECOND, 001);
		  return cal.getTime();
	}
	
    /**
     * 将时间戳转换为时间字符串【yyyy-MM-dd HH:mm:ss】
     * @param s
     * @return
     */
    public static String stampToDate(String s){
        String res;
        Date date = new Date(Long.parseLong(s) * 1000L);
        res = datetimeFormat.format(date);
        return res;
    }
	
	public static void main(String[] args) {		
		System.out.println(DateUtil.getDate(-2));
	}

}
