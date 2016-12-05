package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.*;
import com.mongodb.BasicDBList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by luobotao on 2016/4/8.
 * 订单接口
 */
public interface OrderService {

    Order findOne(String id);


    /**
     * 根据订单号查询订单
     *
     * @param orderNO
     * @return
     */
    Order findByOrderNo(String orderNO);


    /**
     * 根据id查询运单
     *
     * @param id order._id
     * @return 运单信息
     */
    Order findOneById(String id);

    /**
     * 根据id查询运单
     *
     * @param id order._id
     * @return 运单信息
     */
    Order findOneByObjectId(ObjectId id);

    /**
     * 根据站点和运单号查询单个订单信息
     *
     * @param areaCode 站点编码（可为空，为空则所有站点）
     * @param mailNum  运单号
     * @return 订单
     * @author: liyanlei
     * 2016年4月12日下午3:38:38
     */
    Order findOneByMailNum(String areaCode, String mailNum);

	/**
	 * 根据其他快递的运单号查询订单数目
	 * @param newMailNum 新运单号
	 * @return 订单数目
	 */
	public long findCountByNewMailNum(String newMailNum);



    /**
     * 根据其他快递的运单号查询订单
     *
     * @param newMailNum
     * @return
     */
    List<Order> findOneByNewMailNum(String newMailNum);


    /**
     * 根据订单号查询订单
     *
     * @param orderNO
     * @return
     */
    Order findByOrderNoAndSrc(String orderNO, Srcs srcs);

    /**
     * 按照查询条件分页检索订单
     *
     * @param pageIndex    当前页
     * @param orderQueryVO 查询条件
     * @return 分页对象（分页信息和数据）
     * @author: liyanlei
     * 2016年4月15日下午1:30:21
     */
    PageModel<Order> findPageOrders(Integer pageIndex, OrderQueryVO orderQueryVO);


    /**
     * 带查询条件去检索订单
     *
     * @param pageModel    分页对象
     * @param orderQueryVO 查询条件
     * @return 分页对象（分页信息和数据）
     */
    PageModel<Order> findPageOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO);

    /**
     * 分页查询所有运单
     * @param pageModel
     * @return
     */
    PageModel<Order> findAllPageOrders(PageModel<Order> pageModel);


    /**
     * 保存或者更新订单
     *
     * @param order 订单对象
     * @return 保存返回结果
     */
    Key<Order> save(Order order);


    /**
     * 根据站点编码获取该站点订单数据
     *
     * @param areaCode 站点编码
     * @return 订单数据（今日未到站、历史未到站、今日已到站）
     */
    OrderNumVO getOrderNumVO(String areaCode);



    /**
     * 根据站点编码获取历史未到站订单数目||某天已到站
     *
     * @param areaCode    站点编号
     * @param dateArrived 到站日期
     * @return 订单条数
     */
    OrderNumVO findHistoryNoArrivedAndArrivedNums(String areaCode, String dateArrived);

    /**
     * 根据站点编码获取历史未到站订单数目||某天已到站
     *
     * @param areaCodeList 站点编号集合
     * @param dateArrived  到站日期
     * @return 订单条数
     */
    OrderNumVO findHistoryNoArrivedAndArrivedNums(List<String> areaCodeList, String dateArrived);


    /**
     * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
     *
     * @param mailNumList   mailNum集合
     * @param expressStatus 物流状态
     * @return 订单的条数
     */
    long getCounByMailNumsAndExpressStatus(BasicDBList mailNumList, ExpressStatus expressStatus);

    /**
     * 带查询条件去检索订单
     *
     * @param pageModel    分页对象
     * @param orderQueryVO 查询条件
     * @return 分页对象（分页信息和数据）
     */
    PageModel<Order> findOrders(PageModel<Order> pageModel, OrderQueryVO orderQueryVO);


    /**
     * 按照查询条件检索订单-不分页
     *
     * @param orderQueryVO 检索条件
     * @return 订单集合
     * @author: liyanlei
     * 2016年4月15日下午4:28:49
     */
    List<Order> findOrders(OrderQueryVO orderQueryVO);


    /**
     * 根据运单号更新订单的订单状态
     * 同时会修改该订单所处包裹里的状态
     *
     * @param mailNum        运单号
     * @param orderStatusOld 可为null,若为null则不检验旧状态否则须旧状态满足才可更新
     * @param orderStatusNew 订单新状态
     */
    void updateOrderOrderStatu(String mailNum, OrderStatus orderStatusOld, OrderStatus orderStatusNew);

    /**
     * 按给定条件更新指定字段 -- 暂时没有用到
     *
     * @param orderUpdateVO 待更新字段和值
     * @param orderQueryVO  检索条件
     * @return 更新的记录数
     * @author: liyanlei
     * 2016年4月16日下午1:23:37
     */
    int updateOrder(OrderUpdateVO orderUpdateVO, OrderQueryVO orderQueryVO);

    /**
     * 根据站点编码和到站时间获取该站点已分派的订单数 -- 暂时没有用到（接口实现还有问题）
     *
     * @param areaCode    站点编号
     * @param betweenTime 查询时间范围
     * @return 订单条数
     */
    long getDispatchedNums(String areaCode, String betweenTime);

    /**
     * 得到指定站点当天(昨天)更新的所有订单 -- 把物流信息同步到mysql中时用到
     *
     * @param areaCode 站点编号
     * @return 订单集合
     */
    List<Order> getTodayUpdateOrdersBySite(String areaCode);


    String updateParcelWithOrder(Order order);

    List<Order> findByDateAdd(Date date);

    String findWayNameBySite(Site site);

    /**
     * 根据orderNo或者mailNum查询订单
     *
     * @param keyword
     * @return
     */
    Order findByOrderNoOrMailNum(String keyword);

    /**
     * 根据areaCode和mailNumList查询
     *
     * @param areaCode
     * @param mailNumList
     * @return
     */
    List<Order> findByAreaCodeAndMailNums(String areaCode, BasicDBList mailNumList);


    /**
     * 根据站点和状态分组统计(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数) -- 多个站点
     *
     * @param dateArrived  到站时间
     * @param areaCodeList 站点编号集合
     * @return Map<areaCode, MailStatisticVO>
     */
    Map<String, MailStatisticVO> sumWithAreaCodesAndOrderStatus(String dateArrived, List<String> areaCodeList);

    /**
     * 根据站点和状态分组统计(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数)--单个站点
     *
     * @param dateArrived 到站时间
     * @param areaCode    站点编号
     * @return MailStatisticVO
     */
    MailStatisticVO sumWithAreaCodeAndOrderStatus(String dateArrived, String areaCode);

    /**
     * 根据站点编号集合和时间查询各个站点的不同状态的运单的汇总信息(缺少历史未到站 && 已到站订单数 && 转其他站点的订单数)
     *
     * @param areaCodeList 站点编号集合
     * @param dateArrived  到站时间
     * @return 不同状态的运单的汇总信息
     */
    MailStatisticVO findSummaryByAreaCodesAndTime(List<String> areaCodeList, String dateArrived);

    /**
     * 获取一个站点下,未进行打包的订单集合
     *
     * @param areaCode 站点编号
     * @return 订单集合
     */
    List<Order> findNotDispatchOrdersWithAreaCode(String areaCode);

    /**
     * 根据站点编号和物流状态分页查询
     *
     * @param areaCode      站点编号
     * @param expressStatus 物流状态
     * @param startNum      跳过的条数
     * @param pageSize      查询的条数
     * @return 分页数据
     */
    PageModel<Order> findPageByAreaCodeAndExpressStatus(String areaCode, ExpressStatus expressStatus, Integer startNum, Integer pageSize);


    /**
     * 分页查询运单号/手机号/姓名/地址四个字段中包含关键字（keyword）的运单
     *
     * @param pageModel 分页对象
     * @param tradeNo   订单号
     * @param uId       发件人
     * @param keyword   搜索关键字
     * @return 分页对象（分页信息和数据）
     */
    PageModel<Order> findPageOrders(PageModel<Order> pageModel, String tradeNo, ObjectId uId, String keyword);

    /**
     * 分页查询运单号/手机号/姓名/地址四个字段中包含关键字（keyword）的运单
     * @param pageIndex 当前页
     * @param tradeNo 订单号
     * @param uId 发件人
     * @param keyword 搜索关键字
     * @return 分页对象（分页信息和数据）
     */
    public PageModel<Order> findPageOrders(Integer pageIndex, String tradeNo, ObjectId uId, String keyword);
    /**
     * 根据商品订单号查询该订单下包含的运单数目
     *
     * @param tradeNo //商户订单号(我们自己生成的支付订单号)
     * @return 订单下包含的运单数目
     */
    long findCountByTradeNo(String tradeNo, Integer removeStatus);


    /**
     * 订单导入完成，生成区域码和运单号
     *
     * @param order
     * @return
     */
    Order afterImportDealWithOrder(Order order);

    Order reduceMailNumWithOrder(Order order);

    Order reduceAreaCodeWithOrder(Order order);


    List<Order> findAllByTradeNo(String tradeNo);

    /**
     * 揽件入库
     * 根据相关条件查询出所有揽件入库的订单
     *
     * @param pageIndex
     * @param orderQueryVO
     * @return
     */
    PageModel<OrderHoldToStoreVo> findPageOrdersForHoldToStore(Integer pageIndex, OrderQueryVO orderQueryVO);

    /**
     * 揽件入库
     * 根据站点下的用户列表获取该站点 揽件的订单数量
     *
     * @param user 当前用户
     * @return
     */
    OrderHoldToStoreNumVO getOrderHoldToStoreNum(User user);

    /**
     * 揽件入库
     * 根据运单号查询
     *
     * @param mailNum
     * @return
     */
    Order findOneByMailNum(String mailNum);

    /**
     * 查询指定mailNum集合的订单中物流状态不为expressStatus的订单的条数
     *
     * @param mailNumList     mailNum集合
     * @param orderStatusList 订单状态集合
     * @return 订单的条数
     */
    long getCounByMailNumsAndOrderStatusList(BasicDBList mailNumList, List<OrderStatus> orderStatusList);


    List<String> reduceMailNum(String quantity);

    Site getSiteWithAddress(String address);

    /**
     * 此商户订单号下的所有已入库的运单
     *
     * @param tradeNo
     * @return
     */
    long findArrCountByTradeNo(String tradeNo);


    /**
     * 根据物流状态查询订单
     *
     * @param pageModel 分页信息
     * @param remark    物流remark包含关键词
     * @param startDate
     * @param endDate
     * @return
     */
    PageModel<Order> findPageOrdersByExpress(PageModel<Order> pageModel, String remark, String startDate, String endDate);

    String findBestSiteWithAddress(String address);


    /**
     * 根据运单号模糊查询
     *
     * @param mailNum 运单号
     * @return 运单
     */
    Order findOneByMailNumLike(String mailNum);
	/**
	 * 根据查询条件分页查询数据
	 * @param appOrderQueryVO 查询条件
	 * @param startNum 跳过的条数
	 * @param pageSize 查询的条数
     * @return 分页数据
     */
	public PageModel<Order> findPageByAppOrderQuery(AppOrderQueryVO appOrderQueryVO, String orderStr, Integer startNum, Integer pageSize) throws Exception;

	/**
	 * 根据查询条件查询数据
	 * @param appOrderQueryVO 查询条件
	 * @return 符合条件的数据
     */
	public List<Order> findListByAppOrderQuery(AppOrderQueryVO appOrderQueryVO, String orderStr) throws Exception;

	/**
	 * 查询符合条件的数据的条数
	 * @param appOrderQueryVO 查询条件
	 * @return 符合条件的数据的条数
	 */
	public long findCountByAppOrderQuery(AppOrderQueryVO appOrderQueryVO) throws Exception;

    public List<Order> findOrdersByAreaCodeAndExpressStatusAndDateAdd(List<String> areaCodes,List<ExpressStatus> expressStatuses,Date startDate,Date endDate,String dateTyp);

    /**
     * 根据运单号集合查询运单集合
     * @param mailNums 运单号集合
     * @return  运单集合
     * @throws Exception
     */
    public List<Order> findListByMailNums(String [] mailNums) throws Exception;

    /**
     * 根据运单号集合查询运单集合 -- 没有用到
     * @param pageModel 分页信息
     * @param mailNums 运单号集合
     * @return 分页信息和当前页的数据
     * @throws Exception
     */
    public PageModel<Order> findPageByMailNums(PageModel pageModel, String [] mailNums) throws Exception;

    public List<Order> findOrdersByAreaCodeAndExpressStatusAndExchangeFlag(List<String> areaCodes,List<ExpressStatus> expressStatuses,String exchangeFlag);

    public List<Order> findListByParentCode(String parentCode);
}
