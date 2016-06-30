package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.ConstantService;
import com.bbd.saas.mongoModels.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ConstantServiceTest {
	@Autowired
	private ConstantService constantService;

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
		constantService.save(constant);
		Assert.isTrue(true);//无用
	}

	@Test
	public void testFindPages() throws Exception{

	}

}
