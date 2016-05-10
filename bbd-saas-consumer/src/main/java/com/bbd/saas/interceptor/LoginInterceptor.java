package com.bbd.saas.interceptor;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luobotao on 2016/4/8.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/login","/500","/404","/resources","site","register","bbd","main.jsp","sendVerifyCode"};

    @Autowired
    AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURI();
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if(StringUtils.isNotEmpty(UserSession.get(request))){
                User user = adminService.get(UserSession.get(request));
                if (user != null) return true;
            }

            response.sendRedirect(request.getContextPath()+"/login");
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
