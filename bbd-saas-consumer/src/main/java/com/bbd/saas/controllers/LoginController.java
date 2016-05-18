package com.bbd.saas.controllers;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Numbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		try{
			User user = userService.findUserByLoginName(loginForm.getUserName());
			if(user!=null){
				if(loginForm.getPassWord().equals(user.getPassWord())){//login success
					if(user.getRole()==UserRole.COMPANY){//公司用户
						Postcompany postcompany = postcompanyService.selectPostmancompanyById(Numbers.parseInt(user.getCompanyId(),0));
						if(postcompany!=null){
							if( "0".equals(postcompany.getSta())){//未审核
								redirectAttrs.addAttribute("companyId",user.getCompanyId());
								redirectAttrs.addAttribute("phone",user.getLoginName());
								return "redirect:register/regitsterCompanyView";
							}
							if( "2".equals(postcompany.getSta())){//审核失败
								redirectAttrs.addAttribute("companyId",user.getCompanyId());
								redirectAttrs.addAttribute("phone",user.getLoginName());
								return "redirect:register/regitsterCompanyUpdate";
							}
						}
					}else{//站长
						//判断登录用户的站点状态是否是通过审核状态
						if(user.getSite()!=null){
							//驳回
							if(user.getSite().getStatus()== SiteStatus.TURNDOWN){
								redirectAttrs.addAttribute("siteid",user.getSite().getId().toString());
								return "redirect:register/regitsterSiteUpdate";
							}
							//未审核
							if( StringUtils.isBlank(user.getSite().getAreaCode()) || user.getSite().getStatus()== SiteStatus.WAIT){
								redirectAttrs.addAttribute("siteid",user.getSite().getId().toString());
								return "redirect:register/regitsterSiteView";
							}
						}
						if(user.getUserStatus()== UserStatus.INVALID){
							redirectAttrs.addFlashAttribute("message", "此账号已停用，请联系公司相关负责人开通");
							return "redirect:/login";
						}
						if(user.getRole()!= UserRole.SITEMASTER && user.getRole()!= UserRole.COMPANY){
							redirectAttrs.addFlashAttribute("message", "用户角色不可登录此系统");
							return "redirect:/login";
						}
					}
					int loginCount = user.getLoginCount();
					loginCount = ++loginCount;
					user.setLoginCount(loginCount);
					userService.save(user);
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
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[login error]"+e.getMessage());
			return "redirect:/login";
		}

	}



}
