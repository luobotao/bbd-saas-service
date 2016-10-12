package com.bbt.demo.provider;

import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.models.PostDelivery;
import com.bbd.saas.vo.PostDeliveryQueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class PostDeliveryTest {
	@Autowired
	private PostDeliveryService postDeliveryService;

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
		PostDelivery postdelivery =  postDeliveryService.findOneById(21859);
		postdelivery = this.getDefultPostDelivery(postdelivery, "BBD", "BBD1868851573", "test1129483", "9473");
		postDeliveryService.updateOne(postdelivery);
		Assert.isTrue(true);//无用

	}

	private PostDelivery getDefultPostDelivery(PostDelivery postdelivery, String companyCode, String mailNum, String orderNo, String postmanid){
		postdelivery.setCompany_code("companyCode");
		postdelivery.setDateUpd(new Date());
		postdelivery.setMail_num(mailNum);
		postdelivery.setOut_trade_no(orderNo);
		postdelivery.setPostman_id(Integer.parseInt(postmanid));
		postdelivery.setResultmsg("");
		postdelivery.setGoods_fee(0);
		postdelivery.setNeed_pay("0");
		postdelivery.setFlg("1");
		postdelivery.setSta("1");
		postdelivery.setStaffid("");
		postdelivery.setTyp("4");
		return postdelivery;
	}


	@Test
	public void testSelectListByQuery() throws Exception{
		PostDeliveryQueryVO postDeliveryQueryVO = new PostDeliveryQueryVO();
		postDeliveryQueryVO.siteId = "577f45c21e06c8875c4771ed";
		postDeliveryQueryVO.time = "2016-08-25";
		List<Map<String, Object>> list =  postDeliveryService.findListByQuery(postDeliveryQueryVO);
		Assert.isTrue(true);//无用

	}
}
