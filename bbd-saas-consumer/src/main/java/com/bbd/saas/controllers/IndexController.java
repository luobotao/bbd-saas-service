package com.bbd.saas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
@SessionAttributes("index")
public class IndexController {
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

}
