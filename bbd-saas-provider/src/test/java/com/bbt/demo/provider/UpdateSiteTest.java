package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.ExcelReaderX;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Key;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class UpdateSiteTest {
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(UpdateSiteTest.class);

	@Autowired
	private SiteService siteService;
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
		// 对读取Excel表格内容测试
		ExcelReaderX excelReader = new ExcelReaderX();
		InputStream is = new FileInputStream("E:\\updateSite\\上海站点名称更改明细.xlsx");
		List<List<String>> rowList = excelReader.readExcelContent(is, 0, 2);
		System.out.println("获得Excel表格的内容:");
		StringBuffer  rowSB = null;
		for (List<String> row : rowList) {
			rowSB = new StringBuffer();
			for(String col : row){
				rowSB.append(col);
				rowSB.append("        ");
			}
			System.out.println(rowSB.toString());
		}
		Assert.isTrue(true);//无用
	}
	@Test
	public void testUpdate() throws Exception{
		// 对读取Excel表格内容测试
		ExcelReaderX excelReader = new ExcelReaderX();
		InputStream is = new FileInputStream("E:\\updateSite\\上海站点名称更改明细.xlsx");
		List<List<String>> rowList = excelReader.readExcelContent(is, 0, 2);
		System.out.println("获得Excel表格的内容:");
		StringBuffer  rowSB = null;
		for (List<String> row : rowList) {
			Site site = new Site();
			site.setAreaCode(row.get(0).trim());
			site.setName(row.get(1).trim());
			//doUpdate(site);
			System.out.println(row.get(0).trim() + ":" + doUpdate(site));
		}
		Assert.isTrue(true);//无用
	}
	@Test
	public void testOneUpdate() throws Exception{
		Site site = new Site();
		site.setAreaCode("101010-047");
		site.setName("雅宝路站");
		//doUpdate(site);
		System.out.println(doUpdate(site));
		Assert.isTrue(true);//无用
	}
	private boolean doUpdate(Site site){
		Site siteNew = siteService.findSiteByAreaCode(site.getAreaCode());
		boolean r = false;
		if(siteNew != null){
			siteNew.setName(site.getName());
			//siteNew.setResponser(site.getResponser());
			Key<Site> result = siteService.save(siteNew);
			if(result != null){
				r = true;
			}
		}
		return r;
	}



}
