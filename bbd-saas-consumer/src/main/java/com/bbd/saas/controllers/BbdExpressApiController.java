package com.bbd.saas.controllers;

import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.Result;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.mongoModels.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/bbd")
@SessionAttributes("bbdExpress")
public class BbdExpressApiController {
	public static final Logger logger = LoggerFactory.getLogger(BbdExpressApiController.class);
	@Autowired
	SiteService siteService;
	@Autowired
	SitePoiApi sitePoiApi;
	/**
	 * description: 推送站点信息接口
	 * 2016年4月14日下午4:05:01
	 * @author: ctt
	 * @return
	 */
	@RequestMapping(value="/addSitePOI/{siteId}", method=RequestMethod.GET)
	@ResponseBody
	public String addSitePOI(@PathVariable String siteId, HttpServletRequest request ) {
		Site site = siteService.findSite(siteId);
		String siteAddress = site.getProvince()+site.getCity()+site.getArea()+site.getAddress();
		logger.info(site.getId().toString());
		Result<double[]> result = sitePoiApi.addSitePOI(site.getId().toString(),"",site.getName(),siteAddress,0);
		//更新站点的经度和纬度
		logger.info("result code:"+result.code);
		if(result.code==0&&result.data!=null) {
			double[] data = result.data;
			site.setLng(data[0] + "");    //经度
			site.setLat(data[1] + "");    //纬度
			site.setDeliveryArea("0");
			siteService.save(site);
		}
		return result.code+"";
	}

}