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
    public static final String ORDER_INFO_TIME_LONG = "o_info_tm.";//发送时间（生命周期60*60*12秒）12小时

    //日期格式==>年-月
    public static final String DATE_PATTERN_YM = "yyyy-MM";

    //日期格式==>年-月-日
    public static final String DATE_PATTERN_YMD = "yyyy/MM/dd";
    public static final String DATE_PATTERN_YMD2 = "yyyy-MM-dd";

    //每个站点设置的到站权限的派件员的最大数目key(value从mongodb中的constants表里获取)
    public static String TOSISTE_PERMISSION_COUNT = "toSitePermsnCount";
    //小件员的每单收益 key(value从mongodb中的constants表里获取)
    public static String REWARD_SEND = "rewardForSend";
    //设置到站权限的派件员默认密码
    public static String TOSISTE_PERMISSION_DEFAULT_PWD = "123456";
    //地址匹配不到站点的areaCode
    public static String NO_SITE_AREACODE = "9999-999";
    //派件员有到站权限 -- mongodb库User表
    public static int HAVE_TOSITE_PERMISSION = 1;
    //派件员无到站权限
    public static int NO_TOSITE_PERMISSION = 0;
    //派件员有到站权限 -- MYSQL库
    public static int POSTMAN_HAVE_TOSITE_PERMISSION = 5;
    //地图分多次查询点
    public static int PAGESIZE_MAP = 1000;
    public static String BBD_COMPANYID = "8";
    //public static String BBD_COMPANYID = "8";
    public static int UPPERLIMIT = 300;
    public static int LOWERLIMIT = 50;
    //运力分布每次查询站点的数量
    public static int PAGESIZE_CAPACITY = 200;

/*
    //派件员有到站分派权限 -- mongodb库User表
    public static int HAVE_DISPATCH_PERMISSION = 1;
    //派件员无到站分派权限
    public static int NO_DISPATCH_PERMISSION = 0;
    //派件员有到站分派权限 -- MYSQL库
    public static int POSTMAN_HAVE_DISPATCH_PERMISSION = 4;
    //运单移除
    public static int ISREMOVED = 1;
    //运单未被移除
    public static int  ISNOTREMOVED = 0;
*/


}
