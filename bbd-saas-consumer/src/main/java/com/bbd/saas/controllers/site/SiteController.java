package com.bbd.saas.controllers.site;

import com.bbd.saas.api.AdminUserService;
import com.bbd.saas.api.SiteService;
import com.bbd.saas.api.UserService;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.form.LoginForm;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.OSSUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 站点相关处理
 */
@Controller
@RequestMapping("/site")
@SessionAttributes("loginForm")
public class SiteController {
	public static final Logger logger = LoggerFactory.getLogger(SiteController.class);
	@Autowired
	SiteService siteService;
	@Autowired
	UserService userService;

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(Model model) {
		return "site/siteRegister";
	}


	@RequestMapping(value="/siteView", method=RequestMethod.GET)
	public String siteView(Model model, HttpServletRequest request) {
		Site site =siteService.findSite(request.getParameter("siteid"));
		model.addAttribute("site",site);
//		OSSUtils ossUtils = new OSSUtils();
//		ossUtils.getACCESS_KEY();
		return "site/siteView";
	}

	@ResponseBody
	@RequestMapping(value="/checkSiteWithLoginName", method=RequestMethod.GET)
	public Boolean checkSiteWithUsername(Model model,@RequestParam(value = "loginName", required = true) String loginName) {
		User user = userService.findUserByLoginName(loginName);
		if(user==null)
			return true;
		else
			return false;
	}

	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processSubmit(@RequestParam MultipartFile licensePic, @Valid SiteForm siteForm, BindingResult result,Model model,RedirectAttributes redirectAttrs) throws IOException {
		redirectAttrs.addFlashAttribute("message", "注册成功");
		logger.info(licensePic.getName()+"====================="+licensePic.getOriginalFilename());
		if (result.hasErrors()) {
			model.addAttribute("message","请检查必填项");
			return null;
		}
//		if (licensePic != null) {
//			String path= Configuration.root().getString("oss.upload.vendorimg.image", "upload/vendorimg/images/");//上传路径
//			String BUCKET_NAME= StringUtil.getBUCKET_NAME();
//			if (licensePic != null && licensePic.getFile() != null) {
//				String fileName = licensePic.getFilename();
//				File file = licensePic.getFile();//获取到该文件
//				int p = fileName.lastIndexOf('.');
//				String type = fileName.substring(p, fileName.length()).toLowerCase();
//				if (".jpg".equals(type)||".gif".equals(type)||".png".equals(type)||".jpeg".equals(type)||".bmp".equals(type)) {
//					// 检查文件后缀格式
//					String fileNameLast = UUID.randomUUID().toString().replaceAll("-", "")+type;//最终的文件名称
//					String endfilestr = OSSUtils.uploadFile(file,path,fileNameLast, type,BUCKET_NAME);
//					formPage.licensePic=endfilestr;
//				}
//			}
//		}


		Site site = new Site();
		BeanUtils.copyProperties(siteForm,site);
		logger.info(siteForm.getEmail()+"000000000000000"+site.getEmail());
		site.setDateAdd(new Date());
		site.setDateUpd(new Date());
		site.setStatus(SiteStatus.WAIT);
		site.setMemo("提交成功，我们将在3-5个工作日内完成审核。\n" +
				"您可使用注册时填写的账号和密码登录，以查看审核状态。");
		site.setFlag("0");
		Key<Site> siteKey = siteService.save(site);//保存站点
		redirectAttrs.addAttribute("siteid",siteKey.getId().toString());
		//向用户表插入登录用户
		User user = new User();
		user.setLoginName(site.getUsername());
		user.setPassWord(site.getPassword());
		user.setDateAdd(new Date());
		user.setRealName(site.getResponser());
		site.setId(new ObjectId(siteKey.getId().toString()));
		user.setSite(site);
		user.setRole(UserRole.SITEMASTER);
		user.setPhone(site.getPhone());
		userService.save(user);
		return "redirect:siteView";
	}
}
