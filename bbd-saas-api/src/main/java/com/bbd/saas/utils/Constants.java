package com.bbd.saas.utils;

/**
 * 常量定义
 * Created by luobotao on 2016/4/8.
 */
public class Constants {


    //每个站点设置的到站分派权限的派件员的最大数目
    public static String DISPATCH_PERMISSION_COUNT = "dispatchPermsnCount";
    //设置到站分派权限的派件员默认密码
    public static String DISPATCH_PERMISSION_DEFAULT_PWD = "123456";
    //地址匹配不到站点的areaCode
    public static String NO_SITE_AREACODE = "9999-999";
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
    //商户订单详情页每页条数
    public static final int PAGESIZE_MAIL = 50;


}
