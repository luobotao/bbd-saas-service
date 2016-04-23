package com.bbd.saas.constants;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理端Session
 * @author luobotao
 * Date: 2015年4月14日 下午6:18:12
 */
public class UserSession {

    public static void put(HttpServletResponse response,String value) {
        Cookie cookie = new Cookie(Constants.ADMIN_SESSION_NAME, value);
        cookie.setMaxAge(60 * 60);//保存一小时
        response.addCookie(cookie);
    }

    public static void remove(HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.ADMIN_SESSION_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String get(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (Constants.ADMIN_SESSION_NAME.equals(cookie.getName())) {
                    cookie.setMaxAge(60 * 60);//更新Session一小时
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
