package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.ExpressStatStationService;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.models.ExpressStatStation;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
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
import java.util.Map;

/**
 * 统计汇总
 */
@Controller
@RequestMapping("/mailStatistic")
public class MailStatisticController {

	public static final Logger logger = LoggerFactory.getLogger(MailStatisticController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	ExpressStatStationService expressStatStationService;

	/**
	 * Description: 跳转到统计汇总页面
	 * @param pageIndex 页数
	 * @param areaCode 站点编号
	 * @param time 时间范围
	 * @param request 请求
	 * @param model
	 * @return 返回页面
	 * @author liyanlei
	 * 2016年5月6日下午4:27:46
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String areaCode, String time, final HttpServletRequest request, Model model) {
		try {
			//设置默认查询条件 -- 当天
			time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD2));
			//查询数据
			PageModel<ExpressStatStation> pageModel = getList(pageIndex, areaCode, time, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			List<SiteStatus> statusList = new ArrayList<SiteStatus>();
			statusList.add(SiteStatus.APPROVE);
			statusList.add(SiteStatus.INVALID);
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyIdAndStatusList(currUser.getCompanyId(), statusList);
			logger.info("=====统计汇总页面列表===" + pageModel);
			model.addAttribute("pageModel", pageModel);
			model.addAttribute("time", time);
			model.addAttribute("siteList", siteVOList);
			model.addAttribute("role", currUser.getRole());
			return "page/mailStatistic";
		} catch (Exception e) {
			logger.error("===跳转到统计汇总页面==出错 :" + e.getMessage());
		}
		return "page/mailStatistic";
	}

	/**
	 * 分页查询，Ajax更新列表
	 * @param pageIndex 页数
	 * @param areaCode 站点编号
	 * @param time 时间(一天的时间)
	 * @param request 请求
	 * @return 分页列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<ExpressStatStation> getList(Integer pageIndex, String areaCode, String time, final HttpServletRequest request) {
		//查询数据
		PageModel<ExpressStatStation> pageModel = new PageModel<ExpressStatStation>();
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			//设置默认查询条件 -- 当天
			time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD2));
			if(currUser.getRole() == UserRole.COMPANY){//公司角色
				//设置默认查询条件
				//areaCode = StringUtil.initStr(areaCode, "141725-001");
				if(areaCode != null && !"".equals(areaCode)){//只查询一个站点
					//列表数据
					List<ExpressStatStation> dataList = findOneSiteData(areaCode, time);
					pageModel.setTotalCount(1);
					pageModel.setDatas(dataList);
				}else {//查询本公司下的所有站点 （全部）
					pageModel = expressStatStationService.findPageByCompanyIdAndTime(pageIndex, currUser.getCompanyId(), time);
				}
			}else if(currUser.getRole() == UserRole.SITEMASTER){//z站长
				if(currUser.getSite() != null){
					areaCode = currUser.getSite().getAreaCode();
					//列表数据
					List<ExpressStatStation> dataList = findOneSiteData(areaCode, time);
					pageModel.setTotalCount(1);
					pageModel.setDatas(dataList);
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
	}
	private List<ExpressStatStation> findOneSiteData(String areaCode, String time){
		List<ExpressStatStation> dataList = expressStatStationService.findByAreaCodeAndTime(areaCode, time);
		Site site = siteService.findSiteByAreaCode(areaCode);
		if(dataList == null || dataList.size() == 0){
			dataList = new ArrayList<ExpressStatStation>();
			dataList.add(new ExpressStatStation(site.getCompanyId(), areaCode, site.getName(), time));
		}
		return dataList;
	}
	/**
	 * Description: 导出数据
	 * @param areaCode 站点编号
	 * @param time 时间查询范围
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年5月10日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String areaCode, String time,
						   final HttpServletRequest request, final HttpServletResponse response) {
		List<ExpressStatStation> dataList = null;
		//设置默认查询条件 -- 当天
		time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD2));
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			String[] titles = null;
			int[] colWidths = null;
            boolean isShowSite = false;
			if(currUser.getRole() == UserRole.COMPANY){//公司角色
				if (areaCode != null && !"".equals(areaCode)) {//只查询一个站点
					dataList = findOneSiteData(areaCode, time);
				} else {//查询本公司下的所有站点 （全部）
					//当前公司下的所有站点
					//查询登录用户的公司下的所有站点
					List<SiteStatus> statusList = new ArrayList<SiteStatus>();
					statusList.add(SiteStatus.APPROVE);
					statusList.add(SiteStatus.INVALID);
					List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyIdAndStatusList(currUser.getCompanyId(), statusList);
					if(siteVOList != null && siteVOList.size() > 0){
						if(dataList == null){
							dataList = new ArrayList<ExpressStatStation>();
						}
						Map<String, ExpressStatStation> essMap = expressStatStationService.findMapByCompanyIdAndTime(currUser.getCompanyId(), time);
						ExpressStatStation ess = null;
						for(SiteVO site : siteVOList){
							ess = essMap.get(site.getAreaCode());
							if(ess == null){
								ess = new ExpressStatStation(currUser.getCompanyId(), site.getAreaCode(), site.getName(), time);
								ess.setSitename(site.getName());
							}
							dataList.add(ess);
						}
						ExpressStatStation summary = expressStatStationService.findSummaryByCompanyIdAndTime(currUser.getCompanyId(), time);
						if(summary == null){
							summary = new ExpressStatStation(currUser.getCompanyId(), null, null, null);
						}
						summary.setSitename("总计");
						dataList.add(summary);
					}
				}
				//表头 "未到站订单数",  3000,
				titles = new String[]{"站点",  "未到站订单数", "已到站订单数", "已分派", "签收", "滞留", "拒收", "转站", "转其他快递"};
				colWidths = new int[] {13000,  3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000};
				isShowSite = true;
			}else if(currUser.getRole() == UserRole.SITEMASTER){//站长角色
				if(currUser.getSite() != null){
					areaCode = currUser.getSite().getAreaCode();
				}
				//列表数据
				dataList = findOneSiteData(areaCode, time);
				//表头 "未到站订单数",  3000,
				titles = new String[]{"未到站订单数", "已到站订单数", "已分派", "签收", "滞留", "拒收", "转站", "转其他快递"};
				colWidths = new int[] {3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000};
				isShowSite = false;
			}
			//导出==数据写到Excel中并写入response下载
			List<List<String>> rowList = objectToTable(dataList, isShowSite);
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.exportExcel("统计汇总", rowList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===统计汇总===出错:" + e.getMessage());
		}
	}

	private List<List<String>> objectToTable(List<ExpressStatStation> dataList, boolean isShowSite){
		if(dataList == null){
			return null;
		}
		List<List<String>> rowList = new ArrayList<List<String>>();
		for(ExpressStatStation expressStatStation : dataList){
			List<String> row = new ArrayList<String>();
			if(isShowSite){
				row.add(expressStatStation.getSitename());
			}
			row.add(expressStatStation.getNostationcnt()+"");
			row.add(expressStatStation.getStationcnt()+"");
			row.add(expressStatStation.getDeliverycnt()+"");
			row.add(expressStatStation.getSuccesscnt()+"");
			row.add(expressStatStation.getDailycnt()+"");
			row.add(expressStatStation.getRefusecnt()+"");
			row.add(expressStatStation.getChangestationcnt()+"");
			row.add(expressStatStation.getChangeexpresscnt()+"");
			rowList.add(row);
		}
		return  rowList;
	}

}
