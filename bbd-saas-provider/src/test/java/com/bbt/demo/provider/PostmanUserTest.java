package com.bbt.demo.provider;

import com.bbd.saas.api.mysql.PostmanUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class PostmanUserTest {
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(PostmanUserTest.class);

	@Autowired
	private PostmanUserService postmanUserService;
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
	public void testUpdateSiteName() throws Exception{
		int i = postmanUserService.updateSitenameBySiteId("578dce98a40b9e2a7c7f98aa", "双合");
		Assert.isTrue(true);//无用
	}


}
