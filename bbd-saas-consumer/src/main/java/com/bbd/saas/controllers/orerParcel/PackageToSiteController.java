package com.bbd.saas.controllers.orerParcel;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.OrderPacelService;
import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/packageToSite")
@SessionAttributes("packageToSite")
public class PackageToSiteController {
	public static final Logger logger = LoggerFactory.getLogger(PackageToSiteController.class);
	@Autowired
	OrderService orderService;
	@Autowired
	OrderPacelService orderPacelService;
	@Autowired
	AdminService adminService;


	@ResponseBody
	@RequestMapping(value="/checkOrderByMailNum", method=RequestMethod.GET)
	public Order checkOrderByMailNum(@RequestParam(value = "mailNum", required = true) String mailNum) {
		return orderService.findOneByMailNum(mailNum);
	}
	@ResponseBody
	@RequestMapping(value="/checkOrderParcelByParcelCode", method=RequestMethod.GET)
	public boolean checkOrderParcelByParcelCode(@RequestParam(value = "parcelCode", required = true) String parcelCode) {
		OrderParcel orderParcel =  orderPacelService.findOrderParcelByParcelCode(parcelCode);
		if(orderParcel==null)
			return false;
		else
			return true;
	}

	@ResponseBody
	@RequestMapping(value = "/getOrderPage", method = RequestMethod.GET)
	public PageModel<Order> getOrderPage(Integer pageIndex, Integer arriveStatus,String between,String parcelCode, String mailNum) {

		if(StringUtils.isNotBlank(mailNum)){//进行运单到站操作
			Order order = orderService.findOneByMailNum(mailNum);
			if(order!=null){
				order.setOrderStatus(OrderStatus.NOTDISPATCH);
				order.setDateUpd(new Date());
				orderService.save(order);
			}
		}
		if (pageIndex==null) pageIndex =0 ;
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.arriveStatus = arriveStatus;
		orderQueryVO.between = between;
		orderQueryVO.parcelCode = parcelCode;
		orderQueryVO.mailNum = mailNum;

		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<Order> orderPage = orderService.findOrders(pageModel,orderQueryVO);
		for(Order order : orderPage.getDatas()){
			order.setParcelCode(orderPacelService.findParcelCodeByOrderId(order.getId().toHexString()));//设置包裹号
		}
		return orderPage;
	}

	/**
	 * 批量到站
	 * @param ids
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/arriveBatch", method = RequestMethod.POST)
	public boolean arriveBatch(String ids) {
		BasicDBList idList = (BasicDBList) JSON.parse(ids);
		for (Object mailNum :idList){
			orderService.updateOrderOrderStatu(mailNum.toString(),OrderStatus.NOTARR,OrderStatus.NOTDISPATCH);
		}
		return true;
	}

	/**
	 * description: 跳转到包裹到站页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model,HttpServletRequest request,
						@RequestParam(value = "arriveStatus", required = false, defaultValue = "-1") Integer arriveStatus,
						@RequestParam(value = "between", required = false) String between,
						@RequestParam(value = "parcelCode", required = false) String parcelCode,
						@RequestParam(value = "mailNum", required = false)String mailNum) {

		User user = adminService.get(UserSession.get(request));
		OrderNumVO orderNumVO = orderService.getOrderNumVO(user.getSite().getAreaCode());

		PageModel<Order> orderPage = getOrderPage(0,arriveStatus,between,parcelCode,mailNum);

		model.addAttribute("orderPage", orderPage);
		//未到站订单数
		model.addAttribute("non_arrival_num", orderNumVO.getNoArrive());
		model.addAttribute("history_non_arrival_num", orderNumVO.getNoArriveHis());
		model.addAttribute("arrived_num", orderNumVO.getArrived());
		return "page/packageToSite";
	}

}
