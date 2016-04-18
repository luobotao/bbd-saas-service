package com.bbd.saas.controllers;

import java.util.ArrayList;
import java.util.Date;
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
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExportUtil;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
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
		status = Numbers.defaultIfNull(status, -1);//全部
		//到站时间
		arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -2));
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
		pageIndex = Numbers.defaultIfNull(pageIndex, 0);
		status = Numbers.defaultIfNull(status, -1);
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
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
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
		//表格数据
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> row = null;
		if(orderList != null){
			for(Order order : orderList){
				row = new ArrayList<String>();
				row.add(order.getParcelCode());
				row.add(order.getMailNum());
				row.add(order.getOrderNo());
				row.add(order.getSrc().getMessage());
				row.add(order.getReciever().getName());
				row.add(order.getReciever().getPhone());
				StringBuffer address = new StringBuffer();
				address.append(order.getReciever().getProvince());
				address.append(order.getReciever().getCity());
				address.append(order.getReciever().getArea());
				address.append(order.getReciever().getAddress());
				row.add(address.toString());
				row.add(Dates.formatDateTime_New(order.getDatePrint()));
				row.add(Dates.formatDate2(order.getDateMayArrive()));
				row.add(Dates.formatDateTime_New(order.getDateArrived()));
				if(order.getUser() == null){
					row.add("");
					row.add("");
				}else{
					row.add(order.getUser().getRealName());
					row.add(order.getUser().getPhone());
				}
				if(order.getOrderStatus() == null){
					row.add("未到站");
				}else{
					row.add(order.getOrderStatus().getMessage());
				}
			}
		}
		//表头
		String[] titles = { "省/直辖市", "市", "区", "地址关键词", "站点" };
		ExportUtil.exportExcel("数据查询", dataList, titles, response);
	}	
	

}
