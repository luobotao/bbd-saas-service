package com.bbt.demo.provider;

import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.Result;
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

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UpdateSiteLoactionTest {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(UpdateSiteLoactionTest.class);
    @Autowired
    private SiteService siteService;
    @Autowired
    SitePoiApi sitePoiApi;

    @Test
    public void updateLoaction() throws Exception {
        List<Site> siteList = siteService.findAllSiteList();
        siteList.forEach(site -> {
            logger.info(site.getAreaCode());
            setLatAndLng(site.getId().toHexString());
        });

    }


    private void setLatAndLng(String siteId) {
        //设置经纬度
        Site site = siteService.findSite(siteId);
        String siteAddress = site.getProvince() + site.getCity() + site.getArea() + site.getAddress();
        logger.info(site.getId().toString());
        try {
            Result<double[]> result = sitePoiApi.addSitePOI(site.getId().toString(), site.getCompanyId(), site.getSiteSrc().getStatus(), site.getName(), siteAddress, 0, site.getSitetype() != null ? site.getSitetype().getStatus() : 1);
            //更新站点的经度和纬度
            logger.info("[addSitePOI]result :" + result.toString());
            if (result.code == 0 && result.data != null) {
                double[] data = result.data;
                site.setLng(data[0] + "");    //经度
                site.setLat(data[1] + "");    //纬度
                site.setDeliveryArea("0");
                siteService.save(site);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
