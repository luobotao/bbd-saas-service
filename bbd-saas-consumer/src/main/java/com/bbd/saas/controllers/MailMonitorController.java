package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.*;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.OrderMonitorVO;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.SiteVO;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 运单监控
 */
@Controller
@RequestMapping("/mailMonitor")
@SessionAttributes("mailMonitor")
public class MailMonitorController {
	
	public static final Logger logger = LoggerFactory.getLogger(MailMonitorController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	OrderPacelService orderPacelService;
	@Autowired
	SiteService siteService;
	@Autowired
	ToOtherSiteLogService toOtherSiteLogService;

	/**
	 * Description: 跳转到运单监控页面
	 * @param pageIndex 页数
	 * @param areaCode 站点编号
	 * @param timeBetween 时间范围
	 * @param request 请求
	 * @param model
	 * @return 返回页面
	 * @author liyanlei
	 * 2016年5月6日下午4:27:46
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String areaCode, String timeBetween, final HttpServletRequest request, Model model) {
		try {
			//设置默认查询条件
			timeBetween = StringUtil.initStr(timeBetween, Dates.getBetweenTime(new Date(), -20));
			//查询数据
			PageModel<OrderMonitorVO> orderMonitorVOPage = getList(pageIndex, areaCode, timeBetween, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
			logger.info("=====运单监控页面列表===" + orderMonitorVOPage);
			model.addAttribute("orderMonitorVOPage", orderMonitorVOPage);
			model.addAttribute("timeBetween", timeBetween);
			model.addAttribute("siteList", siteVOList);
			return "page/mailMonitor";
		} catch (Exception e) {
			logger.error("===跳转到运单监控页面==出错 :" + e.getMessage());
		}
		return "page/mailQuery";
	}

	/**
	 * 分页查询，Ajax更新列表
	 * @param pageIndex 页数
	 * @param areaCode 站点编号
	 * @param timeBetween 时间范围
	 * @param request 请求
     * @return 分页列表数据
     */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<OrderMonitorVO> getList(Integer pageIndex, String areaCode, String timeBetween, final HttpServletRequest request) {
		//查询数据
		PageModel<OrderMonitorVO> orderMonitorVOPage = new PageModel<OrderMonitorVO>();
		try {
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			//设置默认查询条件
			areaCode = StringUtil.initStr(areaCode, "141725-001");
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			//设置查询条件
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.areaCode = areaCode;
			//分页数据
			orderMonitorVOPage.setPageNo(pageIndex);
			orderMonitorVOPage.setTotalCount(20);
			//列表数据
			List<OrderMonitorVO> dataList = new ArrayList<OrderMonitorVO>();
			OrderMonitorVO orderMonitorVO = new OrderMonitorVO();
			orderMonitorVO.setDispatched(orderService.getDispatchedNums(areaCode, timeBetween));
			dataList.add(orderMonitorVO);
			orderMonitorVOPage.setDatas(dataList);
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return orderMonitorVOPage;
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
			orderQueryVO.arriveBetween = arriveBetween_expt;
			orderQueryVO.mailNum = mailNum;
			orderQueryVO.areaCode = areaCode;
			//查询数据
			List<Order> orderList = orderService.findOrders(orderQueryVO);	
			//导出==数据写到Excel中并写入response下载
			//表格数据
			List<List<String>> dataList = new ArrayList<List<String>>();
			List<String> row = null;
			String parcelCodeTemp = null;
			
			if(orderList != null){
				for(Order order : orderList){
					row = new ArrayList<String>();
					parcelCodeTemp = orderPacelService.findParcelCodeByOrderId(order.getId().toHexString());
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
					row.add(Dates.formatDateTime_New(order.getDatePrint()));
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
			ExportUtil.exportExcel("运单监控", dataList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===运单监控数据导出===出错:" + e.getMessage());
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
