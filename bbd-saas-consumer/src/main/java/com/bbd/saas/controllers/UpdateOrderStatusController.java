package com.bbd.saas.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;

@Controller
@RequestMapping("/updOrderStatus")
public class UpdateOrderStatusController {
	
	public static final Logger logger = LoggerFactory.getLogger(UpdateOrderStatusController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	
	/**
	 * Description: 跳转到包裹分派页面
	 * @param currPage 当前页
	 * @param status 状态
	 * @param arriveBetween 到站时间
	 * @param courier 派件员
	 * @param redirectAttrs
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:25:07
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String arriveBetween, String courierId, final HttpServletRequest request, Model model) {
		//设置默认查询条件
		status = Numbers.defaultIfNull(status, -1);//全部
		//到站时间前天、昨天和今天
		arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -10));
		//查询数据
		PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, courierId, request);
		logger.info("=====运单分派====" + orderPage);
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("arriveBetween", arriveBetween);
		return "page/updateOrderStatus";
	}
	
	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, String courierId, final HttpServletRequest request) {
		//参数为空时，默认值设置
		pageIndex = Numbers.defaultIfNull(pageIndex, 0);
		status = Numbers.defaultIfNull(status, -1);
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.dispatchStatus = status;
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.userId = courierId;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		//查询数据
		PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
		//查询派件员姓名电话
		if(orderPage != null && orderPage.getDatas() != null){
			List<Order> dataList = orderPage.getDatas();
			User courier = null;
			for(Order order : dataList){
				courier = userService.findOne(order.getUserId());
				UserVO userVO = new UserVO();
				userVO.setLoginName(courier.getLoginName());
				userVO.setRealName(courier.getRealName());
				order.setUserVO(userVO);
			}
		}		
		return orderPage;
	}
	
	/**
	 * Description: 获取本站点下的所有状态为有效的派件员
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日上午11:06:19
	 */
	@ResponseBody
	@RequestMapping(value="/getAllUserList", method=RequestMethod.GET)
	public List<UserVO> getAllUserList(final HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		//查询
		List<UserVO> userVoList = userService.findUserListBySite(user.getSite());
		return userVoList;
	}
	
	/**
	 * Description: 运单分派--把到站的包裹分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:55
	 */
	@ResponseBody
	@RequestMapping(value="/updateOrderStatus", method=RequestMethod.GET)
	public Map<String, Object> updateOrderStatus(String mailNum, String courierId, Integer status, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		//查询运单信息
		Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
		if(order == null){//运单不存在,与站点无关
			map.put("operFlag", 0);//0:运单号不存在
		}else{//运单存在
			if(status == OrderStatus.NOTDISPATCH.getStatus()){//未分派，更新用户
				//查询派件员信息
				User user = userService.findOne(courierId);
				//运单分派给派件员
				order.setUserId(user.getId().toString());
			}
			//更新运单状态
			order.setOrderStatus(OrderStatus.status2Obj(status));
			//更新运单
			Key<Order> r = orderService.save(order);
			if(r != null){
				map.put("operFlag", 1);//1:分派成功
				//刷新列表
				OrderQueryVO orderQueryVO = new OrderQueryVO();
				orderQueryVO.dispatchStatus = status;
				orderQueryVO.areaCode = currUser.getSite().getAreaCode();
				//查询数据
				PageModel<Order> orderPage = orderService.findPageOrders(0, orderQueryVO);
				if(orderPage != null && orderPage.getDatas() != null){
					List<Order> dataList = orderPage.getDatas();
					User courier = null;
					for(Order order1 : dataList){
						courier = userService.findOne(order1.getUserId());
						UserVO userVO = new UserVO();
						userVO.setLoginName(courier.getLoginName());
						userVO.setRealName(courier.getRealName());
						order1.setUserVO(userVO);
					}
				}
				map.put("orderPage", orderPage); 
			}else{
				map.put("operFlag", 3);//3:分派失败
			}
		}
		return map;
	}
}
