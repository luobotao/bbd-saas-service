package com.bbd.saas.controllers;

import com.bbd.poi.api.Geo;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.AdminUserService;
import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.AdminUser;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dataQuery")
@SessionAttributes("dataQuery")
public class DataQueryController {
	
	public static final Logger logger = LoggerFactory.getLogger(DataQueryController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	OrderParcelService orderPacelService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	PostcompanyService postCompanyService;
	@Autowired
	Geo geo;
	
	/**
	 * Description: 跳转到数据查询页面
	 * @param pageIndex 页数
	 * @param statusStr 状态集合status1,status2---
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request
	 * @param model
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:27:46
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, String statusStr, String arriveBetween, String mailNum, final HttpServletRequest request, Model model) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//到站时间
			arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -2));
			//查询数据
			PageModel<Order> orderPage = this.getList(pageIndex, statusStr, arriveBetween, mailNum, request);
			/*for(Order order : orderPage.getDatas()){
				String parcelCodeTemp = orderPacelService.findParcelCodeByOrderId(order.getId().toHexString());
				order.setParcelCode(parcelCodeTemp);//设置包裹号
			}*/
			logger.info("=====数据查询页面列表===" + orderPage);
			model.addAttribute("orderPage", orderPage);
			model.addAttribute("arriveBetween", arriveBetween);
			return "page/dataQuery";
		} catch (Exception e) {
			logger.error("===跳转到数据查询页面==出错 :" + e.getMessage());
		}
		return "page/dataQuery";
	}

	/**
	 * Description: 分页查询，Ajax更新列表
	 * @param pageIndex 页数
	 * @param statusStr 状态集合status1,status2---
	 * @param arriveBetween 到站时间
	 * @param mailNum 运单号
	 * @param request
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:27:16
	 */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, String statusStr, String arriveBetween, String mailNum, final HttpServletRequest request) {
		//查询数据
		PageModel<Order> orderPage = null;
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			//设置查询条件
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.arriveBetween = arriveBetween;
			orderQueryVO.mailNum = mailNum;
			orderQueryVO.areaCode = user.getSite().getAreaCode();
			//状态集合
			if(StringUtils.isNotBlank(statusStr) && !"-1".equals(statusStr)){
				String [] statusS = statusStr.split(",");
				List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
				for(String status : statusS){
					orderStatusList.add(OrderStatus.status2Obj(Integer.parseInt(status)));
				}
				orderQueryVO.orderStatusList = orderStatusList;
			}
			orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
			//设置派件员快递和电话
			if(orderPage != null && orderPage.getDatas() != null){
				List<Order> dataList = orderPage.getDatas();
				User courier = null;
				UserVO userVO = null;
				for(Order order : dataList){
					//设置派件员快递和电话
					courier = userService.findOne(order.getUserId());
					if(courier != null){
						userVO = new UserVO();
						userVO.setLoginName(courier.getLoginName());
						userVO.setRealName(courier.getRealName());
						order.setUserVO(userVO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("===分页查询，Ajax查询列表数据===出错:" + e.getMessage());
		}
		return orderPage;		
	}
	
	/**
	 * Description: 
	 * @param mailNum
	 * @return
	 * @author liyanlei
	 * 2016年4月22日下午6:31:35
	 */
	@RequestMapping(value="/getOrderMail", method=RequestMethod.GET)
	public String getOrderMail(String mailNum, final HttpServletRequest request, Model model) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
			model.addAttribute("order", order);
			//物流信息的默认经纬度为商家经纬度
			AdminUser adminUser = adminUserService.findOne(order.getAdminUserId().toString());
			if(adminUser != null){
				Sender sender = adminUser.getSender();
				if(sender != null){
					if(sender.getLat() == null || sender.getLon() == null){//商家没有经纬度，则设置为公司经纬度
						String companyAddress = "";
						if (currUser.getCompanyId() != null ){
							Postcompany company = postCompanyService.selectPostmancompanyById(Integer.parseInt(currUser.getCompanyId()));
							if(company != null){
								StringBuffer  addressBS = new StringBuffer();
								addressBS.append(StringUtil.initStr(company.getProvince(),""));
								addressBS.append(StringUtil.initStr(company.getCity(),""));
								addressBS.append(StringUtil.initStr(company.getArea(),""));
								addressBS.append(StringUtil.initStr(company.getAddress(),""));
								companyAddress = addressBS.toString();
							}
						}
						//设置地图默认的中心点
						SiteVO centerSite = getDefaultPoint(companyAddress);
						model.addAttribute("defaultLat", centerSite.getLat());
						model.addAttribute("defaultLng", centerSite.getLng());
					}else{
						model.addAttribute("defaultLat", sender.getLat());
						model.addAttribute("defaultLng", sender.getLon());
					}
				}
			}
		} catch (Exception e) {
			logger.error("===查看物流信息===出错 :" + e.getMessage());
		}
		return "page/showOrderMail";
	}
	//地图默认中心位置--公司公司经纬度
	private SiteVO getDefaultPoint(String address){
		SiteVO centerSite = new SiteVO();
		if(address == null || "".equals(address)){
			centerSite.setName("");
			centerSite.setLat("39.915");
			centerSite.setLng("116.404");
			centerSite.setDeliveryArea("50000");
			return centerSite;
		}
		try {
			MapPoint mapPoint = geo.getGeoInfo(address);
			if (mapPoint != null) {
				logger.info("[address]:" + address + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度："+ mapPoint.getLat());
				centerSite.setName("");
				centerSite.setLat(mapPoint.getLat()+"");
				centerSite.setLng(mapPoint.getLng()+"");
				centerSite.setDeliveryArea("50000");
			}else{//默认中心位置为北京
				logger.info("[address]:" + address + " [search geo result] null,无法获取经纬度");
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("50000");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[address]:" + address + " [search geo result] exception,异常");
		}
		return centerSite;
		//map.put("centerSite", centerSite);
	}
	/**
	 * Description: 导出数据
	 * @param statusStr 状态集合status1,status2---
	 * @param mailNum 运单号
	 * @param request
	 * @param response
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日下午4:30:41
	 */
	@RequestMapping(value="/exportToExcel", method=RequestMethod.GET)
	public void exportData(String statusStr, String arriveBetween_expt, String mailNum,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			String areaCode = user.getSite().getAreaCode();
			String siteName = user.getSite().getName();
			//设置查询条件
			OrderQueryVO orderQueryVO = new OrderQueryVO();
			orderQueryVO.arriveBetween = arriveBetween_expt;
			orderQueryVO.mailNum = mailNum;
			orderQueryVO.areaCode = areaCode;
			//运单状态集合
			if(StringUtils.isNotBlank(statusStr) && !"-1".equals(statusStr)){
				String [] statusS = statusStr.split(",");
				List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
				for(String status : statusS){
					orderStatusList.add(OrderStatus.status2Obj(Integer.parseInt(status)));
				}
				orderQueryVO.orderStatusList = orderStatusList;
			}
			//查询数据
			List<Order> orderList = orderService.findOrders(orderQueryVO);
			//导出==数据写到Excel中并写入response下载
			//表格数据
			List<List<String>> dataList = new ArrayList<List<String>>();
			List<String> row = null;
			if(orderList != null){
				for(Order order : orderList){
					row = new ArrayList<String>();
					row.add(siteName);
					row.add(areaCode);
					row.add(order.getMailNum());
					row.add(order.getReciever().getName());
					row.add(order.getReciever().getPhone());
					StringBuffer address = new StringBuffer();
					address.append(order.getReciever().getProvince());
					address.append(order.getReciever().getCity());
					address.append(order.getReciever().getArea());
					address.append(order.getReciever().getAddress());
					row.add(address.toString());
					row.add(Dates.formatDateTime_New(order.getDateDriverGeted()));
					row.add(Dates.formatDate2(order.getDateMayArrive()));
					row.add(Dates.formatDateTime_New(order.getDateArrived()));
					//签收时间 start
					if(order.getOrderStatus() != null && order.getOrderStatus() == OrderStatus.SIGNED){
						row.add(Dates.formatDateTime_New(order.getDateUpd()));
					}else{
						row.add("");
					}
					//签收时间  end
					setCourier(order.getUserId(), row);
					if(order.getOrderStatus() == null){
						row.add("未到站");
					}else{
						row.add(order.getOrderStatus().getMessage());
					}
					//异常单子展示异常信息
					if(order.getOrderStatus()==OrderStatus.RETENTION || order.getOrderStatus()==OrderStatus.REJECTION){
						List<Express> expresses = order.getExpresses();
						if(expresses!=null && expresses.size()>0){
							Express express= expresses.get(expresses.size()-1);
							String reson = express.getRemark()==null?"":express.getRemark().replaceAll("订单已被滞留，滞留原因：","").replaceAll("订单已被拒收，拒收原因:","");
							row.add(reson);
						}

					}
					dataList.add(row);
				}
			}
			
			//表头
			String[] titles = {  "站点名称", "站点编码","运单号", "收货人", "收货人手机" , "收货人地址" , "司机取货时间" , "预计到站时间", "到站时间", "签收时间", "派送员", "派送员手机", "状态","异常原因" };
			int[] colWidths = {   6000, 3500,5000, 3000, 3500, 12000, 5500, 3500, 5500, 5500, 3000, 3500, 3000,4000};
			ExportUtil.exportExcel(siteName + "_数据导出", dataList, titles, colWidths, response);
		} catch (Exception e) {
			logger.error("===数据导出===出错:" + e.getMessage());
		}
	
	}	
	
	void setCourier(String userId, List<String> row){
		if(userId == null || "".equals(userId)){
			row.add("");
			row.add("");
		}else{
			User courier = userService.findOne(userId);
			if(courier != null){
				row.add(courier.getRealName());
				row.add(courier.getLoginName());
			}else{
				row.add("");
				row.add("");
			}
		}
	}

}
