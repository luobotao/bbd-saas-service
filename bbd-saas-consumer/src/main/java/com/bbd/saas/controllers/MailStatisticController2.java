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
import com.bbd.saas.vo.Option;
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
 * 统计汇总
 */
@Controller
@RequestMapping("/mailStatistic2")
public class MailStatisticController2 {

	public static final Logger logger = LoggerFactory.getLogger(MailStatisticController2.class);
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	ExpressStatStationService expressStatStationService;

	/**
	 * Description: 跳转到统计汇总页面
	 * @param pageIndex 页数
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param time 时间
	 * @param request 请求
	 * @param model
	 * @return 返回页面
	 * @author liyanlei
	 * 2016年5月6日下午4:27:46
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String areaCodeStr, String time, Integer siteStatus, Integer areaFlag, final HttpServletRequest request, Model model) {
		try {
			//设置默认查询条件 -- 当天
			time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD));
			//查询数据
			PageModel<ExpressStatStation> pageModel = getList(null, null, null, pageIndex, areaCodeStr, time, siteStatus, areaFlag, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			logger.info("=====统计汇总页面列表===" + pageModel);
			model.addAttribute("pageModel", pageModel);
			model.addAttribute("time", time);
			model.addAttribute("siteList", SiteCommon.getSiteOptions(siteService, currUser.getCompanyId(), currUser.getGroup()));
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
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param time 时间(一天的时间)
	 * @param request 请求
	 * @return 分页列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<ExpressStatStation> getList(String prov, String city, String area, Integer pageIndex, String areaCodeStr, String time, Integer siteStatus, Integer areaFlag, final HttpServletRequest request) {
		siteStatus = Numbers.defaultIfNull(siteStatus, -1);
		areaFlag = Numbers.defaultIfNull(areaFlag, -1);
		//查询数据
		PageModel<ExpressStatStation> pageModel = new PageModel<ExpressStatStation>();
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			//设置默认查询条件 -- 当天
			time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD));
			if(currUser.getRole() == UserRole.COMPANY){//公司角色
				//设置默认查询条件
				//areaCode = StringUtil.initStr(areaCode, "141725-001");
				//分页查询站点
				PageModel<Option> sitePageModel = new PageModel<Option>();
				sitePageModel.setPageNo(pageIndex);
				List<SiteStatus> statusList = Lists.newArrayList();
				if(siteStatus == null || siteStatus == -1){//全部（有效||无效）
					statusList.add(SiteStatus.APPROVE);
					statusList.add(SiteStatus.INVALID);
				}else{
					statusList.add(SiteStatus.status2Obj(siteStatus));
				}
				List<String> areaCodeList = getAreaCodeAndStatusList(currUser.getCompanyId(), currUser.getGroup(), prov, city, area, areaCodeStr, statusList, areaFlag);
				//分页查询
				sitePageModel = siteService.getSitePage(sitePageModel, currUser.getCompanyId(), areaCodeList, statusList, areaFlag);
				pageModel.setTotalCount(sitePageModel.getTotalCount());
				List<Option> siteList = sitePageModel.getDatas();
				if(siteList != null && siteList.size() > 0){
					List<ExpressStatStation> dataList = getCurrPageDataList(siteList, currUser.getCompanyId(), time);
					//最后一页 -- 需要显示汇总行
					if((pageModel.getPageNo() + 1) == pageModel.getTotalPages()){
						ExpressStatStation summary = getSummaryRow(areaCodeList, currUser.getCompanyId(), time);
						dataList.add(summary);
					}
					pageModel.setDatas(dataList);
				}
			}else if(currUser.getRole() == UserRole.SITEMASTER){//z站长
				if(currUser.getSite() != null){
					areaCodeStr = currUser.getSite().getAreaCode();
					//列表数据
					List<ExpressStatStation> dataList = findOneSiteData(areaCodeStr, time);
					pageModel.setTotalCount(1);
					pageModel.setDatas(dataList);
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
	}

	/**
	 * 获得站点编码集合和状态集合
	 * @param companyId 公司id
	 * @param prov 省
	 * @param city 市
	 * @param area 区
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param statusList 状态集合
     * @return 站点编码集合 areaCodeList
     */
	private List<String> getAreaCodeAndStatusList(String companyId, String group, String prov, String city, String area, String areaCodeStr, List<SiteStatus> statusList, Integer areaFlag){
		List<String> areaCodeList = null;
		if(StringUtils.isBlank(areaCodeStr)){//全部(公司下的全部areaCodeList == null|省市区下的全部 areaCodeList.isEmpty)
			if(StringUtils.isNotBlank(prov)){//某个省市区下的全部站点
				List<Option> optionList = siteService.findOptByCompanyIdAndAddress(companyId, group, prov, city, area, null, statusList, areaFlag);
				areaCodeList = new ArrayList<String>();
				if(optionList != null && !optionList.isEmpty()){
					for(Option option : optionList){
						areaCodeList.add(option.getCode());
					}
				}
			}
		}else{//部分站点
			String [] areaCodes = areaCodeStr.split(",");
			areaCodeList = Arrays.asList(areaCodes);
		}
		return areaCodeList;
	}

	/**
	 * 查询汇总行数据，只有最后一页才会有汇总行
	 * @param areaCodeList 站点编码集合
	 * @param companyId 公司Id
	 * @param time 日期
     * @return 汇总行
     */
	private ExpressStatStation getSummaryRow(List<String> areaCodeList, String companyId, String time){
		ExpressStatStation summary = null;
		if(areaCodeList == null){//查询全部 -- 同一个公司的所有站点
			summary = expressStatStationService.findSummaryByCompanyIdAndTime(companyId, time);
		}else{//部分站点
			summary = expressStatStationService.findSummaryByAreaCodesAndTime(areaCodeList, time);
		}
		if(summary == null){
			summary = new ExpressStatStation(companyId, null, null, null);
		}
		summary.setSitename("总计");
		return summary;
	}

	/**
	 * 查询当前页的数据
	 * @param siteList
	 * @param companyId 公司Id
	 * @param time 日期
	 * @return List<ExpressStatStation> dataList
     */
	private List<ExpressStatStation> getCurrPageDataList(List<Option> siteList, String companyId, String time){
		List<String> areaCodeList = new ArrayList<String>();
		for(Option option : siteList){
			areaCodeList.add(option.getCode());
		}
		Map<String, ExpressStatStation> essMap = expressStatStationService.findByAreaCodeListAndTime(areaCodeList, time);
		return toRowDatas(siteList, companyId, time, essMap);
	}
	private List<ExpressStatStation> toRowDatas(List<Option> siteList, String companyId, String time, Map<String, ExpressStatStation> essMap){
		List<ExpressStatStation> dataList = new ArrayList<ExpressStatStation>();
		ExpressStatStation ess = null;
		for(Option option : siteList){
			ess = essMap.get(option.getCode());
			if(ess == null){
				ess = new ExpressStatStation(companyId, option.getId(), option.getName(),time);
				ess.setSitename(option.getName());
			}
			dataList.add(ess);
		}
		return dataList;
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
	 * @param areaCodeStr 站点编号集合areaCode1,areaCode2---
	 * @param time 时间查询范围
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年5月10日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String prov, String city, String area, String areaCodeStr, String time,
						   final HttpServletRequest request, final HttpServletResponse response) {
		//设置默认查询条件 -- 当天
		time = StringUtil.initStr(time, Dates.dateToString(Dates.addDays(new Date(), -1), Constants.DATE_PATTERN_YMD2));
		try {
			List<ExpressStatStation> dataList = null;
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			String[] titles = null;
			int[] colWidths = null;
            boolean isShowSite = false;
			if(currUser.getRole() == UserRole.COMPANY){//公司角色
				List<Option> optionList = null;
				if(StringUtils.isBlank(areaCodeStr)){//全部(公司下的全部||省市区下的全部 )
					List<SiteStatus> statusList = new ArrayList<SiteStatus>();
					statusList.add(SiteStatus.APPROVE);
					statusList.add(SiteStatus.INVALID);
					optionList = siteService.findOptByCompanyIdAndAddress(currUser.getCompanyId(), currUser.getGroup(), prov, city, area, null, statusList);
				}else{//部分站点
					String [] areaCodes = areaCodeStr.split(",");
					optionList = siteService.findByAreaCodes(areaCodes);
				}
				if(optionList != null && !optionList.isEmpty()){
					Map<String, ExpressStatStation> essMap = null;
					ExpressStatStation summary = null;
					if(StringUtils.isBlank(areaCodeStr) && StringUtils.isBlank(prov)){//公司下的全部站点
						essMap = expressStatStationService.findMapByCompanyIdAndTime(currUser.getCompanyId(), time);
						summary = expressStatStationService.findSummaryByCompanyIdAndTime(currUser.getCompanyId(), time);
					}else{//多个站点
						List<String> areaCodeList = new ArrayList<String>();
						for(Option option : optionList){
							areaCodeList.add(option.getCode());
						}
						essMap = expressStatStationService.findByAreaCodeListAndTime(areaCodeList, time);
						summary = expressStatStationService.findSummaryByAreaCodesAndTime(areaCodeList, time);
					}
					dataList = toRowDatas(optionList, currUser.getCompanyId(), time, essMap);
					if(summary == null){
						summary = new ExpressStatStation(currUser.getCompanyId(), null, null, null);
					}
					summary.setSitename("总计");
					dataList.add(summary);
				}
				//表头 "未到站订单数",  3000,
				titles = new String[]{"站点",  "未到站订单数", "已到站订单数", "已分派", "签收", "滞留", "拒收", "转站", "转其他快递"};
				colWidths = new int[] {13000,  3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000};
				isShowSite = true;
			}else if(currUser.getRole() == UserRole.SITEMASTER){//站长角色
				if(currUser.getSite() != null){
					String areaCode = currUser.getSite().getAreaCode();
					//列表数据
					dataList = findOneSiteData(areaCode, time);
				}
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
