package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.ReturnReasonService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.ExpressStatStationService;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.models.ExpressStatStation;
import com.bbd.saas.vo.SiteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GeoRecHistoServiceTest {
	@Autowired
	private OrderService orderService;


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

		Assert.isTrue(true);//无用

	}

}
