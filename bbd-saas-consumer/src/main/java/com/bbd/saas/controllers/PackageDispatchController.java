package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.api.mysql.SmsInfoService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.ExpressExchangeStatus;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.models.PostDelivery;
import com.bbd.saas.mongoModels.ExpressExchange;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.Sender;
import com.bbd.saas.vo.UserVO;
import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/packageDispatch")
@SessionAttributes("packageDispatch")
public class PackageDispatchController {
	
	public static final Logger logger = LoggerFactory.getLogger(PackageDispatchController.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	PostDeliveryService postDeliveryService;
	@Autowired
	ExpressExchangeService expressExchangeService;
	@Autowired
	SmsInfoService smsInfoService;
	@Value("${bbd.contact}")
	private String contact;

	/**
	 * Description: 跳转到包裹分派页面
	 * @param pageIndex 当前页
	 * @param status 状态
	 * @param arriveBetween 到站时间
	 * @param request
	 * @param model
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:25:07
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request, Model model) {
		try {
			//设置默认查询条件
			status = Numbers.defaultIfNull(status, -1);//未分派
			//到站时间前天、昨天和今天
			arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -2));
			//查询数据
			PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, request);
			logger.info("=====运单分派====" + orderPage);
			model.addAttribute("orderPage", orderPage);
			model.addAttribute("arriveBetween", arriveBetween);
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			model.addAttribute("areaCode", user == null? "" : user.getSite() == null ? "" : user.getSite().getAreaCode());
		} catch (Exception e) {
			logger.error("===跳转到包裹分派页面===出错:" + e.getMessage());
		}
		return "page/packageDispatch";
	}

	/**
	 * 分页Ajax更新
	 * @param pageIndex 当前页
	 * @param status 状态
	 * @param arriveBetween 到站时间范围
	 * @param request 请求
     * @return 当前页的数据和分页信息
     */
	@ResponseBody
	@RequestMapping(value="/getList", method=RequestMethod.GET)
	public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
		//查询数据
		PageModel<Order> orderPage = null;
		try {
			//参数为空时，默认值设置
			pageIndex = Numbers.defaultIfNull(pageIndex, 0);
			status = Numbers.defaultIfNull(status, -1);
			//当前登录的用户信息
			User user = adminService.get(UserSession.get(request));
			orderPage = queryData(pageIndex, status, arriveBetween, user.getSite().getAreaCode());
		} catch (Exception e) {
			logger.error("===分页Ajax更新列表===出错:" + e.getMessage());
		}				
		return orderPage;
	}
	private PageModel<Order> queryData(Integer pageIndex, Integer status, String arriveBetween, String areaCode){
		//设置查询条件
		OrderQueryVO orderQueryVO = new OrderQueryVO();
		orderQueryVO.dispatchStatus = status;
		orderQueryVO.arriveBetween = arriveBetween;
		orderQueryVO.areaCode = areaCode;
		PageModel<Order> pageModel = new PageModel<Order>();
		pageModel.setPageNo(pageIndex);
		pageModel.setPageSize(50);
		PageModel<Order> orderPage = orderService.findPageOrders(pageModel, orderQueryVO);
		//查询派件员姓名电话
		if(orderPage != null && orderPage.getDatas() != null){
			List<Order> orderList = formatOrder(orderPage.getDatas()) ;
			orderPage.setDatas(orderList);
		}
		return orderPage;
	}
	/**
	 * Description: 运单分派--把到站的包裹分派给派件员
	 * @param mailNum 运单号
	 * @param courierId 派件员id
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:55
	 */
	@ResponseBody
	@RequestMapping(value="/dispatch", method=RequestMethod.GET)
	public Map<String, Object> dispatch(String mailNum, String courierId, Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			map = new HashMap<String, Object>();
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询运单信息
			Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
			//查询派件员信息
			User user = userService.findOne(courierId);
			int r = doOneDispatch(order, user);
			map.put("operFlag", r);
			if(r == 1){//1:分派成功，查询列表数据
				PageModel<Order> orderPage = queryData(pageIndex, status, arriveBetween, currUser.getSite().getAreaCode());
				map.put("orderPage", orderPage);
			}
		} catch (Exception e) {
			logger.error("===运单分派===出错:" + e.getMessage());
		}
		return map;
	}
	
	/**
	 * Description: 运单分派--mongodb库中更新派件员和运单状态，添加一条数据到mysql库或者更新mysql库中快递员信息
	 * @param order 订单
	 * @param user 派件员
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:08
	 */
	private int saveOrderMail(Order order, User user){

		//运单分派给派件员
		order.setUserId(user.getId().toString());
		//更新物流信息
		setOrderExpress(order, user);
		//更新运单状态--已分派
		order.setOrderStatus(OrderStatus.DISPATCHED);
		order.setDateUpd(new Date());
		//更新运单
		Key<Order> r = orderService.save(order);
		if(r != null){
			saveOneOrUpdatePost(order, user);
			smsInfoService.sendToSending(order.getSrcMessage(),order.getMailNum(),user.getRealName(),user.getLoginName(),contact,order.getReciever().getPhone());
			if(Srcs.DANGDANG.equals(order.getSrc())||Srcs.PINHAOHUO.equals(order.getSrc())||Srcs.DDKY.equals(order.getSrc())){
				ExpressExchange expressExchange=new ExpressExchange();
				expressExchange.setOperator(user.getRealName());
				expressExchange.setStatus(ExpressExchangeStatus.waiting);
				expressExchange.setPhone(user.getLoginName());
				expressExchange.setOrder(order.coverOrderVo());
				expressExchange.setDateAdd(new Date());
				expressExchangeService.save(expressExchange);
			}
			return 1;//1:分派成功
		}else{
			return 3;//3:分派失败
		}
	}
	
	/**
	 * Description: 设置订单的物流信息
	 * @param order 订单
	 * @param user 派件员
	 * @author liyanlei
	 * 2016年4月22日下午3:32:35
	 */
	@SuppressWarnings("deprecation")
	private void setOrderExpress(Order order, User user){
		String remark = null;
		if(order.getOrderStatus() == OrderStatus.NOTDISPATCH){
			remark = "配送员正在为您派件，配送员电话：" + user.getRealName() + " " + user.getLoginName();
		}else{
			remark = "配送员正在为您重新派件，配送员电话：" + user.getRealName() + " " + user.getLoginName();
		}
		OrderCommon.addOrderExpress(ExpressStatus.Delivering, order, user, remark);
	}
	/**
	 * Description: 运单号不存在，则添加一条记录；存在，则更新派件员postManId和staffId
	 * @param order
	 * @param user
	 * @author: liyanlei
	 * 2016年4月21日上午10:37:30
	 */
	private void saveOneOrUpdatePost(Order order, User user){
		int row = postDeliveryService.findCountByMailNum(order.getMailNum());
		if(row == 0){ //保存--插入mysql数据库
			PostDelivery postDelivery = new PostDelivery();
			postDelivery.setCompany_code(user.getSite().getCompanycode());
			postDelivery.setDateNew(new Date());
			postDelivery.setDateUpd(new Date());
			postDelivery.setMail_num(order.getMailNum());
			postDelivery.setOut_trade_no(order.getOrderNo());
			postDelivery.setPostman_id(user.getPostmanuserId());
			postDelivery.setReceiver_province(order.getReciever().getProvince());
			postDelivery.setReceiver_city(order.getReciever().getCity());
			postDelivery.setReceiver_district(order.getReciever().getArea());
			int len = order.getReciever().getAddress().length() >127 ? 127 : order.getReciever().getAddress().length();
			postDelivery.setReceiver_address(order.getReciever().getAddress().substring(0, len));
			postDelivery.setReceiver_company_name("");
			len = order.getReciever().getName().length() >15 ? 15 : order.getReciever().getName().length();
			postDelivery.setReceiver_name(order.getReciever().getName().substring(0, len));
			postDelivery.setReceiver_phone(order.getReciever().getPhone());
			postDelivery.setReceiver_province(order.getReciever().getProvince());
			postDelivery.setSender_company_name(order.getSrcMessage());
			Sender sender = order.getSender();
			if(sender!=null){
				len = order.getSender().getAddress().length() >127 ? 127 : order.getSender().getAddress().length();
				postDelivery.setSender_address(order.getSender().getAddress().substring(0, len));
				postDelivery.setSender_city(order.getSender().getCity());
				len = order.getSender().getName().length() > 15 ? 15 : order.getSender().getName().length();
				postDelivery.setSender_name(order.getSender().getName().substring(0, len));
				postDelivery.setSender_phone(order.getSender().getPhone());
				postDelivery.setSender_province(order.getSender().getProvince());
			}else{
				postDelivery.setSender_address("");
				postDelivery.setSender_city("");
				postDelivery.setSender_name("");
				postDelivery.setSender_phone("");
				postDelivery.setSender_province("");
			}

			postDelivery.setStaffid(user.getStaffid());
			postDelivery.setGoods_fee(0);
			postDelivery.setGoods_number(order.getGoods()==null?0:order.getGoods().size());
			postDelivery.setPay_status("1");
			postDelivery.setPay_mode("4");
			postDelivery.setFlg("1");
			postDelivery.setSta("1");
			postDelivery.setTyp("4");
			postDelivery.setNeed_pay("0");
			postDelivery.setIslooked("0");
			postDelivery.setIscommont("0");
			postDeliveryService.insert(postDelivery);
			logger.info("运单分派成功，已更新到mysql的bbt数据库的postdelivery表，mailNum==="+order.getMailNum()+" staffId=="+user.getStaffid()+" postManId=="+user.getPostmanuserId());			
		}else{//已保存过了，更新快递员信息
			postDeliveryService.updatePostAndStatusAndCompany(order.getMailNum(), user.getPostmanuserId(), user.getStaffid(), "1", null);
			logger.info("运单重新分派成功，已更新到mysql的bbt数据库的postdelivery表，mailNum==="+order.getMailNum()+" staffId=="+user.getStaffid()+" postManId=="+user.getPostmanuserId());
		}
		
	}

	/**
	 * 批量分派
	 * @param mailNums mailNum集合
	 * @param courierId 派件员
	 * @param pageIndex 当前页
	 * @param status 状态
	 * @param arriveBetween 到站时间范围
	 * @param request 请求
     * @return 批量有问题的运单（mgs:mailNum-结果值;mailNum-结果值;---; msg=0，全部分派失败）
	 * 和分派成功后列表数据（orderPage）-只要有一个运单分派成功，就刷新列表数据
     */
	@ResponseBody
	@RequestMapping(value="/batchDispatch", method=RequestMethod.POST)
	public Map<String, Object> batchDispatch(String mailNums, String courierId, Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();;
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			BasicDBList mailNumList = (BasicDBList) JSON.parse(mailNums);
			List<Order> orderList = orderService.findByAreaCodeAndMailNums(currUser.getSite().getAreaCode(), mailNumList);
			if(orderList != null && orderList.size() > 0){
				StringBuffer msg = new StringBuffer();
				int r = -2;
				int num = 0;
				//查询派件员信息
				User user = userService.findOne(courierId);
				if(user == null){
					map.put("msg", "-1");//派件员不存在
					return map;
				}
				for(Order order : orderList){
					r = doOneDispatch(order, user);
					if(r != 1){//分派出问题，记录下来
						msg.append(";");
						msg.append(order.getMailNum());
						msg.append("：#");
						msg.append(r);
						msg.append("#");
					}else{
						num++;
					}
				}
				if(num > 0){//有分派成功的运单就刷新列表数据
					PageModel<Order> orderPage = queryData(pageIndex, status, arriveBetween, currUser.getSite().getAreaCode());
					map.put("orderPage", orderPage);
				}
				if(StringUtil.isNotEmpty(msg.toString())){
					map.put("msg", msg.toString().substring(1));//保存出错的运单信息
				}else{
					map.put("msg", "");//保存出错的运单信息
				}
			}else{
				map.put("msg", "0");//运单不存在或与站点无关
			}
		} catch (Exception e) {
			logger.error("==运单批量分派出错:" + e.getMessage());
		}
		return map;
	}
	private int doOneDispatch(Order order, User user){
		if(order == null){//运单不存在,与站点无关
			return  0;//0:运单号不存在
		}else{//运单存在
			//当运单到达站点(未分派)，首次分派;当运单状态处于滞留时，可以重新分派
			if(OrderStatus.NOTDISPATCH.equals(order.getOrderStatus())//未分派
					||OrderStatus.RETENTION.equals(order.getOrderStatus())) {//滞留
				return saveOrderMail(order, user);//更新数据库字段值（mongodb和mysql）
			}else if(order.getUserId() != null && !"".equals(order.getUserId())){//重复扫描，此运单已分派过了
				return  2;//0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:分派失败;4:未知错误（只有状态为未分派、滞留的运单才能分派！）。
			}else{
				return  4;//0:运单号不存在;1:分派成功;2:重复扫描，此运单已分派过了;3:分派失败;4:未知错误（只有状态为未分派、滞留的运单才能分派！）。
			}
		}
	}

	/**
	 * Description: 获取本站点下的所有状态为有效的派件员和站长
	 * @param request
	 * @return
	 * @author: liyanlei
	 * 2016年4月15日上午11:06:19
	 */
	@ResponseBody
	@RequestMapping(value="/getAllUserList", method=RequestMethod.GET)
	public List<UserVO> getAllUserList(final HttpServletRequest request) {
		//查询
		List<UserVO> userVoList = null;
		try {
			User user = adminService.get(UserSession.get(request));//当前登录的用户信息
			userVoList = userService.findUserListBySite(user.getSite());
		} catch (Exception e) {
			logger.error("===获取本站点下的所有状态为有效的派件员===出错:" + e.getMessage());
		}
		return userVoList;
	}


	/**
	 * 格式化orderList给item加入用户信息
	 * @param orderList
	 * @return
     */
	public List<Order> formatOrder(List<Order> orderList){
		User courier = null;
		UserVO userVO = null;
		for(Order order1 : orderList){
			courier = userService.findOne(order1.getUserId());
			if(courier!=null){
				userVO = new UserVO();
				userVO.setLoginName(courier.getLoginName());
				userVO.setRealName(courier.getRealName());
				order1.setUserVO(userVO);
			}
		}
		return orderList;
	}

	/**
	 * 运单取消分派
	 * @param mailNum 运单号
	 * @param request 请求
	 * @return 返回值 true:操作成功；false:操作失败
	 */
	@ResponseBody
	@RequestMapping(value="/checkOrderStatus", method=RequestMethod.POST)
	public Map<String, Object>  checkOrderStatus(String mailNum, final HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询运单信息
			Order order = orderService.findOneByMailNum(null, mailNum);
			map = new ConcurrentHashMap<String, Object>();
			if(order == null){//运单不存在,与站点无关
				map.put("code", -1);
				map.put("msg", "运单不存在");
			}else if(!order.getAreaCode().equals(currUser.getSite().getAreaCode())){
				map.put("code", -1);
				map.put("msg", "此运单不属于本站点");
			}else if(order.getOrderStatus() != OrderStatus.DISPATCHED){
				map.put("code", 0);
				map.put("msg", order.getOrderStatus().getMessage());
			}else{
				map.put("code", 1);
			}
		} catch (Exception e) {
			logger.error("===取消分派，检查运单状态===出错:" + e.getMessage());
		}
		return map;
	}
	/**
	 * 运单取消分派
	 * @param mailNum 运单号
	 * @param request 请求
     * @return 返回值 true:操作成功；false:操作失败
     */
	@ResponseBody
	@RequestMapping(value="/cancelDispatch", method=RequestMethod.POST)
	public Map<String, Object>  cancelDispatch(String mailNum, final HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			if(mailNum != null){
				mailNum = mailNum.trim();
			}
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			//查询运单信息
			Order order = orderService.findOneByMailNum(null, mailNum);
			map = new ConcurrentHashMap<String, Object>();
			if(order == null){//运单不存在,与站点无关
				map.put("success", false);
				map.put("msg", "运单不存在");
			}else if(!order.getAreaCode().equals(currUser.getSite().getAreaCode())){
				map.put("success", false);
				map.put("msg", "此运单不属于本站点");
			}else if(order.getOrderStatus() != OrderStatus.DISPATCHED){
				map.put("success", true);
				map.put("msg", "该订单已被处理");
			}else{//运单存在
				//添加物流信息
				return saveOrderMail(order, currUser, map);
			}
		} catch (Exception e) {
			logger.error("===取消运单分派===出错:" + e.getMessage());
		}
		return map;
	}
	/**
	 * Description: 运单分派--mongodb库中跟新派件员和运单状态，添加一条数据到mysql库或者更新mysql库中快递员信息
	 * @param order 订单
	 * @param currUser 当前用户
	 * @author: liyanlei
	 * 2016年4月16日上午11:36:08
	 */
	private Map<String, Object> saveOrderMail(Order order, User currUser, Map<String, Object> map){
		//运单分派给派件员
		order.setUserId(null);
		//更新物流信息
		//OrderCommon.addOrderExpress(ExpressStatus.ArriveStation, order, currUser, "取消分派");
		//更新运单状态--未分派
		order.setOrderStatus(OrderStatus.NOTDISPATCH);
		order.setExpressStatus(ExpressStatus.ArriveStation);
		order.setDateUpd(new Date());
		//更新运单
		Key<Order> r = orderService.save(order);
		if(r != null && r.getId() != null){
			int num = postDeliveryService.deleteByMailNum(order.getMailNum());
			int j = 0;
			while (num == 0 && j < 3 ){//最多执行3次
				num = postDeliveryService.deleteByMailNum(order.getMailNum());
				j++;
			}
			logger.info("运单取消分派成功，已删除mysql的bbt数据库的postdelivery表中记录，mailNum："+order.getMailNum()+", 执行次数："+j);
			//smsInfoService.sendToSending(order.getSrc().getMessage(),order.getMailNum(),currUser.getRealName(),currUser.getLoginName(),contact,order.getReciever().getPhone());
			map.put("success", true);
			map.put("msg", "取消分派成功");
		}else{
			map.put("success", false);
			map.put("msg", "取消分派失败");
		}
		return map;
	}

}
