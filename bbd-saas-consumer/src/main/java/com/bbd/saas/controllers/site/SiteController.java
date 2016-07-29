package com.bbd.saas.controllers.site;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.Result;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExportUtil;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.vo.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
	@Autowired
	AdminService adminService;
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


	public static final int MAXSIZE = 100000;

	/**
	 * 更新配送范围
	 * @param radius 半径
	 * @param siteId 站点编号
	 * @param request 请求
     * @return
     */
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

	/**
	 * 删除站点关键词
	 * @param id 关键词id
	 * @param model
	 * @param request 请求
     * @return
     */
	@RequestMapping(value="/deleteSitePoiKeyword/{id}", method=RequestMethod.GET)
	public String deleteSitePoiKeyword(@PathVariable String id, Model model, HttpServletRequest request){
		Result result = siteKeywordApi.deleteSitePoiKeyword(id);
		return "redirect:/deliverRegion/map/3";
	}

	/**
	 * 批量删除
	 * @param ids id编号集合
	 * @param model
	 * @param request 请求
     * @return
     */
	@RequestMapping(value="/piliangDeleteSitePoiKeyword/{ids}", method=RequestMethod.GET)
	public String piliangDeleteSitePoiKeyword(@PathVariable String ids, Model model, HttpServletRequest request){
		List<String> idList = Arrays.asList(ids.split(","));
		Result result = siteKeywordApi.deleteSitePoiKeyword(idList);
		logger.info("批量删除完成");
		return "redirect:/deliverRegion/map/3";
	}

	/**
	 * 导入关键词
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
		User user = adminService.get(UserSession.get(request));
		String siteId = user.getSite().getId().toString();
		try	{
			InputStream input = fileTmp.getInputStream();
			XSSFWorkbook workBook = new XSSFWorkbook(input);
			XSSFSheet sheet = workBook.getSheetAt(0);
			if (sheet != null){
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
					XSSFRow row = sheet.getRow(i);
					Result result = siteKeywordApi.addSitePoiKeyword(siteId,row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString());
					logger.info("[import result]"+result.toString());
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
		//火狐浏览器没有扩展名
		//response.setContentType("application/binary;charset=ISO8859_1");
		//扩展名.xlsx
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=ISO8859_1");
		try
		{
			ServletOutputStream outputStream = response.getOutputStream();
			String fileName = new String((siteName).getBytes(), "ISO8859_1")+Dates.formatDate2(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			PageList<SiteKeyword> siteKeywordPageList = siteKeywordApi.findSiteKeyword(siteId+"",null,null,0,MAXSIZE,"");
			List<SiteKeyword> siteKeywordList = siteKeywordPageList.list;
			String[] titles = { "省/直辖市", "市", "区", "地址关键词" };
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
		String fileName="地址关键词模板.xlsx";//new String("地址关键词模板.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
		File file = getDictionaryFile(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", new String("地址关键词模板.xlsx".getBytes("UTF-8"),"iso-8859-1"));
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
	@RequestMapping(value="putAllOverLay", method=RequestMethod.POST)
	public Result putAllOverLay(@RequestParam String siteId, @RequestParam String jsonStr){
		//处理电子围栏数据
		String[] pointArr = jsonStr.split(";");
		List<List<MapPoint>> points = new ArrayList<List<MapPoint>>();
		for (String pointStr: pointArr) {
			List<MapPoint> mapPointList = new ArrayList<MapPoint>();
			String[] mapPointArr = pointStr.split(",");
			for (String mapPointStr: mapPointArr) {
				String[] arr = mapPointStr.split("_");
				MapPoint mapPoint = new MapPoint(Numbers.parseDouble(arr[0],0.0),Numbers.parseDouble(arr[1],0.0));
				mapPointList.add(mapPoint);
			}
			points.add(mapPointList);
		}
		/*User user = adminService.get(UserSession.get(request));
		String siteId = user.getSite().getId().toString();*/
		Result result = new Result();
		try{
			result = sitePoiApi.updateSiteEfence(siteId,points);
			logger.info("保存电子围栏结果：" + result.code + "，"+result.toString());
		}catch (Exception e){
			result.code=-1;
			logger.info("保存电子围栏异常：" + e.toString());
		}
		return result;

	}

	//--------------------------站点配送区域引导--------------------------------------------------
	@ResponseBody
	@RequestMapping(value="/deliverRegionWithAjax", method=RequestMethod.POST)
	public Map<String, Object> deliverRegionWithAjax(Model model, HttpServletRequest request){
		return dealSiteKeywordWithAjax(request);
	}

	@ResponseBody
	@RequestMapping(value = "importSiteKeywordFileWithAjax", method=RequestMethod.POST)
	public   Map<String, Object> importSiteKeywordFileWithAjax(@RequestParam("file") MultipartFile file,  RedirectAttributes redirectAttributes,HttpServletRequest request) {
		MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
		MultipartFile fileTmp = mulRequest.getFile("file");
		String filename = fileTmp.getOriginalFilename();
		if (filename == null || "".equals(filename)){
			return null;
		}
		User user = adminService.get(UserSession.get(request));
		String siteId = user.getSite().getId().toString();
		try	{
			InputStream input = fileTmp.getInputStream();
			XSSFWorkbook workBook = new XSSFWorkbook(input);
			XSSFSheet sheet = workBook.getSheetAt(0);
			if (sheet != null){
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
					XSSFRow row = sheet.getRow(i);
					Result result = siteKeywordApi.addSitePoiKeyword(siteId,row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString());
					logger.info("[import result]"+result.toString());
					logger.info("成功导入"+i+"条，地址："+row.getCell(3).toString());
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		Map<String, Object> model = dealSiteKeywordWithAjax(request);
		return model;
	}

	/**
	 * 处理ajax返回的关键词的信息
	 * @param request
	 * @return
     */
	public  Map<String, Object> dealSiteKeywordWithAjax(HttpServletRequest request) {
		//获取用户站点信息
		//--------panel 1-----------------------
		User user = adminService.get(UserSession.get(request));
		Site site = siteService.findSite(user.getSite().getId().toString());
		String between = request.getParameter("between");
		String keyword = request.getParameter("keyword")==null?"":request.getParameter("keyword");
		int page = Numbers.parseInt(request.getParameter("page"),0);
		PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
		if(StringUtils.isNotBlank(between)) {//导入时间
			DateBetween dateBetween = new DateBetween(between);
			logger.info(dateBetween.getStart()+":"+dateBetween.getEnd());
			//导入地址关键词
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId()+"",dateBetween.getStart(),dateBetween.getEnd(),page,10,keyword);
		}else{
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
		}
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("siteKeywordPageList", siteKeywordPage.list);
		modelMap.put("page", siteKeywordPage.getPage());
		modelMap.put("pageNum", siteKeywordPage.getPageNum());
		modelMap.put("pageCount", siteKeywordPage.getCount());
		return modelMap;
	}

	@ResponseBody
	@RequestMapping(value="/deleteSitePoiKeywordWithAjax/{id}", method=RequestMethod.GET)
	public Map<String, Object> deleteSitePoiKeywordWithAjax(@PathVariable String id, Model model, HttpServletRequest request){
		Result result = siteKeywordApi.deleteSitePoiKeyword(id);
		return dealSiteKeywordWithAjax(request);
	}



	@ResponseBody
	@RequestMapping(value="/piliangDeleteSitePoiKeywordWithAjax/{ids}", method=RequestMethod.GET)
	public Map<String, Object> piliangDeleteSitePoiKeywordWithAjax(@PathVariable String ids, Model model, HttpServletRequest request){
		List<String> idList = Arrays.asList(ids.split(","));
		Result result = siteKeywordApi.deleteSitePoiKeyword(idList);
		logger.info("批量删除完成");
		return dealSiteKeywordWithAjax(request);
	}

	/**
	 * 根据省市区查询站点集合--select下拉框
	 * @param prov 省
	 * @param city 市
	 * @param area 区
	 * @param request 请求
     * @return siteList(name,areaCode)
     */
	@ResponseBody
	@RequestMapping(value="/getSiteList", method=RequestMethod.GET)
	public List<Option> getSiteListByAddr(String prov, String city, String area, String siteName, Integer isAll, final HttpServletRequest request) {
		String userId = UserSession.get(request);
		if(userId != null && !"".equals(userId)) {
			//当前登录的用户信息
			User currUser = adminService.get(userId);
			List<SiteStatus> statusList = null;
			if(isAll != 1){
				//查询登录用户的公司下的所有站点
				statusList = new ArrayList<SiteStatus>();
				statusList.add(SiteStatus.APPROVE);
				statusList.add(SiteStatus.INVALID);
			}
			return siteService.findOptByCompanyIdAndAddress(currUser.getCompanyId(), prov, city, area, siteName, statusList);
		}
		return null;
	}


}
