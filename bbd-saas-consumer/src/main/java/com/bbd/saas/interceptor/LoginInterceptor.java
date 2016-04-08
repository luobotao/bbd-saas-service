package com.bbd.saas.interceptor;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.constants.AdminSession;
import com.bbd.saas.mongoModels.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luobotao on 2016/4/8.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/login"};

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
            if(StringUtils.isNotEmpty(AdminSession.get(request))){
                AdminUser adminUser = adminService.get(AdminSession.get(request));
                if (adminUser != null) return true;
            }
            response.sendRedirect("login");
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
