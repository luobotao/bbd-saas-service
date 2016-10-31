package com.bbd.saas.controllers.map;

import com.bbd.poi.api.Geo;
import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.Services.RedisService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.SiteVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
 * 配送区域 -- 公司账号
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
	@Autowired
	Geo geo;
	@Autowired
	PostcompanyService postCompanyService;
	@Autowired
	RedisService redisService;
	/**
	 * description: 跳转到系统设置-配送区域-绘制电子地图页面
	 * 2016年4月5日下午4:05:01
	 * @author: liyanlei
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/map", method=RequestMethod.GET)
	public String toMapPage(Integer activeNum, Model model, HttpServletRequest request ) {
		try{
			activeNum = Numbers.divToPageNum(activeNum, 1);
			model.addAttribute("activeNum", activeNum);
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				/******************配送范围*****************start****************/
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
				getCompanyAndMapCenter(currUser.getCompanyId(), model);
				/******************配送范围*****************end****************/
				/******************绘制电子围栏*****************start****************/
				//设置电子围栏
				if(siteVOList != null && siteVOList.size() > 0){
					for(SiteVO siteVO : siteVOList){
						List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(siteVO.getId().toString());
						String efenceStr = dealSitePoints(sitePoints);
						siteVO.seteFence(efenceStr);
						//logger.info("siteName===" + siteVO.getName() + "    efenceStr==="+efenceStr);
					}
				}
				/******************绘制电子围栏****************end****************/
/*
				*//******************关键词*****************start****************//*
				//获取参数
				String between = request.getParameter("between");
				String keyword = StringUtil.initStr(request.getParameter("keyword"), "");
				int page = Numbers.parseInt(request.getParameter("page"), 0);
				String siteId = null;
				if(siteVOList != null && siteVOList.size() > 0){
					siteId = siteVOList.get(0).getId();
				}
				PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
				if (StringUtils.isNotBlank(between)) {//预计到站时间
					DateBetween dateBetween = new DateBetween(between);
					logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
					//导入地址关键词
					siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId, dateBetween.getStart(), dateBetween.getEnd(), page, 10, keyword);
				} else {
					siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId, null, null, page, 10, keyword);
				}
				//设置站点名称
				List<SiteKeyword> keywordList = siteKeywordPage.getList();
				if (keywordList != null && keywordList.size() > 0){
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
				model.addAttribute("between", between);
				model.addAttribute("keyword", keyword);
				model.addAttribute("siteKeywordPageList", siteKeywordPage.list);
				model.addAttribute("pageIndex", siteKeywordPage.getPage());
				model.addAttribute("pageNum", siteKeywordPage.getPageNum());
				model.addAttribute("pageCount", siteKeywordPage.getCount());
				*//******************关键词*****************end****************//*
			*/

				//公共
				model.addAttribute("siteList", siteVOList);
				return "map/deliverAreaMap";
			}else{
				return "redirect:/login";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "redirect:/login";
		}

	}
	private void getCompanyAndMapCenter(String companyId, Model model){
		String companyAddress = "";
		Postcompany company = new Postcompany();
		if (companyId != null ){
			company = postCompanyService.selectPostmancompanyById(Integer.parseInt(companyId));
			if(company != null){
				StringBuffer  addressBS = new StringBuffer();
				addressBS.append(StringUtil.initStr(company.getProvince(),""));
				addressBS.append(StringUtil.initStr(company.getCity(),""));
				addressBS.append(StringUtil.initStr(company.getArea(),""));
				addressBS.append(StringUtil.initStr(company.getAddress(),""));
				companyAddress = addressBS.toString();
				getDefaultCompany(company);
			}else{
				getDefaultCompany(company);
			}
		}else{
			getDefaultCompany(company);
		}
		model.addAttribute("company", company);
		SiteVO centerPoint = getDefaultCenterPoint(companyAddress);
		model.addAttribute("centerPoint", centerPoint);
	}
	private void getDefaultCompany(Postcompany company){
		if(company == null){
			company = new Postcompany();
		}
		company.setCompanycode(StringUtil.initStr(company.getCompanycode(), "BBD"));
		company.setCompanyname(StringUtil.initStr(company.getCompanyname(), "北京棒棒达快递有限公司"));
		company.setProvince(StringUtil.initStr(company.getProvince(), "北京"));
		company.setCity(StringUtil.initStr(company.getCity(), "北京市"));
		company.setArea(StringUtil.initStr(company.getArea(), "朝阳区"));
		company.setAddress(StringUtil.initStr(company.getAddress(), "双井"));
	}
	//地图默认中心位置--公司公司经纬度
	private SiteVO getDefaultCenterPoint(String address){
		SiteVO centerSite = new SiteVO();
		if(address == null || "".equals(address)){
			centerSite.setName("");
			centerSite.setLat("39.915");
			centerSite.setLng("116.404");
			centerSite.setDeliveryArea("50000");
			return centerSite;
		}
		try {
			MapPoint mapPoint = geo.getGeoInfo(address);
			if (mapPoint != null) {
				logger.info("[address]:" + address + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度："+ mapPoint.getLat());
				centerSite.setName("");
				centerSite.setLat(mapPoint.getLat()+"");
				centerSite.setLng(mapPoint.getLng()+"");
				centerSite.setDeliveryArea("50000");
			}else{//默认中心位置为北京
				logger.info("[address]:" + address + " [search geo result] null,无法获取经纬度");
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("50000");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[address]:" + address + " [search geo result] exception,异常");
		}
		return centerSite;
		//map.put("centerSite", centerSite);
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
	public Map<String, Object> getSiteById(String prov, String city, String area, String siteId, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		if(siteId != null && !"".equals(siteId)){//只查询一个站点
			//获取用户站点信息
			//--------panel 1-----------------------
			Site site = siteService.findSite(siteId);
			SiteVO siteVO = new SiteVO();
			/*siteVO.setName(site.getName());
			siteVO.setLat(site.getLat());
			siteVO.setLng(site.getLng());
			siteVO.setDeliveryArea(site.getDeliveryArea());*/
			BeanUtils.copyProperties(site, siteVO);
			map.put("site", site);
		}else {//查询本公司下的所有站点 （全部）
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findSiteVOByCompanyIdAndAddress(currUser.getCompanyId(), prov, city, area, SiteStatus.APPROVE, -1);
				map.put("siteList", siteVOList);
			}
		}
		return map;
	}

	/**
	 * 获取站点电子围栏
	 * @param prov 省
	 * @param city 市
	 * @param area 区
	 * @param siteId 站点Id
	 * @param areaFlag 配送区域
	 * @param request 请求
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/getFence", method=RequestMethod.GET)
	public Map<String, Object> getFenceBySiteId(String prov, String city, String area, String siteId, Integer areaFlag, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		areaFlag = Numbers.defaultIfNull(areaFlag, -1);
		if(siteId != null && !"".equals(siteId)){//只查询一个站点
			//获取用户站点信息
			Site site = siteService.findSite(siteId);
			SiteVO siteVO = new SiteVO();
			BeanUtils.copyProperties(site, siteVO);
			/*siteVO.setName(site.getName());
			siteVO.setLat(site.getLat());
			siteVO.setLng(site.getLng());
			siteVO.setDeliveryArea(site.getDeliveryArea());*/
			//电子围栏
			List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(siteId);
			String eFence = dealSitePoints(sitePoints);
			siteVO.seteFence(eFence);
			map.put("site", siteVO);
		}else {//查询本公司下的所有站点 （全部）
			String userId = UserSession.get(request);
			if(userId != null && !"".equals(userId)) {
				//当前登录的用户信息
				User currUser = adminService.get(userId);
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findSiteVOByCompanyIdAndAddress(currUser.getCompanyId(),prov, city, area, SiteStatus.APPROVE, areaFlag);
				//设置电子围栏
				if(siteVOList != null && siteVOList.size() > 0){
					for (SiteVO siteVO : siteVOList){
						List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(siteVO.getId());
						String efenceStr = dealSitePoints(sitePoints);
						siteVO.seteFence(efenceStr);
					}
				}
				map.put("siteList", siteVOList);

			}
		}
		return map;
	}



	/**
	 * 获取站点关键词 --  不需要了
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
		//--------panel 3-----------------------
		PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
		if (StringUtils.isNotBlank(between)) {//预计到站时间
			DateBetween dateBetween = new DateBetween(between);
			logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
			//导入地址关键词
			siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId, dateBetween.getStart(), dateBetween.getEnd(), pageIndex, 10, keyword);
		} else {
			siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId, null, null, pageIndex, 10, keyword);
		}
		//设置站点名称
		List<SiteKeyword> keywordList = siteKeywordPage.getList();
		if (keywordList != null && keywordList.size() > 0){
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
