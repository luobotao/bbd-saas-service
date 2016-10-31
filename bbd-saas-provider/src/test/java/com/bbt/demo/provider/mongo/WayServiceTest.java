package com.bbt.demo.provider.mongo;

import com.bbd.saas.api.mongo.WayService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class WayServiceTest {
	@Autowired
	private WayService wayService;

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
	public void testSave() throws Exception{
		long num = wayService.findWayBySiteId("57202f80c35cc57f53be7d4c");
		System.out.println(num);
		Assert.isTrue(true);//无用
	}
	@Test
	public void testStringBuffer() throws Exception{
		StringBuffer msg = new StringBuffer();
		System.out.println("00000"+msg.toString()+"====");
	}

}
