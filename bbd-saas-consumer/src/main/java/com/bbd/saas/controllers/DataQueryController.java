package com.bbd.saas.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.NumberUtil;
import com.bbd.saas.utils.PageModel;
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
	@Autowired
	AdminService adminService;
	/**
	 * description: 跳转到数据查询页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei          
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String arriveBetween, String mailNum, final HttpServletRequest request, Model model) {
		//设置默认查询条件
		status = NumberUtil.defaultIfNull(status, -1);//全部
		//到站时间
		//arriveBetween = StringUtils.defaultIfBlank(arriveBetween, FormatDate.getBetweenTime(new Date(), -2));
		//查询数据
		PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, mailNum, request);
		logger.info("=====数据查询页面列表===" + orderPage);
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("arriveBetween", arriveBetween);
		return "page/dataQuery";
	}
	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, String mailNum, final HttpServletRequest request) {
		//参数为空时，默认值设置
		pageIndex = NumberUtil.defaultIfNull(pageIndex, 0);
		status = NumberUtil.defaultIfNull(status, -1);
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.orderStatus = status;
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.mailNum = mailNum;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		//查询数据
		PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
		return orderPage;		
	}
	
	/**
	 * Description: 导出数据
	 * @param status 状态
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportData", method=RequestMethod.GET)
	public void exportData(Integer status, String arriveBetween_expt, String mailNum, 
			final HttpServletRequest request, final HttpServletResponse response) {
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.orderStatus = status;
		orderQueryVO.arriveBetween = arriveBetween_expt;
		orderQueryVO.mailNum = mailNum;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		//查询数据
		List<Order> orderList = orderService.findOrders(orderQueryVO);	
		//导出==数据写到Excel中并写入response下载

	}	

}
