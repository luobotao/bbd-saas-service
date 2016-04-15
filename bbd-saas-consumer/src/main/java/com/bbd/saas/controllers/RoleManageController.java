package com.bbd.saas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/roleManage")
@SessionAttributes("roleManage")
public class RoleManageController {

	/**
	 * 版权：zuowenhai新石器时代<br/>
	 * 作者：zuowenhai@neolix.cn <br/>
	 * 生成日期：2016-04-11 <br/>
	 * 描述：角色管理
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("username", "张三");
		return "systemSet/roleManage";
	}

}
