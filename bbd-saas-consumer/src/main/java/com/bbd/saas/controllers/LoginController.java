package com.bbd.saas.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.samples.mvc.form.FormBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@SessionAttributes("loginForm")
public class LoginController {

	@Autowired
	AdminUserService adminUserService;

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
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) {
		if (result.hasErrors()) {
			return null;
		}


		System.out.println("============="+adminUserService.findAdminUserByUserName("adsf").getAppkey());
		String message = "Form submitted successfully.  Bound " + loginForm;
		if (ajaxRequest) {
			model.addAttribute("message", message);
			return null;
		} else {
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/form";			
		}
	}
	
}
