package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.ReturnReasonService;
import com.bbd.saas.mongoModels.ReturnReason;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ReturnReasonServiceTest {
	@Autowired
	private ReturnReasonService returnReasonService;

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
		ReturnReason returnReason = new ReturnReason();
		returnReason.setStatus(0);
		returnReason.setMessage("收件人拒收退回");
		returnReasonService.save(returnReason);

		returnReason = new ReturnReason();
		returnReason.setStatus(1);
		returnReason.setMessage("发件人通知退回");
		returnReasonService.save(returnReason);

		returnReason = new ReturnReason();
		returnReason.setStatus(2);
		returnReason.setMessage("客户电话、地址错误无法派送退回");
		returnReasonService.save(returnReason);

		returnReason = new ReturnReason();
		returnReason.setStatus(3);
		returnReason.setMessage("滞留超出派送时效退回");
		returnReasonService.save(returnReason);

		returnReason = new ReturnReason();
		returnReason.setStatus(4);
		returnReason.setMessage("其他");
		returnReasonService.save(returnReason);

		Assert.isTrue(true);//无用

	}

}
