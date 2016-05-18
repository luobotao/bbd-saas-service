package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.ToOtherSiteLogService;
import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.OrderMonitorVO;
import com.bbd.saas.vo.SiteVO;
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
import java.util.Date;
import java.util.List;

/**
 * 运单监控
 */
@Controller
@RequestMapping("/mailMonitor")
public class MailMonitorController {

	public static final Logger logger = LoggerFactory.getLogger(MailMonitorController.class);

	@Autowired
	OrderService orderService;
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	ToOtherSiteLogService toOtherSiteLogService;
	@Autowired
	OrderLogService orderLogService;

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
			timeBetween = StringUtil.initStr(timeBetween, Dates.getBetweenTime(new Date(), -2));
			//查询数据
			PageModel<OrderMonitorVO> orderMonitorVOPage = getList(pageIndex, areaCode, timeBetween, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
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
		PageModel<OrderMonitorVO> pageModel = new PageModel<OrderMonitorVO>();
		try {
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			//设置默认查询条件
			//areaCode = StringUtil.initStr(areaCode, "141725-001");
			if(areaCode != null && !"".equals(areaCode)){//只查询一个站点
				OrderMonitorVO orderMonitorVO = getOneSiteAllData(areaCode, timeBetween);
				//列表数据
				List<OrderMonitorVO> dataList = new ArrayList<OrderMonitorVO>();
				dataList.add(orderMonitorVO);
				pageModel.setTotalCount(dataList.size());
				pageModel.setDatas(dataList);
			}else {//查询本公司下的所有站点 （全部）
				//当前登录的用户信息
				User currUser = adminService.get(UserSession.get(request));
				PageModel<Site> sitePageModel = new PageModel<Site>();
				sitePageModel.setPageNo(pageIndex);
				PageModel<Site> sitePage = siteService.getSitePage(sitePageModel,currUser.getCompanyId(), SiteStatus.APPROVE);
				pageModel.setTotalCount(sitePage.getTotalCount());//总条数
				List<Site> siteList = sitePage.getDatas();//数据
				if(siteList != null && siteList.size() > 0){
					//列表数据
					List<OrderMonitorVO> dataList = new ArrayList<OrderMonitorVO>();
					pageModel.setTotalCount(sitePage.getTotalCount());
					for(Site site : siteList){
						OrderMonitorVO  orderMonitorVO = getOneSiteData(site.getAreaCode(), timeBetween);
						orderMonitorVO.setSiteName(site.getName());
						dataList.add(orderMonitorVO);
					}
					pageModel.setDatas(dataList);
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
	}

	/**
	 * 获得一个站点的名称和不同状态的订单数
	 * @param areaCode 站点编号
	 * @param timeBetween 查询时间范围
	 * @return 运单监控实体
	 */
	private OrderMonitorVO  getOneSiteAllData(String areaCode, String timeBetween){
		OrderMonitorVO  orderMonitorVO = getOneSiteData(areaCode, timeBetween);
		Site site = siteService.findSiteByAreaCode(areaCode);
		String siteName = null;
		if(site != null){
			siteName = site.getName();
		}
		orderMonitorVO.setSiteName(StringUtil.initStr(siteName,""));
		return orderMonitorVO;
	}
	/**
	 * 查询一个站点的订单统计数据
	 */
	private OrderMonitorVO  getOneSiteData(String areaCode, String timeBetween){
		String start = null, end = null;
		if(timeBetween != null && !"".equals(timeBetween)){
			String[] times  = timeBetween.split("-");
			start = times[0];
			end = times[1];
		}
		//转站从mongodb中toOtherSiteLog表中取数据，其他数据从mysql中的orderLog表中取数据
		OrderMonitorVO orderMonitorVO = orderLogService.statisticOrderNum(areaCode, start, end);
		//历史未到站订单数
		orderMonitorVO.setNoArrive(orderService.getNoArriveHis(areaCode));
		//查询转站数目
		orderMonitorVO.setToOtherSite(toOtherSiteLogService.countByFromAreaCodeAndTime(areaCode, timeBetween));
		return orderMonitorVO;
	}
	/**
	 * Description: 导出数据
	 * @param areaCode 站点编号
	 * @param timeBetween 时间查询范围
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年5月10日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String areaCode, String timeBetween,
						   final HttpServletRequest request, final HttpServletResponse response) {
		List<OrderMonitorVO> monitorVOList = null;
		try {
			monitorVOList = new ArrayList<OrderMonitorVO>();
			if (areaCode != null && !"".equals(areaCode)) {//只查询一个站点
				OrderMonitorVO orderMonitorVO = getOneSiteAllData(areaCode, timeBetween);
				//列表数据
				monitorVOList.add(orderMonitorVO);
			} else {//查询本公司下的所有站点 （全部）
				//当前登录的用户信息
				User currUser = adminService.get(UserSession.get(request));
				//当前公司下的所有站点
				List<Site> siteList = siteService.findSiteListByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
				if (siteList != null && siteList.size() > 0) {
					//列表数据
					for (Site site : siteList) {
						OrderMonitorVO orderMonitorVO = getOneSiteData(site.getAreaCode(), timeBetween);
						orderMonitorVO.setSiteName(site.getName());
						monitorVOList.add(orderMonitorVO);
					}
				}
			}
			//导出==数据写到Excel中并写入response下载
			List<List<String>> dataList = objectToTable(monitorVOList);
			//表头
			String[] titles = {"站点", "未到站订单数", "已到站订单数", "未分派数", "已分派数", "签收数", "滞留数", "拒收数", "转站数"};
			int[] colWidths = {10000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000};
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.exportExcel("运单监控", dataList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===运单监控数据导出===出错:" + e.getMessage());
		}
	}
	private List<List<String>> objectToTable (List <OrderMonitorVO> monitorVOList) {
		//表格数据
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> row = null;
		if (monitorVOList != null) {
			for (OrderMonitorVO monitorVO : monitorVOList) {
				row = new ArrayList<String>();
				row.add(monitorVO.getSiteName());//
				row.add(monitorVO.getNoArrive() + "");//
				row.add(monitorVO.getArrived() + "");//
				row.add(monitorVO.getNoDispatch() + "");//
				row.add(monitorVO.getDispatched() + "");//
				row.add(monitorVO.getSigned() + "");//
				row.add(monitorVO.getRetention() + "");//
				row.add(monitorVO.getRejection() + "");//
				row.add(monitorVO.getToOtherSite() + "");//
				dataList.add(row);
			}
		}
		return dataList;
	}

}
