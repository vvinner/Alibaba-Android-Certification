package com.porster.gift.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;



/**
 *
 * <p>
 * 时间格式转换工具类
 * </p>
 *
 * @author Administrator Sep 1, 2010
 */
public class DateFormatUtils {

	/*********** 日期格式化字符串格式,value:yyyy-MM-dd HH:mm:ss ***********/
	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将Date转换成String类型,使用默认的format
	 *
	 * @param date
	 *            时间
	 * @return String
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		String strDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT,Locale.getDefault());
		try {
			strDate = sdf.format(date);
		} catch (Exception ex) {
			strDate = null;
		}
		return strDate;
	}

	/**
	 * 将Date转换成String类型
	 *
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		String strDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
		try {
			strDate = sdf.format(date);
		} catch (Exception ex) {
			strDate = null;
		}
		return strDate;
	}

	/**
	 * 将Date转换成String类型,使用默认的format
	 *
	 * @param millis
	 *            时间
	 * @return String
	 */
	public static String formatDate(long millis) {
		Date date = new Date(millis);
		return formatDate(date);
	}

	/**
	 * 将Date转换成String类型
	 *
	 * @param millis
	 *            时间
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String formatDate(long millis, String format) {
		Date date = new Date(millis);
		return formatDate(date, format);
	}

	/**
	 * 将String转换成Date类型
	 *
	 * @param strDate
	 *            String 格式的字符串时间
	 * @param format
	 *            String 格式
	 * @return String
	 */
	public static Date formatStrToDate(String strDate, String format) {
		if (strDate == null || strDate.equals("")) {
			return null;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
		try {
			date = sdf.parse(strDate);
		} catch (Exception ex) {
			date = null;
		}
		return date;
	}

	public static String formatStrToStr(String strDate, String format) {
		if (strDate == null || strDate.equals("")) {
			return "";
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
		try {
			date = sdf.parse(strDate);
		} catch (Exception ex) {
			date = null;
			return strDate;
		}
		return formatDate(date, format);

	}

	/**
	 *
	 * @param strDate
	 * @param oldFormat
	 * @param newFormat
	 * @return
	 */
	public static String formatDateStr(String strDate, String oldFormat,
			String newFormat) {
		if (strDate == null || strDate.equals("")) {
			return null;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat,Locale.getDefault());
		try {
			date = sdf.parse(strDate);
			sdf.applyPattern(newFormat);
		} catch (Exception ex) {
			date = null;
			return strDate;
		}
		return sdf.format(date);

	}

	/**
	 * 日期加天数得到新的日期
	 *
	 * @param d
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static Date addDate(Date d, long day) {
		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return new Date(time);
	}

	public static String addDays(String date, long day, String returnFormat) {
		Date d = formatStrToDate(date, DATEFORMAT);
		if (d == null)
			return "";
		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return formatDate(time, returnFormat);
	}

	/**
	 * 此日期所表示的一周中的某一天。
	 *
	 * @param date
	 * @param format
	 * @return String (format+day)
	 */
	@SuppressWarnings("deprecation")
	public static String formatDateToWeek(Date date, String format) {
		if (date == null) {
			return null;
		}
		if (format == null || format.equals("")) {
			format = "星期";
		}
		int day = date.getDay();
		String week = format;
		switch (day) {
		case 1:
			week += "一";
			break;
		case 2:
			week += "二";
			break;
		case 3:
			week += "三";
			break;
		case 4:
			week += "四";
			break;
		case 5:
			week += "五";
			break;
		case 6:
			week += "六";
			break;
		case 0:
			week += "日";
			break;
		default:
			break;
		}
		return week;
	}

	/**
	 * 获取某个日期是星期几
	 *
	 * @param date
	 * @return 返回结果如:"周日"
	 */
	@SuppressWarnings("deprecation")
	public static String getWeek(Date date, int location) {
		if (date != null) {
			int day = date.getDay();
			String Sun = "周日", Mon = "周一", Tue = "周二", Wed = "周三", Thu = "周四", Fri = "周五", Sat = "周六";
			if (location == 1) {
				Sun = "Sun";
				Mon = "Mon";
				Tue = "Tue";
				Wed = "Wed";
				Thu = "Thu";
				Fri = "Fri";
				Sat = "Sat";
			}
			switch (day) {
			case 0:
				return Sun;
			case 1:
				return Mon;
			case 2:
				return Tue;
			case 3:
				return Wed;
			case 4:
				return Thu;
			case 5:
				return Fri;
			case 6:
				return Sat;
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取某个日期是星期几
	 *
	 * @param date
	 * @return 返回结果如:"周日"
	 */
	public static String getWeek(Date date) {
		return getWeek(date, 1);
	}

	/**
	 *
	 * @Description todo
	 * @Date 2013-4-25
	 * @param strDate
	 * @param location
	 *            语言类型,1:zh,2:en
	 * @return String
	 */
	public static String formatStrToWeek(String strDate, int location) {
		Date date = formatStrToDate(strDate, "yyyy-MM-dd");
		return getWeek(date, location);
	}

	/**
	 * 此日期所表示的一周中的某一天。
	 *
	 * @param strDate
	 * @return String (format+day)
	 */
	@Deprecated
	public static String formatStrToWeek(String strDate) {
		Date date = formatStrToDate(strDate, "yyyy-MM-dd");
		return getWeek(date);
	}

	/**
	 * 时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd",Locale.getDefault());
		Date beginDate;
		Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 *
	 * @param startTime
	 *            开始时间
	 * @param finishTime
	 *            结束时间
	 * @param fomater
	 *            当前时间格式
	 * @return
	 */
	public static String formatDate(String startTime, String finishTime,
			String fomater) {
		return formatDate(startTime, finishTime, fomater, 1);
	}

	/**
	 * @param startTime
	 *            开始时间
	 * @param finishTime
	 *            结束时间
	 * @param fomater
	 *            当前时间格式
	 * @param location
	 *            国际 1：中文 2：英文
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String formatDate(String startTime, String finishTime,
			String fomater, int location) {
		if (isEmpty(startTime) || isEmpty(finishTime)) {
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		if (fomater == null || fomater.length() == 0) {
			fomater = DATEFORMAT;
		}
		if (startTime.length() < fomater.length()
				&& finishTime.length() < fomater.length()) {
			fomater = "yyyy-MM-dd";
		}
		String year = "年";
		String month = "月";
		String day = "日";
		String separ="-";
		if (location != 1) {
			year = "-";
			month = "-";
			day = " ";
			separ="~";
		}
		String zero = "00:00", zeroTime = " HH:mm";
		String stime = startTime.substring(startTime.length() - 5,
				startTime.length());
		String etime = startTime.substring(finishTime.length() - 5,
				finishTime.length());
		if (!isEmpty(stime) && !isEmpty(etime) && stime.equals(etime)
				&& stime.equals(zero)) {
			zeroTime = "";
		}
		Date date1 = DateFormatUtils.formatStrToDate(startTime, fomater);
		Date date2 = DateFormatUtils.formatStrToDate(finishTime, fomater);
		if (date1 != null && date2 != null) {
			if (date1.getYear() == date2.getYear()) {
				if (date1.getMonth() == date2.getMonth()
						&& date1.getDay() == date2.getDay()) {
					// 09月12日 13:00 MM月dd日 HH:mm
					sb.append(DateFormatUtils.formatDateStr(startTime, fomater,
							"MM" + month + "dd" + day + " HH:mm"));
					sb.append(separ);
					sb.append(DateFormatUtils.formatDateStr(finishTime,
							fomater, "HH:mm"));
				} else {// 跨天、跨月的09月12日 13:00-09月13日14:00
					String sf = "MM" + month + "dd" + day + zeroTime;
					sb.append(DateFormatUtils.formatDateStr(startTime, fomater,
							sf));
					sb.append(separ);
					sb.append(DateFormatUtils.formatDateStr(finishTime,
							fomater, sf));
				}
			} else {// 2012年12月31日 13:00-2013年01月01日14:00 yyyy年MM月dd日 HH:mm
				String sf = "yyyy" + year + "MM" + month + "dd" + day
						+ zeroTime;
				sb.append(DateFormatUtils.formatDateStr(startTime, fomater, sf));
				sb.append(separ);
				sb.append(DateFormatUtils
						.formatDateStr(finishTime, fomater, sf));
			}
		}
		return new String(sb);
	}

	/**
	 *
	 * @param startTime
	 *            开始时间
	 * @param finishTime
	 *            结束时间
	 * @param formater
	 *            当前时间格式
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String formatDateTime(String startTime, String finishTime,
			String formater) {
		if (isEmpty(startTime) || isEmpty(finishTime)) {
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		if (formater == null || formater.length() == 0) {
			formater = DATEFORMAT;
		}
		if (startTime.length() < formater.length()
				&& finishTime.length() < formater.length()) {
			formater = "yyyy-MM-dd";
		}
		String zero = "00:00", zeroTime = " HH:mm";
		String stime = startTime.substring(startTime.length() - 5,
				startTime.length());
		String etime = startTime.substring(finishTime.length() - 5,
				finishTime.length());
		if (!isEmpty(stime) && !isEmpty(etime) && stime.equals(etime)
				&& stime.equals(zero)) {
			zeroTime = "";
		}
		Date date1 = DateFormatUtils.formatStrToDate(startTime, formater);
		Date date2 = DateFormatUtils.formatStrToDate(finishTime, formater);
		if (date1 != null && date2 != null) {
			if (date1.getYear() == date2.getYear()) {
				if (date1.getMonth() == date2.getMonth()
						&& date1.getDay() == date2.getDay()) {
					// 09月12日 13:00 MM月dd日 HH:mm
					sb.append(DateFormatUtils.formatDateStr(startTime,
							formater, "yyyy年MM月dd日 HH:mm"));
					sb.append("-");
					sb.append(DateFormatUtils.formatDateStr(finishTime,
							formater, " HH:mm"));
				} else {// 跨天、跨月的09月12日 13:00-09月13日14:00
						// String sf="yyyy年MM月dd日"+zeroTime;
					sb.append(DateFormatUtils.formatDateStr(startTime,
							formater, "yyyy年MM月dd日"));
					sb.append("-");
					sb.append(DateFormatUtils.formatDateStr(finishTime,
							formater, " dd日"));
				}
			} else {// 2012年12月31日 13:00-2013年01月01日14:00 yyyy年MM月dd日 HH:mm
				String sf = "yyyy年MM月dd日" + zeroTime;
				sb.append(DateFormatUtils
						.formatDateStr(startTime, formater, sf));
				sb.append("-");
				sb.append(DateFormatUtils.formatDateStr(finishTime, formater,
						sf));
			}
		}
		return new String(sb);
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @return true: 报名已截至,false: 报名未截至
	 */
	public static boolean confineApply(String startTime, String endTime) {
		// Logger.v("DateFormatUtils confineApply","startTime="+startTime+" endTime="+endTime);
		if (isEmpty(startTime) && isEmpty(endTime)) {
			return false;
		}
		String applyTimeFormate = "yyyy-MM-dd HH:mm:ss";
		Date applyStartDate = DateFormatUtils.formatStrToDate(startTime,
				applyTimeFormate);
		Date applyEndDate = DateFormatUtils.formatStrToDate(endTime,
				applyTimeFormate);
		String temp = DateFormatUtils.formatDate(System.currentTimeMillis(),
				applyTimeFormate);
		Date currentTime = DateFormatUtils.formatStrToDate(temp,
				applyTimeFormate);
		long startDate = -1L, finishDate = -1L;
		if (applyStartDate != null) {// 不空则当前时间与开始时间差,空则不限制
			startDate = currentTime.getTime() - applyStartDate.getTime();
		} else {
			startDate = 1L;
		}
		if (applyEndDate != null) {// 不空则结束时间与当前时间差,空则不限制
			finishDate = applyEndDate.getTime() - currentTime.getTime();
		} else {
			finishDate = 1L;
		}
		// 是否再这个时间段内
		return startDate >= 0 && finishDate >= 0 ? false : true;
	}

	static boolean isEmpty(String str) {
		return str == null || str.length() == 0 ? true : false;
	}

	/**
	 *
	 * @Description 格式化时间 :1小时内显示分钟，8小时以上显示具体时间(日期+时间)
	 * @Date 2013-4-11
	 * @param createDate
	 *            yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String formatTime(String createDate) {
		// 时间显示策略---1小时内显示分钟，8小时以上显示具体时间(日期+时间)；
		long d1 = fromDateStringToLong(createDate);
		long now = fromDateStringToLong(DateFormatUtils.formatDate(new Date()));
		long ss = (now - d1) / (1000); // 共计秒数
		int MM = (int) ss / 60; // 共计分钟数
		int hh = (int) ss / 3600; // 共计小时数
		if (hh < 1) {
			if (MM < 1) {
				return "1分钟前";
			}
			return MM + "分钟前";
		} else if (hh >= 8) {
			return DateFormatUtils.formatDateStr(createDate,
					"yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm");
		} else {
			return hh + "小时前";
		}
	}
	/**
	 * 显示时间,n分钟前,n小时前,n天前,超过7天显示标准格式
	 * @param createDate
	 * @return
	 */
	public static String showTime(String createDate){
		return showTime(createDate, "");
	}
	/**
	 * 显示时间,n分钟前,n小时前,n天前,超过7天显示标准格式
	 * @param createDate
	 * @param format	时间格式
	 * @return
	 */
	public static String showTime(String createDate,String format) {
		long d1 = fromDateStringToLong(createDate);
		long now = fromDateStringToLong(DateFormatUtils.formatDate(new Date()));
		long ss = (now - d1) / (1000); // 共计秒数
		int MM = (int) ss / 60; // 共计分钟数
		int hh = (int) ss / 3600; // 共计小时数
		if (hh < 1) {
			if (MM < 1) {
				return "1分钟前";
			}
			return MM + "分钟前";
		} else if (hh < 168) {//7天内
			if(hh < 24){//一天内
				return hh + "小时前";
			}else if(hh <  48){//昨天
				return "昨天 "+DateFormatUtils.formatDateStr(createDate,"yyyy-MM-dd HH:mm:ss", "HH:mm");
			}else if(hh <  72){//前天
				return "前天 "+DateFormatUtils.formatDateStr(createDate,"yyyy-MM-dd HH:mm:ss", "HH:mm");
			}else {
				return (int)(hh/24)+"天前 "+DateFormatUtils.formatDateStr(createDate,"yyyy-MM-dd HH:mm:ss", "HH:mm");
			}
		} else {
			if(TextUtils.isEmpty(format))format="yyyy-MM-dd HH:mm:ss";
			return DateFormatUtils.formatDateStr(createDate,"yyyy-MM-dd HH:mm:ss",format);
		}
	}

	/**
	 *
	 * @Description 计算时间毫秒
	 * @Date 2013-4-11
	 * @param dateTime
	 *            yyyy-mm-dd hh:mm:ss
	 * @return long 毫秒数
	 */
	public static long fromDateStringToLong(String dateTime) { // 此方法计算时间毫秒
		Date date = null; // 定义时间类型
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		try {
			date = inputFormat.parse(dateTime); // 将字符型转换成日期型
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * @Description todo
	 * @Date 2013-7-30
	 * @param dateTime
	 * @param en_fromate
	 *            MMM dd, yyyy
	 * @param srcFormate
	 * @return
	 * @return String
	 */
	public static String formateDateEn(String dateTime, String en_fromate,
			String srcFormate) {
		Date date = null;
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat(srcFormate,Locale.getDefault());
			date = inputFormat.parse(dateTime);
			en_fromate=en_fromate==null?"MMM dd, yyyy":en_fromate;
			DateFormat df1 = new SimpleDateFormat(en_fromate, Locale.ENGLISH);
			return df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/***
	 * @Date 2013-8-1
	 * @param dateTime
	 * @param format 要转换的新格式
	 * @param srcFormate 当前时间格式
	 * @param locale 1:英文,其他中文
	 * @return String
	 */
	public static String formateDateLoc(String dateTime, String format,
			String srcFormate,int locale) {
		Date date = null;
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat(srcFormate,Locale.getDefault());
			date = inputFormat.parse(dateTime);
			Locale L=Locale.CHINA;
			if(locale==1){
				L=Locale.ENGLISH;
			}
			DateFormat df1 = new SimpleDateFormat(format, L);
			return df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

}
