package com.bbd.saas.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author liyanlei
 */
public class FormatDate {

	private static transient int gregorianCutoverYear = 1582;

	/** 闰年中每月天数 */
	private static final int[] DAYS_P_MONTH_LY = { 31, 29, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };

	/** 非闰年中每月天数 */
	private static final int[] DAYS_P_MONTH_CY = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };

	/** 代表数组里的年、月、日 */
	private static final int Y = 0, M = 1, D = 2;

	
	/**
	 * 是否是闰年  ,平年28天.闰年29天.
	 * 闰年的条件是符合下面二者之一：(1)年份能被4整除，但不能被100整除；(2)能被400整除。
	 **/
	public static boolean isLeap(String yearstr) {
		int year = Integer.parseInt(yearstr);
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
			return true;
		}
		return false;
	}
	/**
	 * 获取当前日期格式的天--字符串
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDate(String format) throws Exception {
		Date current = new Date();
		String currentStr = dateToString(current, format);
		return currentStr;
	}
	/**
	 * 获取今天开始的时间
	 * @return
	 * @throws Exception
	 */
	public static Long getToday() throws Exception{
		Date d = new Date();
		String d2 = FormatDate.dateToString(d, "yyyy-MM-dd");
		Date d3 = FormatDate.stringToDate(d2, "yyyy-MM-dd");
		long s3 = d3.getTime();
		return s3;
	}
	/**
	 * 将字符串格式的日期（yyyy-MM-dd）转换成日期格式的日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date stringToOnlyDate(String date) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 将字符串格式的日期（yyyy-MM）转换成日期格式的日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDateWithoutDay(String date) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 将字符串格式的日期（yyyy-MM）转换成日期格式的日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getDateYearMonth(String date) throws Exception {
		try {
			Date day = stringToDateWithoutDay(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			return sdf.format(day);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * @param Date
	 * @author String 日期转换成字符串 格式[yyyy/MM]
	 **/
	public static String dateToStringWithOutDay(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		if (date == null)
			return "";
		return df.format(date);
	}
	/**
	 * 将字符串格式的日期（yyyy-MM-dd HH:mm:ss）转换成日期格式的日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String date) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN_YMDT);
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 将字符串格式的日期（yyyy）转换成日期格式的日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDateOnlyYear(String date) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 按日期格式将字符串日期转换成日期格式日期
	 *
	 * @param date
	 * @param dateModel
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String date, String dateModel)
			throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(dateModel);
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @param Date
	 * @author String 日期转换成字符串 格式[yyyy/MM/dd HH:mm:ss]
	 **/
	public static String dateToString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null)
			return "";
		return df.format(date);
	}
	/**
	 * @param Date
	 * @author String
	 **/
	public static String dateToStringNoPattern(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		if (date == null)
			return "";
		return df.format(date);
	}
	/**
	 * @param Date
	 * @author String 日期转换成字符串 格式[yyyy]
	 **/
	public static String dateToStringOnlyYear(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy");
		if (date == null)
			return "";
		return df.format(date);
	}
	/**
	 * 将日期按指定格式转换成字符串
	 *
	 * @param date
	 * @param dateModel
	 *            时间格式
	 * @return
	 */
	public static String dateToString(Date date, String dateModel) {
		DateFormat df = new SimpleDateFormat(dateModel);
		if (date == null)
			return "";
		return df.format(date);
	}

	public static String dateToStringWithoutTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null)
			return "";
		return df.format(date);
	}

	public static String dateToStringTimeTwo(Date date) {
		DateFormat df = new SimpleDateFormat("MM-dd");
		if (date == null)
			return "";
		return df.format(date);
	}

	public static String dateToStringTime(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd");
		if (date == null)
			return "";
		return df.format(date);
	}
	
	public static String dateStrToStringTimeTwo(String date) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfs = new SimpleDateFormat("MM-dd");
		String resultDate = "";
		try {
		Date tdate = df.parse(date);
		resultDate = dfs.format(tdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultDate;

	}


	/**
	 * 获取当期日期是月的第几天
	 * @param date
	 * @return
	 */
	public static String getDateDay(String date) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date tdate = df.parse(date);
			calendar.setTime(tdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

	}

	/**
	 * 获取当前日期所在月 共有几天
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int getMonthDay(String date) throws Exception{
		Calendar calendar = convert(stringToDate(date, "yyyy-MM-dd"));//获取日历对象
		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		calendar.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = calendar.get(Calendar.DATE);
		return maxDate;
	}
	/**
	 * @param String
	 * @author String 得到当前日期前一个月的日期 格式[yyyy/MM/dd]
	 **/
	public static String getMonthReduce(String datetime) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
			Calendar calendar = Calendar.getInstance();// 日历对象
			Date date = sdf.parse(datetime);
			calendar.setTime(date);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
			datetime = sdf.format(calendar.getTime());// 输出格式
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datetime;
	}


	/**
	 * @param datetime
	 * @author String 得到当前日期前一个月的日期 格式[yyyy/MM/dd]
	 **/
	public static String getMonthRed(String datetime) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化对象
			Calendar calendar = Calendar.getInstance();// 日历对象
			Date date = sdf.parse(datetime);
			calendar.setTime(date);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			datetime = sdf.format(calendar.getTime());// 输出格式
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datetime;
	}


	/**
	 * @param Date
	 * @author String 日期转换成字符串 格式[yyyy年MM月dd日]
	 **/
	public static String dateToStringYMD(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		if (date == null)
			return "";
		return df.format(date);
	}
	
	/**
	 * 
	 * @Title: ymdDateStrToStringMD 
	 * @author zhangmeijia
	 * @param date
	 * @return    
	 * @date 2015-12-14 下午7:24:12 
	 * @version 1.0
	 * @throws Exception 
	 */
	public static String ymdDateStrToStringMD(String date) throws Exception {
		
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df = new SimpleDateFormat("MM月dd日");
		if (date == null) {
			return "";
		} 
		Date datet = dfs.parse(date);
		return df.format(datet);
	}

	/**
	 * @param Date
	 * @author Timestamp Date转换成Timestamp类型 格式[yyyy/MM/dd HH:mm:ss]
	 **/
	public static Timestamp dateToTimestamp(Date date) {
		if (date == null)
			return null;
		return Timestamp.valueOf(FormatDate.dateToString(date));
	}

	public static Date stringToDateWithoutTime(String date) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 将代表日期的字符串分割为代表年月日的整形数组
	 *
	 * @param date
	 * @return
	 */
	public static int[] splitYMD(String date) {
		date = date.replace("-", "");
		int[] ymd = { 0, 0, 0 };
		ymd[Y] = Integer.parseInt(date.substring(0, 4));
		ymd[M] = Integer.parseInt(date.substring(4, 6));
		ymd[D] = Integer.parseInt(date.substring(6, 8));
		return ymd;
	}

	/**
	 * 检查传入的参数代表的年份是否为闰年
	 *
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return year >= gregorianCutoverYear ? ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))
				: (year % 4 == 0);
	}

	/**
	 * 日期加1天
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private static int[] addOneDay(int year, int month, int day) {
		if (isLeapYear(year)) {
			day++;
			if (day > DAYS_P_MONTH_LY[month - 1]) {
				month++;
				if (month > 12) {
					year++;
					month = 1;
				}
				day = 1;
			}
		} else {
			day++;
			if (day > DAYS_P_MONTH_CY[month - 1]) {
				month++;
				if (month > 12) {
					year++;
					month = 1;
				}
				day = 1;
			}
		}
		int[] ymd = { year, month, day };
		return ymd;
	}

	/**
	 * 将不足两位的月份或日期补足为两位
	 *
	 * @param decimal
	 * @return
	 */
	public static String formatMonthDay(int decimal) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(decimal);
	}

	/**
	 * 将不足四位的年份补足为四位
	 *
	 * @param decimal
	 * @return
	 */
	public static String formatYear(int decimal) {
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(decimal);
	}

	/**
	 * 计算两个日期之间相隔的天数
	 *
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static long countDay(String begin, String end) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate, endDate;
		long day = 0;
		try {
			beginDate = format.parse(begin);
			endDate = format.parse(end);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 以循环的方式计算日期
	 *
	 * @param beginDate
	 *            endDate
	 * @param days
	 * @return
	 */
	public static List<String> getEveryday(String beginDate, String endDate) {
		long days = countDay(beginDate, endDate);
		int[] ymd = splitYMD(beginDate);
		List<String> everyDays = new ArrayList<String>();
		everyDays.add(beginDate);
		for (int i = 0; i < days; i++) {
			ymd = addOneDay(ymd[Y], ymd[M], ymd[D]);
			everyDays.add(formatYear(ymd[Y]) + "-" + formatMonthDay(ymd[M])
					+ "-" + formatMonthDay(ymd[D]));
		}
		return everyDays;
	}

	/**
	 * 时间比较 比较date是否在stime(开始时间)和etime(结束时间)之间 若是返回true，否则返回false
	 *
	 * @return boolean
	 */
	public static boolean compareDate(Date date, Date stime, Date etime) {
		if (date == null || stime == null || etime == null)
			return false;
		long now = date.getTime();
		long st = stime.getTime();
		long et = etime.getTime();
		if (now >= st && now <= et) {
			return true;
		}
		return false;
	}

	// 返回当前日期的星期
	public static int getWeekOfDate() {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); // 设置日期格式
		GregorianCalendar nd = new GregorianCalendar();
		nd.getTime();
		return nd.get(GregorianCalendar.DAY_OF_WEEK);

	}

	// 返回当前日期的星期
	public static int getWeekOfDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// System.out.println("DAY_OF_WEEK: " +
		// calendar.get(Calendar.DAY_OF_WEEK));
		// System.out.println("DAY_OF_WEEK: " +
		// calendar.get(Calendar.DAY_OF_MONTH));
		return calendar.get(Calendar.DAY_OF_WEEK);

	}

	public static String getWeekByDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String week = "";
		int number = calendar.get(Calendar.DAY_OF_WEEK);
		switch (number) {
			case 1:
				week = "星期日";
				break;
			case 2:
				week = "星期一";
				break;
			case 3:
				week = "星期二";
				break;
			case 4:
				week = "星期三";
				break;
			case 5:
				week = "星期四";
				break;
			case 6:
				week = "星期五";
				break;
			case 7:
				week = "星期六";
				break;
		}
		return week;

	}

	/**
	 * 根据开始时间和结束时间产生以日为单位的数组
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDayStrs(String startDate, String endDate) {
		List<String> list = new ArrayList<String>();
		addNextDay(startDate, endDate, list);
		list.add(endDate);
		return list;
	}

	private static List<String> addNextDay(String startDate, String endDate,
										   List<String> list) {
		if (!startDate.equals(endDate)) {
			list.add(startDate);
			String next = getDayAfterDate(startDate);
			addNextDay(next, endDate, list);
		}
		return list;
	}

	/**
	 * 根据开始时间和结束时间产生以月为单位的数组如（"2014年12月", "2015年02月",
	 * "yyyy年MM月"）-----["2014年12月"、"2015年01月"、"2015年02月"]
	 *
	 * @param startDate
	 * @param endDate
	 * @param dateModel
	 * @return
	 */
	public static List<String> getMonthStrs(String startDate, String endDate,
											String dateModel) {
		List<String> list = new ArrayList<String>();
		addNextMonth(startDate, endDate, dateModel, list);
		return list;

	}

	public static List<String> addNextMonth(String startDate, String endDate,
											String dateModel, List<String> list) {
		list.add(startDate);
		if (!startDate.equals(endDate)) {
			String next = addOneMonth(startDate, dateModel);
			addNextMonth(next, endDate, dateModel, list);
		}
		return list;
	}

	/**
	 * 根据开始时间和结束时间产生以周为单位的数组如（"2014-12-01", "2014-12-22",
	 * "yyyy-MM-dd"）-----["2014-12-01至2014-12-07"
	 * 、"2014-12-08至2014-12-14"、"2014-12-15至2014-12-21"]
	 *
	 * @param startDate
	 *            起始日期（周一）
	 * @param endDate
	 *            结束日期（周一）
	 * @param dateModel
	 *            日期格式
	 * @return
	 */
	public static List<String> getWeekStrs(String startDate, String endDate,
										   String dateModel) {
		List<String> list = new ArrayList<String>();
		addNextWeek(startDate, endDate, dateModel, list);
		return list;
	}

	public static List<String> addNextWeek(String startDate, String endDate,
										   String dateModel, List<String> list) {
		if (!startDate.equals(endDate)) {
			String next = addOneWeek(startDate, dateModel);
			String nextStr = countDay(next, dateModel, -1);
			System.out.println(next);
			list.add(startDate + "至" + nextStr);
			addNextWeek(next, endDate, dateModel, list);
		}
		return list;
	}

	/**
	 * 对字符串日期按指定格式增加一个月后产生下个月的名称
	 *
	 * @param date
	 * @param dateModel
	 * @return
	 */
	public static String addOneMonth(String date, String dateModel) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateModel);
		Date curDate = null;
		Date ret = null;
		try {
			curDate = sdf.parse(date);
			ret = addOneMonth(curDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(ret);
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年3月17日下午2:00:26
	 * Description: 对字符串日期按指定格式增加num个的月后产生num个月的名称
	 * @param date 
	 * @param dateModel
	 * @param num
	 * @return
	 */
	public static String addMonth(String date, String dateModel, int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateModel);
		Date curDate = null;
		Date ret = null;
		try {
			curDate = sdf.parse(date);
			ret = addMonth(curDate, num); // 把日期往后增加一天.整数往后推,负数往前移动
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(ret);
	}

	/**
	 * 对日期增加一个月
	 *
	 * @param date
	 * @return
	 */
	public static Date addOneMonth(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	/**
	 * 对日期增加指定数量的月
	 *
	 * @param date
	 * @return
	 */
	public static Date addMonth(Date date, int num) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, num);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}

	/**
	 * 增加一周
	 *
	 * @param date
	 * @param dateModel
	 * @return
	 */
	public static String addOneWeek(String date, String dateModel) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateModel);
		Date curDate = null;
		Date ret = null;
		try {
			curDate = sdf.parse(date);
			ret = addOneWeek(curDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(ret);
	}

	/**
	 * 增加一周
	 *
	 * @param date
	 * @return
	 */
	public static Date addOneWeek(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static Date addOneDay(Date date) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;

	}

	/**
	 * 增加指定天数
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addDay(Date date,int count) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, count);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;

	}

	/**
	 * 增加指定天数
	 * @param date
	 * @param count
	 * @return
	 */
	public static String addDay(String date,int count) {
		try {
			Calendar calendar = new GregorianCalendar();
			Date stringToDate = stringToDate(date, Constants.DATE_PATTERN_YMD);
			calendar.setTime(stringToDate);
			calendar.add(Calendar.DATE, count);// 把日期往后增加一天.整数往后推,负数往前移动
			Date dated = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			date = dateToString(dated, Constants.DATE_PATTERN_YMD);
			return date;
		} catch (Exception e) {
		}
		return null;

	}
	/**
	 * 增加指定小时
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addHour(Date date,int count) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, count);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 增加指定分钟
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addMinute(Date date,int count) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, count);

		return calendar.getTime();
	}

	/**
	 * 增加一年
	 *
	 * @param date
	 * @return
	 */
	public static Date addYear(Date date,int count) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, count);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}

	/**
	 * 对指定日期，根据日期格式进行计算后返回日期格式字符串
	 *
	 * @param date
	 *            日期
	 * @param dateModel
	 *            日期格式
	 * @param num
	 *            计算的数量
	 * @return
	 */
	public static String countDay(String date, String dateModel, int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateModel);
		Date curDate = null;
		try {
			curDate = sdf.parse(date);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(curDate);
			calendar.add(Calendar.DATE, num);// 把日期往后增加一天.整数往后推,负数往前移动
			curDate = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(curDate);

	}

	/**
	 * 获得指定日期的前一天
	 * @param theDate
	 * @return
	 */
	public static String getDayBeforeDate(String theDate) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(theDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param theDate
	 * @return
	 */
	public static Date getDayBeforeDate(Date date1) {
		String theDate = dateToString(date1);
		Calendar c = Calendar.getInstance();
		Date date = null;
		Date dayBef = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(theDate);
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);

			String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			dayBef = stringToDate(dayBefore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dayBef;
	}
	/**
	 * 获得指定日期的前一天(不包含时分秒)
	 *
	 * @param theDate
	 * @return
	 */
	public static Date getDayBeforeDate_Notime(Date date1) {
		String theDate = dateToString(date1);
		Calendar c = Calendar.getInstance();
		Date date = null;
		Date dayBef = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(theDate);
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);

			String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			dayBef = stringToOnlyDate(dayBefore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dayBef;
	}

	/**
	 * 获得指定日期的后一天
	 *
	 * @param theDate
	 * @return
	 */
	public static String getDayAfterDate(String theDate) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(theDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 查询到上个星期五中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finfFriday(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			// 使用do-while时, 值为星期五
			if (getWeekByDate(date).endsWith("星期四"))
				break;
			list.add(date);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 查询到上个星期五中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<String> findFri(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String next = countDay(curTime, "yyyy-MM-dd", -1);
		Date date = sdf.parse(next);
		List<String> list = new ArrayList<String>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			// 使用do-while时, 值为星期五
			if (getWeekByDate(date).endsWith("星期四"))
				break;
			String dateStr = sdf.format(date);
			list.add(dateStr);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 查询到下个星期一中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finMon(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			if (getWeekByDate(date).endsWith("星期二"))
				break;
			list.add(date);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 如果今天是周一，则查询到上个星期一中间的所有日期包含周一，如果今天非周一，则查找到本周一
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finBeforeMon(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		if (getWeekByDate(date).endsWith("星期一")) {
			String beforeDay = getDayBeforeDate(curTime);
			Date beforeDate = new Date();
			try {
				beforeDate = stringToDateWithoutTime(beforeDay);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Calendar calendar = new GregorianCalendar();
			while (true) {
				// 使用do-while时, 值为星期五
				if (getWeekByDate(beforeDate).endsWith("星期一"))
					break;
				list.add(beforeDate);
				calendar.setTime(beforeDate);
				calendar.add(Calendar.DATE, -1);
				beforeDate = calendar.getTime();
			}
			list.add(beforeDate);
		} else {
			Calendar calendar = new GregorianCalendar();
			while (true) {
				// 使用do-while时, 值为星期五
				if (getWeekByDate(date).endsWith("星期日"))
					break;
				list.add(date);
				calendar.setTime(date);
				calendar.add(Calendar.DATE, -1);
				date = calendar.getTime();
			}
		}
		return list;
	}


	/**
	 * 查询到下个周一
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finafterMon(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		if (getWeekByDate(date).endsWith("星期一")) {
			curTime = getDayAfterDate(curTime);
			try {
				date = stringToDateWithoutTime(curTime);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (getWeekByDate(date).endsWith("星期二")) {
			String afterDay = getDayAfterDate(curTime);
			Date afterDate = new Date();
			try {
				afterDate = stringToDateWithoutTime(afterDay);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Calendar calendar = new GregorianCalendar();
			while (true) {
				// 使用do-while时, 值为星期五
				if (getWeekByDate(afterDate).endsWith("星期二"))
					break;
				list.add(afterDate);
				calendar.setTime(afterDate);
				calendar.add(Calendar.DATE, 1);
				afterDate = calendar.getTime();
			}

		} else {
			Calendar calendar = new GregorianCalendar();
			while (true) {
				// 使用do-while时, 值为星期五
				if (getWeekByDate(date).endsWith("星期二"))
					break;
				list.add(date);
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				date = calendar.getTime();
			}
		}
		return list;
	}

	/**
	 * 如果今天是1号，则查询到上个月的1号，中间的所有日期，包含上月1号，如果今天非1号，则查找到本月1号
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finBeforeMonthFirstDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int num = calendar.get(Calendar.DAY_OF_MONTH);
		if (num == 1) {
			String beforeDay = getDayBeforeDate(curTime);
			try {
				date = stringToDateWithoutTime(beforeDay);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_MONTH);
			if (num == 1) {
				list.add(date);
			}
			if (num == 1)
				break;
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}

	/**
	 * 如果今天是1月1号，则查询到上一年的1月1号，中间的所有日期，包含去年的1月1号，如果今天非1月1号，则查找到本年1月1号
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finBeforeYearFirstDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int num = calendar.get(Calendar.DAY_OF_YEAR);
		if (num == 1) {
			String beforeDay = getDayBeforeDate(curTime);
			try {
				date = stringToDateWithoutTime(beforeDay);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_YEAR);
			if (num == 1) {
				list.add(date);
			}
			if (num == 1)
				break;
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}
	/**
	 * 查询下下一年的一月一号之间的日期
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finNextNextYearFirstDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int num = calendar.get(Calendar.DAY_OF_YEAR);
		if (num == 1) {
			String beforeDay = getDayBeforeDate(curTime);
			try {
				date = stringToDateWithoutTime(beforeDay);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int count = 0;
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_YEAR);
			if (num == 1) {
				count ++;
			}
			if (num == 1 && count == 2){
				break;
			}
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}
	/**
	 * 查询上上一年的最后一天之间的日期
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> findBeforBeforYearLastDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int count = 0;
		int k = 2;
		int num = calendar.get(Calendar.DAY_OF_YEAR);
		if (num == 1) {
			k = 1;
			String beforeDay = getDayBeforeDate(curTime);
			try {
				date = stringToDateWithoutTime(beforeDay);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_YEAR);
			if (num == 1) {
				count ++;
				list.add(date);
			}
			if (num == 1 && count == k)
				break;
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}
	/**
	 * 则查找到下月1号
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finAfterMonthFirstDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int num = calendar.get(Calendar.DAY_OF_MONTH);
		if (num == 1) {
			String afterDay = getDayAfterDate(curTime);
			try {
				date = stringToDateWithoutTime(afterDay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_MONTH);
			if (num == 1) {
				list.add(date);
			}
			if (num == 1)
				break;
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}
	/**
	 * 则查找到下下月1号
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> finNextNextMonthFirstDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int num = calendar.get(Calendar.DAY_OF_MONTH);
		if (num == 1) {
			String afterDay = getDayAfterDate(curTime);
			try {
				date = stringToDateWithoutTime(afterDay);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int count = 0;
		int coun = 2;
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_MONTH);
			if (num == 1) {
				count ++;
			}
			if (num == 1 && coun == count){
				break;
			}
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}
	/**
	 * 则查找到上上月最后一天
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> findBeforBeforMonthLastDay(String curTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(curTime);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int count = 0;
		int k = 2;
		int num = calendar.get(Calendar.DAY_OF_MONTH);
		if (num == 1) {
			k = 1;
			String before = getDayBeforeDate(curTime);
			try {
				date = stringToDateWithoutTime(before);
				list.add(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (true) {
			calendar.setTime(date);
			num = calendar.get(Calendar.DAY_OF_MONTH);
			if (num == 1) {
				count ++;
				list.add(date);
			}
			if (num == 1 && count == k)
				break;
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			list.add(date);
		}
		return list;
	}

	/**
	 * 查询到下个星期一中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<String> findMon(String curTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String next = countDay(curTime, "yyyy-MM-dd", 1);
		Date date = sdf.parse(next);
		List<String> list = new ArrayList<String>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			// 使用do-while时, 值为星期五
			if (getWeekByDate(date).endsWith("星期二"))
				break;
			String dateStr = sdf.format(date);
			list.add(dateStr);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 查询到上上星期日中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> findBeforBeforSun(String curTime) throws ParseException {
		int count = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String befor = countDay(curTime, "yyyy-MM-dd", -1);
		Date date = sdf.parse(befor);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			// 使用do-while时, 值为星期五
			if (getWeekByDate(date).endsWith("星期日")){
				count ++;
			}
			if(count == 2){
				break;
			}
			list.add(date);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 查询到下下个星期一中间的所有日期
	 *
	 * @param curTime
	 * @return
	 * @throws Exception
	 */
	public static List<Date> findNextNextMon(String curTime) throws Exception {
		//计算今天是周几
		int day = getWeekOfDate(stringToDate(curTime,"yyyy-MM-dd"));
		int count = 0;
		int num = 0;
		if(day == 1){
			num = 3;
		}else{
			num = 2;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String next = countDay(curTime, "yyyy-MM-dd", 1);
		Date date = sdf.parse(next);
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		while (true) {
			// 使用do-while时, 值为星期五
			if (getWeekByDate(date).endsWith("星期二")){
				count ++;
				if(num == count){
					break;
				}
			}
			list.add(date);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
		return list;
	}

	/**
	 * 返回当月最后一天的日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DAY_OF_MONTH, rightNow
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		rightNow.set(Calendar.HOUR_OF_DAY, 23);
		rightNow.set(Calendar.MILLISECOND, 59);
		rightNow.set(Calendar.SECOND, 59);
		rightNow.set(Calendar.MINUTE, 59);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return rightNow.getTime();
	}

	/**
	 * 获取某年某月最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static  String getlastDayOfMonth(String str) throws Exception{
		return  getlastDayOfMonth(str, Constants.DATE_PATTERN_YM);
	}
	/**
	 * 获取某年某月最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static  String getlastDayOfMonth(String str, String format) throws Exception{
		Date date=FormatDate.stringToDateWithFormat(str, format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 1);
		calendar.roll(Calendar.DATE, -1);
		Date last=calendar.getTime();
		String lastDay=FormatDate.dateToStringByPara(last, Constants.DATE_PATTERN_YMD);
		//System.out.println("===============last:"+FormatDate.dateToString(last));
		return lastDay;
	}

	/**
	 * 将日期转换为日历
	 *
	 * @param date
	 *            日期
	 * @return 日历
	 */
	private static Calendar convert(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 日期减一天
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String subtractOneDate(String date) throws ParseException {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dft.parse(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		return dft.format(calendar.getTime());
	}

	/**
	 * 根据日期取得相应周(去重)
	 * @author 刘伟
	 * @since 2014-12-30
	 * @param dateList
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getEffWeekStrs(List<String> dateList)
			throws ParseException {
		List<String> result = new ArrayList<String>();
		List<String> distinctDateList = new ArrayList<String>();
		// 判空
		if (dateList == null) {
			return result;
		}
		// 日期所属周去重
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int weekOfYear = -1;
		for (String date : dateList) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			if (weekOfYear == -1
					|| weekOfYear != cal.get(Calendar.WEEK_OF_YEAR)) {
				distinctDateList.add(date);
				weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
			}
		}

		result = distinctWeekList(distinctDateList);
		return result;
	}

	/**
	 *
	 * @param distinctDateList
	 * @return
	 * @throws ParseException
	 */
	private static List<String> distinctWeekList(List<String> distinctDateList) throws ParseException {
		List<String> result = new ArrayList<String>();
		for (String date : distinctDateList) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			if (1 == dayWeek) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			String imptimeBegin = sdf.format(cal.getTime());
			cal.add(Calendar.DATE, 6);
			String imptimeEnd = sdf.format(cal.getTime());
			result.add(imptimeBegin + "至" + imptimeEnd);
		}
		return result;
	}

	/**
	 * 根据日期取得相应月(去重)
	 * @author 刘伟
	 * @since 2014-12-30
	 * @param dateList
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getEffMonthStrs(List<String> dateList)
			throws ParseException {
		List<String> result = new ArrayList<String>();
		List<String> distinctDateList = new ArrayList<String>();
		// 判空
		if (dateList == null) {
			return result;
		}
		// 日期所属周去重
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		int month = -1;
		for (String date : dateList) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			if (month == -1 || month != cal.get(Calendar.MONTH)) {
				distinctDateList.add(date);
				month = cal.get(Calendar.MONTH);
			}
		}

		result = distinctMonthList(distinctDateList);
		return result;
	}

	/**
	 *
	 * @param distinctDateList
	 * @return
	 * @throws ParseException
	 */
	private static List<String> distinctMonthList(List<String> distinctDateList) throws ParseException {
		List<String> result = new ArrayList<String>();
		for (String dateStr : distinctDateList) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月"); // 设置时间格式
			Date date = sdf.parse(dateStr);
			result.add(sdf.format(date));
		}
		return result;
	}

	/**
	 * 根据日期取得相应月(去重)
	 * @author 刘伟
	 * @since 2014-12-30
	 * @param dateList
	 * @return
	 * @throws ParseException
	 */
	public static String getPreviousMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(cal.getTime());
	}
	/**
	 * 根据日期取得当前月
	 * @author 李艳磊
	 * @since 2015-9-14
	 * @return
	 *
	 */
	public static String getCurrentMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(cal.getTime());
	}

	/**
	 * 返回当天最后一刻
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastSecondOfDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.HOUR, 23);
		rightNow.set(Calendar.SECOND, 59);
		rightNow.set(Calendar.MINUTE, 59);
		return rightNow.getTime();
	}

	/**
	 * 日期转换成字符串 格式[yyyy/MM]
	 * @param date
	 * @param pattern
	 * @return 日期
	 * @author 刘伟
	 */
	public static String dateToStringByPara(Date date,String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		if (date == null)
			return "";
		return df.format(date);
	}

	/**
	 * 判断是否当日
	 * @param date
	 * @return
	 * @author 刘伟
	 */
	public static boolean chkToday(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null)
			return false;
		String newDate = df.format(date);
		String today = df.format(new Date());
		return today.equals(newDate);
	}

	public static String convertWeekByDate(Date time) {
		String timeRange = "";
		SimpleDateFormat sdf2=new SimpleDateFormat("MM.dd"); //设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		if(1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		timeRange = sdf2.format(cal.getTime())+"-";
		cal.add(Calendar.DATE, 6);
		timeRange += sdf2.format(cal.getTime());
		return timeRange;
	}

	/**
	 * 获取当前时间周或者月的开始和结束日期, 并以 “_”分开
	 * @param date
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String getWeekMonthStartEnd(String date, String type) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(date));
		String timeRange = null;
		if(Constants.WEEK.equals(type)){
			//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
			if(1 == dayWeek) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			timeRange = sdf.format(cal.getTime())+"_";
			cal.add(Calendar.DATE, 6);
			timeRange += sdf.format(cal.getTime());
		}else if(Constants.MONTH.equals(type)){
			//获取当前月的第一天
			cal.add(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
			timeRange = sdf.format(cal.getTime())+"_";
			//获取当前月的最后一天
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			timeRange += sdf.format(cal.getTime());
		}else if(Constants.YEAR.equals(type)){
			//获取当前年的第一天
			cal.add(Calendar.YEAR, 0);
			cal.set(Calendar.DAY_OF_YEAR,1);//设置为1号,当前日期既为本年第一天
			timeRange = sdf.format(cal.getTime())+"_";
			//获取当前年的最后一天
			cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
			timeRange += sdf.format(cal.getTime());
		}
		return timeRange;
	}
	/**
	 * 获取当前日期所在周周末和上周1    上周1_这周末，并以 “_”分开
	 * 	   <p>例如：type week</p>
	 *     <p>date:2015-10-31</p>
	 *     <p>result : 2015-10-19_2015-11-1</p>
	 *     <p>例如：type month</p>
	 *     <p>date:2015-10-31</p>
	 *     <p>result : 2015-9-1_2015-10-31</p>
	 * @param date
	 * @param type
	 * @return
	 * @throws Exception
	 * @author TANGCY
	 *
	 */
	public static String getWeekMonth2StartEnd(String date, String type) throws Exception{
		String timeRange = null;
		String startEnd = getWeekMonthStartEnd(date, type);
		String[] arr = startEnd.split("_");
		if(Constants.WEEK.equals(type)){//增加一周
			String addOneWeek = addDay(arr[0], -7);
			timeRange = addOneWeek+"_"+arr[1];
		}else if(Constants.MONTH.equals(type)){//增加一月
			String addOneMonth = getMonthReduce(arr[0]);
			timeRange = addOneMonth+"_"+arr[1];
		}
		return timeRange;
	}
	/**
	 * 获取当前日期所在的信息
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getweekOfTime(String date) throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		int year = calendar.get(Calendar.YEAR);
		//解决周跨年的问题
		String convertWeekByDate = convertWeekByDate(calendar.getTime());
		String[] split = getWeekYear(calendar).split("-");
		if (!split[0].equals(split[1])){
			year = Integer.parseInt(split[1]);
		}
		
		int week = isLastDateOfWeek(date);
		return year + "_" + week + "_"+ convertWeekByDate;
	}

	/**
	 * 判断日期是不是在当前日期所在周的日期之内
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static boolean dateInCurWeekOrMonth(String date, String type) throws Exception{
		if(date == null || "".equals(date)){
			return true;
		}
		String[] strarEnd = null;
		//获取当前周的周一到周日]
		if(Constants.WEEK.equals(type)){
			String mondaySunday = getWeekMonthStartEnd(FormatDate.dateToString(new Date(), "yyyy-MM-dd"), type);
			strarEnd = mondaySunday.split("_");
		}else if(Constants.MONTH.equals(type)){//获取当前月的开始和结束日期
			String earlyLate = getWeekMonthStartEnd(FormatDate.dateToString(new Date(), "yyyy-MM-dd"), type);
			strarEnd = earlyLate.split("_");
		}
		String start = strarEnd[0]; // 获取当前日期所在周的周一
		// 当日期大于等于 当前日期所在的周的周一, 返回true
		if(date.compareTo(start) >= 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 得到指定日期前几个月
	 * @param date
	 * @return
	 */
	public static Date getDateAfterNMouth(Date date,int num) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, num);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	/**
	 * 获得指定日期的前某天(不包含时分秒)
	 *
	 * @param theDate
	 * @return
	 */
	public static Date getDayBefore_Notime(Date date1, int num, String format) {
		String theDate = dateToString(date1, format);
		Calendar c = Calendar.getInstance();
		Date date = null;
		Date dayBef = null;
		try {
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - num);

			String dayBefore = new SimpleDateFormat(format).format(c.getTime());
			dayBef = stringToDateWithFormat(dayBefore,format);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dayBef;
	}
	/**
	 * 获得指定日期的连续的前几天的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (1995-11-12)
	 * @param num---往前推的天数 (5)
	 * @return (1995-11-08，1995-11-09，1995-11-10，1995-11-11，1995-11-12)
	 */
	public static List<String> getBeforeNoDays(String theDate, int num, String format) {
		List<String> dateList=new ArrayList<String>();
		try {
			Calendar c = Calendar.getInstance();
			Date date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			for (int i=0; i<num; i++) {
				String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				dateList.add(dayBefore);
				c.add(Calendar.DATE,-1);
				//c.set(Calendar.DATE, day + i);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 获得指定日期的连续的前几天的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (1995-11-12)
	 * @param num---往前推的天数 (5)
	 * @return (1995-11-08，1995-11-09，1995-11-10，1995-11-11，1995-11-12)
	 */
	public static List<String> getBeforeNoDays(String theDate, int num) {
		return getBeforeNoDays(theDate, num , "yyyy-MM-dd");
	}
	/**
	 * 获得指定日期的连续的后几天的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (1995/11/08|1995-11-08)
	 * @param format---theDate的日期格式 (yyyy/MM/dd|yyyy-MM-dd)
	 * @param num---往后推的天数 (5)
	 * @return (1995-11-08，1995-11-09，1995-11-10，1995-11-11，1995-11-12)
	 */
	public static List<String> getAfterNoDays(String theDate, int num, String format) {
		List<String> dateList=new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			for(int i=0;i<num;i++){
				String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				dateList.add(dayBefore);
				c.add(Calendar.DATE,1);
				//c.set(Calendar.DATE, day + i);
			}
			return dateList;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定日期的连续的后几天的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (1995/11/08|1995-11-08)
	 * @param format---theDate的日期格式 (yyyy/MM/dd|yyyy-MM-dd)
	 * @param num---往后推的天数 (5)
	 * @return (1995-11-08，1995-11-09，1995-11-10，1995-11-11，1995-11-12)
	 */
	public static List<String> getAfterNoDays(String theDate, int num) {
		return getAfterNoDays(theDate, num, "yyyy-MM-dd");
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年1月12日上午11:17:06
	 * Description: 获得指定日期的连续的几天的集合
	 * @param theDate	指定日期 (2016/01/08|2016-01-08)
	 * @param num		往后|前推的天数 (3)。 num>0,往后推；num<0,往前推
	 * @return	2016/01/08，2016/01/09，2016/01/10    ；   2016/01/06，2016/01/07，2016/01/08，
	 */
	public static List<String> getNoDays(String theDate, int num) {
		if(num>=0){
			return getAfterNoDays(theDate, num);
		}else{
			return getBeforeNoDays(theDate, -num);
		}
		
	}
	/**
	 * @author liyanlei
	 * @date 2016年1月12日上午11:17:06
	 * Description: 获得指定日期的连续的几天的集合
	 * @param theDate	指定日期 (2016/01/08|2016-01-08)
	 * @param format	theDate的日期格式 (yyyy/MM/dd|yyyy-MM-dd)
	 * @param num		往后|前推的天数 (3)。 num>0,往后推；num<0,往前推
	 * @return	2016/01/08，2016/01/09，2016/01/10    ；   2016/01/06，2016/01/07，2016/01/08，
	 */
	public static List<String> getNoDays(String theDate, int num, String formate) {
		if(num>=0){
			return getAfterNoDays(theDate, num, formate);
		}else{
			return getBeforeNoDays(theDate, num, formate);
		}
		
	}
	/**
	 * 获得指定日期的后num+1个周的周一
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01-03，2014-12-29至2015-01-04，2015-01-05至2015-01-11，2015-01-12至2015-01-18)
	 * @param num---往后推的周数，包含本周 (3)
	 * @return (2015-01-19)
	 */
	public static String getMondayAfterNoWeeks(String theDate, int num) {
		if(num==0){
			return theDate;
		}
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			//"yyyy-MM-dd"
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
			date = df.parse(theDate);
			c.setTime(date);

			int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (day_of_week == 0){
				day_of_week = 7;
			}
			c.add(Calendar.DATE, -day_of_week + 1);//theDate所在周的周一

			c.add(Calendar.DATE, num*7);//

			return df.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theDate;
	}
	/**
	 * 获得指定日期的前num个周的周日
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01-03，2014-12-29至2015-01-04，2015-01-05至2015-01-11，2015-01-12至2015-01-18)
	 * @param num---往前推的周数，包含本周 (3)
	 * @return (2015-01-19)
	 */
	public static String getSundayBeforeNoWeeks(String theDate, int num) {
		if(num==0){
			return theDate;
		}
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			//"yyyy-MM-dd"
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
			date = df.parse(theDate);
			c.setTime(date);

			int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (day_of_week == 0){
				day_of_week = 7;
			}
			c.add(Calendar.DATE, -day_of_week + 7);//theDate所在周的周日

			c.add(Calendar.DATE, -num*7);//往前推几周

			return df.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theDate;
	}
	/**
	 * 获得指定日期的连续的后几周的集合,+最后一个周的周日
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01-03)
	 * @param num---往后推的周数，包含本周 (3)
	 * @return (2014-12-29至2015-01-04，2015-01-05至2015-01-11，2015-01-12至2015-01-18,2015-01-18)
	 */
	public static List<String> getAfterNoWeeks(String theDate, int num) {
		List<String> dateList=new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			//"yyyy-MM-dd"
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
			date = df.parse(theDate);
			c.setTime(date);

			int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (day_of_week == 0){
				day_of_week = 7;
			}

			c.add(Calendar.DATE, -day_of_week + 1);
			String monday=df.format(c.getTime());
			c.add(Calendar.DATE, 6);
			String sunday=df.format(c.getTime());
			dateList.add(monday+"至"+sunday);

			for(int i=1;i<num;i++){
				c.add(Calendar.DATE, 1);
				monday=df.format(c.getTime());
				c.add(Calendar.DATE, 6);
				sunday=df.format(c.getTime());
				dateList.add(monday+"至"+sunday);
			}
			dateList.add(sunday);//最后一个周的周日
			/*c.add(Calendar.DATE, 1);//下一个周一
			monday=df.format(c.getTime());
			dateList.add(monday);*/
			return dateList;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 此方法返回结果是1(2011年第1周)。如果加上一句calendar.setMinimalDaysInFirstWeek(7);返回结果是52（2010年第52周）
	public static int getWeekNumber(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	
	/**
	 * 获得指定日期的连续的后几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往后推的月数 (3)
	 * @return (2015-01，2015-02，2015-03)
	 */
	public static List<String> getAfterNoMonths(String theDate, String format, int num) {
		List<String> dateList=new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			for(int i=0;i<num;i++){
				String monthBefore = new SimpleDateFormat("yyyy-MM").format(c.getTime());
				dateList.add(monthBefore);
				c.add(Calendar.MONTH, 1);
				//c.set(Calendar.MONDAY, month + i);
			}
			return dateList;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 获得指定日期的连续的几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往后|前推的月数 (3)
	 * @return (2015-01，2015-02，2015-03) || (2015-03，2015-02，2015-01)
	 */
	public static List<String> getNoMonths(String theDate, String format, int num) {
		if(num>=0){//往后
			return getAfterNoMonths(theDate, format, num);
		}else{//往前
			return getBeforeNoMonths(theDate, format, -num);
		}
		
	}
	/**
	 * 获得指定日期的连续的前几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往前推的月数 (3)
	 * @return (2015-03，2015-02，2015-01)
	 */
	public static List<String> getBeforeNoMonths(String theDate, String format, int num) {
		List<String> dateList=new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			for(int i=0;i<num;i++){
				String monthBefore = new SimpleDateFormat("yyyy-MM").format(c.getTime());
				dateList.add(monthBefore);
				c.add(Calendar.MONTH, -1);
				//c.set(Calendar.MONDAY, month + i);
			}
			return dateList;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 获取某月第一天
	 * @param yTime
	 * @throws Exception
	 */
	public static String getFirstDayOfMonth(String yTime)throws Exception{
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d=sdf1.parse(yTime); 
		Calendar cal = Calendar.getInstance();  
		cal.setTime(d);
		cal.set(Calendar.DATE, 1);
		return sdf1.format(cal.getTime());
	}
	/**
	 * 获取某月第一天
	 * @param yTime
	 * @throws Exception
	 */
	public static String getFirstDayOfMonth(String yTime, String format)throws Exception{
		SimpleDateFormat sdf1=new SimpleDateFormat(format);
		java.util.Date d=sdf1.parse(yTime); 
		Calendar cal = Calendar.getInstance();  
		cal.setTime(d);
		cal.set(Calendar.DATE, 1);
		return sdf1.format(cal.getTime());
	}
	/**
	 * 获得指定日期的连续的后几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往后推的月数 (3,2015-01，2015-02，2015-03,)
	 * @return (2015-04-01)
	 * @throws Exception 
	 */
	public static String getFirstDayAfterNoMonths(String theDate, String format, int num) throws Exception {
		if(num==0){
			return getFirstDayOfMonth(theDate, format);
		}
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			c.set(Calendar.MONDAY, month + num);
			return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得指定日期的连续的前几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往前推的月数 (3,2015-03，2015-02，2015-01)
	 * @return (2015-01)
	 */
	public static String getMonthBeforeNo(String theDate, String format, int num) {

		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			if(num==0){
				return theDate;
			}else{
				num--;
			}
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			c.set(Calendar.MONDAY, month - num );
			//c.add(Calendar.DATE, -1);//
			return new SimpleDateFormat("yyyy-MM").format(c.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得指定日期的连续的前几月的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @param num---往前推的月数 (3,2015-01，2015-02，2015-03,)
	 * @return (2015-04-01)
	 */
	public static String getLastDayBeforeNoMonths(String theDate, String format, int num) {

		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			if(num==0){
				return getlastDayOfMonth(theDate, format);
			}else{
				num--;
			}
			date = new SimpleDateFormat(format).parse(theDate);
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			c.set(Calendar.MONDAY, month - num + 1);
			c.add(Calendar.DATE, -1);//
			return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定日期的连续的后几周的集合
	 * @author liyanlei
	 * @param theDate---指定日期 (2015-01/2015-01-05)
	 * @param format---theDate的日期格式 (yyyy-MM/yyyy-MM-dd)
	 * @return true:周末，false：不是周末
	 */
	public static boolean isWeekend(String theDate,String format) {
		try {
			DateFormat sdf = new SimpleDateFormat(format);
			Date bdate = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bdate);
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 是否为周六
	 * @param theDate
	 * @return
	 */
	public static boolean isSaturday(String theDate) {
		try {
			DateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN_YMD);
			Date bdate = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bdate);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 是否为周日
	 * @param theDate
	 * @return
	 */
	public static boolean isSunday(String theDate) {
		try {
			DateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN_YMD);
			Date bdate = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bdate);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 判断给定日期是否为周末（周六周日）
	 * @author liyanlei
	 * @param theDate---yyyy-MM-dd格式的日期 (2015-01-03)
	 * @return true:周末，false：不是周末
	 */
	public static boolean isWeekend(String theDate) {
		return isWeekend(theDate, Constants.DATE_PATTERN_YMD);
	}

	public static Date stringToDateWithFormat(String date, String format) throws Exception {
		try {
			if (StrTool.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public static String toDateWithFormat(Date date, String format) {
		String dateStr = "";
		try {
			if (date == null){
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			dateStr = sdf.format(date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	/**
	 * 获取用户权限有效结束时间
	 *
	 * @param userStartDate
	 * @param days
	 * @param months
	 * @param years
	 * @return
	 */
	public static Date getEndDate(Date userStartDate, Integer days, Integer months, Integer years) {
		Date temp_date = null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(userStartDate);

			calendar.add(calendar.DATE, days);
			calendar.add(calendar.MONTH, months);
			calendar.add(calendar.YEAR, years);

			temp_date = calendar.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp_date;
	}
	/**
	 * 比较两个日期
	 * @author liyanlei
	 * @param date1
	 * @param date2
	 * @return -1：date1<date2 , 1 : date1>date2 , 0 : date1=date2
	 */
	public static int compare_date(String date1, String date2) {
		//return compare_date(date1,date2,"yyyy-MM-dd hh:mm");
		return compare_date(date1,date2,"yyyy-MM-dd");
	}

	/**
	 * 比较两个日期
	 *
	 * @author liyanlei
	 * @param date1
	 * @param date2
	 * @param format
	 * @return -1：date1<date2 , 1 : date1>date2 , 0 : date1=date2
	 */
	public static int compare_date(String date1, String date2, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前 ==== "+date1+">"+date2);
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后 ==== "+date1+"<"+date2);
				return -1;
			} else {
				// System.out.println("dt1=dt2 ==== "+date1+"="+date2);
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 比较两个日期
	 *
	 * @author liyanlei
	 * @param date1
	 * @param date2
	 * @return -1：date1<date2 , 1 : date1>date2 , 0 : date1=date2
	 */
	public static int compare_date(Date date1, String date2) {
		// return compare_date(date1,date2,"yyyy-MM-dd hh:mm");
		return compare_date(date1, date2, "yyyy-MM-dd");
	}

	/**
	 * 比较两个日期
	 *
	 * @author liyanlei
	 * @param date1
	 * @param date2
	 * @param format
	 * @return -1：date1<date2 , 1 : date1>date2 , 0 : date1=date2
	 */
	public static int compare_date(Date date1, String date2, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			//Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (date1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前 ==== "+date1+">"+date2);
				return 1;
			} else if (date1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后 ==== "+date1+"<"+date2);
				return -1;
			} else {
				// System.out.println("dt1=dt2 ==== "+date1+"="+date2);
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期相差的天数
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 计算两个日期相差的月数
	 * @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(String smdate, String bdate)throws ParseException {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(smdate));
		c2.setTime(sdf.parse(bdate));
		result =(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12 + c2.get(Calendar.MONTH)- c1.get(Calendar.MONTH);
		return Math.abs(result)+1;
	}
	/**
	 * 计算两个日期相差的月数
	 * @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpaceByFormate(String smdate, String bdate, String formate)throws ParseException {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(smdate));
		c2.setTime(sdf.parse(bdate));
		result =(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12 + c2.get(Calendar.MONTH)- c1.get(Calendar.MONTH);
		return Math.abs(result)+1;
	}
	/**
	 * 获取指定日期类型的当前时间
	 * @param dataPattern
	 * @return
	 */
	public static Date getDateToday(String dataPattern)throws Exception {
		Date date = new Date();
		String dateToString = dateToString(date, dataPattern);
		return stringToDate(dateToString, dataPattern);
	}
	/**
	 * 得到指定日期的（周一至周日）日期
	 *
	 * @return yyyy-MM-dd至yyyy-MM-dd
	 */
	public static  String getWeekDayByDate(Date date) {
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		String monday=df2.format(c.getTime());
		c.add(Calendar.DATE,  6);
		String sunday=df2.format(c.getTime());
		//System.out.println("周一====周日========"+monday+"至"+sunday);
		return monday+"至"+sunday;
	}
	/**
	 * @author wangkj
	 * 获取指定时间周格式              12-29至01-04<br />第1周
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public  static String getWeek(Date nowDay)throws Exception{
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDay);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf1.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf1.format(cal.getTime());
		int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
		return imptimeBegin+"至"+imptimeEnd+"<br />第"+week_of_year+"周";
	}

	/**
	 * 判断是否合法的日期
	 * @param value
	 * @return
	 */
	public static boolean isValidateDay(String value){
		if(value == null || value.length() == 0){
			return false;
		}
		String regex = "\\d{4}[-|/]\\d{1,2}[-|/]\\d{1,2}";//日期的匹配  yyyy-MM-dd
		if(Pattern.matches(regex, value)){//匹配时间.
			return true;
		}
		return false;
	}
	/**
	 * 判断是否合法的月份
	 * @param value
	 * @return
	 */
	public static boolean isValidateMonth(String value){
		if(value == null || value.length() == 0){
			return false;
		}
		String regex = "\\d{4}[-|/]\\d{1,2}";//日期的匹配  yyyy-MM
		if(Pattern.matches(regex, value)){//匹配时间.
			return true;
		}
		return false;
	}

	/**
	 * 一周中的第几天，星期的天数+1
	 * @param  date（星期一）
	 * @return （2）
	 * @throws ParseException
	 */
	public static int  getDayOfWeek(String date) throws ParseException {
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar c = Calendar.getInstance();
		c.setTime(df2.parse(date));
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) ;
		if(day_of_week==1){//周日设置为8
			return 8;
		}
		//System.out.println("day_of_week=="+day_of_week);
		return day_of_week;
	}
	/**
	 * 取得最小的时间
	 * @param  date（星期一）
	 * @return （2）
	 * @throws ParseException
	 */
	public static String  getMinDate(List<String> dateList) throws ParseException {
		String min = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(dateList!=null){
			int size=dateList.size();
			if(size>0){
				min=dateList.get(0);
				for(int i=1;i<size;i++){
					if (!StringUtils.isEmpty(min)&& !StringUtils.isEmpty(dateList.get(i))
							&& sdf.parse(dateList.get(i)).getTime() < sdf.parse(min).getTime()) {
						min = dateList.get(i);
					}
				}
			}
		}
		return min;
	}
	/**
	 * 取得最大的时间
	 * @param  date（星期一）
	 * @return （2）
	 * @throws ParseException
	 */
	public static String  getMaxDate(List<String> dateList) throws ParseException {
		String max = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(dateList!=null){
			int size=dateList.size();
			if(size>0){
				max=dateList.get(0);
				for(int i=1;i<size;i++){
					if (!StringUtils.isEmpty(max)&& !StringUtils.isEmpty(dateList.get(i))
							&& sdf.parse(dateList.get(i)).getTime() > sdf.parse(max).getTime()) {
						max = dateList.get(i);
					}
				}
			}
		}
		return max;
	}

	/***
	 * 时间拼接成 2015年第44周(10.26-11.01) 这种格式
	 *  2015_44_10.26-11.01
	 * @param oldWeekDate
	 * @return 2015年第44周(10.26-11.01)
	 */
	public static String getWeekTransiDate(String oldWeekDate){
		StringBuffer sb = new StringBuffer();
		String[] splitDate = oldWeekDate.split("_");
		String year = splitDate[0]+"年";
		String week = "第"+splitDate[1]+"周";
		String dateRange = "("+splitDate[2]+")";
		return sb.append(year).append(week).append(dateRange).toString();
	}


	/**
	 * 获取随机日期
	 *
	 * @param beginDate
	 *            起始日期，格式为：yyyy-MM-dd
	 * @param endDate
	 *            结束日期，格式为：yyyy-MM-dd
	 * @return
	 */

	public static Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());

			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取随机日期
	 *
	 * @param beginDate
	 *            起始日期，格式为：yyyy-MM-dd
	 * @param endDate
	 *            结束日期，格式为：yyyy-MM-dd
	 * @return 格式为：yyyy-MM-dd
	 */

	public static String randomDate(String beginDate, Date end) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			Date randomDate=new Date(date);
			return dateToString(randomDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}

	private static boolean compareMonthDay(String first, String second) throws ParseException{
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd"); //设置时间格式
		Date firstDate = sdf2.parse(first);
		Date secondDate = sdf2.parse(second);
		if (firstDate.after(secondDate)){
			return true;
		}
		return false;
	}
	/**
	 * 获取当前日期所在周的的周一和周日所在的年
	 * @param time
	 * @return
	 */
	public static String getWeekYear(Calendar calendar) {
		String timeRange = "";
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy"); //设置时间格式
		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		if(1 == dayWeek) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		timeRange = sdf2.format(calendar.getTime())+"-";
		calendar.add(Calendar.DATE, 6);
		timeRange += sdf2.format(calendar.getTime());
		return timeRange;
	}
	//当前日期为日期所在周的最后一天
	private static int isLastDateOfWeek(String date){
		Calendar calendar = Calendar.getInstance();
		try {
			Date curr = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			calendar.setTime(curr);
			int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
			if(1 == dayWeek) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int day = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
			calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek()-day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static boolean isDayFormat(String date){
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		boolean dateFlag = m.matches();
		if (dateFlag) {
			return true;
		}
		return false;
	}
	/**
	 * Description: 开始时间和结束时间
	 * @param date 开始时间
	 * @param diff 开始时间和结束时间的间隔 ，以开始时间为基础整数往后推,负数往前移动
	 * @return 开始时间--结束时间，2016-4-15
	 * @author: liyanlei
	 * 2016年4月14日上午10:14:01
	 */
	public static String getBetweenTime(Date date, int diff){
		StringBuffer between = new StringBuffer();
		between.append(dateToString(date, Constants.DATE_PATTERN_YMD));
		between.append(" - ");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, diff);// 把日期往后增加diff天.整数往后推,负数往前移动
		between.append(dateToString(calendar.getTime(), Constants.DATE_PATTERN_YMD));
		return between.toString();
	}
	
	/**
	 * Description: 开始时间和结束时间
	 * @param datestr 开始时间
	 * @param format datestr日期格式
	 * @param diff 开始时间和结束时间的间隔 ，以开始时间为基础整数往后推,负数往前移动
	 * @return 开始时间--结束时间，2016-4-15
	 * @throws Exception
	 * @author: liyanlei
	 * 2016年4月14日上午10:26:37
	 */
	public static String getBetweenTime(String datestr, String format, int diff) throws Exception{
		Date date = stringToDate(datestr, Constants.DATE_PATTERN_YMD);
		return getBetweenTime(date, diff);
	}
	/**
	 * Description: 开始时间和结束时间
	 * @param datestr 开始时间
	 * @param diff 开始时间和结束时间的间隔 ，以开始时间为基础整数往后推,负数往前移动
	 * @return 开始时间--结束时间，2016-4-15
	 * @throws Exception
	 * @author: liyanlei
	 * 2016年4月14日上午10:26:37
	 */
	public static String getBetweenTime(String datestr,  int diff) throws Exception{
		return getBetweenTime(datestr, Constants.DATE_PATTERN_YMD, diff);
		 
	}
	
	public static void main(String[] args) {
		try {
			String getweekOfTime = getweekOfTime("2016-1-4");
			//System.out.print(getweekOfTime);
			//System.out.print(isDayFormat("2016-01-04"));
			System.out.print(getMonthBeforeNo("2016-01", Constants.DATE_PATTERN_YM, 3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

