package com.bbt.demo.provider;

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

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ExpressStatStationServiceTest {
	@Autowired
	private ReturnReasonService returnReasonService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private ExpressStatStationService essService;


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
		String companyIds [] = new String[]{"8", "99"};
		for(String companyId : companyIds){
			List<SiteVO> siteVOList = siteService.findAllSiteVOByCompanyId(companyId, SiteStatus.APPROVE);
			for (SiteVO siteVO : siteVOList){
				ExpressStatStation ess = new ExpressStatStation();
				//System.out.println(companyId);
				ess.setCompanyId(Integer.parseInt(companyId));
				ess.setTim("2016-06-29");
				ess.setSitename(siteVO.getName());
				ess.setAreacode(siteVO.getAreaCode());
				ess.setNostationcnt(Tool.getRandomInt(50));
				ess.setStationcnt(Tool.getRandomInt(50));
				ess.setDeliverycnt(Tool.getRandomInt(50));
				ess.setSuccesscnt(Tool.getRandomInt(50));
				ess.setDailycnt(Tool.getRandomInt(50));
				ess.setRefusecnt(Tool.getRandomInt(50));
				ess.setChangestationcnt(Tool.getRandomInt(50));
				ess.setChangeexpresscnt(Tool.getRandomInt(50));
				essService.insert(ess);
			}
		}
		Assert.isTrue(true);//无用

	}

}
