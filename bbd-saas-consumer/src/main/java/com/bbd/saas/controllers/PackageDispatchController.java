package com.bbd.saas.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.FormatDate;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StrTool;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;

@Controller
@RequestMapping("/packageDispatch")
@SessionAttributes("packageDispatch")
public class PackageDispatchController {
	
	public static final Logger logger = LoggerFactory.getLogger(PackageDispatchController.class);
	
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
	 * @param between 到站时间
	 * @param courier 派件员
	 * @param redirectAttrs
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:25:07
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String between, String courierId, Model model) {
		between = StrTool.initStr(between, FormatDate.getBetweenTime(new Date(), -2));
		PageModel<Order> orderPage = getList(pageIndex, status, between, courierId);
		logger.info(orderPage+"=========");
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("between", between);
		
		return "page/packageDispatch";
	}
	
	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String between, String courierId) {
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
	 * Description: 运单分派--把到站的包裹分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/dispatch", method=RequestMethod.GET)
	public Map dispatch(String mailNum, String courierId, Model model) {
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
	public List<UserVO> getAllUserList(String siteId, final HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		
		List<UserVO> userVoList = userService.findUserListBySite(user.getSite().getAreaCode());
		if(userVoList == null || userVoList.size() == 0){
			UserVO uservo = new UserVO();
			//uservo.setId(new ObjectId("5546548"));
			uservo.setRealName("张三");
			uservo.setLoginName("loginName");
			uservo.setPhone("12345678945");
			userVoList.add(uservo);
		}
		return userVoList;
	}

}
