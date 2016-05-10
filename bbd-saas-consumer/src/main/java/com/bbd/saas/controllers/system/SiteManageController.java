package com.bbd.saas.controllers.system;

import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.Result;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.OpenUserService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.SignUtil;
import com.bbd.saas.vo.UserQueryVO;
import flexjson.JSONDeserializer;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置 -- 站点管理
 * 骆波涛
 */
@Controller
@RequestMapping("/system/siteManage")
public class SiteManageController {
	public static final Logger logger = LoggerFactory.getLogger(SiteManageController.class);

	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	SitePoiApi sitePoiApi;
	
	/**
     * 获取某一公司下的站点列表信息
     * @param 
     * @return
     */
	@RequestMapping(method=RequestMethod.GET)
	public String siteManage(HttpServletRequest request,Model model,Integer pageIndex, Integer roleId, Integer status,String keyword) {
		PageModel<Site> sitePage = getSitePage(request,0,-1,keyword);
		model.addAttribute("sitePage", sitePage);
		return "systemSet/siteManage";
	}

	/**
     * 站点分页查询
     * @param 
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/getSitePage", method = RequestMethod.GET)
	public PageModel<Site> getSitePage(HttpServletRequest request, Integer pageIndex, Integer status, String keyword) {
		User user = adminService.get(UserSession.get(request));
		if (pageIndex==null) pageIndex =0 ;

		PageModel<Site> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);

		PageModel<Site> sitePage = siteService.getSitePage(pageModel,user.getCompanyId(),status,keyword);
		return sitePage;
	}
	/**
     * 审核通过站点
     * @param
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/validSite", method = RequestMethod.GET)
	public boolean validSite(HttpServletRequest request, String siteId) {
		siteService.validSite(siteId);//审核通过站点
//		Site site = siteService.findSite(siteId);
//		String siteAddress = site.getProvince()+site.getCity()+site.getArea()+site.getAddress();
//		logger.info(site.getId().toString());
//		Result<double[]> result = sitePoiApi.addSitePOI(site.getId().toString(),"",site.getName(),siteAddress,0);
//		//更新站点的经度和纬度
//		logger.info("[addSitePOI]result :"+result.toString());
//		if(result.code==0&&result.data!=null) {
//			double[] data = result.data;
//			site.setLng(data[0] + "");    //经度
//			site.setLat(data[1] + "");    //纬度
//			site.setDeliveryArea("0");
//			siteService.save(site);
//		}
		return true;
	}
	/**
     * 审核驳回站点
     * @param
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/turnDownSite", method = RequestMethod.GET)
	public boolean turnDownSite(HttpServletRequest request, String siteId,String message) {
		Site site = siteService.findSite(siteId);
		site.setMemo( "抱歉，您提交的信息未通过审核。您可修改后重新提交。");
		site.setStatus(SiteStatus.TURNDOWN);
		site.setTurnDownMessage(message);
		site.setDateUpd(new Date());
		siteService.save(site);
		return true;
	}
	/**
     * 停用站点
     * @param siteId
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/stopSite", method = RequestMethod.GET)
	public boolean stopSite(String siteId) {
		Site site = siteService.findSite(siteId);
		site.setStatus(SiteStatus.INVALID);
		site.setDateUpd(new Date());
		siteService.save(site);
		return true;
	}
	/**
     * 启用站点
     * @param siteId
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/startSite", method = RequestMethod.GET)
	public boolean startSite(String siteId) {
		Site site = siteService.findSite(siteId);
		site.setStatus(SiteStatus.APPROVE);
		site.setDateUpd(new Date());
		siteService.save(site);
		return true;
	}

	/**
	 * 删除站点
	 * @param siteId
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/delSite", method = RequestMethod.GET)
	public boolean delSite(String siteId) {
		userService.delUsersBySiteId(siteId);//删除此站点下的所有用户
		siteService.delSiteBySiteId(siteId);//删除站点信息
		return true;
	}

	
}
