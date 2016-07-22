package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.ExcelUtil2007;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OrderExpressStatusTest {
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderExpressStatusTest.class);

	@Autowired
	private OrderService orderService;
	private ObjectId uId = new ObjectId("573c5f421e06c8275c08183c");
	//junit.framework.TestCase时用
	public void setUp() throws Exception{
        System.out.println("set up");
        // 生成成员变量的实例
    }
	//junit.framework.TestCase时用
    public void tearDown() throws Exception{
        System.out.println("tear down");
    }
	@Test
	public void testReadExcel() throws Exception{
		//readOrderInfo(1, 2, 4, 6);//sheet1
		readOrderInfo(2, 1, 3, 52);//sheet2
		Assert.isTrue(true);//无用
	}
	private void readOrderInfo(int sheetIndex, int areaCodeIndex, int nameIndex, int totalRows) throws FileNotFoundException {
		// 对读取Excel表格内容测试
		ExcelUtil2007 excelReader = new ExcelUtil2007();
		InputStream is = new FileInputStream("E:\\updateSite\\54.xlsx");
		List<List<String>> rowList = excelReader.readExcelContent(is, sheetIndex, areaCodeIndex, nameIndex, totalRows);
		System.out.println("获得Excel表格的内容:");
		StringBuffer  rowSB = null;
		int size = rowList.size();
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		for (int i = 0; i < size; i++) {
			List<String> row = rowList.get(i);
			rowSB = new StringBuffer(i+"");
			List<String> addressList = getDataByMailNum(row.get(0));
			map.put(i+1, addressList);
			if(addressList != null && addressList.size() > 0){
				for(String col : addressList){
					rowSB.append("   ");
					rowSB.append(col);
					rowSB.append("   ");
				}
			}
			System.out.println(rowSB.toString());
		}
	}
	@Test
	public void testUpdateExcel() throws Exception{
		//addAddress(1, 2, 4, 6, 8);//sheet1
		addAddress(2, 1, 3, 52, 6);//sheet2
		Assert.isTrue(true);//无用
	}
	private void addAddress(int sheetIndex, int areaCodeIndex, int nameIndex, int totalRows, int startCol ) throws IOException {
		// 对读取Excel表格内容测试
		ExcelUtil2007 excelReader = new ExcelUtil2007();
		String filePath = "E:\\updateSite\\6月份深圳BBD各站数据.xlsx";
		InputStream is = new FileInputStream(filePath);
		List<List<String>> rowList = excelReader.readExcelContent(is, sheetIndex, areaCodeIndex, nameIndex, totalRows);
		System.out.println("获得Excel表格的内容:");
		int size = rowList.size();
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		map.put(0, getTitle());//标题
		for (int i = 0; i < size; i++) {
			List<String> row = rowList.get(i);
			List<String> addressList = getDataByMailNum(row.get(0));
			map.put(i+1, addressList);
		}
		int [] colWidths = new int[]{5000, 3000, 3500};
		excelReader.creat2007Excel(filePath, sheetIndex, startCol, colWidths, map);
		is.close();
	}
	private List<String> getTitle(){
		List<String> titleList = new ArrayList<>();
		titleList.add("原单号");
		titleList.add("ExpressStatusName");
		titleList.add("物流状态");
		return  titleList;
	}
	@Test
	public void testOneUpdate() throws Exception{
		System.out.println(getDataByMailNum("BBD186866451"));
		System.out.println(getDataByMailNum("2008000000005"));
		Assert.isTrue(true);//无用
	}
	private List<String> getDataByMailNum(String mailNum){
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			List<String> statusList = new ArrayList<String>();
			statusList.add(mailNum);
			statusList.add(order.getExpressStatus().toString());
			statusList.add(order.getExpressStatus().getMessage());
			return statusList;
		}
		return null;
	}



}
