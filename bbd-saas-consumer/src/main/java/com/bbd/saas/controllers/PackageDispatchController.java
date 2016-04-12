package com.bbd.saas.controllers;

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
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;

@Controller
@RequestMapping("/packageDispatch")
@SessionAttributes("packageDispatch")
public class PackageDispatchController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	OrderService orderService;
	/**
	 * description: 跳转到包裹分派页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model) {
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageSize(2);
		pageModel.setPageNo(3);
		PageModel<Order> orderPage = orderService.findOrders(pageModel);
		List<Order> datas = orderPage.getDatas();
		User user = new User();
		user.setRealName("张XX");
		user.setPhone("13256478978");
		for(Order order : datas){
			order.setUser(user);
		}
		logger.info(orderPage+"=========");
		model.addAttribute("username", "张三");
		model.addAttribute("orderPage", orderPage);
		//未到站订单数
		model.addAttribute("non_arrival_num", "76");
		model.addAttribute("history_non_arrival_num", "78");
		model.addAttribute("arrived_num", "80");
		model.addAttribute("username", "张三");
		return "page/packageDispatch";
	}
	
	/**
	 * Description: 运单分派--把到站的包裹分派给派件员
	 * @param mailNum 运单号
	 * @param senderId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/checkMailNum", method=RequestMethod.GET)
	public Map dispatch(String mailNum, String senderId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("success", true); //1:分派成功;
		map.put("erroFlag", 2); //0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:拒收运单重新分派。
		return map;
	}

}
