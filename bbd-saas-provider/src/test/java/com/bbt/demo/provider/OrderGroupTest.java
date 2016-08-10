package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.OrderService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OrderGroupTest {
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderGroupTest.class);

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
		//orderService.sumWithAreaCodeAndOrderStatus("2016/08/09", "101010-085");
		List<String> areaCodeList = new ArrayList<String>();
		areaCodeList.add("101010-085");
		orderService.sumWithAreaCodesAndOrderStatus("2016/08/09", areaCodeList);
		Assert.isTrue(true);//无用
	}

}
