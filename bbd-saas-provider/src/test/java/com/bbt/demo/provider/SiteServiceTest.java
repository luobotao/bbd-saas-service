package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class SiteServiceTest {
	@Autowired
	private SiteService siteService;

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
	public void testCopyBean() throws Exception{
		Site site = siteService.findSiteByAreaCode("101010-085");
		SiteVO siteVo = new SiteVO();
		BeanUtils.copyProperties(site, siteVo);
		siteVo.setId(site.getId().toString());
		Assert.isTrue(true);//无用
	}

	@Test
	public void testFindPages() throws Exception{

	}

}
