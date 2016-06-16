package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.mongoModels.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TradeServiceTest {
	@Autowired
	private TradeService tradeService;

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
		Trade trade = new Trade();
		trade.setAmountMay(200);
		trade.setAmountReal(300);
		trade.setAmountReturn(500);
		trade.setDateAdd(new Date());
		trade.setDateUpd(new Date());
		tradeService.save(trade);
		Assert.isTrue(true);//无用
	}

}
