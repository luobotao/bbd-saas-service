package com.bbd.saas.controllers.map;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.SiteVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配送区域
 */
@Controller
@RequestMapping("/deliverArea")
public class DeliverAreaController {

	public static final Logger logger = LoggerFactory.getLogger(DeliverAreaController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	SiteKeywordApi siteKeywordApi;
	@Autowired
	SitePoiApi sitePoiApi;
	/**
	 * description: 跳转到系统设置-配送区域-绘制电子地图页面
	 * 2016年4月5日下午4:05:01
	 * @author: liyanlei
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/map", method=RequestMethod.GET)
	public String toMapPage(String siteId, String activeNum, Model model, HttpServletRequest request ) {
		try{
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
				//获取参数
				siteId = StringUtil.initStr(siteId, "");
				String between = request.getParameter("between");
				String keyword = StringUtil.initStr(request.getParameter("keyword"), "");
				int page = Numbers.parseInt(request.getParameter("page"), 0);
				siteId = currUser.getSite().getId().toString();
				//业务开始
				if(siteId != null && !"".equals(siteId)){//只查询一个站点
					//获取用户站点信息
					//--------panel 1-----------------------
					Site site = siteService.findSite(siteId);
					//导入地址关键词
					//--------panel 3-----------------------
					PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
					if (StringUtils.isNotBlank(between)) {//预计到站时间
						DateBetween dateBetween = new DateBetween(between);
						logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
						//导入地址关键词
						siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", dateBetween.getStart(), dateBetween.getEnd(), page, 10, keyword);
					} else {
						siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
					}
					model.addAttribute("siteList", siteVOList);
					model.addAttribute("activeNum", activeNum);
					model.addAttribute("site", site);
					model.addAttribute("between", between);
					model.addAttribute("keyword", keyword);
					model.addAttribute("siteKeywordPageList", siteKeywordPage.list);
					model.addAttribute("pageIndex", siteKeywordPage.getPage());
					model.addAttribute("pageNum", siteKeywordPage.getPageNum());
					model.addAttribute("pageCount", siteKeywordPage.getCount());
					List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(currUser.getSite().getId().toString());
					String siteStr = dealSitePoints(sitePoints);
					logger.info("siteStr======="+siteStr);
					model.addAttribute("sitePoints", siteStr);
				}else {//查询本公司下的所有站点 （全部）

				}
				return "map/deliverAreaMap";
			}else{
				return "redirect:/login";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "redirect:/login";
		}

	}

	private String dealSitePoints(List<List<MapPoint>> sitePoints) {
		if(sitePoints == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < sitePoints.size(); j++) {
			for (int i = 0; i < sitePoints.get(j).size(); i++) {
				String str = sitePoints.get(j).get(i).getLng()+"_"+sitePoints.get(j).get(i).getLat();
				sb.append(str);
				if(i<sitePoints.get(j).size()-1){
					sb.append(",");
				}
			}
			if(j<sitePoints.size()-1) {
				sb.append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取站点的配送范围--半径
	 * @param siteId 站点Id
	 * @param request 请求
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/getSiteById", method=RequestMethod.GET)
	public Map<String, Object> getSiteById(String siteId, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		if(siteId != null && !"".equals(siteId)){//只查询一个站点
			//获取用户站点信息
			//--------panel 1-----------------------
			Site site = siteService.findSite(siteId);
			SiteVO siteVO = new SiteVO();
			siteVO.setName(site.getName());
			siteVO.setLat(site.getLat());
			siteVO.setLng(site.getLng());
			siteVO.setDeliveryArea(site.getDeliveryArea());
			map.put("site", site);
		}else {//查询本公司下的所有站点 （全部）
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
				//设置地图默认的中心点
				SiteVO centerSite = new SiteVO();
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("5000");
				map.put("centerSite", centerSite);
				map.put("siteList", siteVOList);
			}else{
				SiteVO centerSite = new SiteVO();
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("5000");
				map.put("centerSite", centerSite);
			}
		}
		return map;
	}

	/**
	 * 获取站点电子围栏
	 * @param siteId 站点Id
	 * @param request 请求
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/getFence", method=RequestMethod.GET)
	public Map<String, Object> getFenceBySiteId(String siteId, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		if(siteId != null && !"".equals(siteId)){//只查询一个站点
			//获取用户站点信息
			Site site = siteService.findSite(siteId);
			SiteVO siteVO = new SiteVO();
			siteVO.setName(site.getName());
			siteVO.setLat(site.getLat());
			siteVO.setLng(site.getLng());
			siteVO.setDeliveryArea(site.getDeliveryArea());
			//电子围栏
			List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(siteId);
			String eFence = dealSitePoints(sitePoints);
			siteVO.seteFence(eFence);
			//logger.info("siteStr======="+siteStr);
			map.put("site", site);
		}else {//查询本公司下的所有站点 （全部）
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
				//设置电子围栏
				if(siteVOList != null && siteVOList.size() > 0){
					for (SiteVO siteVO : siteVOList){
						List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(siteId);
						String eFence = dealSitePoints(sitePoints);
						siteVO.seteFence(eFence);
					}
				}
				//设置地图默认的中心点
				SiteVO centerSite = new SiteVO();
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("5000");
				map.put("centerSite", centerSite);
				map.put("siteList", siteVOList);
			}else{
				SiteVO centerSite = new SiteVO();
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("5000");
				map.put("centerSite", centerSite);
			}
		}
		return map;
	}

	/**
	 * 获取站点关键词
	 * @param siteId 站点Id
	 * @param request 请求
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryKeyWord", method=RequestMethod.GET)
	public PageList<SiteKeyword> getKeyWordBySiteId(String siteId, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		String between = request.getParameter("between");
		String keyword = StringUtil.initStr(request.getParameter("keyword"), "");
		int pageIndex = Numbers.parseInt(request.getParameter("pageIndex"), 0);
		if(siteId != null && !"".equals(siteId)){//只查询一个站点
			//导入地址关键词

		}else {//查询本公司下的所有站点 （全部）
			siteId = null;
		}
		//--------panel 3-----------------------
		PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
		if (StringUtils.isNotBlank(between)) {//预计到站时间
			DateBetween dateBetween = new DateBetween(between);
			logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
			//导入地址关键词
			siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId + "", dateBetween.getStart(), dateBetween.getEnd(), pageIndex, 10, keyword);
		} else {
			siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId + "", null, null, pageIndex, 10, keyword);
		}
		List<SiteKeyword> keywordList = siteKeywordPage.getList();
		if (keywordList != null && keywordList.size() > 0){
			System.out.println("count====="+keywordList.size());
			String siteName = null;
			for(SiteKeyword siteKeyword : keywordList){
				Site site = siteService.findSite(siteKeyword.getSiteId());
				if(site != null){
					siteKeyword.siteId = StringUtil.initStr(site.getName(), "");
				}else {
					siteKeyword.siteId = "";
				}
			}
		}
		return siteKeywordPage;
	}
}
