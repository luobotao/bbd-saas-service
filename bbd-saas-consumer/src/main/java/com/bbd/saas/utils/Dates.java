package com.bbd.saas.utils;

import com.bbd.saas.constants.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Dates {
    private static final Map<String, SimpleDateFormat> formatCache = new ConcurrentHashMap<>();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat DATE_FORMAT_IMPORT = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    private static final SimpleDateFormat CHINESE_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat CHINESE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private static final SimpleDateFormat CHINESE_DATE_TIME_FORMAT_NEW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_New = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT_New = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat MONTHANDDAY_FORMAT = new SimpleDateFormat("MM月dd日");
    private static final SimpleDateFormat DATE_TIME_FORMAT_New = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat DATE_TIME_FORMAT_NEW = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String formatSimpleDate(Date date) {
        if(date==null){
            return "";
        }
        return DATE_FORMAT.format(date);
    }
    public static String formatDateTimeNewWithOut(Date date) {
        if(date==null){
            return "";
        }
        return DATE_TIME_FORMAT_NEW.format(date);
    }
    /**
     * yyyy年MM月dd日
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if(date==null){
            return "";
        }
        return CHINESE_DATE_FORMAT.format(date);
    }

    /**
     * yyyy年MM月dd日 HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if(date==null){
            return "";
        }
        return CHINESE_DATE_TIME_FORMAT.format(date);
    }
    /**
     * yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime_New(Date date) {
        if(date==null){
            return "";
        }
        return CHINESE_DATE_TIME_FORMAT_NEW.format(date);
    } /**
     * yyyy/MM/dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime_New1(Date date) {
        if(date==null){
            return "";
        }
        return DATE_FORMAT_IMPORT.format(date);
    }
    /**
     * yyyy.MM.dd
     * @param date
     * @return
     */
    public static String formatDateNew(Date date) {
        if(date==null){
            return "";
        }
        return DATE_FORMAT_New.format(date);
    }
    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDate2(Date date) {
        if(date==null){
            return "";
        }
    	return DATE_FORMAT2.format(date);
    }
    /**
     * HH:mm
     * @param date
     * @return
     */
    public static String formatTimeNew(Date date) {
        if(date==null){
            return "";
        }
        return TIME_FORMAT_New.format(date);
    }
    /**
     * yyyy.MM.dd HH:mm
     * @param date
     * @return
     */
    public static String formatDateTimeNew(Date date) {
        if(date==null){
            return "";
        }
        return DATE_TIME_FORMAT_New.format(date);
    }
    /**
     * 转成类似11月5日
     * @param date
     * @return
     */
    public static String formatMonthAndDay(Date date) {
        if(date==null){
            return "";
        }
    	return MONTHANDDAY_FORMAT.format(date);
    }
    /**
     * 转成星期几
     * @param date
     * @return
     */
	public static String format2Week(Date date) {
        if(date==null){
            return "";
        }
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		return dayNames[dayOfWeek - 1];
		// return WEEK_FORMAT.format(date);
	}

    public static String formatDate(Date date, String format) {
        if (null == date || StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat formatter;
        if (formatCache.containsKey(format)) {
            formatter = formatCache.get(format);
        } else {
            formatter = new SimpleDateFormat(format);
        }

        return formatter.format(date);
    }

    public static Date parseDate(String str) {
        try {
            return DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseFullDate(String str) {
        try {
            return CHINESE_DATE_TIME_FORMAT_NEW.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseImportDate(String str) {
        try {
            return DATE_FORMAT_IMPORT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据导入订单，更改格式
     * @param str
     * @return
     */
    public static String parseDateWithImportOrder(String str) {
        try {
            Date date = DATE_FORMAT_IMPORT.parse(str);
            return formatDateTime_New(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static Date parseDate(String source, String format) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat formatter;
        if (formatCache.containsKey(format)) {
            formatter = formatCache.get(format);
        } else {
            formatter = new SimpleDateFormat(format);
        }

        try {
            return formatter.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convert(String source, String sourceFormat, String targetFormat) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(sourceFormat) || StringUtils.isBlank(targetFormat)) {
            return null;
        }

        return formatDate(parseDate(source, sourceFormat), targetFormat);
    }


    /**
     * 获取当前日期的0点0分0秒
     * @param date
     * @return
     */
    public static Date getBeginOfDay(Date date) {
        if (null == date) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取当前时间的23点59分59秒
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        if (null == date) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public static boolean isBetween(Date date, Date begin, Date end) {
        if (null == date) {
            return false;
        }

        if (begin != null && end != null) {
            return !date.before(begin) && !date.after(end);
        }

        if (begin != null) {
            return !date.before(begin);
        }

        if (end != null) {
            return !date.after(end);
        }

        return true;
    }

    public static String getCurrentTime(){
    	return formatDateTime(new Date());
    }
    
    public static String getCurrentDay(){
    	return formatDate(new Date());
    }
    /**
     * 将系统毫秒数转成日期
     * @param time
     * @return
     */
    public static Date parseDateByLong(Long time){
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
    	return calendar.getTime();
    }
    
    /**
     * 获取指定时间后的addDate天内的所有时间
     * @param dBegin
     * @param addDate
     * @return
     */
    public static List<Date> findDates(Date dBegin,int addDate) {  
    	List<Date> lDate = Lists.newArrayList();
        Calendar calBegin = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(dBegin);  
        Calendar calEnd = Calendar.getInstance();  
        calEnd.setTime(calBegin.getTime());  
        while (addDate-->0) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);  
            lDate.add(calBegin.getTime());  
        }  
        return lDate;  
    } 
    
    /**
	 * Description: 开始时间和结束时间
	 * @param date 开始时间
	 * @param diff 开始时间和结束时间的间隔 ，以开始时间为基础整数往后推,负数往前移动
	 * @return 开始时间 - 结束时间，2016/4/15 - 2016/4/17
	 * @author: liyanlei
	 * 2016年4月14日上午10:14:01
	 */
	public static String getBetweenTime(Date date, int diff){
		StringBuffer between = new StringBuffer();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, diff);// 把日期往后增加diff天.整数往后推,负数往前移动
		if(diff > 0){
			between.append(dateToString(date, Constants.DATE_PATTERN_YMD));
			between.append(" - ");
			between.append(dateToString(calendar.getTime(), Constants.DATE_PATTERN_YMD));
		}else{
			between.append(dateToString(calendar.getTime(), Constants.DATE_PATTERN_YMD));
			between.append(" - ");
			between.append(dateToString(date, Constants.DATE_PATTERN_YMD));
		}
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
	/**
	 * @param date
	 * @author String 日期转换成字符串 格式[yyyy/MM/dd HH:mm:ss]
	 **/
	public static String dateToString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null)
			return "";
		return df.format(date);
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
			if (StringUtil.initStr(date, null) == null)
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
	 * 获取指定日期是月的第几天
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
    public static Date addDays(Date date, int num){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,num);
        return c.getTime();
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
	 * format---theDate的日期格式 (yyyy/MM/dd|yyyy-MM-dd)
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
	 * @param formate	theDate的日期格式 (yyyy/MM/dd|yyyy-MM-dd)
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
	 * 获得指定日期的连续的后几周的集合//,+最后一个周的周日
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
			//dateList.add(sunday);//最后一个周的周日
			
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
			//int month = c.get(Calendar.MONTH);
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
			//int month = c.get(Calendar.MONTH);
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
	 * @param str 年份
	 * @return Date
	 */
	public static  String getlastDayOfMonth(String str) throws Exception{
		return  getlastDayOfMonth(str, Constants.DATE_PATTERN_YM);
	}
	/**
	 * 获取某年某月最后一天日期
	 * @param str 年份
	 * @return Date
	 */
	public static  String getlastDayOfMonth(String str, String format) throws Exception{
		Date date= stringToDateWithFormat(str, format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 1);
		calendar.roll(Calendar.DATE, -1);
		Date last=calendar.getTime();
		String lastDay= dateToString(last, Constants.DATE_PATTERN_YMD);
		//System.out.println("===============last:"+FormatDate.dateToString(last));
		return lastDay;
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
			if (StringUtil.initStr(date, null) == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public static String stringToDateWithFormat(Date date, String format) {
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
	 * 比较两个日期
	 * @author liyanlei
	 * @param date1
	 * @param date2
	 * @return -1：date1<date2 , 1 : date1>date2 , 0 : date1=date2
	 */
	public static int compare_date(String date1, String date2) {
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
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
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
			Date dt2 = df.parse(date2);
			if (date1.getTime() > dt2.getTime()) {
				return 1;
			} else if (date1.getTime() < dt2.getTime()) {
				return -1;
			} else {
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
	 * @param smdate <String>
	 * @param bdate <String>
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
	 * Description: 计算两个日期相差的月数
	 * @param smdate
	 * @param bdate
	 * @param formate
	 * @return
	 * @throws ParseException
	 * @author: liyanlei
	 * 2016年4月18日下午4:22:00
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
	 * Description:  获取指定时间周格式              12-29至01-04<br />第1周
	 * @param nowDay
	 * @return
	 * @throws Exception
	 * @author: liyanlei
	 * 2016年4月18日下午4:22:27
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
	 * @param end
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
    public static boolean isSameDay(Date date, Date another) {
        if(date == null || another == null){
           return false;
        }
        long diff = (date.getTime() - another.getTime())/(1000*60*60);
        if(diff > -25 && diff < 25 ){
            return true;
        }
        System.out.println("date.getTime()===" + date.getTime() + "  another.getTime()===" + another.getTime() + "  diff===" + diff);
        return false;
    }


    public static void main(String[] args) {
    	/*List<Date> a = findDates(new Date(), 7);
    	for(Date b:a){
    		System.out.println(b);
    	}*/

        try {
            int i = daysBetween(new Date(2016,5,11,01,01), new Date(2016,5,12,01,00));
            System.out.println(i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //isSameDay(new Date(), new Date(2016,5,12));
	}


}
