package com.bbd.saas.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserVO;

@Controller
@RequestMapping("/packageDispatch")
@SessionAttributes("packageDispatch")
public class PackageDispatchController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	
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
	public String index(Integer currPage, String status, String between, String courier, RedirectAttributes redirectAttrs, Model model) {
		if(currPage==null){
			currPage = 1;
		}
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageSize(2);
		pageModel.setPageNo(currPage);
		PageModel<Order> orderPage = orderService.findOrders(pageModel,null);
		List<Order> datas = orderPage.getDatas();
		User user = new User();
		user.setRealName("张XX");
		user.setPhone("13256478978");
		for(Order order : datas){
			order.setUser(user);
		}
		//分页信息
		orderPage.setPageSize(10);
		orderPage.setTotalCount(25);
		orderPage.setPageNo(currPage);
//		orderPage.setTotalPages(10);
		logger.info(orderPage+"=========");
		model.addAttribute("username", "张三");
		model.addAttribute("orderPage", orderPage);
		
		UserVO uservo = new UserVO();
		//uservo.setId(new ObjectId("5546548"));
		uservo.setLoginName("loginName");
		uservo.setPhone("12345678945");
		redirectAttrs.addFlashAttribute("user", uservo);
		
		return "page/packageDispatch";
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
		Order order = orderService.findOneByMailNum(mailNum);
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
	private void saveOrderMail(Order order, String courierId, Map<String, Object> map){
		//查询派件员信息
		User user = userService.findOne(courierId);
		//运单分派给派件员
		order.setUser(user);
		//更新运单
		orderService.update(order);
		map.put("operFlag", 1);//1:分派成功
	}
	
	/**
	 * Description: 获取本站点下的所有派件员
	 *  mailNum 运单号
	 *  senderId 派件员id
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

}
