package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.constants.AdminSession;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.utils.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@SessionAttributes("loginForm")
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	AdminService adminService;


	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}


	@ModelAttribute("loginForm")
	public LoginForm createLoginForm() {
		return new LoginForm();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void form() {
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@Valid LoginForm loginForm, BindingResult result,
								@ModelAttribute("ajaxRequest") boolean ajaxRequest,RedirectAttributes redirectAttrs,HttpServletResponse response) {
		if (result.hasErrors()) {
			return null;
		}
		AdminUser adminUser = adminUserService.findAdminUserByUserName(loginForm.getUserName());
		if(adminUser!=null){
			if(loginForm.getPassWord().equals(adminUser.getPassWord())){//login success
				AdminSession.put(response,adminUser.getId().toHexString());//set adminid to cookies
				adminService.put(adminUser);//set adminUser to redis
				return "redirect:/";
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
