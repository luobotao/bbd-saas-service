package com.bbd.saas.utils;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作Excel表格的功能类 -- 2007版(.xlsx)
 */
public class ExcelUtil2007 {
	private POIFSFileSystem fs;
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private XSSFRow row;

	/**
	 * 读取Excel表格表头的内容
	 * @param is
	 * @return String 表头内容的数组
	 */
	public String[] readExcelTitle(InputStream is) {
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			//title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell(i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * @param is
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, String> readExcelContent33(InputStream is) {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;

			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				str += getCellFormatValue(row.getCell(j)).trim() + "    ";
				j++;
			}
			content.put(i, str);
			str = "";
		}
		return content;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 *
	 * @param cell Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(XSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_STRING:
				strCell = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				strCell = String.valueOf(cell.getNumericCellValue());
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(cell.getBooleanCellValue());
				break;
			case XSSFCell.CELL_TYPE_BLANK:
				strCell = "";
				break;
			default:
				strCell = "";
				break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 *
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	@SuppressWarnings("deprecation")
	private String getDateCellValue(XSSFCell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate();
			} else if (cellType == XSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据XSSFCell类型设置数据
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(XSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
				// 如果当前Cell的Type为NUMERIC
				case XSSFCell.CELL_TYPE_NUMERIC:
				case XSSFCell.CELL_TYPE_FORMULA: {
					// 判断当前的cell是否为Date
					if (XSSFDateUtil.isCellDateFormatted(cell)) {
						// 如果是Date类型则，转化为Data格式

						//方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
						//cellvalue = cell.getDateCellValue().toLocaleString();

						//方法2：这样子的data格式是不带带时分秒的：2011-10-12
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						cellvalue = sdf.format(date);

					} else {// 如果是纯数字
						// 取得当前Cell的数值
						cellvalue = String.valueOf(cell.getNumericCellValue());
						if(cellvalue.indexOf("E") != -1){
							DecimalFormat df = new DecimalFormat("#");
							cellvalue = df.format(cell.getNumericCellValue());
						}
						//cellvalue = cell.getStringCellValue();
					}
					break;
				}
				// 如果当前Cell的Type为STRIN
				case XSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				// 默认的Cell值
				default:
					cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}
	/**
	 * 读取Excel数据内容
	 * @param is
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public List<List<String>> readExcelContent(InputStream is, int areaCodeIndex, int nameIndex, int totalRows){
		return readExcelContent(is, 0, areaCodeIndex, nameIndex, totalRows);
	}


	/**
	 * 读取Excel数据内容
	 * @param is
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public List<List<String>> readExcelContent(InputStream is, int sheetIndex, int areaCodeIndex, int nameIndex, int totalRows) {
		List<List<String>> rowList = new ArrayList<List<String>>();
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(sheetIndex);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		if(totalRows > 0){
			rowNum = totalRows;
		}
		System.out.println("行数：" + rowNum);
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			List<String> colList = new ArrayList<String>();
			colList.add(getStringCellValue(row.getCell(areaCodeIndex)).trim());//areaCode
			colList.add(getStringCellValue(row.getCell(nameIndex)).trim());//站点新名称
			rowList.add(colList);
		}
		return rowList;
	}

	/**
	 * 对读取Excel表格标题测试
	 * @param excelReader
	 * @throws FileNotFoundException
     */
	private void testReadTitle(ExcelUtil2007 excelReader, InputStream is) throws FileNotFoundException {
		String[] title = excelReader.readExcelTitle(is);
		System.out.println("获得Excel表格的标题:");
		for (String s : title) {
			System.out.print(s + " ");
		}
	}

	/**
	 * 对读取Excel表格内容测试
	 * @param excelReader
	 * @throws FileNotFoundException
     */
	private void testReadBody(ExcelUtil2007 excelReader, InputStream is) throws FileNotFoundException {
		Map<Integer, String> map = excelReader.readExcelContent33(is);
		System.out.println("获得Excel表格的内容:");
		for (int i = 1; i <= map.size(); i++) {
			System.out.println(map.get(i));
		}
	}


	/**
	 * 创建2007版Excel文件
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public  void creat2007Excel(String filePath, int sheetIndex, int startCol, int[] colWidths, Map<Integer, List<String>> addressRowMap) throws FileNotFoundException,IOException {
		InputStream is = new FileInputStream(filePath);
		wb = new XSSFWorkbook(is);
		// 创建一个工作薄对象
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		for (Map.Entry<Integer,List<String>> entry : addressRowMap.entrySet()) {
			System.out.println("key= " + entry.getKey() + " , value= " + entry.getValue());
			XSSFRow row = sheet.getRow(entry.getKey());// 得到一行对象
			//row.setHeightInPoints(23);// 设置行高23像素
			XSSFCellStyle style = getStyle(wb);// 创建样式对象
			List<String> addressList =  entry.getValue();
			if(addressList != null && addressList.size() >0){
				int colNums = colWidths.length;
				for(int i = 0; i < colNums; i++){
					// 设置各列的宽度
					sheet.setColumnWidth(startCol + i, colWidths[i]);
					XSSFCell cell = row.createCell(startCol + i);// 创建单元格
					cell.setCellStyle(style);// 应用样式对象
					cell.setCellValue(addressList.get(i));// 写入当前日期

				}
			}
		}
		// 文件输出流
		FileOutputStream os = new FileOutputStream(filePath);
		wb.write(os);// 将文档对象写入文件输出流
		os.close();// 关闭文件输出流
		is.close();
		System.out.println(" excel 修改成功");
	}

	private static XSSFCellStyle getStyle(XSSFWorkbook workBook){
		XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
		// 设置对齐方式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 顶部边框粗线
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 底部边框双线
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边边框
		return style;
	}
	private void setStyle1(XSSFWorkbook workBook, XSSFCellStyle style){
		// 设置字体
		XSSFFont font = workBook.createFont();// 创建字体对象
		font.setFontHeightInPoints((short) 15);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		font.setFontName("黑体");// 设置为黑体字
		style.setFont(font);// 将字体加入到样式对象
		// 格式化日期
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		XSSFCell cell = row.createCell(1);// 创建单元格
		cell.setCellValue(new Date());// 写入当前日期
		cell.setCellStyle(style);// 应用样式对象

		// 设置边框
		style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
		style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
		style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
	}
	public static void main(String[] args) {
		try {
			ExcelUtil2007 excelReader = new ExcelUtil2007();
			String filePath = "E:\\updateSite\\北京站点新命名.xlsx";
			InputStream is = new FileInputStream(filePath);
			// 对读取Excel表格标题测试
			excelReader.testReadTitle(excelReader, is);
			// 对读取Excel表格内容测试
			excelReader.testReadBody(excelReader, is);
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
