package com.bbd.saas.controllers.map;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.vo.SiteVO;
import com.bbd.saas.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运力分布
 */
@Controller
@RequestMapping("/capacityDistribution")
public class CapacityDistributionController {
	public static final Logger logger = LoggerFactory.getLogger(CapacityDistributionController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	PostmanUserService postmanUserService;
	@Autowired
	UserService userService;
	/**
	 * 跳转到运力分布页面
	 * @param request
	 * @param model
     * @return
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String getAllSiteAndCourier(final HttpServletRequest request, Model model) {
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询登录用户的公司下的所有站点
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
			//查询登录用户的公司下的所有派件员信息
			List<UserVO> userVOList = postmanUserService.findLatAndLngByCompanyId(currUser.getCompanyId());
			logger.info("=====运力分布站点===" + siteVOList);
			//设置地图默认的中心点
			SiteVO centerSite = new SiteVO();
			centerSite.setName("");
			centerSite.setLat("39.915");
			centerSite.setLng("116.404");
			model.addAttribute("centerSite", centerSite);
			model.addAttribute("siteList", siteVOList);
			model.addAttribute("userList", userVOList);
			return "map/capacityDistribution";
		} catch (Exception e) {
			logger.error("===跳转到运力分布页面==出错 :" + e.getMessage());
		}
		return "map/capacityDistribution";
	}

	/**
	 *  根据站点Ajax更新地图
	 * @param siteId 站点Id
	 * @param request 请求
	 * @return 分页列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/getSiteAndCourierList", method=RequestMethod.GET)
	public Map<String, Object> getAllSiteAndCourier(String siteId, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(siteId != null && !"".equals(siteId)){//只查询一个站点
				Site site = siteService.findSite(siteId);
				List<User> userList = userService.findUsersBySite(site, UserRole.SENDMEM, UserStatus.VALID);//所有小件员
				if (userList != null && userList.size() >0){
					List<Integer> postmanIdList = new ArrayList<Integer>();
					for (User user : userList){
						postmanIdList.add(user.getPostmanuserId());
					}
					List<UserVO> userVOList = postmanUserService.findLatAndLngByIds(postmanIdList);
					map.put("userList", userList);
					logger.error("==all===userVOList:" + userVOList.size());
				}
				map.put("site", site);
				map.put("centerSite", site);
			}else {//查询本公司下的所有站点 （全部）
				//当前登录的用户信息
				User currUser = adminService.get(UserSession.get(request));
				//查询登录用户的公司下的所有站点
				List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId());
				//查询登录用户的公司下的所有派件员信息
				List<UserVO> userVOList = postmanUserService.findLatAndLngByCompanyId(currUser.getCompanyId());
				logger.error("==all===userVOList:" + userVOList.size());
				//设置地图默认的中心点
				SiteVO centerSite = new SiteVO();
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				map.put("centerSite", centerSite);
				map.put("siteList", siteVOList);
				map.put("userList", userVOList);
			}
		} catch (Exception e) {
			logger.error("===ajax查询所有站点和派件员经纬度===出错:" + e.getMessage());
		}
		return map;
	}


}
