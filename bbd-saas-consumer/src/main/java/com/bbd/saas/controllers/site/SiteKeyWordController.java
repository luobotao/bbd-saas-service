package com.bbd.saas.controllers.site;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
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
import com.bbd.saas.mongoModels.SiteKeyTemp;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.SiteVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 站点关键词相关处理
 */
@Controller
@RequestMapping("/siteKeyWord")
public class SiteKeyWordController {
	public static final Logger logger = LoggerFactory.getLogger(SiteKeyWordController.class);
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
	@Value("${oss.url}")
	private String ossUrl;
	public static final int MAXSIZE = 100000;

	/**
	 * 下载excel模板
	 * @param request 请求
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/downloadTemplate", method=RequestMethod.GET)
	public void downloadSiteKeywordTemplate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
			List<String> siteOptionList = null;
			if(siteVOList != null && siteVOList.size() > 0){
				siteOptionList = new ArrayList<String>();
				for (SiteVO siteVO : siteVOList){
					siteOptionList.add(siteVO.getName());
				}
			}
			//下载导出模板==数据写到Excel中并写入response下载
			//表格数据
			//表头
			String[] titles = { "站点名称", "省/直辖市", "市", "区", "地址关键词"};
			int[] colWidths = { 10000, 5000, 5000, 5000, 20000};
			ExportUtil exportUtil = new ExportUtil();
			exportUtil.setSelectOption(0, siteOptionList);
			exportUtil.exportExcel("地址关键词模板", null, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===下载地址关键词模板===出错:" + e.getMessage());
		}
	}

	/**
	 * 导出该站点的关键词
	 * @return
     */
	@RequestMapping(value="/exportKeyWord", method=RequestMethod.GET)
	public String exportSiteKeywordFile(HttpServletRequest request,HttpServletResponse response){
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		//查询登录用户的公司下的所有站点
		List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(currUser.getCompanyId(), SiteStatus.APPROVE);
		//火狐浏览器没有扩展名
		response.setContentType("application/binary;charset=ISO8859_1");
		//扩展名.xlsx
		//response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=ISO8859_1");
		try{
			ServletOutputStream outputStream = response.getOutputStream();
			String fileName = new String(("地址关键词").getBytes(), "ISO8859_1")+Dates.formatDate2(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			String[] titles = { "站点名称", "省/直辖市", "市", "区", "地址关键词"};
			int[] colWidths = { 10000, 5000, 5000, 5000, 20000};
			exportExcel(siteVOList, titles, colWidths, outputStream, request);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void exportExcel(List<SiteVO> siteVOList, String[] titles, int[] colWidths, ServletOutputStream outputStream, HttpServletRequest request) {
		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet("关键词");
		ExportUtil exportUtil = new ExportUtil(workBook, sheet);
		XSSFCellStyle headStyle = exportUtil.getHeadStyle();
		XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			cell = headRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(titles[i]);
		}
		if(colWidths != null){//设置列宽
			for (int i = 0; i < colWidths.length; i++){
				sheet.setColumnWidth(i, colWidths[i]);
			}
		}
		String siteId = request.getParameter("siteId");
		String between = request.getParameter("between");
		String keyword = StringUtil.initStr(request.getParameter("keyword"), "");
		DateBetween dateBetween = new DateBetween(between);

		if(siteId == null || "".equals(siteId) ){// 查询全部站点
			if(siteVOList != null && siteVOList.size() > 0){
				PageList<SiteKeyword> siteKeywordPageList = null;
				List<SiteKeyword> keywordList = null;
				if(StringUtils.isNotBlank(between)) {//预计站点入库时间
					logger.info(dateBetween.getStart()+":"+dateBetween.getEnd());
					for (SiteVO siteVO : siteVOList){
						siteKeywordPageList = siteKeywordApi.findSiteKeyword(siteVO.getId()+"", dateBetween.getStart(), dateBetween.getEnd(), 0, MAXSIZE, keyword);
						keywordList = siteKeywordPageList.list;
						getOneSiteKeyWords(siteVO.getName(), keywordList, sheet, bodyStyle);
					}
				}else{
					for (SiteVO siteVO : siteVOList){
						siteKeywordPageList = siteKeywordApi.findSiteKeyword(siteVO.getId()+"", null, null, 0, MAXSIZE, keyword);
						keywordList = siteKeywordPageList.list;
						getOneSiteKeyWords(siteVO.getName(), keywordList, sheet, bodyStyle);
					}
				}

			}
		}else{//单个站点
			PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
			if(StringUtils.isNotBlank(between)) {//预计站点入库时间
				logger.info(dateBetween.getStart()+":"+dateBetween.getEnd());
				//导入地址关键词
				siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId,dateBetween.getStart(),dateBetween.getEnd(), 0, MAXSIZE, keyword);
			}else{
				siteKeywordPage = siteKeywordApi.findSiteKeyword(siteId, null, null, 0, MAXSIZE, keyword);
			}
			List<SiteKeyword> keywordList = siteKeywordPage.list;
			Site site = siteService.findSite(siteId);
			String siteName = null;
			if(site == null){
				siteName = "";
			}else {
				siteName = StringUtil.initStr(site.getName(),"");
			}
			getOneSiteKeyWords(siteName, keywordList, sheet, bodyStyle);
		}

		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				outputStream.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 把一个站点的所有关键词添加到excel中
	 * @parm siteName 站点名称
	 * @param list 关键词列表
	 * @param sheet 表单
	 * @param bodyStyle excel样式
     */
	private void getOneSiteKeyWords(String siteName, List<SiteKeyword> list, XSSFSheet sheet, XSSFCellStyle bodyStyle){
		// 构建表体数据
		if (list != null && list.size() > 0){
			XSSFCell cell = null;
			for (int j = 0; j < list.size(); j++){
				XSSFRow bodyRow = sheet.createRow(j + 1);
				SiteKeyword siteKeyword = list.get(j);

				cell = bodyRow.createCell(0);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteName);

				cell = bodyRow.createCell(1);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getProvince());

				cell = bodyRow.createCell(2);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getCity());

				cell = bodyRow.createCell(3);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getDistict());

				cell = bodyRow.createCell(4);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(siteKeyword.getKeyword());
			}
		}
	}


	/**
	 * 导入关键词
	 * @return
	 */
	@RequestMapping(value = "importKeyword", method=RequestMethod.POST)
	public String importSiteKeywordFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try	{
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile fileTmp = mulRequest.getFile("file");
			String filename = fileTmp.getOriginalFilename();
			if (filename == null || "".equals(filename)){
				return null;
			}
			User user = adminService.get(UserSession.get(request));
			String siteId = user.getSite().getId().toString();
			InputStream input = fileTmp.getInputStream();
			XSSFWorkbook workBook = new XSSFWorkbook(input);
			XSSFSheet sheet = workBook.getSheetAt(0);
			List<SiteKeyTemp> siteKeyTempList = null;
			Site site = null;
			//校验站点名称
			if (sheet != null){
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
					XSSFRow row = sheet.getRow(i);
					SiteKeyTemp siteKeyTemp = new SiteKeyTemp();
					siteKeyTemp.setRow(i);
					siteKeyTemp.setCol(0);
				}
			}
			//保存
			/*Result result = siteKeywordApi.addSitePoiKeyword(siteId,row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString());
			logger.info("[import result]"+result.toString());
			logger.info("成功导入"+i+"条，地址："+row.getCell(3).toString());
			*/
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "redirect:/deliverArea/map/3";
	}

	//--------------------------站点配送区域引导--------------------------------------------------


	@ResponseBody
	@RequestMapping(value = "ajaxImportKeyword", method=RequestMethod.POST)
	public   Map<String, Object> ajaxImportKeyword(@RequestParam("file") MultipartFile file,  RedirectAttributes redirectAttributes,HttpServletRequest request) {
		MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
		MultipartFile fileTmp = mulRequest.getFile("file");
		String filename = fileTmp.getOriginalFilename();
		if (filename == null || "".equals(filename)){
			return null;
		}
		String siteId = StringUtil.initStr(request.getParameter("siteId"), "");
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
		Map<String, Object> model = dealSiteKeywordWithAjax(siteId, null);
		return model;
	}
	/**
	 * 导入关键词
	 * @return
	 */
	@RequestMapping(value = "importKeyWord", method=RequestMethod.POST)
	public String importSiteKeyword(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try	{
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile fileTmp = mulRequest.getFile("file");
			String filename = fileTmp.getOriginalFilename();
			if (filename == null || "".equals(filename)){
				return null;
			}
			String siteId = StringUtil.initStr(request.getParameter("siteId"), "");
			InputStream input = fileTmp.getInputStream();
			XSSFWorkbook workBook = new XSSFWorkbook(input);
			XSSFSheet sheet = workBook.getSheetAt(0);
			List<SiteKeyTemp> siteKeyTempList = null;
			Site site = null;
			//校验站点名称
			if (sheet != null){
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
					XSSFRow row = sheet.getRow(i);
					SiteKeyTemp siteKeyTemp = new SiteKeyTemp();
					siteKeyTemp.setRow(i);
					siteKeyTemp.setCol(0);
				}
			}
			//保存
			/*Result result = siteKeywordApi.addSitePoiKeyword(siteId,row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString());
			logger.info("[import result]"+result.toString());
			logger.info("成功导入"+i+"条，地址："+row.getCell(3).toString());
			*/
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "redirect:/deliverArea/map/3";
	}

	/**
	 * 处理ajax返回的关键词的信息
	 * @param siteId
	 * @param map
     * @return
     */
	public  Map<String, Object> dealSiteKeywordWithAjax(String siteId, Map<String, Object> map) {
		if (map == null){
			map = new HashMap<String, Object>();
		}
		//获取用户站点信息
		//--------panel 1-----------------------
		Site site = siteService.findSite(siteId);
		String between = request.getParameter("between");
		String keyword = request.getParameter("keyword")==null?"":request.getParameter("keyword");
		int pageIndex = Numbers.parseInt(request.getParameter("pageIndex"),0);
		PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
		if(StringUtils.isNotBlank(between)) {//预计站点入库时间
			DateBetween dateBetween = new DateBetween(between);
			logger.info(dateBetween.getStart()+":"+dateBetween.getEnd());
			//导入地址关键词
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId()+"",dateBetween.getStart(),dateBetween.getEnd(),pageIndex,10,keyword);
		}else{
			siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, pageIndex, 10, keyword);
		}
		//设置站点名称
		List<SiteKeyword> keywordList = siteKeywordPage.getList();
		if (keywordList != null && keywordList.size() > 0){
			System.out.println("count==impt==="+keywordList.size());
			String siteName = null;
			for(SiteKeyword siteKeyword : keywordList){
				site = siteService.findSite(siteKeyword.getSiteId());
				if(site != null){
					siteKeyword.siteId = StringUtil.initStr(site.getName(), "");
				}else {
					siteKeyword.siteId = "";
				}
			}
		}
		map.put("pageList", siteKeywordPage);
		return map;
	}

	/**
	 * 删除关键词
	 * @param id 关键词id
	 * @param request 请求
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/deleteKeyword/{id}", method=RequestMethod.GET)
	public Map<String, Object> deleteSitePoiKeywordWithAjax(@PathVariable String id, HttpServletRequest request){
		Result result = siteKeywordApi.deleteSitePoiKeyword(id);
		Map<String, Object> map = new HashMap<String, Object>();
		/*if(result != null && result.code == 1){//删除成功，刷新列表
			map.put("result", true);
			dealSiteKeywordWithAjax(request, map);
		}else{
			map.put("result", false);
		}*/
		if(result != null ){//删除成功，刷新列表
			map.put("result", true);
			String siteId = request.getParameter("siteId");
			dealSiteKeywordWithAjax(siteId, map);
		}else{
			map.put("result", false);
		}
		return map;
	}

	/**
	 * 批量删除关键词
	 * @param ids 关键词id集合
	 * @param request 请求
     * @return
     */
	@ResponseBody
	@RequestMapping(value="/batchDeleteKeyword/{ids}", method=RequestMethod.GET)
	public Map<String, Object> piliangDeleteSitePoiKeywordWithAjax(@PathVariable String ids, HttpServletRequest request){
		List<String> idList = Arrays.asList(ids.split(","));
		Result result = siteKeywordApi.deleteSitePoiKeyword(idList);
		Map<String, Object> map = new HashMap<String, Object>();
		if(result != null){//删除成功，刷新列表
			map.put("result", true);
			String siteId = request.getParameter("siteId");
			dealSiteKeywordWithAjax(siteId, map);
		}else{
			map.put("result", false);
		}
		logger.info("批量删除完成");
		return map;
	}


}
