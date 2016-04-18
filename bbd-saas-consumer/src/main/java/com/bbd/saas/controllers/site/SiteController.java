package com.bbd.saas.controllers.site;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.Result;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.form.SiteForm;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExportUtil;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.OSSUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
	@Autowired
	SitePoiApi sitePoiApi;
	@Autowired
	SiteKeywordApi siteKeywordApi;
	@Autowired
	HttpServletRequest request;

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

	public static final int MAXSIZE = 100000;


	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(Model model) {
		return "site/siteRegister";
	}


	@RequestMapping(value="/siteView", method=RequestMethod.GET)
	public String siteView(Model model, HttpServletRequest request) {
		Site site =siteService.findSite(request.getParameter("siteid"));
		model.addAttribute("site",site);
		model.addAttribute("ossUrl",ossUrl);
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
//		if (result.hasErrors()) {
//			model.addAttribute("message","请检查必填项");
//			return null;
//		}
		Site site = new Site();
		if (licensePic != null  && licensePic.getInputStream() != null) {
			String fileName = licensePic.getOriginalFilename();
			int p = fileName.lastIndexOf('.');
			String type = fileName.substring(p, fileName.length()).toLowerCase();
			if (".jpg".equals(type)||".gif".equals(type)||".png".equals(type)||".jpeg".equals(type)||".bmp".equals(type)) {
					// 检查文件后缀格式
					String fileNameLast = UUID.randomUUID().toString().replaceAll("-", "")+type;//最终的文件名称
					String endfilestr = OSSUtils.uploadFile(licensePic.getInputStream(),path,fileNameLast,licensePic.getSize(), type,BUCKET_NAME,ACCESS_ID,ACCESS_KEY);
					site.setLicensePic(endfilestr);
			}
		}

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

	@ResponseBody
	@RequestMapping(value="/updateSiteWithRadius/{radius}/{siteId}", method = RequestMethod.GET)
	public String updateSiteWithRadius(@PathVariable String radius,@PathVariable String siteId,  HttpServletRequest request ) {
		Site site = siteService.findSite(siteId);
		//推送站点信息给poi
		String siteAddress = site.getProvince()+site.getCity()+site.getArea()+site.getAddress();
		site.setDeliveryArea(radius);
		Result<double[]> result = sitePoiApi.updateSitePOIBaseInfo(site.getId().toString(),"",site.getName(),siteAddress, Numbers.parseInt(radius,0));
		siteService.save(site);
		System.out.println("result code:"+result.code);
		return "success";
	}

	@RequestMapping(value="/deleteSitePoiKeyword/{id}", method=RequestMethod.GET)
	public String deleteSitePoiKeyword(@PathVariable String id, Model model, HttpServletRequest request){
		Result result = siteKeywordApi.deleteSitePoiKeyword(id);
		return "redirect:/deliverRegion/map/3";
	}

	@RequestMapping(value="/piliangDeleteSitePoiKeyword/{ids}", method=RequestMethod.GET)
	public String piliangDeleteSitePoiKeyword(@PathVariable String ids, Model model, HttpServletRequest request){
		List<String> idList = Arrays.asList(ids.split(","));
		Result result = siteKeywordApi.deleteSitePoiKeyword(idList);
		logger.info("批量删除完成");
		return "redirect:/deliverRegion/map/3";
	}

	/**
	 * 导入
	 * @return
	 */
	@RequestMapping(value = "importSiteKeywordFile", method=RequestMethod.POST)
	public String importSiteKeywordFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
		MultipartFile fileTmp = mulRequest.getFile("file");
		String filename = fileTmp.getOriginalFilename();
		if (filename == null || "".equals(filename)){
			return null;
		}
		try	{
			InputStream input = fileTmp.getInputStream();
			XSSFWorkbook workBook = new XSSFWorkbook(input);
			XSSFSheet sheet = workBook.getSheetAt(0);
			if (sheet != null){
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
					XSSFRow row = sheet.getRow(i);

					siteKeywordApi.addSitePoiKeyword("570e12e06efa872f6c15a7b5",row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString());
					logger.info("成功导入"+i+"条，地址："+row.getCell(3).toString());
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "redirect:/deliverRegion/map/3";
	}

	/**
	 * 导出该站点的关键词
	 * @return
     */
	@RequestMapping(value="/exportSiteKeywordFile", method=RequestMethod.GET)
	public String exportSiteKeywordFile(HttpServletRequest request,HttpServletResponse response){
		String userId = UserSession.get(request);
		User user = userService.findOne(userId);
		String siteId = user.getSite().getId().toString(); //request.getParameter("siteId");
		String siteName = user.getSite().getName().toString();//request.getParameter("siteName");
		String between = request.getParameter("between");
		String keyword = request.getParameter("keyword");
		response.setContentType("application/binary;charset=ISO8859_1");
		try
		{
			ServletOutputStream outputStream = response.getOutputStream();
			String fileName = new String((siteName).getBytes(), "ISO8859_1")+Dates.formatDateTime_New(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			PageList<SiteKeyword> siteKeywordPageList = siteKeywordApi.findSiteKeyword(siteId+"",null,null,0,MAXSIZE,"");
			List<SiteKeyword> siteKeywordList = siteKeywordPageList.list;
			String[] titles = { "省/直辖市", "市", "区", "地址关键词", "站点" };
			exportExcel(siteName,siteKeywordList, titles, outputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void exportExcel(String siteName,List<SiteKeyword> list, String[] titles, ServletOutputStream outputStream)
	{
		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet(siteName);
		ExportUtil exportUtil = new ExportUtil(workBook, sheet);
		XSSFCellStyle headStyle = exportUtil.getHeadStyle();
		XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell = null;
		for (int i = 0; i < titles.length; i++)
		{
			cell = headRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(titles[i]);
		}
		// 构建表体数据
		if (list != null && list.size() > 0)
		{
			for (int j = 0; j < list.size(); j++)
			{
				XSSFRow bodyRow = sheet.createRow(j + 1);
				SiteKeyword siteKeyword = list.get(j);

				cell = bodyRow.createCell(0);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getProvince());

				cell = bodyRow.createCell(1);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getCity());

				cell = bodyRow.createCell(2);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getDistict());

				cell = bodyRow.createCell(3);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getKeyword());

				cell = bodyRow.createCell(4);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteName);
			}
		}
		try
		{
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				outputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出excel模板
	 * @param response
     * @return
     */
	@RequestMapping(value="/downloadSiteKeywordTemplate", method=RequestMethod.GET)
	public ResponseEntity<byte[]> downloadSiteKeywordTemplate(HttpServletResponse response) throws IOException {
		String fileName="siteKeywordTemplate.xlsx";//new String("你好.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
		File file = getDictionaryFile(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
				headers, HttpStatus.CREATED);
	}

	/**
	 * 获取需要下载的文件
	 * @return
     */
	public File getDictionaryFile(String filename) {
		String path = request.getSession().getServletContext().getRealPath("/") + "resources/tmpl/" + filename;
		logger.info("PATH:"+path);
		return new File(path);
	}

	//电子围栏
	@ResponseBody
	@RequestMapping(value="/updateSiteEfence/{siteId}", method=RequestMethod.GET)
	public String updateSiteEfence(@PathVariable String siteId){
		/*List<double[]> points = new ArrayList<double[]>();
        points.add(new double[]{116.476256,39.908467});
        points.add(new double[]{116.486964,39.904648});
        points.add(new double[]{116.474028,39.899888});
        points.add(new double[]{116.469716,39.904261});
        List list = new ArrayList();
        list.add(points);
        Result result = sitePoiApi.updateSiteEfence(siteId,list);
		logger.info(result.code+"");*/
		return "success";
	}

	//电子围栏
	@ResponseBody
	@RequestMapping(value="/putAllOverLay/{overlays}", method=RequestMethod.GET)
	public String putAllOverLay(@PathVariable Object overlays){
		logger.info(overlays.toString());
		return "success";
	}
}
