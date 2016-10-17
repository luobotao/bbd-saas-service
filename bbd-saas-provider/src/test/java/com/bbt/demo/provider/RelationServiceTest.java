package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.RelationService;
import com.bbd.saas.mongoModels.Relation;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
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
public class RelationServiceTest {
	@Autowired
	private RelationService relationService;

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
		Relation relation = new Relation();
		relation.setMechId("577f45ca1e06c8875c4771f0");
		List<Integer> embraceIdList = Lists.newArrayList();
		embraceIdList.add(9437);
		embraceIdList.add(9075);
		relation.setEmbraceIdList(embraceIdList);
		relation.setDateAdd(new Date());
		relation.setDateUpd(new Date());
		this.relationService.save(relation);
		Assert.isTrue(true);//无用
	}
	@Test
	public void testStringBuffer() throws Exception{
		StringBuffer msg = new StringBuffer();
		System.out.println("00000"+msg.toString()+"====");
	}

}
