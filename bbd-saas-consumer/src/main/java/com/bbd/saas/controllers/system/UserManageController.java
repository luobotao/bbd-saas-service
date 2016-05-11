package com.bbd.saas.controllers.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.mongoModels.Site;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
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
import com.bbd.saas.api.mysql.OpenUserService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.UserForm;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.SignUtil;
import com.bbd.saas.vo.UserQueryVO;

import flexjson.JSONDeserializer;

/**
 * 版权：zuowenhai新石器时代<br/>
 * 作者：zuowenhai@neolix.cn <br/>
 * 生成日期：2016-04-11 <br/>
 * 描述：用户管理
 */
@Controller
@RequestMapping("/userManage")
public class UserManageController {
	public static final Logger logger = LoggerFactory.getLogger(UserManageController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private PostmanUserService userMysqlService;	
	@Autowired
	private OpenUserService openUserMysqlService;
	@Autowired
	SiteService siteService;
	

	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(HttpServletRequest request,Model model) {
		User userNow = adminService.get(UserSession.get(request));//当前登录用户
		if(userNow.getRole()==UserRole.COMPANY){
			List<Site> siteList = siteService.findSiteListByCompanyId(userNow.getCompanyId());
			model.addAttribute("siteList", siteList);
		}
		PageModel<User> userPage = getUserPage(request,0,null,null,null,null);

		model.addAttribute("userNow", userNow);
		model.addAttribute("userPage", userPage);
		return "systemSet/userManage";
	}
	
	/**
     * 获取用户列表信息
     * @param 
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/getUserPage", method = RequestMethod.GET)
	public PageModel<User> getUserPage(HttpServletRequest request,Integer pageIndex,String siteId,String roleId ,  Integer status,String keyword) {
		User userNow = adminService.get(UserSession.get(request));//当前登录用户
		if (pageIndex==null) pageIndex =0 ;

		UserQueryVO userQueryVO = new UserQueryVO();
		userQueryVO.roleId=roleId;
		userQueryVO.status=status;
		userQueryVO.keyword=keyword;

		PageModel<User> pageModel = new PageModel<>();
		pageModel.setPageNo(pageIndex);
		PageModel<User> userPage = new PageModel<>();
		if(UserRole.COMPANY==userNow.getRole()){//公司用户
			userQueryVO.companyId=userNow.getCompanyId();
			Site site = null;
			if(StringUtils.isNotBlank(siteId) && !"-1".equals(siteId)){
				site = siteService.findSite(siteId);
			}
			userPage = userService.findUserList(pageModel,userQueryVO,site);
		}else{//站长
			userPage = userService.findUserList(pageModel,userQueryVO,userNow.getSite());
		}

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
	public PageModel<User> getUserPageFenYe(HttpServletRequest request,Integer pageIndex, String siteId, String roleId, Integer status,String keyword) {
		PageModel<User> userPage = getUserPage(request,pageIndex,siteId,roleId,status,keyword);
		
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
		user.setDateUpdate(dateAdd);
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
			postmanUser.setSta("1");//对应mongdb user表中的userStatus,默认1位有效
			postmanUser.setSpreadticket("");
			postmanUser.setPhone(userForm.getLoginName().replaceAll(" ", ""));
			//staffid就是该用户的手机号
			postmanUser.setStaffid(userForm.getLoginName().replaceAll(" ", ""));
			postmanUser.setDateNew(dateAdd);
			postmanUser.setDateUpd(dateAdd);
			postmanUser.setPoststatus(1);//默认为1
			/*if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==1){
				//快递员
				postmanUser.setPostrole(0);
			}else if(userForm.getRoleId()!=null && Integer.parseInt(userForm.getRoleId())==0){
				//站长
				postmanUser.setPostrole(4);
			}*/
			//快递员
			postmanUser.setPostrole(0);

			int postmanuserId = userMysqlService.insertUser(postmanUser).getId();
			user = userService.findOne(kuser.getId().toString());
			user.setPostmanuserId(postmanuserId);
			userService.save(user);
			return "true";
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
		/*try {
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
		}*/
		
		if(loginName!=null && !loginName.equals("")){
			user = userService.findUserByLoginName(loginName);
			
			if(user!=null && !user.getId().equals("")){ 
				userService.updateUserStatu(loginName, UserStatus.status2Obj(Integer.parseInt(status)));
				int ret = userMysqlService.updateById(Integer.parseInt(status),user.getPostmanuserId());
				return "true";
			}else{
				return "false";
			}

			
		}else{
			return "false";
		}

		
	}
	
	
	/**
	 * ajax异步调用
     * 根据loginName修改user的状态     
     * @return "true"/"false"
     */
	//@ResponseBody
	@RequestMapping(value="/changestatusOuter", method=RequestMethod.POST)
	public void changestatusOuter(HttpServletRequest request,HttpServletResponse response) {
		User user = null;
		System.out.println();
		StringBuilder sb = null;
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			sb = new StringBuilder();   
		    String line = null;  
		    while ((line = in.readLine()) != null) {   
		         sb.append(line);   
		    }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		Map<String,Object> map = null;
		//String data = request.getParameter("data");
		JSONDeserializer deserializer = new JSONDeserializer();
		try{
			map = (Map<String,Object>)deserializer.deserialize(sb.toString());
			//System.out.println(map.get("atcode"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
		String sign = map.get("sign")!=null?(String) map.get("sign"):"";
		String code = map.get("code")!=null?(String) map.get("code"):"";
		String timestamp = map.get("timestamp")!=null?(String) map.get("timestamp"):"";
		String loginName = map.get("loginName")!=null?(String) map.get("loginName"):"";
		String newStatus = map.get("newStatus")!=null?(String) map.get("newStatus"):"";
		String oldStatus = map.get("oldStatus")!=null?(String) map.get("oldStatus"):"";
		//根据code查询对应的token
		String token = openUserMysqlService.selectTokenByCode(code);
		String retflag = "";
		
		Map<Object, Object> m = new HashMap<Object, Object>();
		
		m.put("newStatus", newStatus);
		m.put("oldStatus", oldStatus);
		m.put("loginName", loginName);
		m.put("timestamp", timestamp);

		String getSign = SignUtil.makeOpenSign(m,token);
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		if(getSign.equals(sign)){
			//判断签名是否正确
			if(loginName!=null && !loginName.equals("")){
				user = userService.findUserByLoginName(loginName);
				
				if(user!=null && !user.getId().equals("")){ 
					userService.updateUserStatu(loginName, UserStatus.status2Obj(Integer.parseInt(newStatus)));
					//int ret = userMysqlService.updateById(Integer.parseInt(newStatus),user.getPostmanuserId());
					retflag = "{\"result\":\"ok\"}";
					out.append(retflag);
				}else{
					retflag = "{\"result\":\"error\",\"code\":51002,\"msg\":\"参数错误\"}";
					out.append(retflag);
				}

				
			}else{
				retflag = "{\"result\":\"error\",\"code\":51002,\"msg\":\"参数错误\"}";
				out.append(retflag);
			}
		}else{
			retflag = "{\"result\":\"error\",\"code\":51002,\"msg\":\"签名错误\"}";
			out.append(retflag);
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
