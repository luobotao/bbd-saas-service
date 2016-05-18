package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExportUtil;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.SiteVO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 运单查询
 */
@Controller
@RequestMapping("/mailQuery")
public class MailQueryController {
	
	public static final Logger logger = LoggerFactory.getLogger(MailQueryController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	OrderParcelService orderParcelService;
	@Autowired
	SiteService siteService;

	/**
	 * Description: 跳转到运单查询页面
	 * @param pageIndex 页数
	 * @param status 运单状态
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request 请求
	 * @param model
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:27:46
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String areaCode, Integer status, String arriveBetween, String mailNum, final HttpServletRequest request, Model model) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//设置默认查询条件
			status = Numbers.defaultIfNull(status, -1);//全部
			//到站时间
			//arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -2));
			//查询数据
			PageModel<Order> orderPage = getList(pageIndex, areaCode, status, arriveBetween, mailNum, request);
			if(orderPage != null && orderPage.getDatas() != null){
				for(Order order : orderPage.getDatas()){
					String parcelCodeTemp = orderParcelService.findParcelCodeByOrderId(order.getId().toHexString());
					order.setParcelCode(parcelCodeTemp);//设置包裹号
				}
				//当前登录的用户信息
				User currUser = adminService.get(UserSession.get(request));
				List<SiteStatus> statusList = new ArrayList<SiteStatus>();
				statusList.add(SiteStatus.APPROVE);
				statusList.add(SiteStatus.INVALID);
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyIdAndStatusList(currUser.getCompanyId(), statusList);
				logger.info("=====运单查询页面列表===" + orderPage);
				model.addAttribute("orderPage", orderPage);
				//model.addAttribute("arriveBetween", arriveBetween);
				model.addAttribute("siteList", siteVOList);
				return "page/mailQuery";
			}else{
				orderPage = new PageModel<Order>();
				model.addAttribute("orderPage", orderPage);
			}
		} catch (Exception e) {
			logger.error("===跳转到运单查询页面==出错 :" + e.getMessage());
		}
		return "page/mailQuery";
	}
	
	/**
	 * Description: 分页查询，Ajax更新列表
	 * @param pageIndex 页数
	 * @param areaCode 站点编号
	 * @param status 运单状态
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request 请求
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:27:16
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, String areaCode, Integer status, String arriveBetween, String mailNum, final HttpServletRequest request) {
		//查询数据
		PageModel<Order> orderPage = new PageModel<Order>();
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			status = Numbers.defaultIfNull(status, -1);
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			List<SiteStatus> statusList = new ArrayList<SiteStatus>();
			statusList.add(SiteStatus.APPROVE);
			statusList.add(SiteStatus.INVALID);
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyIdAndStatusList(user.getCompanyId(), statusList);
			//设置查询条件
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.orderStatus = status;
			//orderQueryVO.arriveBetween = arriveBetween;
			orderQueryVO.mailNum = mailNum;
			orderQueryVO.areaCode = areaCode;
			//公司查询
			if(StringUtils.isBlank(areaCode)){//查询全部 -- 同一个公司的所有站点
				List<String> areaCodeList = new ArrayList<String>();
				if(siteVOList != null && siteVOList.size() > 0){
					for (SiteVO siteVO : siteVOList){
						areaCodeList.add(siteVO.getAreaCode());
					}
				}
				orderQueryVO.areaCodeList = areaCodeList;
			}
			//查询数据
			if(orderQueryVO.areaCodeList != null  && orderQueryVO.areaCodeList.size() > 0){
				orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
			}
			//设置包裹号,派件员快递和电话
			if(orderPage != null && orderPage.getDatas() != null){
				List<Order> dataList = orderPage.getDatas();
				String parcelCodeTemp = null;
				User courier = null;
				UserVO userVO = null;
				for(Order order : dataList){
					parcelCodeTemp = orderParcelService.findParcelCodeByOrderId(order.getId().toHexString());
					order.setParcelCode(parcelCodeTemp);//设置包裹号
					courier = userService.findOne(order.getUserId());
					if(courier != null){
						userVO = new UserVO();
						userVO.setLoginName(courier.getLoginName());
						userVO.setRealName(courier.getRealName());
						order.setUserVO(userVO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return orderPage;		
	}



	/**
	 * Description: 查看物流信息
	 * @param areaCode 站点编码
	 * @param mailNum 运单号
	 * @return 页面
	 * @author  liyanlei
	 * 2016年4月22日下午6:31:35
	 */
	@RequestMapping(value="/getOrderMail", method=RequestMethod.GET)
	public String getOrderMail(String areaCode, String mailNum, final HttpServletRequest request, Model model) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			Order order = orderService.findOneByMailNum(areaCode, mailNum);
			model.addAttribute("order", order);
		} catch (Exception e) {
			logger.error("===查看物流信息===出错 :" + e.getMessage());
		}
		return "page/showOrderMail";
	}
	
	/**
	 * Description: 导出数据
	 * @param status 状态
	 * @param areaCode 站点编号
	 * @param mailNum 运单号
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String areaCode, Integer status, String arriveBetween_expt, String mailNum,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			//设置查询条件
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.orderStatus = status;
			//orderQueryVO.arriveBetween = arriveBetween_expt;
			orderQueryVO.mailNum = mailNum;
			orderQueryVO.areaCode = areaCode;
			//公司查询
			if(StringUtils.isBlank(areaCode)){//查询全部 -- 同一个公司的所有站点
				//同一个公司的所有站点
				List<SiteStatus> statusList = new ArrayList<SiteStatus>();
				statusList.add(SiteStatus.APPROVE);
				statusList.add(SiteStatus.INVALID);
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyIdAndStatusList(user.getCompanyId(), statusList);
				List<String> areaCodeList = new ArrayList<String>();
				if(siteVOList != null && siteVOList.size() > 0){
					for (SiteVO siteVO : siteVOList){
						areaCodeList.add(siteVO.getAreaCode());
					}
				}
				orderQueryVO.areaCodeList = areaCodeList;
			}
			//查询数据
			List<Order> orderList = null;
			if(orderQueryVO.areaCodeList != null  && orderQueryVO.areaCodeList.size() > 0){
				orderList = orderService.findOrders(orderQueryVO);
			}
			//导出==数据写到Excel中并写入response下载
			//表格数据
			List<List<String>> dataList = new ArrayList<List<String>>();
			List<String> row = null;
			String parcelCodeTemp = null;
			
			if(orderList != null){
				for(Order order : orderList){
					row = new ArrayList<String>();
					parcelCodeTemp = orderParcelService.findParcelCodeByOrderId(order.getId().toHexString());
					row.add(parcelCodeTemp);//设置包裹号
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
					row.add(Dates.formatDateTime_New(order.getDateDriverGeted()));
					row.add(Dates.formatDate2(order.getDateMayArrive()));
					row.add(Dates.formatDateTime_New(order.getDateArrived()));
					setCourier(order.getUserId(), row);
					if(order.getOrderStatus() == null){
						row.add("未到站");
					}else{
						row.add(order.getOrderStatus().getMessage());
					}
					dataList.add(row);
				}
			}
			
			//表头
			String[] titles = { "包裹号", "运单号", "订单号", "来源", "收货人", "收货人手机" , "收货人地址" , "司机取货时间" , "预计到站时间", "到站时间", "派送员", "派送员手机", "状态" };
			int[] colWidths = { 4000, 5000, 5000, 2000, 2000, 3500, 12000, 5500, 3500, 5500, 2000, 3500, 2000};
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.exportExcel("运单查询", dataList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===运单查询数据导出===出错:" + e.getMessage());
		}
	
	}

	/**
	 * 设置订单的派件员信息
	 * @param userId
	 * @param row
     */
	private void setCourier(String userId, List<String> row){
		if(userId == null || "".equals(userId)){
			row.add("");
			row.add("");
		}else{
			User courier = userService.findOne(userId);
			if(courier != null){
				row.add(courier.getRealName());
				row.add(courier.getLoginName());
			}else{
				row.add("");
				row.add("");
			}
		}
	}

}
