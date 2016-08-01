package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.ExcelReader;
import com.bbd.saas.utils.ExcelUtil2007;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OrderToOtherCompanyTest {
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderToOtherCompanyTest.class);

	@Autowired
	private OrderService orderService;

	@Test
	public void testReadExcel() throws Exception{
		readOrderInfo(0);//sheet2
	}
	private void readOrderInfo(int sheetIndex) throws IOException {
		// 对读取Excel表格内容测试
		ExcelUtil2007 excelReader = new ExcelUtil2007();
		InputStream is = new FileInputStream("/Users/Axu/Desktop/7.xlsx");
		List<List<String>> rowList = Lists.newArrayList();

		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet sheet = wb.getSheetAt(sheetIndex);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		System.out.println("行数：" + rowNum);
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			String mailNum = ExcelReader.getStringCellValue(row.getCell(1));
			Order order = orderService.findOneByMailNum(mailNum);
			if(order!=null){
				String arriveDate = ExcelReader.getDateCellValue(row.getCell(1));
				logger.info(arriveDate+"");
			}

		}




	}





}
