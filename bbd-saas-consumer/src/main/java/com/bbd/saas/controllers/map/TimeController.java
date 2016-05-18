package com.bbd.saas.controllers.map;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.models.OrderLog;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.vo.Express;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/time")
public class TimeController {
	public static final Logger logger = LoggerFactory.getLogger(TimeController.class);
	@Autowired
	OrderLogService orderLogService;
	@Autowired
	OrderService orderService;
	@Autowired
	SiteService siteService;
	/**
	 * description: 跳转到异常件处理页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @return
	 */
	@RequestMapping(value="", method= RequestMethod.GET)
	public String index( ) {
		logger.info("把当天的更新的订单的物流信息同步到mysql数据库中 trigger start ...");
		int siteNum = 0, orderNum = 0, exprNum = 0, i = 0, oneSiteNum = 0;
		try {
			List<Site> siteList = siteService.findAllSiteList();
			List<Express> expressList = null;
			if (siteList != null && siteList.size() > 0) {
				for (Site site : siteList) {
					siteNum++;
					List<Order> orderList = orderService.getTodayUpdateOrdersBySite(site.getAreaCode());
					if (orderList != null && orderList.size() > 0) {
						oneSiteNum = 0;
						for (Order order : orderList) {
							orderNum++;
							//logger.info("order=======orderNo==== " + order.getOrderNo() + "  mailNum==== " + order.getMailNum());
							if (order != null && order.getExpresses() != null && order.getExpresses().size() > 0) {
								expressList = order.getExpresses();
								if (expressList != null && expressList.size() > 0) {
									for (Express express : expressList) {
										exprNum++;
										/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										// public final String format(Date date)
										String s = sdf.format(express.getDateAdd());
										logger.info("同步到mysql数据库中 date====== " + s);*/
										if (Dates.getDayTime(-1).compareTo(express.getDateAdd()) <= 0 && Dates.getDayTime(0).compareTo(express.getDateAdd()) > 0){
											saveOrderLog(site, order, express);
											i++;
											oneSiteNum++;
										}
									}
								}
							}
						}
					}
					logger.info("====站点名称==  " + site.getName() + "    ===同步数据条数 =num==" + oneSiteNum);
				}
			}
		} catch (Exception e) {
			logger.error("把订单物流状态同步到mysql库出错：" + e.getMessage());
		}
		logger.info("同步数据总条数==="+i+"       站点总数==" + siteNum + "       订单总数===" + orderNum + "      物流总条数==" + exprNum);
		logger.info("把当天的更新的订单的物流信息同步到mysql数据库中 trigger end。添加订单数目");
		return "map/time";
	}


	private void  saveOrderLog(Site site, Order order, Express express){
		OrderLog orderLog = new OrderLog();
		orderLog.setAreaCode(order.getAreaCode());
		orderLog.setOperTime(express.getDateAdd());
		orderLog.setStatus(getStatusByRemark(express.getRemark()));
		orderLog.setOrderNo(order.getOrderNo());
		orderLog.setMailNum(order.getMailNum());
		orderLog.setRemark(express.getRemark());
		orderLog.setSiteName(site.getName());
		orderLog.setResponser(site.getResponser());
		orderLog.setProvince(site.getProvince());
		orderLog.setCity(site.getCity());
		orderLog.setArea(site.getArea());
		orderLog.setAddress(site.getAddress());
		orderLog.setUsername(site.getUsername());
		orderLog.setCompanyId(site.getCompanyId());
		orderLog.setCompanyName(site.getCompanyName());
		orderLog.setCompanycode(site.getCompanycode());
		orderLog.setLat(site.getLat());
		orderLog.setLng(site.getLng());
		orderLog.setDate_new(new Date());
		orderLogService.insert(orderLog);
	}
	/**
	 * 根据remark获取状态值
	 * 0-未到达站点，1-已到达站点，2-正在派送，3-已签收，4-已滞留，5-已拒收，6-转站, 7-已丢失,8-已取消
	 * @param remark 物流备注
	 * @return 状态值
	 */
	private int getStatusByRemark(String remark) {
		if(remark == null){
			return 0;
		}
		if (remark.contains("已打印")) {//订单已打印
			return 1;
		} else if (remark.contains("分拣中")) {//订单分拣中
			return 2;
		} else if (remark.contains("已打包")) {//订单已打包
			return 3;
		} else if (remark.contains("司机已取货")) {//司机已取货
			return 4;
		} else if (remark.contains("已送达【")) {//订单已送达【
			return 5;
		} else if (remark.contains("正在为您派件")) {//正在为您派件
			return 6;
		} else if (remark.contains("已被滞留")) {//订单已被滞留
			return 7;
		} else if (remark.contains("重新派件")) {//重新派件
			return 8;
		} else if (remark.contains("您的订单已送达")) {//您的订单已送达
			return 9;
		} else if (remark.contains("已被拒收")) {//订单已被拒收
			return 10;
		} else if (remark.contains("已签收")) { //用户已签收
			return 11;
		} else{
			return 0; //未到站
		}
	}

}
