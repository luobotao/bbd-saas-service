package com.bbd.saas.constants;

/**
 * 常量定义
 * Created by luobotao on 2016/4/8.
 */
public class Constants {

    public static final String ADMIN_SESSION_NAME = "_A_S";//adminuser session
    public static final String REDIS_ADMIN_ID = "SAAS_ADMINUSER_ID_";//adminid Key in redis session

    public static final String BBD_SAAS_VERIFY_CODE = "BBD_SAAS_VERIFY_CODE_";//verifycode id Key in redis
    public static final String BBD_SAAS_VERIFY_CODE_TIME = "BBD_SAAS_VERIFY_CODE_TIME_";//60秒发送间隔

    public final static String COMMA = ",";

    public final static String WEEK = "week";
    public final static String MONTH = "month";
    public final static String YEAR = "year";

    /**
     * 更多连接的前缀(标签精确查询)
     */
    public final static String Exact_URL_PERFIX="/common/goArticleListExact?";

    //cookies存放有效期(单位：秒)
    public static final int COOKIESTIMES = 60 * 60 * 24 * 3;
    //存入cookie的名称
    public static final String COOKIESUSERNAME = "cookieUserName";

    //日期格式==>年
    public static final String DATE_PATTERN_Y = "yyyy";
    //日期格式==>年
    public static final String DATE_PATTERN_Y2 = "yyyy年";
    //日期格式==>年-月
    public static final String DATE_PATTERN_YM = "yyyy-MM";
    //日期格式==>年-月
    public static final String DATE_PATTERN_YM2 = "yyyy年MM月";
    //日期格式==>年-月-日
    public static final String DATE_PATTERN_YM3 = "MM-dd";
    //日期格式==>年-月-日
    public static final String DATE_PATTERN_YMD = "yyyy/MM/dd";
    public static final String DATE_PATTERN_YMD2 = "yyyy-MM-dd";
    public static final String DATE_PATTERN_YMDT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_PATTERN_YMDT2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_YMDT3 = "yyyyMMddHHmmss";

    //日期格式==>年-月-日
    public static final String DATE_PATTERN_YYYY = "yyyy年MM月dd日";
    public static final String DATE_PATTERN_MMDD = "MM月dd日";
    public static final String DATE_PATTERN_ALL_YMD = "yyyy年MM月dd日 hh:mm:ss";
    public static final String DATE_PATTERN_ALL_YMD_24 = "yyyy年MM月dd日 HH:mm:ss";
    //不精确时间 上半年
    public static final String FIRST_HALF_YEAR = "上半年";
    //不精确时间 下半年
    public static final String SECOND_HALF_YEAR = "下半年";
    //不精确时间 年初
    public static final String BEGIN_YEAR = "年初";
    //不精确时间 年中
    public static final String MIDDLE_YEAR = "年中";
    //不精确时间 年底
    public static final String END_YEAR = "年底";
    //不精确时间 年
    public static final String YEAR_STR = "年";
    //不精确时间 月
    public static final String MONTH_STR = "月";
    //不精确时间 上旬
    public static final String MM_SX = "上旬";
    //不精确时间 中旬
    public static final String MM_ZX = "中旬";
    //不精确时间 下旬
    public static final String MM_XX = "下旬";
    //月初
    public static final String MM_BEGIN = "月初";
    //月底
    public static final String MM_END = "月末";
    //日均价
    public static final String TYPE_DAY = "1";
    //周均价
    public static final String TYPE_WEEK = "2";
    //月均价
    public static final String TYPE_MONTH = "3";
    //平均价
    public static final String TYPE_AVERAGE = "4";
    //空
    public static final String STRING_BLANK = "";
}
