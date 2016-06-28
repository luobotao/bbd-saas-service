package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.ExpressStatStationService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
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
			PageModel<ExpressStatStation> orderMonitorVOPage = getList(pageIndex, areaCode, timeBetween, request);
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
			logger.info("=====统计汇总页面列表===" + orderMonitorVOPage);
			model.addAttribute("orderMonitorVOPage", orderMonitorVOPage);
			model.addAttribute("timeBetween", timeBetween);
			model.addAttribute("siteList", siteVOList);
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
	 * @param timeBetween 时间范围
	 * @param request 请求
	 * @return 分页列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<ExpressStatStation> getList(Integer pageIndex, String areaCode, String timeBetween, final HttpServletRequest request) {
		//查询数据
		PageModel<ExpressStatStation> pageModel = new PageModel<ExpressStatStation>();
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			pageModel.setPageNo(pageIndex);
			//设置默认查询条件
			timeBetween = StringUtil.initStr(timeBetween, Dates.getBetweenTime(new Date(), -2));
			DateBetween dateBetween = new DateBetween();
			List<String> dateList = dateBetween.splitDate(timeBetween);
			//设置默认查询条件
			//areaCode = StringUtil.initStr(areaCode, "141725-001");
			if(areaCode != null && !"".equals(areaCode)){//只查询一个站点
				//列表数据
				List<ExpressStatStation> dataList = expressStatStationService.findByAreaCodeAndTime(areaCode, dateList.get(0), dateList.get(1));
				pageModel.setTotalCount(dataList.size());
				pageModel.setDatas(dataList);
			}else {//查询本公司下的所有站点 （全部）

				PageModel<Site> sitePageModel = new PageModel<Site>();
				sitePageModel.setPageNo(pageIndex);
				PageModel<Site> sitePage = siteService.getSitePage(sitePageModel,currUser.getCompanyId(), SiteStatus.APPROVE);
				pageModel.setTotalCount(sitePage.getTotalCount());//总条数
				List<Site> siteList = sitePage.getDatas();//数据
				if(siteList != null && siteList.size() > 0){
					//列表数据
					List<String> areaCodeList = new ArrayList<String>();
					pageModel.setTotalCount(sitePage.getTotalCount());
					for(Site site : siteList){
						areaCodeList.add(site.getAreaCode());
					}
					pageModel.setDatas(expressStatStationService.findByAreaCodeListAndTime(areaCodeList, dateList.get(0), dateList.get(1)));
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return pageModel;
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
		List<ExpressStatStation> dataList = null;
		DateBetween dateBetween = new DateBetween();
		List<String> dateList = dateBetween.splitDate(timeBetween);
		try {

			if (areaCode != null && !"".equals(areaCode)) {//只查询一个站点
				dataList = expressStatStationService.findByAreaCodeAndTime(areaCode, dateList.get(0), dateList.get(1));
			} else {//查询本公司下的所有站点 （全部）
				//当前登录的用户信息
				User currUser = adminService.get(UserSession.get(request));
				//当前公司下的所有站点
				dataList = expressStatStationService.findByCompanyIdAndTime(currUser.getCompanyId(), dateList.get(0), dateList.get(1));
			}
			//导出==数据写到Excel中并写入response下载
			List<List<String>> rowList = objectToTable(dataList);
			//表头 "未到站订单数",  3000,
			String[] titles = {"站点",  "未到站订单数", "已到站订单数", "已分派", "签收", "滞留", "拒收", "转站", "转其他快递"};
			int[] colWidths = {13000,  3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000};
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.exportExcel("统计汇总", rowList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===统计汇总===出错:" + e.getMessage());
		}
	}

	private List<List<String>> objectToTable(List<ExpressStatStation> dataList){
		if(dataList == null){
			return null;
		}
		List<List<String>> rowList = new ArrayList<List<String>>();
		for(ExpressStatStation expressStatStation : dataList){
			List<String> row = new ArrayList<String>();
			row.add(expressStatStation.getSitename());
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
