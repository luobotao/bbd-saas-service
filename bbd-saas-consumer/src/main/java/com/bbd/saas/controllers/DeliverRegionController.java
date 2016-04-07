package com.bbd.saas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	@RequestMapping(value="/map", method=RequestMethod.GET)
	public String toMapPage(Model model) {
		model.addAttribute("username", "张三");
		return "systemSet/deliverRegionMap";
	}
	/**
	 * description: 跳转到系统设置-配送区域-导入地址关键词页面
	 * 2016年4月5日下午4:05:15
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="/key", method=RequestMethod.GET)
	public String toKeyPage(Model model) {
		model.addAttribute("username", "张三");
		return "systemSet/deliverRegionKey";
	}
}
