package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.dao.mongo.OrderNumDao;
import com.bbd.saas.dao.mongo.OrderParcelDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderSetStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.*;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class OrderServiceImpl implements OrderService {
	public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private OrderDao orderDao;
    private OrderNumDao orderNumDao;
    private OrderParcelDao orderParcelDao;
    private UserDao userDao;
	//@Autowired
	//private SitePoiApi sitePoiApi;
	//@Autowired
	private SiteService siteService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private UserService userService;


    public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public OrderParcelDao getOrderParcelDao() {
		return orderParcelDao;
	}

	public void setOrderParcelDao(OrderParcelDao orderParcelDao) {
		this.orderParcelDao = orderParcelDao;
	}

	public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

	public OrderNumDao getOrderNumDao() {
		return orderNumDao;
	}

	public void setOrderNumDao(OrderNumDao orderNumDao) {
		this.orderNumDao = orderNumDao;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * 带查询条件去检索订单
	 * @param pageModel
	 * @param orderQueryVO
	 * @return
	 */
	public PageModel<Order> findOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO){
		if(orderQueryVO!=null){
			if(StringUtils.isNotBlank(orderQueryVO.mailNum)){
				return orderDao.findOrders(pageModel,orderQueryVO);
			}else{
				if(StringUtils.isNotBlank(orderQueryVO.parcelCode)){
					OrderParcel orderParcel = orderParcelDao.findOrderParcelByParcelCode(orderQueryVO.areaCode,orderQueryVO.parcelCode);
					if(orderParcel!=null){
						List<Order> orderList = Lists.newArrayList();
						for(Order order:orderParcel.getOrderList()){
							Order orderTemp = orderDao.findOneByMailNum(order.getAreaCode(),order.getMailNum());
							if(orderTemp!=null)
								orderList.add(orderTemp);
						}
						pageModel.setDatas(orderList);
						pageModel.setPageNo(0);
						pageModel.setPageSize(orderList.size());
						pageModel.setTotalCount(Long.valueOf(orderList.size()));
					}
					return pageModel;
				}
			}
		}
		return orderDao.findOrders(pageModel,orderQueryVO);
	}


    /**
	 * Description: 根据运单号查询订单信息
	 * @param mailNum 运单号
	 * @return
	 * @author: liyanlei
	 * 2016年4月12日下午3:38:38
	 */
	@Override
	public Order findOneByMailNum(String areaCode, String mailNum) {
		return orderDao.findOneByMailNum(areaCode,mailNum);
	}

	/**
	 * 根据其他快递的运单号查询订单
	 * @param newMailNum
	 * @return
     */
	@Override
	public Order findOneByNewMailNum(String newMailNum) {
		return orderDao.findOneByNewMailNum(newMailNum);
	}

	@Override
	public Order findByOrderNo(String orderNo) {
		return orderDao.findByOrderNo(orderNo);
	}

	@Override
	public Order findByOrderNoAndSrc(String orderNO, Srcs srcs) {
		return orderDao.findByOrderNoAndSrc(orderNO,srcs);
	}

	/**
     * Description: 保存订单
     * @param order
     * @return
     * @author: liyanlei
     * 2016年4月12日下午5:59:03
     */
	@Override
	public Key<Order> save(Order order) {
		return orderDao.save(order);
	}

	/**
	 * 根据站点编码获取该站点订单数据
	 * @param areaCode
	 * @return
	 */
	@Override
	public OrderNumVO getOrderNumVO(String areaCode) {
		return orderDao.getOrderNumVO(areaCode);
	}

	/**
	 * 更新订单状态
	 * 此处需要再加上包裹下的订单的状态更新
	 * @param mailNum 运单号
	 * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
	 * @param orderStatusNew
     */
	@Override
	public void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew){
		orderDao.updateOrderOrderStatu(mailNum,orderStatusOld,orderStatusNew);//修改订单表里的状态
		orderParcelDao.updateOrderOrderStatu(mailNum,orderStatusOld,orderStatusNew);//修改包裹表里的订单的状态
	}

	@Override
	public PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO) {
		if(orderQueryVO == null){
			return null;
		}
		PageModel<Order> pageModel = new PageModel<Order>();
		pageModel.setPageNo(pageIndex);
		return orderDao.findPageOrders(pageModel, orderQueryVO);
	}

	@Override
	public List<Order> findOrders(OrderQueryVO orderQueryVO) {
		return orderDao.findOrders(orderQueryVO);
	}

	@Override
	public int updateOrder(OrderUpdateVO orderUpdateVO,
			OrderQueryVO orderQueryVO) {
		//派件员员工Id
    	UpdateResults r = orderDao.updateOrder(orderUpdateVO, orderQueryVO);
    	if(r != null){
    		return r.getUpdatedCount();
    	}
		return 0;
	}

	@Override
	public long getDispatchedNums(String areaCode, String betweenTime) {
		return  orderDao.getDispatchedNums(areaCode, betweenTime);
	}

	@Override
	public List<Order> getTodayUpdateOrdersBySite(String areaCode) {
		return orderDao.getTodayUpdateOrdersByAreaCode(areaCode);
	}

	@Override
	public long getCounByMailNumsAndExpressStatus(BasicDBList idList, ExpressStatus expressStatus) {
		return orderDao.selectCountByMailNumsAndExpressStatus(idList, expressStatus);
	}

	@Override
	public PageModel<Order> findPageOrders(Integer pageIndex, String tradeNo, ObjectId uId, String keyword) {
		PageModel<Order> pageModel = new PageModel<Order>();
		pageModel.setPageNo(pageIndex);
		return orderDao.findPageOrders(pageModel, tradeNo, uId, keyword);
	}

	@Override
	public long findCountByTradeNo(String tradeNo) {
		return orderDao.findCountByTradeNo(tradeNo);
	}

	@Override
	public Order afterImportDealWithOrder(Order order) {
		order = reduceAreaCodeWithOrder(order);
		order = reduceMailNumWithOrder(order);
		return order;
	}

	@Override
	public Order reduceMailNumWithOrder(Order order) {
		if (order!=null&&Strings.isNullOrEmpty(order.getMailNum())) {
			long orderNum = findOrderNum();//递增的9位数字
			order.setMailNum("BBD" + orderNum);
			order.setDatePrint(new Date());
			logger.info("create mailNum:"+order.getMailNum());
			orderDao.updateOrderWithMailNum(order);
		}
		return order;
	}

	@Override
	public Order reduceAreaCodeWithOrder(Order order) {
		if(order!=null&&order.getReciever()!=null) {
			try {
				Reciever reciever = order.getReciever();
				String address = reciever.getProvince() + reciever.getCity() + reciever.getArea() + reciever.getAddress();
				address = StringUtil.filterString(address);
				//List<String> areaCodeList = sitePoiApi.searchSiteByAddress("",address);
				List<String> areaCodeList = null;
				logger.info(address);
				logger.info(areaCodeList.size()+"");
				if(areaCodeList!=null && areaCodeList.size()>0){
					//通过积分获取优选区域码，暂时用第一个
					String siteId = areaCodeList.get(0);
					if (!"".equals(siteId)) {
						logger.info("订单:" + order.getOrderNo() + "，匹配的站点Id为" + siteId);
						//根据站点id获取site信息，更新areacode, areaRemark
						Site site = siteService.findSite(siteId);
						order.setAreaCode(site.getAreaCode());
						order.setAreaRemark(site.getName());
						logger.info("订单:" + order.getOrderNo() + "，匹配的区域码（站点码）为" + order.getAreaCode() + ",站点名称为：" + order.getAreaRemark());
					} else {
						order.setAreaCode("9999-999");
						order.setAreaRemark("no match areacode");
						logger.info("订单:" + order.getOrderNo() + "，匹配的站点区域码失败");
					}
				}
			} catch (Exception e) {
				order.setAreaCode("9999-999");
				order.setAreaRemark("no match areacode");
				logger.info("订单:" + order.getOrderNo() + "，匹配的站点区域码异常:"+e.getMessage());
				e.printStackTrace();
			}
			order = updateOrderWithAreaCode(order);
		}
		return order;
	}


	@Override
	public List<Order> findAllByTradeNo(String tradeNo) {
		return orderDao.findAllByTradeNo(tradeNo);
	}


	private Order updateOrderWithAreaCode(Order order) {
		orderDao.updateOrderWithAreaCode(order.getOrderNo(),order.getAreaCode(),order.getAreaRemark(),order.getPrintStatus());//修改订单表里的状态
		return order;
	}

	/**
	 * 获取递增的9位数字，用于记录运单号
	 *
	 * @return
	 */
	private long findOrderNum() {
		OrderNum one = orderNumDao.findOrderNum();
		long num = Long.parseLong(one.num);
		one.num = (num + 1) + "";
		orderNumDao.updateOrderNum("num",one.num);
		return num;
	}




	/**
	 * 根据相关条件查询出所有揽件入库的订单
	 * @param  pageIndex
	 * @param orderQueryVO
	 * @return
	 */
	@Override
	public  PageModel<OrderHoldToStoreVo> 	findPageOrdersForHoldToStore(Integer pageIndex,List<String> tradeNoList, OrderQueryVO orderQueryVO){

		if(orderQueryVO == null){
			return null;
		}
		PageModel<Order> pageOrders = new PageModel<Order>();
		pageOrders.setPageNo(pageIndex);
		pageOrders = orderDao.findPageOrdersForHoldToStore(pageOrders,tradeNoList, orderQueryVO);
		List<Order>  datas = pageOrders.getDatas();
		List <OrderHoldToStoreVo> orderHoldToStoreList=new ArrayList<>();

		for(Order  order: datas ){
			OrderHoldToStoreVo orderHoldToStoreVo=new OrderHoldToStoreVo();
			String address=order.getReciever().getProvince()+"-"+order.getReciever().getCity()+"-"+order.getReciever().getArea()+"-"+order.getReciever().getAddress();
			String mailNum = order.getMailNum();
			String name = order.getReciever().getName();
			String phone = order.getReciever().getPhone();
			String tradeNo = order.getTradeNo();
			OrderSetStatus orderSetStatus = order.getOrderSetStatus();

			Trade oneByTradeNo = tradeService.findOneByTradeNo(tradeNo);
			ObjectId embraceId = oneByTradeNo.getEmbraceId();

			User user = userService.findOne(embraceId.toString());
			String realName = user.getRealName();
			String loginName = user.getLoginName();

			orderHoldToStoreVo.setMailNum(mailNum);
			orderHoldToStoreVo.setRecieverName(name);
			orderHoldToStoreVo.setRecieverPhone(phone);
			orderHoldToStoreVo.setRecieverAddress(address);
			orderHoldToStoreVo.setUserName(realName);
			orderHoldToStoreVo.setPhone(loginName);
			orderHoldToStoreVo.setOrderSetStatus(orderSetStatus);
			orderHoldToStoreList.add(orderHoldToStoreVo);
		}
		PageModel<OrderHoldToStoreVo> pageModel = new PageModel<OrderHoldToStoreVo>();
		pageModel.setPageNo(pageOrders.getPageNo());
		pageModel.setTotalCount(pageOrders.getTotalCount());
		pageModel.setDatas(orderHoldToStoreList);
		return  pageModel;

	}

	/**
	 * 根据站点编码获取该站点 揽件的订单数量
	 * @param areaCode
	 * @return
	 */
	@Override
	public OrderHoldToStoreNumVO getOrderHoldToStoreNum(String areaCode) {
		return orderDao.getOrderHoldToStoreNum(areaCode);
	}
}
