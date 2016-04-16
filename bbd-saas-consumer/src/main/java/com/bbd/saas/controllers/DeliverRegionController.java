package com.bbd.saas.controllers;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
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
		Site site = user.getSite();
		logger.info("site name:",site.getName());
		activeNum = "3";
		//Date between = request.getParameter("");
		String keyword = request.getParameter("keyword");
		//导入地址关键词
		PageList<SiteKeyword> siteKeywordPageList = siteKeywordApi.findSiteKeyword(site.getId()+"",null,null,0,20,"");
		model.addAttribute("activeNum", activeNum);
		model.addAttribute("site", site);
		model.addAttribute("siteKeywordPageList", siteKeywordPageList.list);

		return "systemSet/deliverRegionMap";
	}

}
