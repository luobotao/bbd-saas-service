package com.bbd.saas.controllers.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 公司、站点注册相关处理
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
	public static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	public static final int MAXSIZE = 100000;

	@RequestMapping(value="/registerView", method=RequestMethod.GET)
	public String registerView(Model model, HttpServletRequest request) {

		return "register/registerView";
	}
}
