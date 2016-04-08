package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.constants.AdminSession;
import com.bbd.saas.mongoModels.AdminUser;
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
	public String index(Model model) {
		model.addAttribute("username", "张三");
		return "index";
	}

	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		AdminUser adminUser = adminService.get(AdminSession.get(request));
		AdminSession.remove(response);//remove from cookies
		adminService.delete(adminUser);//remove adminUser from redis
		return "redirect:/login";
	}

}
