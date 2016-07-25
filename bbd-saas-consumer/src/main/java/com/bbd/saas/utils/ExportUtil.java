package com.bbd.saas.utils;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExportUtil
{
	public static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);
	
	private  XSSFWorkbook wb = null;

	private  XSSFSheet sheet = null;
	//默认构造方法
	public ExportUtil(){};

	/**
	 * @param wb
	 * @param sheet
	 */
	public ExportUtil(XSSFWorkbook wb, XSSFSheet sheet)
	{
		this.wb = wb;
		this.sheet = sheet;
	}

	/**
	 * 合并单元格后给合并后的单元格加边框
	 *
	 * @param region
	 * @param cs
	 */
	public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs)
	{

		int toprowNum = region.getFirstRow();
		for (int i = toprowNum; i <= region.getLastRow(); i++)
		{
			XSSFRow row = sheet.getRow(i);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++)
			{
				XSSFCell cell = row.getCell(j);// XSSFCellUtil.getCell(row,
				// (short) j);
				cell.setCellStyle(cs);
			}
		}
	}

	/**
	 * 设置表头的单元格样式
	 *
	 * @return
	 */
	public XSSFCellStyle getHeadStyle()
	{
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格的背景颜色为淡蓝色
		cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 230);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 设置表体的单元格样式
	 *
	 * @return
	 */
	public XSSFCellStyle getBodyStyle()
	{
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		//font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 230);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		/*cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);*/
		return cellStyle;
	}
	
	/**
	 * Description: 把数据写入Excel，并下载
	 * @param fileName 文件名称
	 * @param dataList 表格数据
	 * @param titles 表格表头（标题）
	 * @param colWidths 列宽
	 * @param response
	 * @author: liyanlei
	 * 2016年4月20日上午11:02:03
	 */
	public  void exportExcel(String fileName, List<List<String>> dataList, String[] titles, int[] colWidths, final HttpServletResponse response){
		ServletOutputStream outputStream = null;
		try{
			// 创建一个workbook 对应一个excel应用文件
			wb = new XSSFWorkbook();
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			sheet = wb.createSheet(fileName);
			ExportUtil exportUtil = new ExportUtil(wb, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			XSSFCell cell = null;
			if(colWidths != null){//设置列宽
				for (int i = 0; i < colWidths.length; i++){
					sheet.setColumnWidth(i, colWidths[i]);
				}
			}
			for (int i = 0; i < titles.length; i++){
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
			}
			// 构建表体数据
			if (dataList != null && dataList.size() > 0){
				List<String> dataRow = null;
				for (int row = 0; row < dataList.size(); row++){
					XSSFRow bodyRow = sheet.createRow(row + 1);
					//bodyRow.setHeight((short)500);
					dataRow = dataList.get(row);
					for(int col = 0; col < dataRow.size(); col++){
						cell = bodyRow.createCell(col);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(dataRow.get(col));
					}
				}
			}
			
			fileName = new String((fileName).getBytes("UTF-8"), "ISO8859_1")+Dates.formatDateTime_New(new Date());
			//输出
			//response.setContentType("application/vnd.ms-excel;charset=UTF-8"); //后缀.xls
			//火狐后缀是.xlsx
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			outputStream = response.getOutputStream();
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
		}catch (IOException e){
			logger.info(fileName + "--导出数据失败。");
			e.printStackTrace();
		}finally{
			try{
				outputStream.close();
			}catch (IOException e){
				logger.info(fileName + "--导出数据--数据输出流无法关闭。");
			}
		}
	}
	/**
	 * 为指定列设置下拉框
	 * @param colNum
	 * @param list
	 * @return
	 */
	public Workbook setSelectOption(int colNum, List<String> list){
		if (list == null){
			return null;
		}
		sheet = wb.getSheetAt(0);
		sheet = setHSSFValidation(sheet, list, 1, 1001, colNum, colNum);
		return wb;
	}

	/**
	 * 设置下拉选项
	 * @param sheet 表单
	 * @param list  下拉选项数据
	 * @param firstRow  开始行
	 * @param endRow  结束行
	 * @param firstCol 开始列
     * @param endCol 结束列
     * @return
     */
	private XSSFSheet setHSSFValidation(XSSFSheet sheet, List<String> list, int firstRow,
										int endRow, int firstCol, int endCol) {
		String[] sb = new String[list.size()];
		for(int count=0;count<list.size();count++){
			sb[count] = list.get(count);
		}
		/*XmlOptions xmlOptions = new XmlOptions();
		CTDataValidation constraint = CTDataValidation.Factory.newInstance(xmlOptions);
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
		// 数据有效性对象
		XSSFDataValidation validationList = new XSSFDataValidation(regions, constraint);*/
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(sb);
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation validationList = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(validationList);
		return sheet;
	}
	
}