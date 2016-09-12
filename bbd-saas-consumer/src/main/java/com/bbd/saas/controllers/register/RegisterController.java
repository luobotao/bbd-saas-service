package com.bbd.saas.controllers.register;

import com.bbd.saas.Services.RedisService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.form.CompanyForm;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.common.ConcurrentReaderHashMap;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 公司、站点注册相关处理
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
	public static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	@Autowired
	RedisService redisService;
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	PostcompanyService postcompanyService;
	@Value("${oss.access.id}")
	private String ACCESS_ID ;
	@Value("${oss.access.key}")
	private String ACCESS_KEY ;
	@Value("${oss.bucket.name}")
	private String BUCKET_NAME;
	@Value("${oss.upload.vendorimg.image}")
	private String path;
	@Value("${oss.url}")
	private String ossUrl;







	@RequestMapping(value="/registerFirst", method=RequestMethod.GET)
	public String registerFirst(Model model) {
		return "register/registerFirst";
	}
	@RequestMapping(value="/registerSecond", method=RequestMethod.GET)
	public String registerSecond(@RequestParam(value = "phone", required = true) String phone,Model model) {
		model.addAttribute("phone",phone);
		return "register/registerSecond";
	}
	@RequestMapping(value="/registerCompany", method=RequestMethod.GET)
	public String registerCompany(@RequestParam(value = "phone", required = true) String phone,Model model) {
		model.addAttribute("phone",phone);
		return "register/registerCompany";
	}
	@RequestMapping(value="/registerSite", method=RequestMethod.GET)
	public String registerSite(@RequestParam(value = "phone", required = true) String phone,Model model) {
		model.addAttribute("phone",phone);
		List<Postcompany> postcompanyList = postcompanyService.selectAllByStatus("1");//审核通过的公司列表
		model.addAttribute("postcompanyList",postcompanyList);
		return "register/registerSite";
	}


	@RequestMapping(value="/regitsterCompanyView", method=RequestMethod.GET)
	public String regitsterCompanyView(Model model, HttpServletRequest request) {
		Postcompany postcompany =postcompanyService.selectPostmancompanyById(Numbers.parseInt(request.getParameter("companyId"),0));
		User user = userService.findUserByLoginName(request.getParameter("phone"));
		model.addAttribute("postcompany",postcompany);
		model.addAttribute("user",user);
		model.addAttribute("ossUrl",ossUrl);
		return "register/regitsterCompanyView";
	}

	/**
	 * 审核中的页面
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value="/regitsterSiteView", method=RequestMethod.GET)
	public String regitsterSiteView(Model model, HttpServletRequest request) {
		Site site =siteService.findSite(request.getParameter("siteid"));
		model.addAttribute("site",site);
		model.addAttribute("ossUrl",ossUrl);
		return "register/regitsterSiteView";
	}

	/**
	 * 站点驳回后修改信息
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value="/regitsterSiteUpdate", method=RequestMethod.GET)
	public String regitsterSiteUpdate(Model model, HttpServletRequest request) {
		Site site =siteService.findSite(request.getParameter("siteid"));
		List<Postcompany> postcompanyList = postcompanyService.selectAllByStatus("1");//审核通过的公司列表
		model.addAttribute("postcompanyList",postcompanyList);
		model.addAttribute("site",site);
		return "register/regitsterSiteUpdate";
	}
	/**
	 * 公司驳回后修改信息
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value="/regitsterCompanyUpdate", method=RequestMethod.GET)
	public String regitsterCompanyUpdate(Model model, HttpServletRequest request) {
		Postcompany postcompany =postcompanyService.selectPostmancompanyById(Numbers.parseInt(request.getParameter("companyId"),0));
		User user = userService.findUserByLoginName(request.getParameter("phone"));
		model.addAttribute("postcompany",postcompany);
		model.addAttribute("user",user);
		model.addAttribute("ossUrl",ossUrl);
		return "register/regitsterCompanyUpdate";
	}
	/**
	 * 验证用户名是否存在
	 * @param model
	 * @param loginName
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/checkLoginName", method=RequestMethod.GET)
	public Boolean checkLoginName(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		try{
			User user = userService.findUserByLoginName(loginName);
			if(user==null)
				return true;
			else
				return false;
		}catch (Exception e){
			return false;
		}
	}
	/**
	 * 注册用户基本信息
	 * @param username
	 * @param verifyCode
	 * @param password
	 * @param redirectAttrs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveUser",method=RequestMethod.POST)
	public Object saveUser(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "verifyCode", required = true) String verifyCode, @RequestParam(value = "password", required = true) String password, RedirectAttributes redirectAttrs) {
		Map<String, Object> result = new ConcurrentReaderHashMap();
		String verifyCodeInRedis = redisService.get(Constants.BBD_SAAS_VERIFY_CODE + username);
		logger.info("用户输入的验证码为："+verifyCode+"；数据库中存在的验证码为："+verifyCodeInRedis);
		if(verifyCode.equals(verifyCodeInRedis) ){
			User userTemp = userService.findUserByLoginName(username);//再次确认此手机号不存在
			if(userTemp==null){
				//向用户表插入登录用户
				User user = new User();
				user.setLoginName(username);//手机号即为登录名
				user.setPassWord(password);
				user.setDateAdd(new Date());
				user.setDateUpdate(new Date());
				user.setUserStatus(UserStatus.INVALID);//注册出来的用户默认为无效
				Key<User> userKey = userService.save(user);
				user.setId((ObjectId)userKey.getId());
				redisService.delete(Constants.BBD_SAAS_VERIFY_CODE + username);//验证码失效
				result.put("status", "1");
				result.put("msg", "ok");
				return result;
			}else{//用户已存在
				result.put("status", ErrorCode.getErrorCode("global.phoneExist"));
				result.put("msg", ErrorCode.getErrorMsg("global.phoneExist"));
				return result;
			}
		}else {//验证码错误
			result.put("status", ErrorCode.getErrorCode("global.verifyCodeError"));
			result.put("msg", ErrorCode.getErrorMsg("global.verifyCodeError"));
			return result;
		}
	}

	/**
	 * 注册公司
	 * @param licensePic
	 * @param companyForm
	 * @param result
	 * @param model
	 * @param redirectAttrs
	 * @return
     * @throws IOException
     */
	@RequestMapping(value="/saveCompany",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	public String saveCompany(@RequestParam MultipartFile licensePic, @Valid CompanyForm companyForm, BindingResult result, Model model,
							  RedirectAttributes redirectAttrs) throws IOException {
		User user = userService.findUserByLoginName(companyForm.getPhone());//
		Postcompany postcompany = new Postcompany();
		if(StringUtils.isNotBlank(companyForm.getCompanyId())){
			postcompany = postcompanyService.selectPostmancompanyById(Numbers.parseInt(companyForm.getCompanyId(),0));
		}
		BeanUtils.copyProperties(companyForm,postcompany);
		if (licensePic != null  && licensePic.getInputStream() != null && licensePic.getSize()>0) {
			String fileName = licensePic.getOriginalFilename();
			int p = fileName.lastIndexOf('.');
			String type = fileName.substring(p, fileName.length()).toLowerCase();
			if (".jpg".equals(type)||".gif".equals(type)||".png".equals(type)||".jpeg".equals(type)||".bmp".equals(type)) {
				// 检查文件后缀格式
				String fileNameLast = UUID.randomUUID().toString().replaceAll("-", "")+type;//最终的文件名称
				String endfilestr = OSSUtils.uploadFile(licensePic.getInputStream(),path,fileNameLast,licensePic.getSize(), type,BUCKET_NAME,ACCESS_ID,ACCESS_KEY);
				postcompany.setLicensePic(endfilestr);
			}
		}
		postcompany.setCompanycode(PinyinUtil.getFirstSpell(postcompany.getCompanyname()));//暂时未检查重复问题
		if(StringUtils.isBlank(companyForm.getCompanyId())){
			postcompany.setAlipaypublickey("");
			postcompany.setAppid("");
			postcompany.setDeliveryflag("1");
			postcompany.setIshot("0");
			postcompany.setLogo("");
			postcompany.setPrivatekey("");
			postcompany.setDateNew(new Date());
			postcompany.setDateUpd(new Date());
			postcompany.setSta("0");//未审核
			int companyId = postcompanyService.insertCompany(postcompany).getId();
			user.setCompanyId(String.valueOf(companyId));
			user.setDateUpdate(new Date());
			user.setRealName(companyForm.getResponser());
			user.setEmail(companyForm.getEmail());
			user.setRole(UserRole.COMPANY);
			userService.save(user);//更新用户
			redirectAttrs.addAttribute("companyId",companyId);
		}else {
			user.setDateUpdate(new Date());
			user.setRealName(companyForm.getResponser());
			user.setEmail(companyForm.getEmail());
			user.setRole(UserRole.COMPANY);
			userService.save(user);//更新用户
			postcompany.setDateUpd(new Date());
			postcompany.setSta("0");//未审核
			postcompanyService.updateCompany(postcompany);
			redirectAttrs.addAttribute("companyId",companyForm.getCompanyId());
		}
		redirectAttrs.addAttribute("phone",companyForm.getPhone());
		return "redirect:regitsterCompanyView";
	}


	/**
	 * 注册站点
	 * @param siteForm
	 * @param result
	 * @param model
	 * @param redirectAttrs
	 * @return
     * @throws IOException
     */
	@RequestMapping(value="/saveSite",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	public String saveSite(@Valid SiteForm siteForm, BindingResult result,Model model,RedirectAttributes redirectAttrs) throws IOException {
		User user = userService.findUserByLoginName(siteForm.getPhone());//
		Site site = new Site();

		BeanUtils.copyProperties(siteForm,site);
		site.setDateAdd(new Date());
		site.setDateUpd(new Date());
		site.setStatus(SiteStatus.WAIT);
		site.setMemo("您的棒棒达快递账号申请信息提交成功。我们将在1-3个工作日完成审核。");
		site.setAreaCode("");
		site.setUsername(siteForm.getPhone());
		Postcompany postcompany =postcompanyService.selectPostmancompanyById(Numbers.parseInt(site.getCompanyId(),0)) ;
		if(postcompany!=null){
			site.setCompanyName(postcompany.getCompanyname());
			site.setCompanycode(postcompany.getCompanycode());
		}
		Key<Site> siteKey = siteService.save(site);//保存站点
		redirectAttrs.addAttribute("siteid",siteKey.getId().toString());
		//向用户表插入登录用户
		user.setRealName(site.getResponser());
		site.setId(new ObjectId(siteKey.getId().toString()));
		user.setSite(site);
		user.setRole(UserRole.SITEMASTER);
		user.setCompanyId(site.getCompanyId());
		user.setDateUpdate(new Date());
		user.setEmail(siteForm.getEmail());
		userService.save(user);//更新用户
		return "redirect:regitsterSiteView";
	}
}
