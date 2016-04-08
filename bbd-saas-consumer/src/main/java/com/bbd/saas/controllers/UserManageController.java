package com.bbd.saas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/userManage")
@SessionAttributes("userManage")
public class UserManageController {
	
	/**
	 * description: 跳转到系统设置-用户管理页面
	 * 2016年4月5日下午4:00:19
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("username", "张三");
		return "systemSet/userManage";
	}

}
