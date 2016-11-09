package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.DangDangGetSiteLogService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.controllers.service.CommonService;
import com.bbd.saas.mongoModels.DangDangGetSiteLog;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExportUtil;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.Option;
import com.bbd.saas.vo.Reciever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/superAreaHandle")
public class SuperAreaHandleController {
	
	public static final Logger logger = LoggerFactory.getLogger(SuperAreaHandleController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	CommonService commonService;
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	DangDangGetSiteLogService dangDangGetSiteLogService;
	/**
	 * Description: 跳转到超区件处理页面
	 * @return
	 * @author liyanlei
	 * 2016年11月08日下午6:27:46
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String toIndexPage() {
		return "page/superAreaHandle";
	}

	/**
	 * Description: 分页查询，Ajax更新列表
	 * @param mailNumStr 运单号集合
	 * @param pageIndex 当前页
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/getList")
	public PageModel<Order> getList(String mailNumStr, Integer pageIndex) {
		//查询数据
		PageModel<Order> pageModel = new PageModel<Order>();
		try {
			if(mailNumStr == null){
				return pageModel;
			}else {
				mailNumStr = mailNumStr.trim();
			}
			String [] mailNums = mailNumStr.split("\n|\r\n");
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			pageModel.setTotalCount(mailNums.length);
			//设置查询条件
			int max = mailNums.length - pageIndex * pageModel.getPageSize() > pageModel.getPageSize() ? pageModel.getPageSize() : mailNums.length - pageIndex * pageModel.getPageSize();
			String[] subMailNums = new String[max];
			for (int i = 0; i < max; i++) {
				subMailNums[i] = mailNums[pageIndex * pageModel.getPageSize() + i];
			}
			List<Order> orderList = this.orderService.findListByMailNums(subMailNums);
			//按照输入运单号的顺序,对orderList排序
			pageModel.setDatas(this.sortOrder(orderList, subMailNums, null));
			this.commonService.setCourierNameAndPhone(pageModel);
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
	}

	/**
	 * 按照输入运单号的顺序,对orderList排序
	 * @param orderList 运单列表
	 * @param mailNums 运单号集合
     * @return
     */
	private List<Order> sortOrder(List<Order> orderList, String [] mailNums, Set<String> areaCodeSet){
		List<Order> orderSortedList = new ArrayList<Order>();
		Map<String, Order> map = new HashMap<String, Order>();
		if(areaCodeSet != null){
			for(Order order : orderList){
				map.put(order.getMailNum(), order);
				areaCodeSet.add(order.getAreaCode());
			}
		}else{
			for(Order order : orderList){
				map.put(order.getMailNum(), order);
			}
		}

		for(String mailNum : mailNums){
			Order order = map.get(mailNum);
			if(order == null){
				order = new Order();
				order.setMailNum(mailNum);
				Reciever reciever = new Reciever();
				order.setReciever(reciever);
			}
			orderSortedList.add(order);
		}
		return orderSortedList;
	}
	/**
	 * Description: 导出数据
	 * @param mailNumStr_expt 运单号
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String mailNumStr_expt,final HttpServletRequest request, final HttpServletResponse response) {
		try {
			if(mailNumStr_expt != null){
				mailNumStr_expt = mailNumStr_expt.trim();
			}
			String [] mailNums = mailNumStr_expt.split("\n|\r\n");
			//查询数据
			List<Order> orderList = this.orderService.findListByMailNums(mailNums);
			List<Order> orderSortedList = null;
			Set<String> areaCodeSet = new HashSet<String>();
			if(orderList != null){
				//按照输入运单号的顺序，导出数据
				orderSortedList = this.sortOrder(orderList, mailNums, areaCodeSet);
			}
			String[] areaCodes = new String[]{};
			areaCodeSet.toArray(areaCodes);
			List<Option> optionList = this.siteService.findByAreaCodes(areaCodes);
			Map<String, String> siteMap = new HashMap<String, String>();
			if(optionList != null && optionList.size() > 0){
				for (Option option : optionList){
					siteMap.put(option.getCode(), option.getName());
				}
			}
			//导出==数据写到Excel中并写入response下载
			commonService.exportSitesToExcel(orderSortedList, siteMap, response);
		} catch (Exception e) {
			logger.error("===数据导出===出错:" + e.getMessage());
		}
	}

	/**
	 * Description: 跳转到超区件处理页面
	 * @return
	 * @author liyanlei
	 * 2016年11月08日下午6:27:46
	 */
	@RequestMapping(value="dangdang", method=RequestMethod.GET)
	public String toDangdangPage() {
		return "page/superAreaDD";
	}

	/**
	 * Description: 分页查询，Ajax更新列表
	 * @param orderIdStr 运单号集合
	 * @param pageIndex 当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDDList")
	public PageModel<DangDangGetSiteLog> getDDList(String orderIdStr, Integer pageIndex) {
		//查询数据
		PageModel<DangDangGetSiteLog> pageModel = new PageModel<DangDangGetSiteLog>();
		try {
			if(orderIdStr == null){
				return pageModel;
			}else {
				orderIdStr = orderIdStr.trim();
			}
			String [] orderIds = orderIdStr.split("\n|\r\n");
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			pageModel.setTotalCount(orderIds.length);
			//设置查询条件
			int max = orderIds.length - pageIndex * pageModel.getPageSize() > pageModel.getPageSize() ? pageModel.getPageSize() : orderIds.length - pageIndex * pageModel.getPageSize();
			String[] subOrderIds = new String[max];
			for (int i = 0; i < max; i++) {
				subOrderIds[i] = orderIds[pageIndex * pageModel.getPageSize() + i];
			}
			List<DangDangGetSiteLog> dataList = this.dangDangGetSiteLogService.findListByOrderIds(subOrderIds);
			//按照输入运单号的顺序,对orderList排序
			pageModel.setDatas(this.sortDDOrder(dataList, subOrderIds));
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
	}

	/**
	 * 按照输入运单号的顺序,对orderList排序
	 * @param orderList 运单列表
	 * @param orderIds 运单号集合
	 * @return
	 */
	private List<DangDangGetSiteLog> sortDDOrder(List<DangDangGetSiteLog> orderList, String [] orderIds){
		List<DangDangGetSiteLog> dataSortedList = new ArrayList<DangDangGetSiteLog>();
		Map<String, DangDangGetSiteLog> map = new HashMap<String, DangDangGetSiteLog>();
		for(DangDangGetSiteLog dangDangGetSiteLog : orderList){
			map.put(dangDangGetSiteLog.getOrderId(), dangDangGetSiteLog);
		}
		for(String orderId : orderIds){
			DangDangGetSiteLog dangDangGetSiteLog = map.get(orderId);
			if(dangDangGetSiteLog == null){
				dangDangGetSiteLog = new DangDangGetSiteLog();
				dangDangGetSiteLog.setOrderId(orderId);
			}
			dataSortedList.add(dangDangGetSiteLog);
		}
		return dataSortedList;
	}
	/**
	 * Description: 导出数据
	 * @param orderIdStr_expt 运单号
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportDDToExcel", method=RequestMethod.GET)
	public void exportDDData(String orderIdStr_expt, final HttpServletResponse response) {
		try {
			if(orderIdStr_expt != null){
				orderIdStr_expt = orderIdStr_expt.trim();
			}
			String [] orderIds = orderIdStr_expt.split("\n|\r\n");
			//查询数据
			List<DangDangGetSiteLog> dataList = this.dangDangGetSiteLogService.findListByOrderIds(orderIds);
			List<DangDangGetSiteLog> dataSortedList = null;
			if(dataList != null){
				//按照输入运单号的顺序，导出数据
				dataSortedList = this.sortDDOrder(dataList, orderIds);
			}
			//导出==数据写到Excel中并写入response下载
			List<List<String>> rowList = new ArrayList<List<String>>();
			if(dataSortedList != null){
				for(DangDangGetSiteLog dangDangGetSiteLog : dataSortedList){
					List<String> row = new ArrayList<String>();
					row.add(dangDangGetSiteLog.getSiteNo());
					row.add(dangDangGetSiteLog.getSiteName());
					row.add(dangDangGetSiteLog.getOrderId());
					row.add(dangDangGetSiteLog.getResponseMap().replaceAll("\n", ""));
					row.add(Dates.formatDateTime_New(dangDangGetSiteLog.getDateAdd()));
					rowList.add(row);
				}
			}
			//表头
			String[] titles = {  "siteNo", "siteName","orderId", "responseMap", "dateAdd" };
			int[] colWidths = {   3500, 6000, 5000, 24000, 6000};
			ExportUtil.exportExcel("数据导出", rowList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===数据导出===出错:" + e.getMessage());
		}
	}
}
