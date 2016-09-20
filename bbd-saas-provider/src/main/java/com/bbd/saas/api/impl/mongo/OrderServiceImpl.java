package com.bbd.saas.api.impl.mongo;


import com.bbd.poi.api.Geo;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.Result;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.WayService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.api.mysql.SiteMySqlService;
import com.bbd.saas.dao.mongo.OrderDao;
import com.bbd.saas.dao.mongo.OrderNumDao;
import com.bbd.saas.dao.mongo.OrderParcelDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.ParcelStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.models.SiteMySql;
import com.bbd.saas.mongoModels.*;
import com.bbd.saas.utils.GeoUtil;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
    @Autowired
    private SitePoiApi sitePoiApi;
    @Autowired
    Geo geo;
    @Autowired
    private SiteMySqlService siteMysqlService;

    @Autowired
    private PostmanUserService userMysqlService;

    @Autowired
    private SiteService siteService;
    @Autowired
    private WayService wayService;


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

    @Override
    public Order findOneById(String id) {
        return orderDao.findOne("_id", new ObjectId(id));
    }

    @Override
    public Order findOneByObjectId(ObjectId id) {
        return orderDao.findOne("_id", id);
    }

    /**
     * 带查询条件去检索订单
     *
     * @param pageModel
     * @param orderQueryVO
     * @return
     */
    public PageModel<Order> findOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {
        if (orderQueryVO != null) {
            if (StringUtils.isNotBlank(orderQueryVO.mailNum)) {
                return orderDao.findOrders(pageModel, orderQueryVO);
            } else {
                if (StringUtils.isNotBlank(orderQueryVO.parcelCode)) {
                    OrderParcel orderParcel = orderParcelDao.findOrderParcelByParcelCode(orderQueryVO.areaCode, orderQueryVO.parcelCode);
                    if (orderParcel != null) {
                        List<Order> orderList = Lists.newArrayList();
                        for (Order order : orderParcel.getOrderList()) {
                            Order orderTemp = orderDao.findOneByMailNum(order.getAreaCode(), order.getMailNum());
                            if (orderTemp != null)
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
        return orderDao.findOrders(pageModel, orderQueryVO);
    }


    /**
     * Description: 根据运单号查询订单信息
     *
     * @param mailNum 运单号
     * @return
     * @author: liyanlei
     * 2016年4月12日下午3:38:38
     */
    @Override
    public Order findOneByMailNum(String areaCode, String mailNum) {
        return orderDao.findOneByMailNum(areaCode, mailNum);
    }

    /**
     * 揽件入库
     * 根据运单号查询
     *
     * @param mailNum
     * @return
     */
    public Order findOneByMailNum(String mailNum) {
        return orderDao.findOneByMailNum(mailNum);
    }

    /**
     * 根据其他快递的运单号查询订单
     *
     * @param newMailNum
     * @return
     */
    @Override
    public List<Order> findOneByNewMailNum(String newMailNum) {
        return orderDao.findOneByNewMailNum(newMailNum);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderDao.findByOrderNo(orderNo);
    }

    @Override
    public Order findByOrderNoAndSrc(String orderNO, Srcs srcs) {
        return orderDao.findByOrderNoAndSrc(orderNO, srcs);
    }

    /**
     * Description: 保存订单
     *
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
     *
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
     *
     * @param mailNum        运单号
     * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
     * @param orderStatusNew
     */
    @Override
    public void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew) {
        orderDao.updateOrderOrderStatu(mailNum, orderStatusOld, orderStatusNew);//修改订单表里的状态
        orderParcelDao.updateOrderOrderStatu(mailNum, orderStatusOld, orderStatusNew);//修改包裹表里的订单的状态
    }

    @Override
    public PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO) {
        if (orderQueryVO == null) {
            return null;
        }
        PageModel<Order> pageModel = new PageModel<Order>();
        pageModel.setPageNo(pageIndex);
        return orderDao.findPageOrders(pageModel, orderQueryVO);
    }

    @Override
    public PageModel<Order> findPageOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO) {
        if (orderQueryVO == null) {
            return null;
        }
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
        if (r != null) {
            return r.getUpdatedCount();
        }
        return 0;
    }

    @Override
    public OrderNumVO findHistoryNoArrivedAndArrivedNums(String areaCode, String dateArrived) {
        return orderDao.selectHistoryNoArrivedAndArrivedNums(areaCode, dateArrived);
    }

    @Override
    public OrderNumVO findHistoryNoArrivedAndArrivedNums(List<String> areaCodeList, String dateArrived) {
        return orderDao.selectHistoryNoArrivedAndArrivedNums(areaCodeList, dateArrived);
    }

    @Override
    public List<Order> getTodayUpdateOrdersBySite(String areaCode) {
        return orderDao.getTodayUpdateOrdersByAreaCode(areaCode);
    }

    @Override
    public long getCounByMailNumsAndExpressStatus(BasicDBList mailNumList, ExpressStatus expressStatus) {
        return orderDao.selectCountByMailNumsAndExpressStatus(mailNumList, expressStatus);
    }

    @Override
    public PageModel<Order> findPageOrders(Integer pageIndex, String tradeNo, ObjectId uId, String keyword) {
        PageModel<Order> pageModel = new PageModel<Order>();
        pageModel.setPageNo(pageIndex);
        pageModel.setPageSize(20);
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
        if (order != null && Strings.isNullOrEmpty(order.getMailNum())) {
            long orderNum = findOrderNum();//递增的9位数字
            order.setMailNum("BBD" + orderNum);
            order.setDatePrint(new Date());
            logger.info("create mailNum:" + order.getMailNum());
            orderDao.updateOrderWithMailNum(order);
        }
        return order;
    }

    /**
     * 生成订单区域码
     *
     * @param order
     * @return
     */
    @Override
    public Order reduceAreaCodeWithOrder(Order order) {
        if (order != null && order.getReciever() != null) {
            try {
                Reciever reciever = order.getReciever();
                String address = reciever.getProvince() + reciever.getCity() + reciever.getArea() + reciever.getAddress();
                address = StringUtil.filterString(address);
                //通过积分获取优选区域码，
                String siteId = findBestSiteWithAddress(address);
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
            } catch (Exception e) {
                order.setAreaCode("9999-999");
                order.setAreaRemark("no match areacode");
                logger.info("订单:" + order.getOrderNo() + "，匹配的站点区域码异常:" + e.getMessage());
                e.printStackTrace();
            }
            order = updateOrderWithAreaCode(order);
        }
        return order;
    }

    /**
     * 更新包裹信息
     *
     * @param order
     */
    public String updateParcelWithOrder(Order order) {
        try {
            if (order != null) {
                //查询订单所在站点是否已有Suspense待打包的包裹
                OrderParcel orderParcel = orderParcelDao.findByOrderInfo(order);
                if (orderParcel == null) {
                    logger.info(String.format("[updateParcelWithOrder] order:%s find OrderParcel null", order.getOrderNo()));
                    //没有，插入orderParcel
                    orderParcel = new OrderParcel();
                    orderParcel.setParcelCode("");
                    orderParcel.setSort_uid("");
                    orderParcel.setDriver_uid("");
                    orderParcel.setStation_uid("");
                    orderParcel.setStatus(ParcelStatus.Suspense);
                    Site site = siteService.findSiteByAreaCode(order.getAreaCode());
                    orderParcel.setAreaRemark(site.getName());
                    orderParcel.setAreaCode(site.getAreaCode());
                    orderParcel.setAreaName(site.getName());
                    orderParcel.setStation_address(site.getAddress());
                    orderParcel.setOrderList(Lists.newArrayList());
                    orderParcel.setDateAdd(new Date());
                    orderParcel.setDateUpd(new Date());
                    orderParcel.setParceltyp("0");
                    orderParcel.setTrackNo("");
                    orderParcel.setSrcAreaCode("");
                    orderParcel.setSrc(order.getSrc().toString());
                    orderParcel.setProvince(site.getProvince());
                    orderParcel.setCity(site.getCity());
                    orderParcel.setArea(site.getArea());
                    orderParcel.setOrdercnt(1);
                    orderParcel.setWayDate("");
                    orderParcel.setWayname("");
                    logger.info(String.format("插入包裹 来源：%s 站点：%s 状态：%s 订单数量%d --> %d", orderParcel.getSrc(), orderParcel.getAreaCode(), orderParcel.getStatus().getMessage(), 0, 1));
                } else {
                    logger.info(String.format("[updateParcelWithOrder] order:%s find OrderParcel id:", orderParcel.getId()));
                    logger.info(String.format("更新包裹 %s 订单数量%d --> %d", orderParcel.getId(), orderParcel.getOrdercnt(), orderParcel.getOrdercnt() + 1));
                    //已有，更新orderParcel里的ordercnt dateUpd
                    orderParcel.setOrdercnt(orderParcel.getOrdercnt() + 1);
                    orderParcel.setDateUpd(new Date());
                }
                //更新orderParcel
                orderParcelDao.save(orderParcel);
                logger.info(String.format("订单%s 插入/更新包裹完成", order.getOrderNo()));
            } else {
                logger.info(String.format("订单%s 已打印无需更新包裹", order.getOrderNo()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("根据订单%s 插入/更新包裹失败，原因：%s", order.getOrderNo(), e.getMessage()));
            return "fail";
        }
        return "success";
    }

    @Override
    public List<Order> findAllByTradeNo(String tradeNo) {
        return orderDao.findAllByTradeNo(tradeNo);
    }

    private Order updateOrderWithAreaCode(Order order) {
        orderDao.updateOrderWithAreaCode(order.getOrderNo(), order.getAreaCode(), order.getAreaRemark(), order.getPrintStatus());//修改订单表里的状态
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
        orderNumDao.updateOrderNum("num", one.num);
        return num;
    }

    @Override
    public long getCounByMailNumsAndOrderStatusList(BasicDBList mailNumList, List<OrderStatus> orderStatusList) {
        return orderDao.selectCountByMailNumsAndExpressStatus(mailNumList, orderStatusList);
    }

    @Override
    public String findBestSiteWithAddress(String address) {
        String resultAreaCode = "";
        List<String> areaCodeList = new ArrayList<String>();
        List<String> mapGuiZiSitesFictitious = new ArrayList<String>();
        try {
            long startTimesearchSiteByAddress = System.currentTimeMillis();   //获取开始时间

//            List<String> areaCodeList = sitePoiApi.searchSiteByAddress("", address);
            //改动 测试V2
            Result<Map<String, List<String>>> areaCodeList1 = sitePoiApi.searchSiteByAddressV2("", address);
            logger.info("[findBestSiteWithAddress]request address:" + address + ", response siteId List size:" + areaCodeList1.data.size());

            long endTimesearchSiteByAddress = System.currentTimeMillis(); //获取结束时间

            logger.info("sass调用searchSiteByAddress运行时间： " + (endTimesearchSiteByAddress - startTimesearchSiteByAddress) + "ms");

//            logger.info("[findBestSiteWithAddress]request address:" + address + ", response siteId List size:" + areaCodeList.size());
            if (areaCodeList1 != null && areaCodeList1.data.size() > 0) {

                if (areaCodeList1.data.get("containerIds") != null && areaCodeList1.data.get("containerIds").size() > 0) {

                    long startTimeGuiZi = System.currentTimeMillis();   //获取开始时间

                    //快递柜逻辑
                    List<String> areaCodeLisContainer = new ArrayList<String>();
                    areaCodeLisContainer = areaCodeList1.data.get("containerIds");
                    for (String siteId : areaCodeLisContainer) {
                        SiteMySql siteMySql = siteMysqlService.selectIdBySiteId(siteId);
                        if (siteMySql != null) {
                            //快递柜站点
                            if (siteMySql.getDaycnt() < siteMySql.getUpperlimit()) {
                                logger.info("快递柜没有达到上限,加入快递柜集合:" + siteId);
                                mapGuiZiSitesFictitious.add(siteId);
                            }
                        }
                    }
                }

                long endTimeGuiZi = System.currentTimeMillis();
                logger.info("快递柜运行时间： " + (endTimesearchSiteByAddress - startTimesearchSiteByAddress) + "ms");

                if (mapGuiZiSitesFictitious.size() > 0) {
                    logger.info("快递柜没有达到上限,返回快递柜集合中的第一个:" + mapGuiZiSitesFictitious.get(0));
                    siteMysqlService.updateSiteDayCntBySiteId(resultAreaCode);
                    return mapGuiZiSitesFictitious.get(0);
                }


                //说明柜子都达到了上限  走老逻辑
                if (areaCodeList1.data.get("siteIds") != null && areaCodeList1.data.get("siteIds").size() > 0 && mapGuiZiSitesFictitious.size() == 0) {
                    //老逻辑
                    areaCodeList = areaCodeList1.data.get("siteIds");
                }

                //虚拟站点集合
                Map<String, SiteMySql> mapSitesFictitious = new HashMap<String, SiteMySql>();
                if (areaCodeList != null && areaCodeList.size() > 0) {
                    if (areaCodeList.size() > 1) {
                        for (String siteId : areaCodeList) {
                            SiteMySql siteMySql = siteMysqlService.selectIdBySiteId(siteId);
                            if (siteMySql != null) {
                                //虚拟站点
                                if (siteMySql.getSitetype() == 2) {
                                    if (siteMySql.getDaycnt() < siteMySql.getUpperlimit()) {
                                        mapSitesFictitious.put(siteId, siteMySql);
                                    }
                                }
                            }
                        }
                        if (mapSitesFictitious.size() > 0) {
                            long startTimemapSitesFictitious = System.currentTimeMillis();   //获取开始时间

                            Map<String, Integer> mapSiteCnts = new TreeMap<String, Integer>();
                            for (Map.Entry<String, SiteMySql> entry : mapSitesFictitious.entrySet()) {
                                //siteid:daycnt
                                mapSiteCnts.put(entry.getKey(), entry.getValue().getDaycnt());
                            }

                            List<Map.Entry<String, Integer>> listcnts = new ArrayList<Map.Entry<String, Integer>>(mapSiteCnts.entrySet());
                            //然后通过比较器来实现排序
                            Collections.sort(listcnts, new Comparator<Map.Entry<String, Integer>>() {
                                //升序排序
                                public int compare(Map.Entry<String, Integer> o1,
                                                   Map.Entry<String, Integer> o2) {
                                    return o1.getValue().compareTo(o2.getValue());
                                }
                            });

                            //得到查询量 最少的站点ID
                            resultAreaCode = listcnts.get(0).getKey();

                            long endTimemapSitesFictitious = System.currentTimeMillis(); //获取结束时间

                            logger.info("虚拟站点整理运行时间： " + (endTimemapSitesFictitious - startTimemapSitesFictitious) + "ms");
                        } else {

                            long startTimeSX = System.currentTimeMillis();   //获取开始时间

                            //没达到下限的
                            Map<String, SiteMySql> mapSitesNoLowerlimit = new HashMap<String, SiteMySql>();
                            //达到下限的 且 小于 上限
                            List<String> areaCodeListLowerlimitNoUpper = new ArrayList<String>();
                            //达到下限的 且 大于等于 上限
                            List<String> areaCodeListLowerlimitUpper = new ArrayList<String>();

                            List<String> areaCodeListnew = new ArrayList<String>();

                            //每个站点增加匹配的上限和下限
                            for (String oneSiteId : areaCodeList) {
                                SiteMySql siteMySql = siteMysqlService.selectIdBySiteId(oneSiteId);
                                if (siteMySql != null) {
                                    //上限
                                    int upperlimit = siteMySql.getUpperlimit();
                                    //下限
                                    int lowerlimit = siteMySql.getLowerlimit();
                                    //订单查询量
                                    int daycnt = siteMySql.getDaycnt();

                                    //大于 下限 小于上限
                                    if (daycnt >= lowerlimit && daycnt < upperlimit) {
                                        areaCodeListLowerlimitNoUpper.add(oneSiteId);
                                    }
                                    //大于 下限 大于上限
                                    if (daycnt >= lowerlimit && daycnt >= upperlimit) {
                                        areaCodeListLowerlimitUpper.add(oneSiteId);
                                    }

                                    //小于下限
                                    if (daycnt < lowerlimit) {
                                        mapSitesNoLowerlimit.put(oneSiteId, siteMySql);
                                    }
                                } else {
                                    logger.info("Mongo的site中存在siteid,Mysql中的site表中没有该siteid!!!");
                                    areaCodeListLowerlimitUpper.add(oneSiteId);
                                }
                            }
                            //如果其中的站点 有1个没有达到下限 则走该逻辑
                            if (mapSitesNoLowerlimit.size() > 0) {
                                Map<String, Integer> mapSiteCnts = new TreeMap<String, Integer>();
                                for (Map.Entry<String, SiteMySql> entry : mapSitesNoLowerlimit.entrySet()) {
                                    //siteid:daycnt
                                    mapSiteCnts.put(entry.getKey(), entry.getValue().getDaycnt());
                                }

                                List<Map.Entry<String, Integer>> listcnts = new ArrayList<Map.Entry<String, Integer>>(mapSiteCnts.entrySet());
                                //然后通过比较器来实现排序
                                Collections.sort(listcnts, new Comparator<Map.Entry<String, Integer>>() {
                                    //升序排序
                                    public int compare(Map.Entry<String, Integer> o1,
                                                       Map.Entry<String, Integer> o2) {
                                        return o1.getValue().compareTo(o2.getValue());
                                    }
                                });
                                //得到查询量 最少的站点ID
                                resultAreaCode = listcnts.get(0).getKey();

                            } else {
                                //如果全部都达到了下限 走该逻辑
                                try {
                                    //通过积分获取优选区域码
                                    MapPoint mapPoint = geo.getGeoInfo(address);//起点地址
                                    Map<String, Integer> map = new TreeMap<String, Integer>();
//						for (String siteId : areaCodeList) {
                                    //全是 大于下限 大于上限的情况
                                    if (areaCodeListLowerlimitUpper.size() > 0 && areaCodeListLowerlimitNoUpper.size() == 0) {
                                        areaCodeListnew = areaCodeListLowerlimitUpper;
                                    } else {
                                        areaCodeListnew = areaCodeListLowerlimitNoUpper;
                                    }
                                    for (String siteId : areaCodeListnew) {
                                        Site site = siteService.findSite(siteId);
                                        if (site != null) {
                                            long startTimegetDistance = System.currentTimeMillis();   //获取开始时间

                                            //获取当前位置到站点的距离，
                                            double length = GeoUtil.getDistance(mapPoint.getLng(), mapPoint.getLat(), Double.parseDouble(site.getLng()), Double.parseDouble(site.getLat())) * 1000;

                                            long endTimegetDistance = System.currentTimeMillis(); //获取结束时间

                                            logger.info("getDistance运行时间： " + (endTimegetDistance - startTimegetDistance) + "ms");

                                            long startTimegetIntegral = System.currentTimeMillis();   //获取开始时间
                                            //获取站点的日均积分
                                            Map<String, Object> result = userMysqlService.getIntegral(site.getAreaCode(), site.getUsername());

                                            long endTimegetIntegral = System.currentTimeMillis(); //获取结束时间

                                            logger.info("getIntegral运行时间： " + (endTimegetIntegral - startTimegetIntegral) + "ms");

                                            //int integral = userMysqlService.getIntegral("101010-016","17710174098");
                                            logger.info("匹配站点" + siteId + "获取积分：" + result.toString());
                                            int integral = 0;
                                            if (result.containsKey("totalscore")) {
                                                integral = (int) result.get("totalscore");
                                            }
                                            int integralVal = 0;
                                            //根据地址到站点的距离计算积分
                                            if (length < 3000) {
                                                integralVal = integral + 5;
                                                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
                                            } else if (length < 5000) {
                                                integralVal = integral + 3;
                                                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
                                            } else {
                                                integralVal = integral + 2;
                                                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
                                            }
                                            logger.info("地址：" + address + "匹配到的站点：" + siteId + "最终积分：" + integralVal);
                                            //保存站点和积分，按照积分进行排序
                                            map.put(siteId, integralVal);
                                        } else {
                                            map.put(siteId, 0);
                                        }
                                    }
                                    //这里将map.entrySet()转换成list
                                    List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
                                    //然后通过比较器来实现排序
                                    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        //降序排序
                                        public int compare(Map.Entry<String, Integer> o1,
                                                           Map.Entry<String, Integer> o2) {
                                            return o2.getValue().compareTo(o1.getValue());
                                        }
                                    });
                                    resultAreaCode = list.get(0).getKey();

                                    long endTimemapSX = System.currentTimeMillis(); //获取结束时间

                                    logger.info("加入上下限逻辑后运行时间： " + (endTimemapSX - startTimeSX) + "ms");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.info("[findBestSiteWithAddress] address:" + address + " exception");
                                    resultAreaCode = areaCodeList.get(0);
                                }
                            }
                        }
                    } else {
                        //通过积分获取优选区域码，暂时用第一个
                        resultAreaCode = areaCodeList.get(0);
                    }
                    logger.info("[findBestSiteWithAddress]request address:" + address + ", response siteId:" + resultAreaCode);
                    siteMysqlService.updateSiteDayCntBySiteId(resultAreaCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultAreaCode;

        //改动 测试V2
//		Result<Map<String,List<String>>> areaCodeList = sitePoiApi.searchSiteByAddressV2("",address);
//		logger.info("[findBestSiteWithAddress]request address:"+address+", response siteId List size:"+ areaCodeList.data.size());
//		String resultAreaCode = "";
//		if(areaCodeList!=null && areaCodeList.data.size()>0){
//			if(areaCodeList.data.get("siteIds").size()>0 && areaCodeList.data.get("containerIds").size()>0) {
//				try {
//					//通过积分获取优选区域码
//					MapPoint mapPoint = geo.getGeoInfo(address);//起点地址
//					Map<String, Integer> map = new TreeMap<String, Integer>();
//
//					for (String siteId : areaCodeList.data.get("siteIds")) {
//						ToScore(siteId,mapPoint,address,map);
//					}
//
//					//通过积分获取优选区域码
//					Map<String, Integer> map2 = new TreeMap<String, Integer>();
//					for (String siteId2 : areaCodeList.data.get("containerIds")) {
//						ToScore(siteId2,mapPoint,address,map2);
//					}
////合并2个map
//					map.putAll(map2);
//
//					//这里将map.entrySet()转换成list
//					List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
//					//然后通过比较器来实现排序
//					Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//						//降序排序
//						public int compare(Map.Entry<String, Integer> o1,
//										   Map.Entry<String, Integer> o2) {
//							return o2.getValue().compareTo(o1.getValue());
//						}
//					});
//					resultAreaCode = list.get(0).getKey();
//				}catch(Exception e){
//					e.printStackTrace();
//					logger.info("[findBestSiteWithAddress] address:"+address+" exception");
//					resultAreaCode = areaCodeList.data.get("siteIds").get(0);
//				}
//			}else{
//				//通过积分获取优选区域码，暂时用第一个
//				if(areaCodeList.data.get("siteIds").size()>0) {
//					resultAreaCode = areaCodeList.data.get("siteIds").get(0);
//				}
//				if(areaCodeList.data.get("containerIds").size()>0) {
//					resultAreaCode = areaCodeList.data.get("containerIds").get(0);
//				}
//			}
//		}
//		logger.info("[findBestSiteWithAddress]request address:"+address+", response siteId:"+resultAreaCode);
//		return resultAreaCode;
    }

    public void ToScore(String siteId, MapPoint mapPoint, String address, Map<String, Integer> map) {
        Site site = siteService.findSite(siteId);
        if (site != null) {
            //获取当前位置到站点的距离，
            double length = GeoUtil.getDistance(mapPoint.getLng(), mapPoint.getLat(), Double.parseDouble(site.getLng()), Double.parseDouble(site.getLat())) * 1000;
            //获取站点的日均积分
            Map<String, Object> result = userMysqlService.getIntegral(site.getAreaCode(), site.getUsername());
            //int integral = userMysqlService.getIntegral("101010-016","17710174098");
            logger.info("匹配站点" + siteId + "获取积分：" + result.toString());
            int integral = 0;
            if (result.containsKey("totalscore")) {
                integral = (int) result.get("totalscore");
            }
            int integralVal = 0;
            //根据地址到站点的距离计算积分
            if (length < 3000) {
                integralVal = integral + 5;
                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
            } else if (length < 5000) {
                integralVal = integral + 3;
                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
            } else {
                integralVal = integral + 2;
                logger.info("站点" + siteId + "增加距离" + length + "匹配积分后，积分由" + integral + "增加为" + integralVal);
            }
            logger.info("地址：" + address + "匹配到的站点：" + siteId + "最终积分：" + integralVal);
            //保存站点和积分，按照积分进行排序
            map.put(siteId, integralVal);
        } else {
            map.put(siteId, 0);
        }
    }

    @Override
    public Site getSiteWithAddress(String address) {
        try {
            //通过积分获取优选区域码，暂时用第一个
            String siteId = findBestSiteWithAddress(address);
            if (!"".equals(siteId)) {
                Site site = siteService.findSite(siteId);
                return site;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> findByDateAdd(Date dateAdd) {
        return orderDao.findByDateAdd(dateAdd);
    }

    @Override
    public String findWayNameBySite(Site site) {
        String wayName = "";
        if (site != null) {
            List<Way> wayList = wayService.findAllWayBySiteId(site.getId().toString());
            if (wayList != null && wayList.size() > 0) {
                Way way = wayList.get(0);
                wayName = way.name;
            }
        }
        return wayName;
    }

    @Override
    public Order findByOrderNoOrMailNum(String keyword) {
        return orderDao.findByOrderNoOrMailNum(keyword);
    }

    @Override
    public List<Order> findByAreaCodeAndMailNums(String areaCode, BasicDBList mailNumList) {
        if (mailNumList == null || mailNumList.size() == 0) {
            return null;
        }
        return orderDao.selectByAreaCodeAndMailNums(areaCode, mailNumList);
    }

    @Override
    public Map<String, MailStatisticVO> sumWithAreaCodesAndOrderStatus(String dateArrived, List<String> areaCodeList) {
        return orderDao.sumWithAreaCodesAndOrderStatus(dateArrived, areaCodeList);
    }

    @Override
    public MailStatisticVO sumWithAreaCodeAndOrderStatus(String dateArrived, String areaCode) {
        return orderDao.sumWithAreaCodeAndOrderStatus(dateArrived, areaCode);
    }

    @Override
    public MailStatisticVO findSummaryByAreaCodesAndTime(List<String> areaCodeList, String dateArrived) {
        return orderDao.selectSummaryByAreaCodesAndTime(areaCodeList, dateArrived);
    }


    /**
     * 获取一个站点下,未进行打包的订单集合
     *
     * @param areaCode
     * @return
     */
    @Override
    public List<Order> findNotDispatchOrdersWithAreaCode(String areaCode) {
        return orderDao.findNotDispatchOrdersWithAreaCode(areaCode);
    }

    @Override
    public PageModel<Order> findPageByAreaCodeAndExpressStatus(String areaCode, ExpressStatus expressStatus, Integer startNum, Integer pageSize) {
        return this.orderDao.selectPageByAreaCodeAndExpressStatus(areaCode, expressStatus, startNum, pageSize);
    }
}
