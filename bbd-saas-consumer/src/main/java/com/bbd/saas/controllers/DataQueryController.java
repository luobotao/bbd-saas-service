package com.bbd.saas.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mongodb.morphia.Key;
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
import com.bbd.saas.enums.OrderStatus;
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
	
	//修改订单状态
	
	/**
	 * Description: 运单分派--把到站的包裹分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:55
	 *//*
	@ResponseBody
	@RequestMapping(value="/dispatch", method=RequestMethod.GET)
	public Map<String, Object> dispatch(String mailNum, String status, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//查询运单信息
		Order order = orderService.findOneByMailNum(user.getSite().getAreaCode(), mailNum);
		if(order == null){//运单不存在,与站点无关
			map.put("operFlag", 0);//0:运单号不存在
		}else{//运单存在
			//当运单到达站点(未分派)，首次分派;当运单状态处于滞留、拒收时，可以重新分派	
			if(OrderStatus.NOTDISPATCH.equals(order.getOrderStatus())//未分派
				|| OrderStatus.RETENTION.equals(order.getOrderStatus()) //滞留
				|| OrderStatus.REJECTION.equals(order.getOrderStatus())){//拒收
				saveOrderMail(order, courierId, user.getSite().getAreaCode(), map);
			}else if(order.getUser() != null){//重复扫描，此运单已分派过了
				map.put("operFlag", 2);//0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:分派失败;4:未知错误（不可预料的错误）。
			}else{
				map.put("operFlag", 4);//0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:分派失败;4:未知错误（不可预料的错误）。
			}
		}
		return map;
	}
	
	*//**
	 * Description: 运单分派
	 * @param order 订单
	 * @param courierId 派件员Id
	 * @param areaCode 站点编码
	 * @param map
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:08
	 *//*
	private void saveOrderMail(Order order, String courierId, String areaCode, Map<String, Object> map){
		//查询派件员信息
		User user = userService.findOne(courierId);
		//运单分派给派件员
		order.setUser(user);
		//更新运单状态--已分派
		order.setOrderStatus(OrderStatus.DISPATCHED);
		//更新运单
		Key<Order> r = orderService.save(order);
		if(r != null){
			map.put("operFlag", 1);//1:分派成功
			//刷新列表
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.dispatchStatus = OrderStatus.DISPATCHED.getStatus();
			orderQueryVO.userId = courierId;
			orderQueryVO.areaCode = areaCode;
			//查询数据
			PageModel<Order> orderPage = orderService.findPageOrders(0, orderQueryVO);
			map.put("orderPage", orderPage); 
		}else{
			map.put("operFlag", 3);//3:分派成功
		}
	}
	*/

}
