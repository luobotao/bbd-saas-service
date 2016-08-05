package com.bbd.saas.controllers.map;

import com.bbd.poi.api.Geo;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mysql.GeoRecHistoService;
import com.bbd.saas.models.GeoRecHisto;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/geoMailManage")
@SessionAttributes("geoMailManage")
public class GeoMailManageController {
	public static final Logger logger = LoggerFactory.getLogger("bbdpoi");
	@Autowired
	OrderService orderService;
	@Autowired
	SitePoiApi sitePoiApi;
	@Autowired
	Geo geo;
	@Autowired
	private GeoRecHistoService geoRecHistoService;

	/**
	 * 处理小件员的地址围栏
	 * @param sitePoints
	 * @return
	 */
	private String dealPostmanUserPoints(List<List<MapPoint>> sitePoints) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < sitePoints.size(); j++) {
			for (int i = 0; i < sitePoints.get(j).size(); i++) {
				String str = sitePoints.get(j).get(i).getLng()+"_"+sitePoints.get(j).get(i).getLat();
				sb.append(str);
				if(i<sitePoints.get(j).size()-1){
					sb.append(",");
				}
			}
			if(j<sitePoints.size()-1) {
				sb.append(";");
			}
		}
		return sb.toString();
	}
	@RequestMapping(value="", method=RequestMethod.GET)
	public String toPage(Model model, String prov, String city, String area,String dateBetween) {
		//默认值
		prov = StringUtil.initStr(prov, "北京");
		city = StringUtil.initStr(city, "北京");
		area = StringUtil.initStr(area, "朝阳区");
		List<GeoRecHisto> orderPointList = getMailMapList(prov, city, area, dateBetween);
		model.addAttribute("orderPointList",orderPointList);//订单目的地集合
		model.addAttribute("prov",prov);
		model.addAttribute("city",city);
		model.addAttribute("area",area);
		return "geo/geoMailManage";
	}
	@ResponseBody
	@RequestMapping(value="/getDataList", method=RequestMethod.POST)
	public List<GeoRecHisto> getMailMapList(String prov, String city, String area,String dateBetween) {
		//到站时间
		dateBetween = StringUtil.initStr(dateBetween, Dates.getBetweenTime(new Date(), -1));
		String [] dates = dateBetween.split(" - ");
		List<GeoRecHisto> orderPointList = geoRecHistoService.findByDates(prov, city, area, dates[0].replace("/","-"), dates[1].replace("/","-"));
		return orderPointList;
	}
}
