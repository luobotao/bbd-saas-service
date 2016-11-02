package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.Option;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;
import com.google.common.collect.Lists;
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
import java.util.*;

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
	 * 跳转到运单查询页面
	 * @param pageIndex 当前页,默认第一页
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param statusStr 运单状态集合status1,status2---
	 * @param arriveBetween 到站时间范围
	 * @param mailNum 运单号
	 * @param request 请求
     * @param model 携带数据
     * @return page
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String areaCodeStr, String statusStr, String arriveBetween, String mailNum, Integer siteStatus, Integer areaFlag, final HttpServletRequest request, Model model) {
		try {
			siteStatus = Numbers.defaultIfNull(siteStatus, -1);
			areaFlag = Numbers.defaultIfNull(areaFlag, -1);
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//到站时间
			arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -2));
			//查询数据
			PageModel<Order> orderPage = getList(null, null, null, pageIndex, areaCodeStr, statusStr, arriveBetween, mailNum, siteStatus, areaFlag, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			if(orderPage == null || orderPage.getDatas() == null){
				orderPage = new PageModel<Order>();
			}
			logger.info("=====运单查询页面列表===" + orderPage);
			model.addAttribute("orderPage", orderPage);
			//查询登录用户的公司下的所有站点
			model.addAttribute("siteList",  SiteCommon.getSiteOptions(siteService, currUser.getCompanyId(), siteStatus, areaFlag));
			model.addAttribute("arriveBetween", arriveBetween);
		} catch (Exception e) {
			logger.error("===跳转到运单查询页面==出错 :" + e.getMessage());
		}
		return "page/mailQuery";
	}
	
	/**
	 * Description: 分页查询，Ajax更新列表
	 * @param pageIndex 页数
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param statusStr 运单状态集合status1,status2---
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request 请求
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:27:16
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(String prov, String city, String area, Integer pageIndex, String areaCodeStr, String statusStr, String arriveBetween, String mailNum, Integer siteStatus, Integer areaFlag, final HttpServletRequest request) {
		siteStatus = Numbers.defaultIfNull(siteStatus, -1);
		areaFlag = Numbers.defaultIfNull(areaFlag, -1);
		//查询数据
		PageModel<Order> orderPage = new PageModel<Order>();
		try {
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			Map<String, String> siteMap = getOrderQueryAndSiteMap(request, prov, city, area, areaCodeStr, statusStr, arriveBetween,mailNum, orderQueryVO, siteStatus, areaFlag);
			//查询数据
			if(orderQueryVO.areaCodeList != null  && orderQueryVO.areaCodeList.size() > 0){
				orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
			}
			//设置包裹号,派件员快递和电话
			if(orderPage != null && orderPage.getDatas() != null){
				List<Order> dataList = orderPage.getDatas();
				//String parcelCodeTemp = null;
				User courier = null;
				UserVO userVO = null;
				for(Order order : dataList){
					/*parcelCodeTemp = orderParcelService.findParcelCodeByOrderId(order.getId().toHexString());
					order.setParcelCode(parcelCodeTemp);//设置包裹号*/
					//站点名称
					order.setAreaName(siteMap.get(order.getAreaCode()));
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return orderPage;		
	}
	private Map<String, String> getOrderQueryAndSiteMap( final HttpServletRequest request, String prov, String city, String area, String areaCodeStr, String statusStr,String arriveBetween, String mailNum, OrderQueryVO orderQueryVO, Integer siteStatus, Integer areaFlag){
		if(mailNum != null){
			mailNum = mailNum.trim();
		}
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		//设置查询条件
		if(orderQueryVO == null){
			orderQueryVO = new OrderQueryVO();
		}
		//orderQueryVO.arriveStatus=1;//已到站 2.6.1版本后将未到站去掉了
		orderQueryVO.arriveBetween = arriveBetween;
		if(StringUtils.isNotBlank(statusStr) && !"-1".equals(statusStr)){
			String [] statusS = statusStr.split(",");
			List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
			for(String status : statusS){
				orderStatusList.add(OrderStatus.status2Obj(Integer.parseInt(status)));
			}
			orderQueryVO.orderStatusList = orderStatusList;
		}
		orderQueryVO.mailNum = mailNum;
		List<Option> optionList = null;
		//areaCodeList查询
		if(StringUtils.isBlank(areaCodeStr)){//全部(公司下的全部|省市区下的全部)
			List<SiteStatus> statusList = Lists.newArrayList();
			if(siteStatus == null || siteStatus == -1){//全部（有效||无效）
				statusList.add(SiteStatus.APPROVE);
				statusList.add(SiteStatus.INVALID);
			}else{
				statusList.add(SiteStatus.status2Obj(siteStatus));
			}
			optionList = siteService.findOptByCompanyIdAndAddress(currUser.getCompanyId(), prov, city, area, null, statusList, areaFlag);
		}else{//部分站点
			String [] areaCodes = areaCodeStr.split(",");
			optionList = siteService.findByAreaCodes(areaCodes);
		}
		Map<String, String> siteMap = new HashMap<String, String>();
		List<String> areaCodeList = new ArrayList<String>();
		if(optionList != null && optionList.size() > 0){
			for (Option option : optionList){
				areaCodeList.add(option.getCode());
				siteMap.put(option.getCode(), option.getName());
			}
		}
		orderQueryVO.areaCodeList = areaCodeList;
		return siteMap;
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
	 * @param statusStr 状态集合status1,status2---
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param mailNum 运单号
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String prov, String city, String area, String areaCodeStr, String statusStr, String arriveBetween_expt, String mailNum,Integer siteStatus, Integer areaFlag,
						   final HttpServletRequest request, final HttpServletResponse response) {
		try {
			siteStatus = Numbers.defaultIfNull(siteStatus, -1);
			areaFlag = Numbers.defaultIfNull(areaFlag, -1);
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			Map<String, String> siteMap = getOrderQueryAndSiteMap(request, prov, city, area, areaCodeStr, statusStr, arriveBetween_expt,mailNum, orderQueryVO, siteStatus, areaFlag);
			//查询数据
			List<Order> orderList = null;
			if(orderQueryVO.areaCodeList != null  && orderQueryVO.areaCodeList.size() > 0){
				orderList = orderService.findOrders(orderQueryVO);
			}
			//导出==数据写到Excel中并写入response下载
			//表格数据
			List<List<String>> dataList = new ArrayList<List<String>>();
			List<String> row = null;

			if(orderList != null){
				for(Order order : orderList){
					row = new ArrayList<String>();
					row.add(StringUtil.initStr(siteMap.get(order.getAreaCode()), ""));
					row.add(order.getAreaCode());
					row.add(order.getMailNum());
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
					//签收时间 start
					if(order.getOrderStatus() != null && order.getOrderStatus() == OrderStatus.SIGNED){
						row.add(Dates.formatDateTime_New(order.getDateUpd()));
					}else{
						row.add("");
					}
					//派件员
					if(StringUtil.isNotEmpty(order.getUserId()) && StringUtil.isNotEmpty(order.getPostmanUser())){
						row.add(order.getPostmanUser());
						row.add(order.getPostmanPhone());
					}else{
						row.add("");
						row.add("");
					}
					if(order.getOrderStatus() == null){
						row.add("未到站");
					}else{
						row.add(order.getOrderStatus().getMessage());
					}
					//异常单子展示异常信息
					if(order.getOrderStatus()==OrderStatus.RETENTION || order.getOrderStatus()==OrderStatus.REJECTION){
						List<Express> expresses = order.getExpresses();
						if(expresses!=null && expresses.size()>0){
							Express express= expresses.get(expresses.size()-1);
							String reson = express.getRemark()==null?"":express.getRemark().replaceAll("订单已被滞留，滞留原因：","").replaceAll("订单已被拒收，拒收原因:","");
							row.add(reson);
						}

					}
					dataList.add(row);
				}
			}
			
			//表头
			String[] titles = { "站点名称","站点编码",  "运单号", "收货人", "收货人手机" , "收货人地址" , "司机取货时间" , "预计到站时间", "到站时间", "签收时间", "派送员", "派送员手机", "状态" ,"异常原因" };
			int[] colWidths = {  6000, 3500, 5000, 3000, 3500, 12000, 5500, 3500, 5500, 5500,  3000,  3500, 3000,4000};
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.exportExcel("运单查询导出", dataList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===运单查询数据导出===出错:" + e.getMessage());
		}
	
	}
}
