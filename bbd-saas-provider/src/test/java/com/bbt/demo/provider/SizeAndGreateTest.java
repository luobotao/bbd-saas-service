package com.bbt.demo.provider;

import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Constant;
import com.bbd.saas.vo.OrderQueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class SizeAndGreateTest {
	@Autowired
	private OrderDao orderDao;

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
	public void testSave() throws Exception{
		Constant constant = new Constant("dispatchPermsnCount", "5" , "setting", "user", new Date(), new Date());
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.areaCode="101010-085";
		orderQueryVO.orderStatus = OrderStatus.TO_OTHER_EXPRESS.getStatus();
		orderDao.selectByQuery(orderQueryVO);
		Assert.isTrue(true);//无用
	}

	@Test
	public void testFindPages() throws Exception{

	}

}
