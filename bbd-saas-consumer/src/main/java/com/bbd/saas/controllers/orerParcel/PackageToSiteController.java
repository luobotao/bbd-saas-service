package com.bbd.saas.controllers.orerParcel;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderPacelService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderParcel;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderNumVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
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


	/**
	 * 根据运单号检查是否存在此订单
	 * @param request
	 * @param mailNum
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/checkOrderByMailNum", method=RequestMethod.GET)
	public Order checkOrderByMailNum(HttpServletRequest request,@RequestParam(value = "mailNum", required = true) String mailNum) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		return orderService.findOneByMailNum(user.getSite().getAreaCode(),mailNum);
	}

	/**
	 * 重新计算到站的数据统计
	 * @param request
	 * @return
     */
	@ResponseBody
	@RequestMapping(value="/updateOrderNumVO", method=RequestMethod.GET)
	public OrderNumVO updateOrderNumVO(HttpServletRequest request) {
		return orderService.getOrderNumVO(adminService.get(UserSession.get(request)).getSite().getAreaCode());
	}

	/**
	 * 根据包裹号检查当前登录用户是否有此包裹
	 * @param request
	 * @param parcelCode
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/checkOrderParcelByParcelCode", method=RequestMethod.GET)
	public boolean checkOrderParcelByParcelCode(HttpServletRequest request,@RequestParam(value = "parcelCode", required = true) String parcelCode) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		OrderParcel orderParcel =  orderPacelService.findOrderParcelByParcelCode(user.getSite().getAreaCode(),parcelCode);
		if(orderParcel==null)
			return false;
		else
			return true;
	}

	/**
	 * 分页展示数据并
	 * @param request
	 * @param pageIndex
	 * @param arriveStatus
	 * @param between
	 * @param parcelCode
	 * @param mailNum
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/getOrderPage", method = RequestMethod.GET)
	public PageModel<Order> getOrderPage(HttpServletRequest request,Integer pageIndex, Integer arriveStatus,String between,String parcelCode, String mailNum) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		if(StringUtils.isNotBlank(mailNum)){//进行运单到站操作
			Order order = orderService.findOneByMailNum(user.getSite().getAreaCode(),mailNum);
			if(order!=null){
				order.setOrderStatus(OrderStatus.NOTDISPATCH);
				order.setDateUpd(new Date());
				order.setDateArrived(new Date());
				orderService.save(order);
			}
		}
		if (pageIndex==null) pageIndex =0 ;
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.arriveStatus = arriveStatus;
		orderQueryVO.between = between;
		orderQueryVO.parcelCode = parcelCode;
		orderQueryVO.mailNum = mailNum;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		PageModel<Order> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<Order> orderPage = orderService.findOrders(pageModel,orderQueryVO);
		for(Order order : orderPage.getDatas()){
			String parcelCodeTemp = orderPacelService.findParcelCodeByOrderId(order.getId().toHexString());
			order.setParcelCode(parcelCodeTemp);//设置包裹号
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
	public String index(Model model,HttpServletRequest request) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -1);
		String start = Dates.formatSimpleDate(cal.getTime());
		cal.add(Calendar.DATE, 2);
		String end = Dates.formatSimpleDate(cal.getTime());
		String between =start+" - "+end;
		User user = adminService.get(UserSession.get(request));
		OrderNumVO orderNumVO = orderService.getOrderNumVO(user.getSite().getAreaCode());

		PageModel<Order> orderPage = getOrderPage(request,0, -1,between,"","");

		model.addAttribute("orderPage", orderPage);
		model.addAttribute("between", between);
		//未到站订单数
		model.addAttribute("non_arrival_num", orderNumVO.getNoArrive());
		model.addAttribute("history_non_arrival_num", orderNumVO.getNoArriveHis());
		model.addAttribute("arrived_num", orderNumVO.getArrived());
		return "page/packageToSite";
	}

}
