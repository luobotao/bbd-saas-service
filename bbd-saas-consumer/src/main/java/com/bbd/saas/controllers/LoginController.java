package com.bbd.saas.controllers;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/login")
@SessionAttributes("loginForm")
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	UserService userService;
	@Autowired
	PostcompanyService postcompanyService;


	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}


	@ModelAttribute("loginForm")
	public LoginForm createLoginForm() {
		return new LoginForm();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void form(Model model) {
		List<Postcompany> postcompanyList = postcompanyService.selectAll();
		model.addAttribute("postcompanyList",postcompanyList);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@Valid LoginForm loginForm, BindingResult result,
								@ModelAttribute("ajaxRequest") boolean ajaxRequest,RedirectAttributes redirectAttrs,HttpServletResponse response) {
		if (result.hasErrors()) {
			return null;
		}
		User user = userService.findUserByLoginName(loginForm.getUserName());
		if(user!=null){
			if(loginForm.getPassWord().equals(user.getPassWord())){//login success
				//判断登录用户的站点状态是否是通过审核状态
				if (user.getSite() == null || !user.getSite().getFlag().equals("2") || StringUtils.isBlank(user.getSite().getAreaCode())) {
					return "redirect:/site/updateSite?siteid="+user.getSite().getId().toHexString();
				}
				if(user.getUserStatus()== UserStatus.INVALID){
					redirectAttrs.addFlashAttribute("message", "用户状态无效");
					return "redirect:/login";
				}
				if(user.getRole()!= UserRole.SITEMASTER){
					redirectAttrs.addFlashAttribute("message", "用户角色不可登录此系统");
					return "redirect:/login";
				}
				UserSession.put(response,user.getId().toHexString());//set adminid to cookies
				adminService.put(user);//set user to redis
				redirectAttrs.addFlashAttribute("user", user);
				return "redirect:/home";
			}else{//password is error
				redirectAttrs.addFlashAttribute("message", "密码错误");
				return "redirect:/login";
			}
		}else{
			redirectAttrs.addFlashAttribute("message", "用户名不存在");
			return "redirect:/login";
		}
	}



}
