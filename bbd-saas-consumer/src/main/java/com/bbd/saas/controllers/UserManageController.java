package com.bbd.saas.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbd.saas.api.UserService;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;

/**
 * 版权：zuowenhai新石器时代<br/>
 * 作者：zuowenhai@neolix.cn <br/>
 * 生成日期：2016-04-11 <br/>
 * 描述：用户接口
 */
@Controller
@RequestMapping("/userManage")
@SessionAttributes("userForm")
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
	public String listUser(Integer page,Model model,HttpServletRequest request) {
		String a = request.getParameter("saasrole");
		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageSize(2);
		pageModel.setPageNo(page-1);
		PageModel<User> userPage = userService.findUserList(pageModel);
		List<User> datas = userPage.getDatas();
		userPage.setPageSize(2);
		userPage.setTotalCount(5);
		userPage.setPageNo(page);
		
		logger.info(userPage+"=========");
		model.addAttribute("userPage", userPage);
		return "systemSet/userManageUserList";
	}
	
	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@RequestMapping(value="userListpost", method=RequestMethod.POST)
	public String userListpost(Model model,HttpServletRequest request) {
		String saasrole = request.getParameter("saasrole");
		String state = request.getParameter("state");
		String keyword = request.getParameter("keyword");
		String page = request.getParameter("page");
		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageSize(2);
		pageModel.setPageNo(Integer.parseInt(page)-1);
		PageModel<User> userPage = userService.findUserList(pageModel);
		List<User> datas = userPage.getDatas();
		userPage.setPageSize(2);
		userPage.setTotalCount(5);
		userPage.setPageNo(Integer.parseInt(page));
		logger.info(userPage+"=========");
		model.addAttribute("userPage", userPage);
		return "systemSet/userManageUserListpost";
	}
	
	/**
     * 保存新建用户
     * @param model
     * @return
     */
	@ResponseBody
	@RequestMapping(value="saveUser", method=RequestMethod.POST)
	public String saveUser(@Valid UserForm userForm, BindingResult result,Model model,RedirectAttributes redirectAttrs,HttpServletResponse response) throws IOException {
		System.out.println("ssss");
		Map<String, Object> map = new HashMap<String, Object>();
	    java.util.Date dateAdd = new java.util.Date();
		User user = new User();
		user.setRealName(userForm.getRealName());
		user.setLoginName(userForm.getLoginName());
		user.setPhone(userForm.getPhone());
		user.setPassWord(userForm.getLoginPass());
		user.setOperate(null);
		user.setRole(UserRole.status2Obj(Integer.parseInt(userForm.getRoleId())));
		user.setDateAdd(dateAdd);
		user.setUserStatus(UserStatus.status2Obj(0));
		System.out.println("============="+user.getUserStatus().getStatus());
		Key<User> kuser = userService.save(user);
		
		if(kuser!=null && !kuser.getId().equals("")){
			/*map.put("success", true); 
			map.put("message", "success"); 
			response.setStatus(200); */
			return "true";
		}else{
			/*map.put("success", false); 
			map.put("message", "error"); 
			response.setStatus(400); */
			return "false";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/checkUser", method=RequestMethod.GET)
	public String checkUser(Model model,@RequestParam(value = "realname", required = true) String realname,HttpServletResponse response) {
		String checkRealName = "";
		try {
			checkRealName=new String(realname.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = userService.findUserByRealName(checkRealName);
		logger.info("realname"+realname);
		if(user!=null && !user.getId().equals("")){
			return "true";
			
		}else{ 
			return "false";
			
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="/changestatus", method=RequestMethod.GET)
	public void changestatus(Model model,@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "status", required = true) String status,HttpServletResponse response) {
		//User user = userService.findUserByRealName(id);
		logger.info("id"+id);
		if(status.equals("0")){
			response.setStatus(200); 
			//return "true";
		}else{
			response.setStatus(400); 
			//return "false";
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/checkLognName", method=RequestMethod.GET)
	public String checkLognName(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		User user = userService.findUserByLoginName(loginName);
		logger.info("loginName"+loginName);
		if(user!=null && !user.getId().equals("")){
			return "true";
			
		}else{ 
			return "false";
			
		}
	}
	
}
