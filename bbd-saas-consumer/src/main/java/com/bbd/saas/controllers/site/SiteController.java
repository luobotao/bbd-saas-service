package com.bbd.saas.controllers.site;

import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.mongoModels.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

/**
 * 站点相关处理
 */
@Controller
@RequestMapping("/site")
@SessionAttributes("loginForm")
public class SiteController {
	public static final Logger logger = LoggerFactory.getLogger(SiteController.class);
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	UserService userService;


	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(Model model) {
		return "site/siteRegister";
	}

	@ResponseBody
	@RequestMapping(value="/checkSiteWithLoginName", method=RequestMethod.GET)
	public Boolean checkSiteWithUsername(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		User user = userService.findUserByLoginName(loginName);
		if(user==null)
			return true;
		else
			return false;
	}

	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processSubmit(@RequestParam MultipartFile licensePic, @Valid SiteForm siteForm, BindingResult result,RedirectAttributes redirectAttrs) throws IOException {
		redirectAttrs.addFlashAttribute("message", "注册成功");
		logger.info(licensePic.getName()+"====================="+licensePic.getOriginalFilename());
		if (result.hasErrors()) {
			logger.info("=================工工aaa===="+licensePic.getOriginalFilename());
		}
		return "redirect:/login";
	}


//	@RequestMapping(value="/register",method=RequestMethod.POST)
//	public String processSubmit(@Valid LoginForm loginForm, BindingResult result,
//								@ModelAttribute("ajaxRequest") boolean ajaxRequest,RedirectAttributes redirectAttrs,HttpServletResponse response) {
//		if (result.hasErrors()) {
//			return null;
//		}
//		AdminUser adminUser = adminUserService.findAdminUserByUserName(loginForm.getUserName());
//		if(adminUser!=null){
//			if(loginForm.getPassWord().equals(adminUser.getPassWord())){//login success
//				AdminSession.put(response,adminUser.getId().toHexString());//set adminid to cookies
//				adminService.put(adminUser);//set adminUser to redis
//				return "redirect:/";
//			}else{//password is error
//				redirectAttrs.addFlashAttribute("message", "密码错误");
//				return "redirect:/login";
//			}
//		}else{
//			redirectAttrs.addFlashAttribute("message", "用户名不存在");
//		}
//		return "site/siteRegister";
//	}
	
}
