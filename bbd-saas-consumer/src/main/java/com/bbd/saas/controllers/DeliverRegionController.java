package com.bbd.saas.controllers;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
		String userId = UserSession.get(request);
		User user = userService.findOne(userId);
		//获取用户站点信息
		//--------panel 1-----------------------
		Site site = user.getSite();
		String between = request.getParameter("between");
		String keyword = request.getParameter("keyword");
		int page = Numbers.parseInt(request.getParameter("page"),0);
		//导入地址关键词
		//--------panel 3-----------------------
		PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
		if(StringUtils.isNotBlank(between)) {//预计到站时间
			DateBetween dateBetween = new DateBetween(between);
			logger.info(dateBetween.getStart()+":"+dateBetween.getEnd());
			//导入地址关键词
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId()+"",dateBetween.getStart(),dateBetween.getEnd(),page,10,keyword);
		}else{
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
		}
		model.addAttribute("activeNum", activeNum);
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
		return "systemSet/deliverRegionMap";
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
