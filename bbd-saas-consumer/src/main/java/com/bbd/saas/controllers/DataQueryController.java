package com.bbd.saas.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.FormatDate;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StrTool;
import com.bbd.saas.vo.OrderQueryVO;

@Controller
@RequestMapping("/dataQuery")
@SessionAttributes("dataQuery")
public class DataQueryController {
	
	public static final Logger logger = LoggerFactory.getLogger(DataQueryController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	/**
	 * description: 跳转到数据查询页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei          
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String between, String mailNum, Model model) {
		between = StrTool.initStr(between, FormatDate.getBetweenTime(new Date(), -2));
		PageModel<Order> orderPage = getList(pageIndex, status, between, mailNum);
		logger.info(orderPage+"=========");
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("between", between);
		return "page/dataQuery";
	}
	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String between, String mailNum) {
		if(pageIndex == null){
			pageIndex = 0;
		}
		if(status == null){
			status = -1;
		}
		//功能做完需要删除between
		if(between != null){
			between = null;
		}
		
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageSize(10);
		pageModel.setPageNo(pageIndex);
		
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.arriveStatus = status;
		orderQueryVO.between = between;
		
		PageModel<Order> orderPage = orderService.findOrders(pageModel, orderQueryVO);
		List<Order> datas = orderPage.getDatas();
		User user = new User();
		user.setRealName("张XX");
		user.setPhone("13256478978");
		for(Order order : datas){
			order.setUser(user);
		}
		return orderPage;
	}
	//分页Ajax更新
	@RequestMapping(value="/exportData", method=RequestMethod.GET)
	public PageModel<Order> exportData(Integer status, String between, String mailNum, final HttpServletResponse response) {
		if(status == null){
			status = -1;
		}
		//功能做完需要删除between
		if(between != null){
			between = null;
		}
		
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageSize(10);
		pageModel.setPageNo(1);
		
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.arriveStatus = status;
		orderQueryVO.between = between;
		
		PageModel<Order> orderPage = orderService.findOrders(pageModel, orderQueryVO);
		List<Order> datas = orderPage.getDatas();
		User user = new User();
		user.setRealName("张XX");
		user.setPhone("13256478978");
		for(Order order : datas){
			order.setUser(user);
		}
		

		return orderPage;
	}	

}
