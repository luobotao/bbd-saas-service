package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@SessionAttributes("index")
public class IndexController {

	@Autowired
	AdminService adminService;

	/**
	 * description: 进入首页
	 * 2016年4月1日下午6:22:38
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model,HttpServletRequest request,String typ) {
		User user = adminService.get(UserSession.get(request));
		model.addAttribute("user", user);
		model.addAttribute("typ", typ);
		return "index";
	}

	/**
	 * 退出登录
	 * @param model
	 * @param request
	 * @param response
     * @return
     */
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		User user = adminService.get(UserSession.get(request));
		UserSession.remove(response);//remove from cookies
		adminService.delete(user);//remove adminUser from redis
		return "redirect:/login";
	}

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(Model model,HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));
		model.addAttribute("user", user);
		return "home";
	}

}
