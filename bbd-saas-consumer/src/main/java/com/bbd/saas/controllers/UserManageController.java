package com.bbd.saas.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbd.saas.api.UserService;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.mongoModels.UserRole;

/**
 * 版权：zuowenhai新石器时代<br/>
 * 作者：zuowenhai@neolix.cn <br/>
 * 生成日期：2016-04-11 <br/>
 * 描述：用户接口
 */
@Controller
@RequestMapping("/userManage")
@SessionAttributes("userManage")
public class UserManageController {
	public static final Logger logger = LoggerFactory.getLogger(UserManageController.class);
	@Autowired
	private UserService userService;
	
	/**
	 * description: 跳转到系统设置-用户管理页面
	 * 2016年4月5日下午4:00:19
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="asasasas", method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("username", "张三");
		return "systemSet/userManage";
	}

	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@RequestMapping(value="userList", method=RequestMethod.GET)
	public String listUser(Model model) {
		model.addAttribute("username", "张三");
		/*ObjectId objectid = (ObjectId)"5326bfc0e6f780b21635248f";
		UserRole userRole = new UserRole();
		userRole.setId(id);;
		User u = new User();
		u.setr
		User  user = userService.save(user);*/
		return "systemSet/userManageUserList";
	}
	
	/**
     * 保存新建用户
     * @param model
     * @return
     */
	@RequestMapping(value="saveUser", method=RequestMethod.POST)
	public String saveUser(@Valid UserForm userForm, BindingResult result,
			@ModelAttribute("ajaxRequest") boolean ajaxRequest,RedirectAttributes redirectAttrs,HttpServletResponse response) {
		System.out.println("ssss");
		//User  user = userService.save(user);
		
		
		
		return "systemSet/userManageasa";
	}
	
	@RequestMapping(value="/checkUser", method=RequestMethod.GET)
	public String checkUser(Model model,@RequestParam(value = "username", required = true) String username) {
		User user = userService.findUserByLoginName(username);
		logger.info("username"+username);
		return "site/siteRegister";
	}
	
	@RequestMapping(value="/checkLognName", method=RequestMethod.GET)
	public String checkLognName(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		User user = userService.findUserByLoginName(loginName);
		logger.info("loginName"+loginName);
		return "site/siteRegister";
	}
	
}
