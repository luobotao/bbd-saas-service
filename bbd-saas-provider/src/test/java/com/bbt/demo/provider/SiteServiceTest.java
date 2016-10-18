package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SiteServiceTest {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(SiteServiceTest.class);
    @Autowired
    private SiteService siteService;
    private ObjectId uId = new ObjectId("573c5f421e06c8275c08183c");

    //junit.framework.TestCase时用
    public void setUp() throws Exception {
        System.out.println("set up");
        // 生成成员变量的实例
    }

    //junit.framework.TestCase时用
    public void tearDown() throws Exception {
        System.out.println("tear down");
    }

    @Test
    public void testCopyBean() throws Exception {
        Site site = siteService.findSiteByAreaCode("101010-085");
        SiteVO siteVo = new SiteVO();
        BeanUtils.copyProperties(site, siteVo);
        siteVo.setId(site.getId().toString());
        Assert.isTrue(true);//无用
    }

    @Test
    public void testFindPages() throws Exception {

    }

    public void testGetAreaCode() throws Exception {
        siteService.dealOrderWithGetAreaCode("北京北京");
        Assert.isTrue(true);//无用
    }

}
