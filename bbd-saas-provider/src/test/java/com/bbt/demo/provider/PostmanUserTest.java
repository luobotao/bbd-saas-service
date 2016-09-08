package com.bbt.demo.provider;

import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.vo.UserVO;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;


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
	@Test
	public void testSelectLatAndLngByIds() throws Exception{
		List<Integer> ids = Lists.newArrayList();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(4);
		ids.add(5);
		List<UserVO> userVOList = postmanUserService.findLatAndLngByIds(ids);
		Assert.isTrue(true);//无用
	}

	@Test
	public void testUpdateByPhone() throws Exception{
		String oldPhone = "15001067777";
		//原有手机号
		PostmanUser postmanUser = postmanUserService.selectPostmanUserByPhone(oldPhone, 0);
		if (postmanUser != null) {//存在修改
			postmanUser.setDateUpd(new Date());
			int i = postmanUserService.updateByPhone(postmanUser, oldPhone);
		}
		Assert.isTrue(true);//无用
	}

}
