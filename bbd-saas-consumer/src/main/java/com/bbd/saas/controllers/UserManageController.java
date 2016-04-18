package com.bbd.saas.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;

/**
 * 版权：zuowenhai新石器时代<br/>
 * 作者：zuowenhai@neolix.cn <br/>
 * 生成日期：2016-04-11 <br/>
 * 描述：用户管理
 */
@Controller
@RequestMapping("/userManage")
@SessionAttributes("userForm")
public class UserManageController {
	public static final Logger logger = LoggerFactory.getLogger(UserManageController.class);
	@Autowired
	private UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	PostmanUserService userMysqlService;	
	
	
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
		try {
			
			if(keyword!=null && !keyword.equals("")){
				keyword=new String(keyword.getBytes("iso-8859-1"),"utf-8");
			}else{
				keyword = "";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UserQueryVO userQueryVO = new UserQueryVO();
		userQueryVO.roleId=roleId;
		userQueryVO.status=status;
		userQueryVO.keyword=keyword;
		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<User> userPage = userService.findUserList(pageModel,userQueryVO);
		
		for(User user : userPage.getDatas()){
			user.setRoleMessage(user.getRole().getMessage());
			if(user.getUserStatus()!=null && !user.getUserStatus().getMessage().equals("")){
				user.setStatusMessage(user.getUserStatus().getMessage());
			}
		}
		
		return userPage;
	}
	
	/**
     * 保存新建用户
     * @param model
     * @return
     */
	@ResponseBody
	@RequestMapping(value="saveUser", method=RequestMethod.POST)
	public String saveUser(HttpServletRequest request,@Valid UserForm userForm, BindingResult result,Model model,
			RedirectAttributes redirectAttrs,HttpServletResponse response) throws IOException {
		User getuser = adminService.get(UserSession.get(request));
		System.out.println("ssss");
		Map<String, Object> map = new HashMap<String, Object>();
	    java.util.Date dateAdd = new java.util.Date();
		User user = new User();
		user.setRealName(userForm.getRealName());
		user.setLoginName(userForm.getLoginName());
		user.setPhone(userForm.getPhone());
		user.setPassWord(userForm.getLoginPass());
		user.setSite(getuser.getSite());
		user.setOperate(getuser);
		user.setStaffid(userForm.getStaffid());
		user.setRole(UserRole.status2Obj(Integer.parseInt(userForm.getRoleId())));
		user.setDateAdd(dateAdd);
		user.setUserStatus(UserStatus.status2Obj(0));
		System.out.println("============="+user.getUserStatus().getStatus());
		Key<User> kuser = userService.save(user);
		/*PostmanUser postmanUser = new PostmanUser();
		postmanUser.setPhone(userForm.getPhone());
		postmanUser.setDateNew(dateAdd);
		postmanUser.setPoststatus(0);
		postmanUser.setPostrole(Integer.parseInt(userForm.getRoleId()));
		
		
		userMysqlService.insert(postmanUser);
		System.out.println("idddd=="+postmanUser.getId());
		return "true";*/
		if(kuser!=null && !kuser.getId().equals("")){
			return "true";
		}else{
			return "false";
		}
	}
	
	/**
     * 保存修改用户
     * @param model
     * @return
     */
	@ResponseBody
	@RequestMapping(value="editUser", method=RequestMethod.POST)
	public String editUser(HttpServletRequest request,@Valid UserForm userForm, BindingResult result,Model model,
			RedirectAttributes redirectAttrs,HttpServletResponse response) throws IOException {
		System.out.println("ssss");
		User getuser = adminService.get(UserSession.get(request));
		User olduser = userService.findUserByLoginName(userForm.getLoginNameTemp());
		
		Map<String, Object> map = new HashMap<String, Object>();
	    java.util.Date dateUpdate = new java.util.Date();
		User user = new User();
		logger.info(userForm.getLoginNameTemp());
		olduser.setRealName(userForm.getRealName());
		//olduser.setLoginName(userForm.getLoginName());
		olduser.setPhone(userForm.getPhone());
		olduser.setPassWord(userForm.getLoginPass());
		olduser.setOperate(getuser);
		olduser.setRole(UserRole.status2Obj(Integer.parseInt(userForm.getRoleId())));
		olduser.setDateUpdate(dateUpdate);
		
		
		Key<User> kuser = userService.save(olduser);
		
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
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "loginName", required = true) String loginName,HttpServletResponse response) {
		User user = null;
		
		try {
			loginName=new String(loginName.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		
		if(id!=null && !id.equals("")){
			user = userService.findOne(id);
		}else if(loginName!=null && !loginName.equals("")){
			user = userService.findUserByLoginName(loginName);
		}System.out.println("================="+UserStatus.status2Obj(Integer.parseInt(status)));
		user.setUserStatus(UserStatus.status2Obj(Integer.parseInt(status)));
		logger.info("id"+id);
		logger.info("loginName"+loginName);
		Key<User> kuser = userService.save(user);
		
		if(kuser!=null && !kuser.getId().equals("")){ 
			return "true";
		}else{
			return "false";
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="/delUser", method=RequestMethod.GET)
	public String delUser(Model model,@RequestParam(value = "loginName", required = true) String loginName,HttpServletResponse response) {
		User user = userService.findUserByLoginName(loginName);
		logger.info("loginName"+loginName);
		userService.delUser(user);
		
		return "true";
		
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
	@RequestMapping(value="/checkStaffIdBySiteByStaffid", method=RequestMethod.GET)
	public String checkStaffIdBySiteByStaffid(HttpServletRequest request,Model model,@RequestParam(value = "staffid", required = true) String staffid) {
		User getuser = adminService.get(UserSession.get(request));
		User user = userService.findOneBySiteByStaffid(getuser.getSite(), staffid);
		logger.info("staffid"+staffid);
		if(user!=null && !user.getId().equals("")){
			return "true";
			
		}else{ 
			return "false";
			
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/getOneUser", method=RequestMethod.GET)
	public User getOneUser(Model model,@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "loginName", required = true) String loginName) {
		User user = null;
		try {
			loginName=new String(loginName.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		if(id!=null && !id.equals("")){
			user = userService.findOne(id);
		}else if(loginName!=null && !loginName.equals("")){
			user = userService.findUserByLoginName(loginName);
		}
		user.setRoleStatus(user.getRole().getStatus());
		return user;
	}
	
}
