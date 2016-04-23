package com.bbd.saas.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.OrderUpdateVO;
import com.bbd.saas.vo.SiteVO;
import com.bbd.saas.vo.UserVO;

@Controller
@RequestMapping("/handleAbnormal")
@SessionAttributes("handleAbnormal")
public class HandleAbnormalController {
	
	public static final Logger logger = LoggerFactory.getLogger(HandleAbnormalController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	PostDeliveryService postDeliveryService;
	/**
	 * description: 跳转到异常件处理页面
	 * 2016年4月1日下午6:13:46
	 * @author: liyanlei
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request, Model model) {
		//设置默认查询条件
		status = Numbers.defaultIfNull(status, -1);//全部，滞留和拒收
		//到站时间前天、昨天和今天
		arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -10));
		//查询数据
		PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, request);
		logger.info("=====异常件处理页面列表====" + orderPage);
		model.addAttribute("orderPage", orderPage);
		model.addAttribute("arriveBetween", arriveBetween);		
		return "page/handleAbnormal";
	}
	
	

	//分页Ajax更新
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
		//参数为空时，默认值设置
		pageIndex = Numbers.defaultIfNull(pageIndex, 0);
		status = Numbers.defaultIfNull(status, -1);
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.abnormalStatus = status;
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.areaCode = user.getSite().getAreaCode();
		//查询数据
		PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
		return orderPage;		
	}
	/**************************重新分派***************开始***********************************/
	/**
	 * Description: 获取本站点下的所有状态为有效的派件员
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日上午11:06:19
	 */
	@ResponseBody
	@RequestMapping(value="/getAllUserList", method=RequestMethod.GET)
	public List<UserVO> getAllUserList(final HttpServletRequest request) {
		User user = adminService.get(UserSession.get(request));//当前登录的用户信息
		//查询
		List<UserVO> userVoList = userService.findUserListBySite(user.getSite());
		return userVoList;
	}
	
	/**
	 * Description: 运单重新分派--把异常的包裹重新分派给派件员
	 * @param mailNum 运单号
	 * @param staffId 派件员员工Id
	 * @param status 更新列表参数--状态
	 * @param pageIndex 更新列表参数--页数
	 * @param arriveBetween 更新列表参数--到站时间
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月18日上午11:41:34
	 */
	@ResponseBody
	@RequestMapping(value="/reDispatch", method=RequestMethod.GET)
	public Map<String, Object> reDispatch(String mailNum, String staffId, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		//查询运单信息
		Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
		if(order == null){//运单不存在,与站点无关--正常情况不会执行
			map.put("operFlag", 0);//0:运单号不存在
		}else{//运单存在
			//查询派件员
			User courier = userService.findOneBySiteByStaffid(currUser.getSite(), staffId);
			order.setUser(courier);
			order.setOrderStatus(OrderStatus.DISPATCHED);//更新运单状态--已分派
			//更新物流信息
			order.setExpressStatus(ExpressStatus.Delivering);
			//添加一条物流信息
			List<Express> expressList = order.getExpresses();
			if(expressList == null){
				expressList = new ArrayList<Express>();
			}
			Express express = new Express();
			express.setDateAdd(new Date());
			express.setRemark("重新派送，快递员电话：" + courier.getRealName() + " " + courier.getLoginName() + "。");
			express.setLat(currUser.getSite().getLat());//站点经纬度
			express.setLon(currUser.getSite().getLng());
			//更新运单
			Key<Order> r = orderService.save(order);
			if(r != null){
				//更新mysql
				User user = userService.findOneBySiteByStaffid(currUser.getSite(), staffId);
				postDeliveryService.updatePostAndStatusAndCompany(mailNum, user.getPostmanuserId(), staffId, "1", null);
				map.put("operFlag", 1);//1:分派成功
				//刷新列表
				map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween)); 
			}else{
				map.put("operFlag", 0);//0:分派失败
			}		
		}
		return map;
	}
	
	/**
	 * Description: 查询列表数据--刷新列表
	 * @param areaCode 站点编号
	 * @param status 状态
	 * @param pageIndex 页码
	 * @param arriveBetween 到站时间
	 * @return
	 * @author: liyanlei
	 * 2016年4月18日上午11:58:26
	 */
	PageModel<Order> getPageData(String areaCode, Integer status, Integer pageIndex, String arriveBetween){
		//刷新列表==设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.abnormalStatus = Numbers.defaultIfNull(status, -1);
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.areaCode = areaCode;
		//查询数据
		PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
		return orderPage;		
	}
	/**************************重新分派***************结束***********************************/
	
	/**************************转其他站点***************开始***********************************/
	/**
	 * Description: 查询除本站点外的其他所有站点的VO对象
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月18日上午10:32:14
	 */
	@ResponseBody
	@RequestMapping(value="/getAllOtherSiteList", method=RequestMethod.GET)
	public List<SiteVO> getAllSiteList(final HttpServletRequest request) {
		//当前登录的用户信息
		User user = adminService.get(UserSession.get(request));
		return siteService.findAllOtherSiteVOList(user.getSite());
	}
	
	/**
	 * Description: 转其他站点
	 * @param mailNum 运单号
	 * @param areaCode 站点编号
	 * @param status 更新列表参数--状态
	 * @param pageIndex 更新列表参数--页码
	 * @param arriveBetween 更新列表参数--到站时间
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月18日上午11:44:27
	 */
	@ResponseBody
	@RequestMapping(value="/toOtherSite", method=RequestMethod.GET)
	public Map<String, Object> toOtherSite(String mailNum, String siteId, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//当前登录的用户信息
		User currUser = adminService.get(UserSession.get(request));
		//查询运单信息
		Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
		if(order == null){//运单不存在,与站点无关--正常情况不会执行
			map.put("operFlag", 0);//0:运单号不存在
		}else{//运单存在
			Site site = siteService.findSite(siteId);
			//更新运单字段
			order.setAreaCode(site.getAreaCode());
			order.setAreaName(site.getName());
			StringBuffer remark = new StringBuffer();
			remark.append(site.getProvince());
			remark.append(site.getCity());
			remark.append(site.getArea());
			remark.append(site.getAddress());
			order.setAreaRemark(remark.toString());//站点的具体地址
			order.setOrderStatus(null);//状态--为空
			order.setUser(null);//未分派
			order.setDateUpd(new Date());//更新时间
			//更新物流信息
			addOrderExpress(order, currUser, site.getName());
			//更新运单
			Key<Order> r = orderService.save(order);
			if(r != null){
				//更新到mysql 删除一条记录
				postDeliveryService.deleteByMailNum(mailNum);
				map.put("operFlag", 1);//1:成功
				//刷新列表
				map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
			}else{ 
				map.put("operFlag", 0);//0:失败
			}
		}
		return map;		
		
	}
	/**
	 * Description: 设置订单的物流信息--转其他站点
	 * @param order 订单
	 * @param user 派件员
	 * @author liyanlei
	 * 2016年4月22日下午3:32:35
	 */
	private void addOrderExpress(Order order, User user, String siteName){
		//更新物流状态
		order.setExpressStatus(ExpressStatus.Packed);
		//更新物流信息
		List<Express> expressList = order.getExpresses();
		if(expressList == null){
			expressList = new ArrayList<Express>();
		}
		Express express = new Express();
		express.setDateAdd(new Date());
		//express.setRemark("已由【" + siteName + "】出库，转送到【" + order.getAreaName() + "】进行配送,操作员电话：" + user.getRealName() + " " + user.getLoginName() + "。");
		express.setRemark("已由【" + user.getSite().getName() + "】出库，转送到【" + order.getAreaName() + "】进行配送。操作员电话：" + user.getRealName() + " " + user.getLoginName() + "。");
		express.setLat(user.getSite().getLat());
		express.setLon(user.getSite().getLng());
		boolean expressIsNotAdd = true;//防止多次添加
		//检查是否添加过了
		for (Express express1 : expressList) {
		    if (express.getRemark().equals(express1.getRemark())) {
		    	expressIsNotAdd = false;
		        break;
		    }
		}
		if (expressIsNotAdd) {//防止多次添加
			expressList.add(express);
		    order.setExpresses(expressList);
		}
	}
	/**************************转其他站点***************结束***********************************/
	/**************************转其他快递公司***************开始***********************************/
	/**
	 * Description: 获取快递公司
	 * @param mailNum 运单号
	 * @param senderId 派件员id
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月11日下午4:15:05
	 */
	@ResponseBody
	@RequestMapping(value="/getAllExpressCompanyList", method=RequestMethod.GET)
	public List<UserVO> getAllExpressCompanyList(String siteId, Model model) {
		UserVO uservo = new UserVO();
		//uservo.setId(new ObjectId("5546548"));
		uservo.setLoginName("loginName");
		uservo.setPhone("12345678945");
		List<UserVO> userVoList = userService.findUserListBySite(siteId);
		if(userVoList == null || userVoList.size() == 0){
			userVoList.add(uservo);
		}
		return userVoList;
	}
	/**
	 * Description: 转其他快递
	 * @param mailNum
	 * @param expressId
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午2:04:59
	 */
	@RequestMapping(value="/toOtherExpress", method=RequestMethod.GET)
	public Map<String, Object> toOtherExpress(String mailNum, String expressId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//====================start================================
		//查询运单信息
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			//当运单到达站点，首次分派;当运单状态处于滞留拒收时，可以重新分派	
			if(ExpressStatus.ArriveStation.equals(order.getExpressStatus())
				||ExpressStatus.Delay.equals(order.getExpressStatus())
				|| ExpressStatus.Refuse.equals(order.getExpressStatus())){
				if(order.getUser() == null){//未分派，可以分派
					//saveOrderMail(order, staffId, map);
				}else{//重复扫描，此运单已分派过了
					map.put("operFlag", 2);
				}
			}else{//重复扫描，此运单已分派过了
				map.put("operFlag", 2);
			}
		}else{
			map.put("erroFlag", 0);//0:运单号不存在
		}
		//=====================end================================
		return map;
	}
	/**************************转其他快递公司***************结束***********************************/
	
	/**************************申请退货***************开始***********************************/
	/**
	 * Description: 退货
	 * @param mailNum 运单号
	 * @param returnReasonType 退货原因类型
	 * @param returnReasonInfo 其他原因--原因详情
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月14日下午1:57:12
	 */
	@RequestMapping(value="/saveReturn", method=RequestMethod.GET)
	public Map<String, Object> dispatch(String mailNum, String returnReasonType, String returnReasonInfo, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//====================start================================
		//查询运单信息
		Order order = orderService.findOneByMailNum("", mailNum);
		if(order != null){
			//当运单到达站点，首次分派;当运单状态处于滞留拒收时，可以重新分派	
			if(ExpressStatus.ArriveStation.equals(order.getExpressStatus())
				||ExpressStatus.Delay.equals(order.getExpressStatus())
				|| ExpressStatus.Refuse.equals(order.getExpressStatus())){
				if(order.getUser() == null){//未分派，可以分派
					//saveOrderMail(order, staffId, map);
				}else{//重复扫描，此运单已分派过了
					map.put("operFlag", 2);
				}
			}else{//重复扫描，此运单已分派过了
				map.put("operFlag", 2);
			}
		}else{
			map.put("erroFlag", 0);//0:运单号不存在
		}
		//=====================end================================
		return map;
	}
	/**************************申请退货***************结束***********************************/
	
	
}
