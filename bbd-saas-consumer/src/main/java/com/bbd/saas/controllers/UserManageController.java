package com.bbd.saas.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.bbd.saas.mongoModels.Site;
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
	public String listUser(HttpServletRequest request,Model model,Integer pageIndex, Integer roleId, Integer status,String keyword) {
		User getuser = adminService.get(UserSession.get(request));
		PageModel<User> userPage = getUserPage(request,0,roleId,status,keyword);

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
	public PageModel<User> getUserPage(HttpServletRequest request,Integer pageIndex, Integer roleId, Integer status,String keyword) {
		System.out.println("=================beigin======================");
		User getuser = adminService.get(UserSession.get(request));
		if (pageIndex==null) pageIndex =0 ;
		if(keyword!=null && !keyword.equals("")){
			try {
				
				System.out.println("========================keyword=="+keyword);
				keyword = URLDecoder.decode(keyword,"UTF-8");
				System.out.println("========================URLDecoder.decode(keyword,UTF-8)=="+keyword);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("=================end======================");
		UserQueryVO userQueryVO = new UserQueryVO();
		userQueryVO.roleId=roleId;
		userQueryVO.status=status;
		userQueryVO.keyword=keyword;
		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<User> userPage = userService.findUserList(pageModel,userQueryVO,getuser.getSite());
		
		for(User user : userPage.getDatas()){
			user.setRoleMessage(user.getRole().getMessage());
			if(user.getUserStatus()!=null && !user.getUserStatus().getMessage().equals("")){
				user.setStatusMessage(user.getUserStatus().getMessage());
			}
		}
		
		return userPage;
	}
	
	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/getUserPageFenYe", method = RequestMethod.GET)
	public PageModel<User> getUserPageFenYe(HttpServletRequest request,Integer pageIndex, Integer roleId, Integer status,String keyword) {
		
		PageModel<User> userPage = getUserPage(request,pageIndex,roleId,status,keyword);
		
		return userPage;
	}
	
	/**
     * 保存新建用户
     * @param userForm
     * @return
     */
	@ResponseBody
	@RequestMapping(value="saveUser", method=RequestMethod.POST)
	public String saveUser(HttpServletRequest request,@Valid UserForm userForm, BindingResult result,Model model,
			RedirectAttributes redirectAttrs,HttpServletResponse response) throws IOException {
		/*String realName = "";
		
		try {
			realName=new String(userForm.getRealName().getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		User getuser = adminService.get(UserSession.get(request));
		//验证该loginName在user表中是否已存在
		User loginuser = userService.findUserByLoginName(userForm.getLoginName());
		//验证该在同一个站点下staffid是否已存在
		//User staffiduser = userService.findOneBySiteByStaffid(getuser.getSite(), userForm.getStaffid());
		//查找在mysql的bbt数据库的postmanuser表中是否存在改userForm.getLoginName() 即手机号记录
		PostmanUser postmanUser = userMysqlService.selectPostmanUserByPhone(userForm.getLoginName()); 
		System.out.println("ssss");
		Map<String, Object> map = new HashMap<String, Object>();
	    java.util.Date dateAdd = new java.util.Date();
		User user = new User();
		user.setRealName(userForm.getRealName().replaceAll(" ", ""));
		user.setLoginName(userForm.getLoginName().replaceAll(" ", ""));
		//user.setPhone(userForm.getPhone());
		user.setPassWord(userForm.getLoginPass());
		user.setSite(getuser.getSite());
		user.setOperate(getuser);
		//staffid就是该用户的手机号
		user.setStaffid(userForm.getLoginName().replaceAll(" ", ""));
		user.setRole(UserRole.status2Obj(Integer.parseInt(userForm.getRoleId())));
		user.setDateAdd(dateAdd);
		user.setUserStatus(UserStatus.status2Obj(1));
		System.out.println("============="+user.getUserStatus().getStatus());
		
		if((loginuser!=null && !loginuser.getId().equals("")) || postmanUser!=null && postmanUser.getId()!=null){
			////loginName在user表中已存在
			return "false";
			
		}else{ 
			//loginName在user表中不存在
			Key<User> kuser = userService.save(user);
			postmanUser = new PostmanUser();
			postmanUser.setNickname(userForm.getRealName().replaceAll(" ", ""));
			postmanUser.setHeadicon("");
			postmanUser.setCardidno("");
			postmanUser.setCompanyname(getuser.getSite().getCompanyName()!=null?getuser.getSite().getCompanyName():"");
			postmanUser.setCompanyid(getuser.getSite().getCompanyId()!=null?Integer.parseInt(getuser.getSite().getCompanyId()):0);
			
			postmanUser.setSubstation(getuser.getSite().getName());
			postmanUser.setAlipayAccount("");
			postmanUser.setToken("");
			postmanUser.setBbttoken("");
			postmanUser.setLat(0d);
			postmanUser.setLon(0d);
			postmanUser.setHeight(0d);
			postmanUser.setAddr("");
			postmanUser.setAddrdes("");
			postmanUser.setShopurl("");
			postmanUser.setSta("1");
			postmanUser.setSpreadticket("");
			postmanUser.setPhone(userForm.getLoginName().replaceAll(" ", ""));
			//staffid就是该用户的手机号
			postmanUser.setStaffid(userForm.getLoginName().replaceAll(" ", ""));
			postmanUser.setDateNew(dateAdd);
			postmanUser.setPoststatus(1);
			/*if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==1){
				//快递员
				postmanUser.setPostrole(0);
			}else if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==0){
				//站长
				postmanUser.setPostrole(4);
			}*/
			//快递员
			postmanUser.setPostrole(0);

			int ret = userMysqlService.insertUser(postmanUser);
			System.out.println("idddd=="+postmanUser.getId());
			if(kuser!=null && !kuser.getId().equals("")){
				
				//postmanUser = userMysqlService.selectPostmanUserByPhone(userForm.getLoginName()); 
				int postmanuserId = userMysqlService.selectIdByPhone(userForm.getLoginName());
				user = userService.findOne(kuser.getId().toString());
				user.setPostmanuserId(postmanuserId);
				kuser = userService.save(user);
				return "true";
			}else{
				return "false";
			}
			
		}
	}
	
	/**
     * 保存修改用户
     * @param userForm
     * @return 
     */
	@ResponseBody
	@RequestMapping(value="editUser", method=RequestMethod.POST)
	public String editUser(HttpServletRequest request,@Valid UserForm userForm, BindingResult result,Model model,
			RedirectAttributes redirectAttrs,HttpServletResponse response) throws IOException {
		System.out.println("ssss");
		//查找在mysql的bbt数据库的postmanuser表中是否存在改userForm.getLoginName() 即手机号记录
		String retSign = "";
		PostmanUser getpostmanUser = userMysqlService.selectPostmanUserByPhone(userForm.getLoginName()); 
		/*String realName = "";
		
		try {
			realName=new String(userForm.getRealName().getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		User getuser = adminService.get(UserSession.get(request));
		User olduser = userService.findUserByLoginName(userForm.getLoginNameTemp());
		
		Map<String, Object> map = new HashMap<String, Object>();
	    java.util.Date dateUpdate = new java.util.Date();
		User user = new User();
		logger.info(userForm.getLoginNameTemp());
		olduser.setRealName(userForm.getRealName().replaceAll(" ", ""));
		//olduser.setLoginName(userForm.getLoginName());
		//olduser.setPhone(userForm.getPhone());
		//olduser.setStaffid(userForm.getStaffid().replaceAll(" ", ""));
		olduser.setPassWord(userForm.getLoginPass());
		if(!olduser.getId().equals(getuser.getId())){
			//如果修改的用户为站长且修改的用户就是当前登录用户的话，不执行olduser.setOperate(getuser);
			olduser.setOperate(getuser);
		}
		olduser.setRole(UserRole.status2Obj(Integer.parseInt(userForm.getRoleId())));
		olduser.setDateUpdate(dateUpdate);
		
		Key<User> kuser = userService.save(olduser);
		PostmanUser postmanUser = new PostmanUser();
		postmanUser.setDateUpd(dateUpdate);
		postmanUser.setNickname(userForm.getRealName().replaceAll(" ", ""));
		postmanUser.setPhone(userForm.getLoginName());
		/*if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==1){
			//快递员
			postmanUser.setPostrole(0);
			
		}else if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==0){
			postmanUser.setPostrole(4);
		}*/
		//快递员
		postmanUser.setPostrole(0);
		if(kuser!=null && !kuser.getId().equals("") && getpostmanUser!=null && getpostmanUser.getId()!=null){
			//postmanuser表中有对应的数据,同时更新到mysql的bbt库的postmanuser表中
			//int ret = userMysqlService.updateByPhone(postmanUser);
			int updateret = userMysqlService.updatePostmanUserById(userForm.getRealName().replaceAll(" ", ""),getpostmanUser.getId());
			//return "true";
			retSign = "true";
			
		}else if(kuser!=null && !kuser.getId().equals("") && getpostmanUser==null){
			//postmanuser表中没有对应的数据,就不更新mysql bbt库中的postmanuser表
			//return "true";
			retSign = "true";
		}else{
			retSign = "false";
		}
		
		return retSign;
	}
	
	/**
	 * ajax异步调用
     * 根据realname验证在user表中是否已存在 
     * @param realname
     * @return "true"/"false"
     */
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
	
	/**
	 * ajax异步调用
     * 根据user的主键id或者loginName修改user的状态     
     * @param id、loginName、status
     * @return "true"/"false"
     */
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
			int ret = userMysqlService.updateById(Integer.parseInt(status),user.getPostmanuserId());
			return "true";
		}else{
			return "false";
		}
		
	}
	
	/**
	 * ajax异步调用
     * 根据loginName删除user表中的记录
     * @param loginName
     * @return "true"/"false"
     */
	@ResponseBody
	@RequestMapping(value="/delUser", method=RequestMethod.GET)
	public String delUser(Model model,@RequestParam(value = "loginName", required = true) String loginName,HttpServletResponse response) {
		User user = userService.findUserByLoginName(loginName);
		logger.info("loginName"+loginName);
		userService.delUser(user);
		userMysqlService.deleteById(user.getPostmanuserId());
		return "true";
		
	}
	
	/**
	 * ajax异步调用
     * 验证user表下是否已存在loginName记录、以及在mysql bbt 中的postmanuser表中是否存在
     * @param loginName
     * @return "true"/"false"
     */
	@ResponseBody
	@RequestMapping(value="/checkLognName", method=RequestMethod.GET)
	public String checkLognName(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		User user = userService.findUserByLoginName(loginName);
		PostmanUser postmanUser = userMysqlService.selectPostmanUserByPhone(loginName);
		logger.info("loginName"+loginName);
		if((user!=null && !user.getId().equals("") || (postmanUser!=null && postmanUser.getId()!=null))){
			return "true";
			
		}else{ 
			return "false";
			
		}
	}
	
	/**
	 * ajax异步调用
     * 根据user对象的site和staffid参数查找在相同的site下是否有该staffid
     * @param staffid
     * @return "true"/"false"
     */
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
	
	/**
	 * ajax异步调用
     * 根据user对象的id获取user对象
     * @param id
     * @return user
     */
	@ResponseBody
	@RequestMapping(value="/getOneUser", method=RequestMethod.GET)
	public User getOneUser(Model model,@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "loginName", required = true) String loginName) {
		User user = null;
		/*try {
			loginName=new String(loginName.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {

		}*/
		if(id!=null && !id.equals("")){
			user = userService.findOne(id);
		}else if(loginName!=null && !loginName.equals("")){
			user = userService.findUserByLoginName(loginName);
		}
		user.setRoleStatus(user.getRole().getStatus());
		return user;
	}
	
}
