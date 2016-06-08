package com.bbd.saas.controllers;

import com.alibaba.dubbo.common.json.JSON;
import com.bbd.poi.api.Geo;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.Result;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.mongoModels.Site;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bbd")
@SessionAttributes("bbdExpress")
public class BbdExpressApiController {
	public static final Logger logger = LoggerFactory.getLogger("bbdpoi");
	@Autowired
	SiteService siteService;
	@Autowired
	SitePoiApi sitePoiApi;
	@Autowired
	Geo geo;
	@Autowired
	private PostmanUserService userMysqlService;
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
		//siteId companyId siteName siteAddress radius
		Result<double[]> result = sitePoiApi.addSitePOI(site.getId().toString(),"",site.getName(),siteAddress,0);
		//更新站点的经度和纬度
		logger.info("[addSitePOI]result :"+result.toString());
		if(result.code==0&&result.data!=null) {
			double[] data = result.data;
			site.setLng(data[0] + "");    //经度
			site.setLat(data[1] + "");    //纬度
			site.setDeliveryArea("0");
			siteService.save(site);
		}
		return result.code+"";
	}

	@RequestMapping(value="/getAreaCode/{address}", method=RequestMethod.GET)
	@ResponseBody
	public String getAreaCode(@PathVariable String address, HttpServletRequest request ) {
		//args :company address
		List<String> areaCodeList = sitePoiApi.searchSiteByAddress("",address);
		logger.info(address);
		logger.info(areaCodeList.size()+"");
		if(areaCodeList!=null && areaCodeList.size()>0){
			//通过积分获取优选区域码，暂时用第一个
			String areaCode = areaCodeList.get(0);
			return areaCode;
		}
		return "";
	}

	@RequestMapping(value="/postAllAreaCode",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String postAllAreaCode(@RequestParam String address) throws UnsupportedEncodingException {
		//args :company address
		String[] addes = address.split(";");
		StringBuffer sb = new StringBuffer();
		for (String str: addes) {
			try {
				List<String> areaCodeList = sitePoiApi.searchSiteByAddress("", str);
				logger.info("[address]:" + str + " [search poi result] :" + areaCodeList.size() + "");
				if (areaCodeList != null && areaCodeList.size() > 0) {
					//通过积分获取优选区域码，暂时用第一个
					String siteId = areaCodeList.get(0);
					Site site = siteService.findSite(siteId);
					sb.append(str).append("\t").append(site.getAreaCode()).append("\t").append(site.getName()).append("\n");
				}else{
					sb.append(str).append("\t").append("").append("\t").append("").append("\n");
				}
			}catch(Exception e){
				sb.append(str).append("\t").append("").append("\t").append("").append("\n");
			}
		}
		String str = sb.toString();
		return str;
	}

	@RequestMapping(value="/getGeoInfo/{address}", method=RequestMethod.GET)
	@ResponseBody
	public String getGeoInfo(@PathVariable String address, HttpServletRequest request ) {
		//args :company address
		String str = "";
		try {
			MapPoint mapPoint = geo.getGeoInfo(address);
			if (mapPoint != null) {
				logger.info("[address]:" + address + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度："+ mapPoint.getLat());
				return JSON.json(mapPoint);
			}else{
				logger.info("[address]:" + address + " [search geo result] null,无法获取经纬度");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[address]:" + address + " [search geo result] exception,异常");
		}
		return str;
	}

	@RequestMapping(value="/getDistance")
	@ResponseBody
	public String getDistance(String city, String start, String ends) throws IOException {
		logger.info("getDistance with info, city:"+city+" start:"+start+" ends:"+ends);
		Gson gson = new Gson();
		MapPoint mapPoint  = gson.fromJson(start, MapPoint.class);
		List<MapPoint> mapPointList = gson.fromJson(ends, new TypeToken<List<MapPoint>>(){}.getType());
		long length = 0;
		if(!"".equals(city)&&mapPoint!=null&&mapPointList!=null&&mapPointList.size()>0) {
			length = geo.getDistance(city, mapPoint, mapPointList,false);
			logger.info("getDistance with info [success] distance:"+length);
		}
		return JSON.json(length);
	}

	@RequestMapping(value="/postAllAreaCodeWithIntegral",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String postAllAreaCodeWithIntegral(@RequestParam String address) throws UnsupportedEncodingException {
		//args :company address

		String[] addes = address.split(";");
		StringBuffer sb = new StringBuffer();
		for (String str: addes) {
			try {
				List<String> areaCodeList = sitePoiApi.searchSiteByAddress("", str);
				logger.info("[address]:" + str + " [search poi result] :" + areaCodeList.size() + "");

				String city="北京";
				MapPoint mapPoint = geo.getGeoInfo(str);//起点地址
				if (areaCodeList != null && areaCodeList.size() > 0) {
					//通过积分获取优选区域码
					for (String siteId: areaCodeList) {
						Site site = siteService.findSite(siteId);
						if(site!=null) {
							//获取当前位置到站点的距离，
							List mapPointList = Lists.newArrayList();
							mapPointList.add(new MapPoint(Double.parseDouble(site.getLng()), Double.parseDouble(site.getLat())));
							long length = geo.getDistance(city, mapPoint, mapPointList, false);
							//获取站点的日均积分
							int integral = userMysqlService.getIntegral(site.getAreaCode(),site.getUsername());
							//int integral = userMysqlService.getIntegral("101010-016","17710174098");
							logger.info("积分："+integral);
							//根据地址到站点的距离计算积分
							if (length < 3000) {
								integral = integral + 5;
							} else if (length < 5000) {
								integral = integral + 3;
							} else {
								integral = integral + 2;
							}
							//保存站点和积分，按照积分进行排序
							sb.append(str).append("\t").append(site.getAreaCode()).append("\t").append(site.getName()).append("\t").append(length).append("\t").append(integral).append("\n");
						}
					}
				}else{
					sb.append(str).append("\t").append("").append("\t").append("").append("\n");
				}
			}catch(Exception e){
				sb.append(str).append("\t").append("").append("\t").append("").append("\n");
			}
		}
		String str = sb.toString();
		return str;
	}
}
