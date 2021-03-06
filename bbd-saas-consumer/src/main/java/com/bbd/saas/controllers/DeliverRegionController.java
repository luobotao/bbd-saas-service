package com.bbd.saas.controllers;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteSrc;
import com.bbd.saas.enums.SiteType;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Numbers;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 配送区域 -- 站点
 */
@Controller
@RequestMapping("/deliverRegion")
@SessionAttributes("deliverRegion")
public class DeliverRegionController {
	public static final Logger logger = LoggerFactory.getLogger(DeliverRegionController.class);
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
	@RequestMapping(value="/map/{activeNum}", method=RequestMethod.GET)
	public String toMapPage(@PathVariable String activeNum, Model model, HttpServletRequest request ) {
		try{
			String userId = UserSession.get(request);
			if(userId!=null&&!"".equals(userId)) {
				User user = adminService.get(userId);
				//获取用户站点信息
				//--------panel 1-----------------------
				Site site = siteService.findSite(user.getSite().getId().toString());
				//导入地址关键词 --------panel 3------ 快递柜有此功能
				if(user.getSite().getSitetype() == SiteType.EXPRESS_CABINET && user.getSite().getSiteSrc() != SiteSrc.QXSH){
					String between = request.getParameter("between");
					String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
					int page = Numbers.parseInt(request.getParameter("page"), 0);
					PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
					if (StringUtils.isNotBlank(between)) {//预计站点入库时间
						DateBetween dateBetween = new DateBetween(between);
						logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
						//导入地址关键词
						siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", dateBetween.getStart(), dateBetween.getEnd(), page, 10, keyword);
					} else {
						siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
					}
					model.addAttribute("between", between);
					model.addAttribute("keyword", keyword);
					model.addAttribute("siteKeywordPageList", siteKeywordPage.list);
					model.addAttribute("page", siteKeywordPage.getPage());
					model.addAttribute("pageNum", siteKeywordPage.getPageNum());
					model.addAttribute("pageCount", siteKeywordPage.getCount());
				}
				model.addAttribute("activeNum", activeNum);
				model.addAttribute("site", site);
				List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(user.getSite().getId().toString());
				String siteStr = dealSitePoints(sitePoints);
				model.addAttribute("sitePoints", siteStr);
				return "systemSet/deliverRegionMap";
			}else{
				return "redirect:/login";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "redirect:/login";
		}

	}

	private String dealSitePoints(List<List<MapPoint>> sitePoints) {
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

}
