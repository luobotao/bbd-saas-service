package com.bbd.saas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/deliverRegion")
@SessionAttributes("deliverRegion")
public class DeliverRegionController {
	

	/**
	 * description: 跳转到系统设置-配送区域-绘制电子地图页面
	 * 2016年4月5日下午4:05:01
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="/map/{activeNum}", method=RequestMethod.GET)
	public String toMapPage(@PathVariable String activeNum, Model model) {
		//获取当前用户
		//获取用户站点信息
		model.addAttribute("activeNum", activeNum);
		return "systemSet/deliverRegionMap";
	}

}
