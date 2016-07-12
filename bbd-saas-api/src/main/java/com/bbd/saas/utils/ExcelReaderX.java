package com.bbd.saas.utils;


import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作Excel表格的功能类 -- 2007版(.xlsx)
 */
public class ExcelReaderX {
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
	public List<List<String>> readExcelContent(InputStream is) {
		List<List<String>> rowList = new ArrayList<List<String>>();
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
		rowNum = 60;

		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			List<String> colList = new ArrayList<String>();

			colList.add(getStringCellValue(row.getCell(1)).trim());
			colList.add(getStringCellValue(row.getCell(3)).trim());

			rowList.add(colList);
		}
		return rowList;
	}

	/**
	 * 对读取Excel表格标题测试
	 * @param excelReader
	 * @throws FileNotFoundException
     */
	private void testReadTitle(ExcelReaderX excelReader, InputStream is) throws FileNotFoundException {
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
	private void testReadBody(ExcelReaderX excelReader, InputStream is) throws FileNotFoundException {
		Map<Integer, String> map = excelReader.readExcelContent33(is);
		System.out.println("获得Excel表格的内容:");
		for (int i = 1; i <= map.size(); i++) {
			System.out.println(map.get(i));
		}
	}
	public static void main(String[] args) {
		try {
			ExcelReaderX excelReader = new ExcelReaderX();
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
