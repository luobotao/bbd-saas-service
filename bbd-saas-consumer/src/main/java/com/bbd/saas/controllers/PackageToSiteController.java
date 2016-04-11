package com.bbd.saas.controllers;

import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/packageToSite")
@SessionAttributes("packageToSite")
public class PackageToSiteController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	OrderService orderService;
	/**
	 * description: 跳转到包裹到站页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model,@RequestParam(value = "page", required = false) Integer page) {
		if(page==null) page =0 ;
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageSize(2);
		pageModel.setPageNo(page);
		PageModel<Order> orderPage = orderService.findOrders(pageModel);

		logger.info(orderPage+"========="+page);
		model.addAttribute("username", "张三");
		model.addAttribute("orderPage", orderPage);
		//未到站订单数
		model.addAttribute("non_arrival_num", "76");
		model.addAttribute("history_non_arrival_num", "78");
		model.addAttribute("arrived_num", "80");
		model.addAttribute("username", "张三");
		return "page/packageToSite";
	}

}
