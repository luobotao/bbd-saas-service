package com.bbd.saas.controllers;

import ch.hsr.geohash.GeoHash;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.json.JSON;
import com.bbd.poi.api.Geo;
import com.bbd.poi.api.PostmanPoiApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.Result;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.GeoRecHistoService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.models.GeoRecHisto;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.GeoUtil;
import com.bbd.saas.vo.Reciever;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bbd")
@SessionAttributes("bbdExpress")
public class BbdExpressApiController {
	public static final Logger logger = LoggerFactory.getLogger(BbdExpressApiController.class);
	@Autowired
	SiteService siteService;
	@Autowired
	OrderService orderService;
	@Autowired
	SitePoiApi sitePoiApi;
	@Autowired
	Geo geo;
	@Autowired
	private PostmanUserService userMysqlService;
	@Autowired
	private GeoRecHistoService geoRecHistoService;
	@Autowired
	private PostmanUserService postmanUserService;
	@Autowired
	private PostmanPoiApi postmanPoiApi;

	/**
	 * description: 推送站点信息接口
	 * 2016年4月14日下午4:05:01
	 * @author: ctt
	 * @return
	 */
	@RequestMapping(value="/addSitePOI/{siteId}", method=RequestMethod.GET)
	@ResponseBody
	public String addSitePOI(@PathVariable String siteId, HttpServletRequest request ) {
		try {
			Site site = siteService.findSite(siteId);
			String siteAddress = site.getProvince()+site.getCity()+site.getArea()+site.getAddress();
			logger.info(site.getId().toString());
			//siteId companyId siteName siteAddress radius
			Result<double[]> result = sitePoiApi.addSitePOI(site.getId().toString(),"",site.getSiteSrc().getStatus(),site.getName(),siteAddress,0,site.getSitetype() != null ? site.getSitetype().getStatus() : 1);
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("[addSitePOI] Exception:"+e.getMessage());
			return "0";
		}
	}

	@RequestMapping(value="/getAreaCode/{address}", method=RequestMethod.GET)
	@ResponseBody
	public String getAreaCode(@PathVariable String address, HttpServletRequest request ) {
		//args :company address

		try {
			return orderService.findBestSiteWithAddress(address);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("[getAreaCode] Exception:"+e.getMessage());
			return "9999-999";
		}

		/*List<String> areaCodeList = sitePoiApi.searchSiteByAddress("",address);
		logger.info(address);
		logger.info(areaCodeList.size()+"");
		if(areaCodeList!=null && areaCodeList.size()>0){
			//通过积分获取优选区域码，暂时用第一个
			String areaCode = areaCodeList.get(0);
			return areaCode;
		}
		return "";*/
	}

	@RequestMapping(value="/postAllAreaCode",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String postAllAreaCode(@RequestParam String address) {
		//args :company address
		try {
			String[] addes = address.split(";");
			StringBuffer sb = new StringBuffer();
			for (String str: addes) {
                try {
                    List<String> areaCodeList = sitePoiApi.searchSiteByAddress("", str);
                    logger.info("[address]:" + str + " [search poi result] :" + areaCodeList.size() + "");
                    if (areaCodeList != null && areaCodeList.size() > 0) {
                        //通过积分获取优选区域码，暂时用第一个
                        String siteId = orderService.findBestSiteWithAddress(str);
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("[postAllAreaCode] Exception:"+e.getMessage());
			return "";
		}
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

	/**
	 * 通过post方式获取地址经纬度（支持地址进行编码和不进行编码的情况）
	 * @param address 地址
	 * @return 经纬度信息
     */
	@RequestMapping(value="/getGeoInfoByPost", method=RequestMethod.POST)
	@ResponseBody
	public String getGeoInfoByPost(String address) {
		//args :company address
		String str = "";
		try {
			MapPoint mapPoint = geo.getGeoInfo(URL.decode(address));//对地址进行解码
			if (mapPoint != null) {
				logger.info("[address]:" + URL.decode(address) + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度："+ mapPoint.getLat());
				return JSON.json(mapPoint);
			}else{
				mapPoint = geo.getGeoInfo(address);//地址无需解码
				if (mapPoint != null) {
					logger.info("[address]:" + address + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度：" + mapPoint.getLat());
					return JSON.json(mapPoint);
				}else{
					logger.info("[address]:" + address + " [search geo result] null,无法获取经纬度");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[address]:" + address + " [search geo result] exception,异常");
		}
		return str;
	}


	@RequestMapping(value="/getDistance")
	@ResponseBody
	public String getDistance(String city, String start, String ends) {
		long length = 0;
		try {
			//getDistance with info, city:深圳 start:{} ends:[{}]
			if(start == null || ends == null || "{}".equals(start) || "[{}]".equals(ends)){
                return "0";
            }
			logger.info("getDistance with info, city:"+city+" start:"+start+" ends:"+ends);
			//getDistance with info, city:北京市 start:{"lng":"116.31807926386","lat":"39.938949605151"} ends:[{"lng":"116.337341","lat":"39.949164"}]
			Gson gson = new Gson();
			MapPoint mapPoint  = gson.fromJson(start, MapPoint.class);
			List<MapPoint> mapPointList = gson.fromJson(ends, new TypeToken<List<MapPoint>>(){}.getType());
			length = 0;
			if(!"".equals(city)&&mapPoint!=null&&mapPointList!=null&&mapPointList.size()>0) {
                length = geo.getDistance(city, mapPoint, mapPointList,false);
                logger.info("getDistance with info [success] distance:"+length);
            }
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			logger.info("getDistance exceptioin:"+e.getMessage());
			return "0";
		}
		return length+"";
	}

	@RequestMapping(value="/postAllAreaCodeWithIntegral",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String postAllAreaCodeWithIntegral(@RequestParam String address){
		//args :company address
		try {
			String[] addes = address.split(";");
			StringBuffer sb = new StringBuffer();
			sb.append("地址").append("\t").append("区域码").append("\t").append("区域名称").append("\t").append("距离（米）").append("\t").append("入驻时间积分").append("\t").append("7日妥投率积分").append("\t").append("7日平均派件时长积分").append("\t").append("站点人数积分").append("\t").append("站点积分").append("\t").append("最终积分").append("\n");
			for (String str: addes) {
                try {
                    List<String> areaCodeList = sitePoiApi.searchSiteByAddress("", str);
                    logger.info("[address]:" + str + " [search poi result] :" + areaCodeList.size() + "");
                    MapPoint mapPoint = geo.getGeoInfo(str);//起点地址
                    if (areaCodeList != null && areaCodeList.size() > 0) {
                        //通过积分获取优选区域码
                        for (String siteId: areaCodeList) {
                            Site site = siteService.findSite(siteId);
                            if(site!=null) {
                                //获取当前位置到站点的距离，
                                /*List mapPointList = Lists.newArrayList();
                                mapPointList.add(new MapPoint(Double.parseDouble(site.getLng()), Double.parseDouble(site.getLat())));
                                long length = geo.getDistance(city, mapPoint, mapPointList, false);*/
                                //获取当前位置到站点的距离，
                                double length = GeoUtil.getDistance(mapPoint.getLng(),mapPoint.getLat(),Double.parseDouble(site.getLng()),Double.parseDouble(site.getLat()));
                                //获取站点的日均积分
                                Map<String, Object> result = userMysqlService.getIntegral(site.getAreaCode(),site.getUsername());
                                //int integral = userMysqlService.getIntegral("101010-016","17710174098");
                                logger.info("积分："+result.toString());
                                int integral = 0;
                                if(result.containsKey("totalscore")){
                                     integral = (int) result.get("totalscore");
                                }
                                int integralVal = 0;
                                //根据地址到站点的距离计算积分
                                if (length < 3000) {
                                    integralVal = integral + 5;
                                } else if (length < 5000) {
                                    integralVal = integral + 3;
                                } else {
                                    integralVal = integral + 2;
                                }
                                //保存站点和积分，按照积分进行排序
                                sb.append(str).append("\t").append(site.getAreaCode()).append("\t").append(site.getName()).append("\t").append(length).append("\t").append(result.get("timscore")).append("\t").append(result.get("perscore")).append("\t").append(result.get("deliveryscore")).append("\t").append(result.get("userscore")).append("\t").append(integral).append("\t").append(integralVal).append("\n");
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("getDistance exceptioin:"+e.getMessage());
			return "";
		}
	}

	@RequestMapping(value="/updateSiteWithOrder",produces = "text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String updateSiteWithOrder(@RequestParam String orderNoStr) {
		//args :company address
		try {
			String[] orderNoes = orderNoStr.split(";");
			StringBuffer sb = new StringBuffer();
			for (String orderNo: orderNoes) {
                try {
                    Order order = orderService.findByOrderNo(orderNo);
                    if(order!=null){
                        //更新订单的运单号
                        order = orderService.reduceAreaCodeWithOrder(order);
                        logger.info("[order]:" + order + " [reduce areacode result] :" + order.getAreaCode() + "");
                        sb.append(orderNo).append("\t").append(order.getAreaCode()).append("\t").append(order.getAreaRemark()).append("\n");
                    }else{
                        sb.append(orderNo).append("\t").append("not match site").append("\t").append("").append("\n");
                    }
                }catch(Exception e){
                    sb.append(orderNo).append("\t").append("error").append("\t").append("").append("\n");
                }
            }
			String str = sb.toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("updateSiteWithOrder exceptioin:"+e.getMessage());
			return "";
		}
	}

	@RequestMapping(value="/updateOrderWithAreaCode/{orderNo}",method=RequestMethod.GET)
	@ResponseBody
	public String updateOrderWithAreaCode(@PathVariable String orderNo) {
		logger.info("当即更新订单"+orderNo+"的区域码");
		String result = "";
		try {
			if(StringUtils.isNotBlank(orderNo)) {
				Order order = orderService.findByOrderNo(orderNo);
				if (order != null) {
					//更新订单的运单号
					order = orderService.reduceAreaCodeWithOrder(order);
					logger.info("[order]:" + order + " [reduce areacode result] :" + order.getAreaCode() + "");
					result = "success";
				} else {
					result = "failed";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "exception";
			logger.info("updateOrderWithAreaCode exceptioin:"+e.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/updateOrderWithDealParcel/{orderNo}",method=RequestMethod.GET)
	@ResponseBody
	public String updateOrderWithDealParcel(@PathVariable String orderNo){
		logger.info("当即打印订单"+orderNo+"后生成包裹");
		String result = "";
		try {
			if(StringUtils.isNotBlank(orderNo)) {

				Order order = orderService.findByOrderNo(orderNo);
				//针对订单进一步处理orderParcel
				logger.info(String.format("订单%s生成区域码%s完成,开始匹配包裹",order.getOrderNo(),order.getAreaCode()));
				result = orderService.updateParcelWithOrder(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "exception";
			logger.info("updateOrderWithDealParcel exceptioin:"+e.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/reduceGeoRecHisto/{dateStr}", method=RequestMethod.GET)
	@ResponseBody
	public String reduceGeoRecHisto(@PathVariable String dateStr, HttpServletRequest request ) {
		logger.info("处理收件人信息");
		//Date date = Dates.parseDate(dateStr,"yyyy-MM-dd");
		try {
			List<String> dateList = Dates.getAfterNoDays(dateStr,1,"yyyy-MM-dd");
			for (String str: dateList) {
                Date date = Dates.parseDate(str,"yyyy-MM-dd");
                logger.info("当前处理时间："+Dates.formatDate(date));
                //data 的格式为yyyy-MM-dd
                if(date != null){
                    List<Order> orderList = orderService.findByDateAdd(date);
                    logger.info(String.format("日期：%s 处理订单数：%s",Dates.formatDate2(date),orderList.size()));
                    dealGeoRecWithOrder(orderList);
                }
                logger.info("geo deal date:"+date+"cover Reciver address to geo finish");
            }
			logger.info(" reduceGeoRecHisto success");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("reduceGeoRecHisto exceptioin:"+e.getMessage());
			return "fail";
		}
	}
	private void dealGeoRecWithOrder(List<Order> orderList) {
		for (Order order : orderList) {
			try {
				Reciever reciever = order.getReciever();
				if (reciever != null) {
					//将订单信息入库
					GeoRecHisto geoRecHisto = geoRecHistoService.findOneByOrderNo(order.getOrderNo());
					if (geoRecHisto == null) {
						geoRecHisto = new GeoRecHisto();
						geoRecHisto.setOrderNo(order.getOrderNo());
						geoRecHisto.province = reciever.getProvince();
						geoRecHisto.city = reciever.getCity();
						geoRecHisto.area = reciever.getArea();
						geoRecHisto.address = reciever.getAddress();
						geoRecHisto.src = order.getSrc().getMessage();
						geoRecHisto.dateAdd = Dates.formatDate2(order.getDateAdd());
						MapPoint mapPoint = geo.getGeoInfo(reciever.getProvince() + reciever.getCity() + reciever.getArea() + reciever.getAddress());
						if (mapPoint != null) {
							geoRecHisto.setLat(mapPoint.getLat());
							geoRecHisto.setLng(mapPoint.getLng());
							GeoHash geoHash = GeoHash.withCharacterPrecision(geoRecHisto.getLat(), geoRecHisto.getLng(), 9);
							geoRecHisto.setGeoStr(geoHash.toBase32());
							geoRecHistoService.insert(geoRecHisto);
							logger.info("订单：" + order.getOrderNo() + "收件人地址转换完成");
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.info("[error]订单：" + order.getOrderNo() + "收件人地址转换异常");
				continue;
			}
		}
	}



	@RequestMapping(value="/postmanEfence", method=RequestMethod.GET)
	public String postmanEfence(Model model, HttpServletRequest request ) {
		try {
			String phone = request.getParameter("phone");
			String siteStr = "";
			if(StringUtils.isNotBlank(phone)){
                //查找用户postmanuser
                PostmanUser postmanUser = postmanUserService.selectPostmanUserByPhone(phone,0);
                if(postmanUser!=null){
                    List<List<MapPoint>> mapPointList = postmanPoiApi.getSiteEfence(postmanUser.getId());
                    siteStr = dealPostmanUserPoints(mapPointList);
                }
            }
			model.addAttribute("sitePoints", siteStr);
			model.addAttribute("phone",phone);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("postmanEfence exceptioin:"+e.getMessage());
		}
		return "geo/postmanRegionMap";
	}

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



	@RequestMapping(value="/geoMatchSite", method=RequestMethod.GET)
	public String geoMatchSite(Model model, HttpServletRequest request ) {
		try {
			String keyword = request.getParameter("keyword");    //订单号或包裹号
			String address = request.getParameter("address");
			if (StringUtils.isNotBlank(keyword)) {
				//根据订单号查询
				Order order = orderService.findByOrderNoOrMailNum(keyword);
				if (order != null) {
					Reciever reciever = order.getReciever();
					if (reciever != null) {
						address = reciever.getProvince() + reciever.getCity() + reciever.getArea() + reciever.getAddress();
					}
				}
			}
			//address = "北京北京市西城区中国北京北京市西城区复兴门内大街2号民生银行";
			if (StringUtils.isNotBlank(address)) {
				Result result = sitePoiApi.searchSiteByAddressDirect(address);
				MapPoint mapPoint = geo.getGeoInfo(address);//起点地址

				Map<String, Object> data = (Map<String, Object>) result.data;
				Map<String, String> efenceMap = new HashMap<String, String>();
				if (data != null && data.get("efences") != null) {
					Map<String, List<List<MapPoint>>> efMap = (Map<String, List<List<MapPoint>>>) data.get("efences");
					for (Map.Entry<String, List<List<MapPoint>>> entry : efMap.entrySet()) {
						efenceMap.put(entry.getKey(), dealPostmanUserPoints(entry.getValue()));
					}
					data.remove("efences");
					model.addAttribute("efenceMap", efenceMap);//当前查询的地址坐标
				}
				model.addAttribute("data", data);//当前查询的地址坐标
				logger.info("Express_geoMatchSite：data=" + data);
			}
			model.addAttribute("keyword", keyword);//当前查询的地址
			model.addAttribute("address", address);//当前查询的地址
			logger.info("Express_geoMatchSite：  keyword=" + keyword + ",  address=" + address + ",  model=" + model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("geoMatchSite： message_erro=" + e.getMessage());
		}
		return "geo/geoMatchSite";
	}
	@ResponseBody
	@RequestMapping(value="/fixAddress", method=RequestMethod.POST)
	public Result fixAddress(String address, String source, String lng, String lat) {
		Result result  = new Result();
		try {
			if(StringUtils.isBlank(address)){
                return new Result(-1, "地址不能为空");
            }
			if(StringUtils.isBlank(source)){
                return new Result(-1, "请选择来源");
            }
			if(StringUtils.isBlank(lng)){
                return new Result(-1, "经度不能为空");
            }
			if(StringUtils.isBlank(lat)){
                return new Result(-1, "纬度不能为空");
            }
			MapPoint point = new MapPoint(Double.parseDouble(lng), Double.parseDouble(lat));
			return geo.fixAddressGeo(address, source, point);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.info("geoMatchSite： message_erro=" + e.getMessage());
			return new Result(-1, "发生异常");
		}
	}
}
