package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.FormatDate;
import com.bbd.saas.utils.NumberUtil;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import com.bbd.saas.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/handleAbnormal")
@SessionAttributes("handleAbnormal")
public class HandleAbnormalController {
	
	public static final Logger logger = LoggerFactory.getLogger(HandleAbnormalController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	/**
	 * description: 跳转到异常件处理页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request, Model model) {
		//设置默认查询条件
		status = NumberUtil.defaultIfNull(status, -1);//全部，滞留和拒收
		//到站时间前天、昨天和今天
		arriveBetween = StringUtils.defaultIfBlank(arriveBetween, FormatDate.getBetweenTime(new Date(), -10));
		//查询数据
		PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, request);
		logger.info("=====异常件处理页面列表====" + orderPage);
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("arriveBetween", arriveBetween);		
		return "page/handleAbnormal";
	}
	
	

	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
		//参数为空时，默认值设置
		pageIndex = NumberUtil.defaultIfNull(pageIndex, 0);
		status = NumberUtil.defaultIfNull(status, -1);
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.abnormalStatus = status;
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		//查询数据
		PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
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
	public List<UserVO> getAllUserList(String courierId, final HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		//查询
		List<UserVO> userVoList = userService.findUserListBySite(user.getSite().getAreaCode());
		return userVoList;
	}
	
	/**
	 * Description: 运单重新分派--把异常的包裹重新分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月16日上午11:38:32
	 */
	@ResponseBody
	@RequestMapping(value="/reDispatch", method=RequestMethod.GET)
	public Map<String, Object> reDispatch(String mailNum, String courierId, Integer status, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		
		//更新字段设置
		User courier = userService.findOne(courierId);//查询派件员
		OrderUpdateVO orderUpdateVO = new OrderUpdateVO();
		orderUpdateVO.user = courier;//运单分派给派件员
		orderUpdateVO.orderStatus = OrderStatus.DISPATCHED;//更新运单状态--已分派
		
		//检索条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.mailNum = mailNum;
		if(status != null && status != -1){
			orderQueryVO.abnormalStatus = status;
		}
		//更新运单
		int i = orderService.updateOrder(orderUpdateVO, orderQueryVO);
		if(i > 0){
			map.put("operFlag", 1);//1:分派成功
			//刷新列表
			orderQueryVO = new OrderQueryVO();
			orderQueryVO.abnormalStatus = NumberUtil.defaultIfNull(status, -1);
			orderQueryVO.areaCode = currUser.getSite().getAreaCode();
			//查询数据
			PageModel<Order> orderPage = orderService.findPageOrders(0, orderQueryVO);
			map.put("orderPage", orderPage); 
		}else{
			map.put("operFlag", 3);//3:分派成功
		}		
		return map;
	}
	
	/**
	 * Description: 获取快递公司
	 * @param mailNum 运单号
	 * @param senderId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/getAllExpressCompanyList", method=RequestMethod.GET)
	public List<UserVO> getAllExpressCompanyList(String siteId, Model model) {
		UserVO uservo = new UserVO();
		//uservo.setId(new ObjectId("5546548"));
		uservo.setLoginName("loginName");
		uservo.setPhone("12345678945");
		List<UserVO> userVoList = userService.findUserListBySite(siteId);
		if(userVoList == null || userVoList.size() == 0){
			userVoList.add(uservo);
		}
		return userVoList;
	}

	/**
	 * Description: 获取本站点下的所有派件员
	 * @param mailNum 运单号
	 * @param senderId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/getAllSiteList", method=RequestMethod.GET)
	public List<UserVO> getAllSiteList(String siteId, Model model) {
		UserVO uservo = new UserVO();
		//uservo.setId(new ObjectId("5546548"));
		uservo.setLoginName("loginName");
		uservo.setPhone("12345678945");
		List<UserVO> userVoList = userService.findUserListBySite(siteId);
		if(userVoList == null || userVoList.size() == 0){
			userVoList.add(uservo);
		}
		return userVoList;
	}
	
	
	/**
	 * Description: 退货
	 * @param mailNum 运单号
	 * @param returnReasonType 退货原因类型
	 * @param returnReasonInfo 其他原因--原因详情
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午1:57:12
	 */
	@RequestMapping(value="/saveReturn", method=RequestMethod.GET)
	public Map dispatch(String mailNum, String returnReasonType, String returnReasonInfo, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//====================start================================
		//查询运单信息
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			//当运单到达站点，首次分派;当运单状态处于滞留拒收时，可以重新分派	
			if(ExpressStatus.ArriveStation.equals(order.getExpressStatus())
				||ExpressStatus.Delay.equals(order.getExpressStatus())
				|| ExpressStatus.Refuse.equals(order.getExpressStatus())){
				if(order.getUser() == null){//未分派，可以分派
					//saveOrderMail(order, courierId, map);
				}else{//重复扫描，此运单已分派过了
					map.put("operFlag", 2);
				}
			}else{//重复扫描，此运单已分派过了
				map.put("operFlag", 2);
			}
		}else{
			map.put("erroFlag", 0);//0:运单号不存在
		}
		//=====================end================================
		return map;
	}
	
	/**
	 * Description: 转其他快递
	 * @param mailNum
	 * @param expressId
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午2:04:59
	 */
	@RequestMapping(value="/toOtherExpress", method=RequestMethod.GET)
	public Map toOtherExpress(String mailNum, String expressId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//====================start================================
		//查询运单信息
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			//当运单到达站点，首次分派;当运单状态处于滞留拒收时，可以重新分派	
			if(ExpressStatus.ArriveStation.equals(order.getExpressStatus())
				||ExpressStatus.Delay.equals(order.getExpressStatus())
				|| ExpressStatus.Refuse.equals(order.getExpressStatus())){
				if(order.getUser() == null){//未分派，可以分派
					//saveOrderMail(order, courierId, map);
				}else{//重复扫描，此运单已分派过了
					map.put("operFlag", 2);
				}
			}else{//重复扫描，此运单已分派过了
				map.put("operFlag", 2);
			}
		}else{
			map.put("erroFlag", 0);//0:运单号不存在
		}
		//=====================end================================
		return map;
	}
	/**
	 * Description: 转其他站点
	 * @param mailNum
	 * @param expressId
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午2:04:59
	 */
	@RequestMapping(value="/toOtherSite", method=RequestMethod.GET)
	public Map toOtherSite(String mailNum, String siteId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//====================start================================
		//查询运单信息
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			//当运单到达站点，首次分派;当运单状态处于滞留拒收时，可以重新分派	
			if(ExpressStatus.ArriveStation.equals(order.getExpressStatus())
				||ExpressStatus.Delay.equals(order.getExpressStatus())
				|| ExpressStatus.Refuse.equals(order.getExpressStatus())){
				if(order.getUser() == null){//未分派，可以分派
					//saveOrderMail(order, courierId, map);
				}else{//重复扫描，此运单已分派过了
					map.put("operFlag", 2);
				}
			}else{//重复扫描，此运单已分派过了
				map.put("operFlag", 2);
			}
		}else{
			map.put("erroFlag", 0);//0:运单号不存在
		}
		//=====================end================================
		return map;
	}
}
