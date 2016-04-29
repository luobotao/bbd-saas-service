package com.bbd.saas.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dates {
    private static final Map<String, SimpleDateFormat> formatCache = new ConcurrentHashMap<String, SimpleDateFormat>();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat CHINESE_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat ENGLISH_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat CHINESE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_New = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat TIME_FORMAT_New = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat MONTHANDDAY_FORMAT = new SimpleDateFormat("MM月dd日");
    private static final SimpleDateFormat DATE_TIME_FORMAT_New = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private static final SimpleDateFormat DATE_TIME_FORMAT_To_Hour = new SimpleDateFormat("yyyy-MM-dd HH");
    private static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat ENGLISH_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) {
        return CHINESE_DATE_FORMAT.format(date);
    }
    public static String formatEnglishDate(Date date) {
    	return ENGLISH_DATE_FORMAT.format(date);
    }
    public static String formatSimpleDate(Date date) {
    	return DATE_FORMAT.format(date);
    }

    /**
     * yyyy年MM月dd日 HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return CHINESE_DATE_TIME_FORMAT.format(date);
    }
    public static String formatDateNew(Date date) {
    	return DATE_FORMAT_New.format(date);
    }
    public static String formatTimeNew(Date date) {
    	return TIME_FORMAT_New.format(date);
    }
    public static String formatDateTimeNew(Date date) {
    	return DATE_TIME_FORMAT_New.format(date);
    }
    /**
     * 转成类似11月5日
     * @param date
     * @return
     */
    public static String formatMonthAndDay(Date date) {
    	return MONTHANDDAY_FORMAT.format(date);
    }
    /**
     * 转成类似2016-01-27 15
     * @param date
     * @return
     */
    public static String formatDATE_TIME_FORMAT_To_Hour(Date date) {
    	return DATE_TIME_FORMAT_To_Hour.format(date);
    }
    /**
     * 转成星期几
     * @param date
     * @return
     */
    public static String format2Week(Date date) {
    	return WEEK_FORMAT.format(date);
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

    /**
     * 验证一个时间是否处在两个时间中间
     * @param date
     * @param begin
     * @param end
     * @return
     */
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

	 public static String formatEngLishDateTime(Date date) {
    	if(date==null){
    		return "";
    	}
    	return ENGLISH_DATE_TIME_FORMAT.format(date);
    }

	 /**
	  * 
	  * <p>Title: SevenBeforeformatEngLishDateTime</p> 
	  * <p>Description:获取当前时间的七天前的日期 </p> 
	  * @param date
	  * @return
	  */
	public static String SevenBeforeformatEngLishDateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		String start = DATE_FORMAT.format(cal.getTime());
		return start;
	}

    /**
     * yyyy年MM月dd日 HH:mm:ss
     * @param str
     * @return
     */
	public static Date parseChinaseDate(String str) {
		try {
            return CHINESE_DATE_TIME_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
	}

    /**
     * 传入一个时间获取该时间与当前时间相关的分钟数
     * @param date
     * @return
     */
    public static int afterNowMinutes(Date date){
        long beginTime = date.getTime();
        long now = System.currentTimeMillis();
        return (int) ((now - beginTime) / (1000 * 60 ));
    }

    public static void main(String args[]){
        Calendar date = Calendar.getInstance();
        date.set(2016,0,13,14,23,12);
        System.out.println(date.getTime()+"======================");
        System.out.println(new Date());
        System.out.println(afterNowMinutes(date.getTime()));
    }
}
