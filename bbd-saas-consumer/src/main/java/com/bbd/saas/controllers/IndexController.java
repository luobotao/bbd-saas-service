package com.bbd.saas.controllers;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("index")
public class IndexController {
	public static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	SiteKeywordApi siteKeywordApi;
	@Autowired
	SitePoiApi sitePoiApi;
	@Autowired
	HttpServletRequest request;
	/**
	 * description: 进入首页
	 * 2016年4月1日下午6:22:38
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Model model,HttpServletRequest request,String typ) {
		User user = adminService.get(UserSession.get(request));
		model.addAttribute("user", user);
		model.addAttribute("typ", typ);
		return "index";
	}

	/**
	 * 退出登录
	 * @param model
	 * @param request
	 * @param response
     * @return
     */
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		User user = adminService.get(UserSession.get(request));
		UserSession.remove(response);//remove from cookies
		adminService.delete(user);//remove adminUser from redis
		return "redirect:/login";
	}

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(Model model,HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));
		model.addAttribute("user", user);
		if(user.getLoginCount()==1) {
			//--------panel 1-----------------------
			Site site = siteService.findSite(user.getSite().getId().toString());
			String between = request.getParameter("between");
			String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
			int page = Numbers.parseInt(request.getParameter("page"), 0);
			//导入地址关键词
			//--------panel 3-----------------------
			PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
			if (org.apache.commons.lang3.StringUtils.isNotBlank(between)) {//预计到站时间
				DateBetween dateBetween = new DateBetween(between);
				logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
				//导入地址关键词
				siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", dateBetween.getStart(), dateBetween.getEnd(), page, 10, keyword);
			} else {
				siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
			}
			model.addAttribute("site", site);
			model.addAttribute("between", between);
			model.addAttribute("keyword", keyword);
			model.addAttribute("siteKeywordPageList", siteKeywordPage.list);
			model.addAttribute("page", siteKeywordPage.getPage());
			model.addAttribute("pageNum", siteKeywordPage.getPageNum());
			model.addAttribute("pageCount", siteKeywordPage.getCount());

			List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(user.getSite().getId().toString());
			String siteStr = dealSitePoints(sitePoints);
			model.addAttribute("sitePoints", siteStr);
		}
		return "home";
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
