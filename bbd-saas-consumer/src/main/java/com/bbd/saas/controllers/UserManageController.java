package com.bbd.saas.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserQueryVO;

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
     * 获取用户列表信息
     * @param 
     * @return
     */
	/*@RequestMapping(value="userList", method=RequestMethod.GET)
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
	}*/
	
	
	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@RequestMapping(value="userList", method=RequestMethod.GET)
	public String listUser(Model model,Integer pageIndex, Integer roleId, Integer status,String keyword) {
		PageModel<User> userPage = getUserPage(0,roleId,status,keyword);
		
		model.addAttribute("userPage", userPage);
		//return "systemSet/userManageUserList";
		return "systemSet/userManage";
	}
	
	/**
	 * description: 跳转到用户管理页面
	 * 2016年4月14日
	 * @author: zuowenhai
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public void index(Model model,Integer pageIndex, Integer roleId, Integer status,String keyword) {


		User user = userService.findUserByLoginName("qweqewqwed");

		model.addAttribute("user", user);
		
		//return "systemSet/userManage";
	}
	
	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/getUserPage", method = RequestMethod.GET)
	public PageModel<User> getUserPage(Integer pageIndex, Integer roleId, Integer status,String keyword) {
		if (pageIndex==null) pageIndex =0 ;
		//logger.info(arriveStatus+"========="+between);
		UserQueryVO userQueryVO = new UserQueryVO();
		userQueryVO.roleId=roleId;
		userQueryVO.status=status;
		userQueryVO.keyword=keyword;
		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<User> userPage = userService.findUserList(pageModel,userQueryVO);
		return userPage;
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

			return "true";
		}else{
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
	public String changestatus(Model model,@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "status", required = true) String status,HttpServletResponse response) {
		//User user = userService.findUserByRealName(id);
		logger.info("id"+id);
		if(status.equals("0")){ 
			return "true";
		}else{
			return "false";
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
	
	@ResponseBody
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public User test(Model model,@RequestParam(value = "id", required = true) String id) {
		User user = userService.findUserByLoginName("qweqewqwed");
		user.setRoleStatus(user.getRole().getStatus());
		return user;
	}
	
}
