package com.bbd.saas.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.FormatDate;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StrTool;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;

@Controller
@RequestMapping("/handleAbnormal")
@SessionAttributes("handleAbnormal")
public class HandleAbnormalController {
	
	public static final Logger logger = LoggerFactory.getLogger(HandleAbnormalController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	/**
	 * description: 跳转到异常件处理页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String between, Model model) {
		between = StrTool.initStr(between, FormatDate.getBetweenTime(new Date(), -2));
		PageModel<Order> orderPage = getList(pageIndex, status, between);
		logger.info(orderPage+"=========");
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("between", between);
		return "page/handleAbnormal";
	}
	
	

	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String between) {
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
	@RequestMapping(value="/getAllUserList", method=RequestMethod.GET)
	public List<UserVO> getAllUserList(String siteId, Model model) {
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
	 * Description: 运单重新分派--把异常的包裹重新分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/reDispatch", method=RequestMethod.GET)
	public Map reDispatch(String mailNum, String courierId, Model model) {
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
					saveOrderMail(order, courierId, map);
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
				
		//Order order = new Order();
		map.put("order", order); //刷新列表，添加此运单
		map.put("success", true); //0:分派成功;
		map.put("operFlag", 2); //0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:拒收运单重新分派--。
		return map;
	}
	/**
	 * Description: 保存派件员信息
	 * @param order 订单
	 * @param courierId 派件员
	 * @param map
	 * @author: liyanlei
	 * 2016年4月14日上午11:00:59
	 */
	private void saveOrderMail(Order order, String courierId, Map<String, Object> map){
		//查询派件员信息
		User user = userService.findOne(courierId);
		//运单分派给派件员
		order.setUser(user);
		//更新运单
		orderService.save(order);
		map.put("operFlag", 1);//1:分派成功
	}
	
	/**
	 * Description: 退货
	 * @param mailNum 运单号
	 * @param reasonType 退货原因类型
	 * @param reasonInfo 其他原因--原因详情
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午1:57:12
	 */
	@RequestMapping(value="/saveReturn", method=RequestMethod.GET)
	public Map dispatch(String mailNum, String reasonType, String reasonInfo, Model model) {
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
