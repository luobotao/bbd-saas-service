package com.bbd.saas.controllers;

import com.bbd.drivers.api.mongo.OrderTrackService;
import com.bbd.drivers.enums.TransStatus;
import com.bbd.drivers.mongoModels.OrderTrack;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.*;
import com.bbd.saas.api.mysql.IncomeService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.*;
import com.bbd.saas.mongoModels.*;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 揽件入库
 * Created by huozhijie on 2016/6/25.
 */

/**
 * 揽件入库
 */
@Controller
@RequestMapping("/holdToStoreController")
@SessionAttributes("holdToStoreController")
public class HoldToStoreController {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    @Autowired
    TradeService tradeService;
    @Autowired
    SiteService siteService;
    @Autowired
    ExpressExchangeService expressExchangeService;
    @Autowired
    OrderParcelService orderPacelService;
    @Autowired
    OrderTrackService orderTrackService;
    @Autowired
    IncomeService incomeService;


    public static final Logger logger = LoggerFactory.getLogger(HoldToStoreController.class);

    /**
     * 跳转到揽件入库页面
     *
     * @param pageIndex //开始页
     * @param status    //状态
     * @param embraceId //拦件员id
     * @param request//
     * @param model//
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Integer pageIndex, @RequestParam(value = "status", defaultValue = "-1") String status, String embraceId, final HttpServletRequest request, Model model) {
        //当前登录的用户信息
        User user = adminService.get(UserSession.get(request));
        if (user != null) {
            //获取当前登录人的站点编码
            String areaCode = user.getSite().getAreaCode();
            //查询 今日成功揽件数量，今日入库，未入库，历史未入库数量
            OrderHoldToStoreNumVO orderHoldToStoreNum = orderService.getOrderHoldToStoreNum(areaCode);
            long historyToStoreNum = orderHoldToStoreNum.getHistoryToStoreNum();
            long todayNoToStoreNum = orderHoldToStoreNum.getTodayNoToStoreNum();
            long successOrderNum = orderHoldToStoreNum.getSuccessOrderNum();
            long todayToStoreNum = orderHoldToStoreNum.getTodayToStoreNum();
            //放在model 中传到页面
            model.addAttribute("historyToStoreNum", historyToStoreNum);
            model.addAttribute("todayNoToStoreNum", todayNoToStoreNum);
            model.addAttribute("successOrderNum", successOrderNum);
            model.addAttribute("todayToStoreNum", todayToStoreNum);


            //获取站长下的所有揽件员
            List<User> userList = userService.findUsersBySite(user.getSite(), UserRole.SENDMEM, UserStatus.VALID);

            try {
                //查询数据
                PageModel<OrderHoldToStoreVo> orderHoldPageModel = getList(pageIndex, status, embraceId, request, model);
                logger.info("=====揽件入库页面====" + orderHoldPageModel);
                model.addAttribute("orderHoldPageModel", orderHoldPageModel);
                model.addAttribute("userList", userList);
            } catch (Exception e) {
                logger.error("===揽件入库页面===出错:" + e.getMessage());
            }
        }
        return "page/holdToStore";
    }

    /**
     * 根据状态，揽件员id 查询
     *
     * @param pageIndex//初始页
     * @param orderSetStatus//状态
     * @param embraceId//揽件员id
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public PageModel<OrderHoldToStoreVo> getList(Integer pageIndex, String orderSetStatus, String embraceId, final HttpServletRequest request, Model model) {

        //今天的时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String start = Dates.formatSimpleDate(cal.getTime());
        String end = Dates.formatSimpleDate(cal.getTime());
        String between = start + " - " + end;

        //前台传来状态的集合
        List<OrderSetStatus> orderSetStatusList = new ArrayList<OrderSetStatus>();

        //把前台传来的状态，按逗号分隔并保存到orderSetStatusList 中
        if (!"-1".equals(orderSetStatus)) {
            if (orderSetStatus.contains(",")) {
                String orderSetStatusVal[] = orderSetStatus.split(",");
                for (int i = 0; i < orderSetStatusVal.length; i++) {
                    orderSetStatusList.add(OrderSetStatus.status2Obj(Integer.parseInt(orderSetStatusVal[i])));
                }
            } else {
                orderSetStatusList.add(OrderSetStatus.status2Obj(Integer.parseInt(orderSetStatus)));
            }
        }

        //首页进行默认值设置
        pageIndex = Numbers.defaultIfNull(pageIndex, 0);

        //当前登录的用户信息
        User user = adminService.get(UserSession.get(request));
        String areaCode = user.getSite().getAreaCode();

        //设置查询条件
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        orderQueryVO.between = between;
        orderQueryVO.areaCode = areaCode;

        //根据embraceId查询tradeNo ，每一个radeNo对应一个Order  封装到tradeNoList 中
        List<String> tradeNoList = new ArrayList<>();
        if (StringUtils.isNotBlank(embraceId) && !("0".equals(embraceId))) {
            List<Trade> tradeList = tradeService.findTradesByEmbraceId(embraceId);
            for (Trade trade : tradeList) {
                tradeNoList.add(trade.getTradeNo());
            }
        }
        //把以上条件依次传人到service 层中进行查询
        PageModel<OrderHoldToStoreVo> orderHoldPageModel = orderService.findPageOrdersForHoldToStore(pageIndex, tradeNoList, orderSetStatusList, orderQueryVO);

        return orderHoldPageModel;
    }

    /**
     * 根据运单号检查是否存在此订单
     * 根据相关条件做，到站，入库操作
     *
     * @param request
     * @param mailNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkHoldToStoreByMailNum", method = RequestMethod.GET)
    public Map<String, Object> checkHoldToStoreByMailNum(HttpServletRequest request, @RequestParam(value = "mailNum", required = true) String mailNum) {
        User user = adminService.get(UserSession.get(request));//当前登录的用户信息
        Map<String, Object> result = new HashMap<String, Object>();//封装返回参数
        String msg = "";
        Order order = orderService.findOneByMailNum(mailNum);//根据运单号查询
        Site site = siteService.findSiteByAreaCode(order.getAreaCode());//查询此订单中的站点对象

        boolean status = true;
        if (order == null) {//运单是否存在
            status = false;
            msg = "【异常扫描】不存在此运单号";
        } else if (order.getOrderSetStatus() != null && order.getOrderSetStatus() != OrderSetStatus.NOEMBRACE && order.getOrderSetStatus() != OrderSetStatus.SCANED && order.getOrderSetStatus() != OrderSetStatus.WAITTOIN) {//是否重复扫描
            status = false;
            msg = "重复扫描，此运单已经扫描过啦";
        } else if (user.getSite().getAreaCode().equals(order.getAreaCode())) {//运单号存在且属于此站
            //到站
            orderToSite(order, user);
            //入库
            doToStore(request, mailNum);
            status = true;
            msg = "扫描成功，完成⼊库。此订单属于您的站点，可直接进⾏【运单分派】操作";
        } else if ("1".equals(site.getType())) {//分拨站点
            //入库
            doToStore(request, mailNum);
            status = true;
            msg = "扫描成功，完成⼊库。请到App中进⾏【分拣】操作";

        } else if ("0".equals(site.getType())) {//不是分拨站点
            //入库
            doToStore(request, mailNum);
            status = true;
            msg = "扫描成功，完成⼊库。请到App中进⾏【揽件集包】操作";
        }

        result.put("msg", msg);
        result.put("status", status);
        return result;
    }

    //到站

    /**
     * 单个订单到站方法
     *
     * @param order
     * @param user
     */
    public void orderToSite(Order order, User user) {
        orderService.updateOrderOrderStatu(order.getMailNum(), OrderStatus.NOTARR, OrderStatus.NOTDISPATCH);//先更新订单本身状态同时会修改该订单所处包裹里的订单状态
        order = orderService.findOneByMailNum(user.getSite().getAreaCode(), order.getMailNum().toString());
        Express express = new Express();
        express.setDateAdd(new Date());
        express.setLat(user.getSite().getLat());
        express.setLon(user.getSite().getLng());
        express.setRemark("订单已送达【" + user.getSite().getName() + "】，正在分派配送员");
        List<Express> expressList = order.getExpresses();
        if (expressList == null)
            expressList = Lists.newArrayList();
        expressList.add(express);//增加一条物流信息
        order.setExpressStatus(ExpressStatus.ArriveStation);
        order.setExpresses(expressList);
        order.setDateUpd(new Date());
        orderService.save(order);

        if (null != order) {
            if (Srcs.DANGDANG.equals(order.getSrc()) || Srcs.PINHAOHUO.equals(order.getSrc())) {
                ExpressExchange expressExchange = new ExpressExchange();
                expressExchange.setOperator(user.getRealName());
                expressExchange.setStatus(ExpressExchangeStatus.waiting);
                expressExchange.setPhone(user.getLoginName());
                expressExchange.setOrder(order);
                expressExchange.setDateAdd(new Date());
                expressExchangeService.save(expressExchange);
            }
        }
        OrderParcel orderParcel = orderPacelService.findOrderParcelByOrderId(order.getId().toHexString());
        if (orderParcel != null) {
            Boolean flag = true;//是否可以更新包裹的状态
            for (Order orderTemp : orderParcel.getOrderList()) {
                if (orderTemp.getOrderStatus() == null || orderTemp.getOrderStatus() == OrderStatus.NOTARR) {
                    flag = false;
                }
            }
            if (flag) {//更新包裹状态，做包裹到站操作
                orderParcel.setStatus(ParcelStatus.ArriveStation);//包裹到站
                orderParcel.setDateUpd(new Date());
                orderPacelService.saveOrderParcel(orderParcel);
                /**修改orderTrack里的状态*/
                try {
                    String trackNo = orderParcel.getTrackNo();
                    if (StringUtils.isNotBlank(trackNo)) {
                        OrderTrack orderTrack = orderTrackService.findOneByTrackNo(trackNo);
                        if (orderTrack != null) {
                            List<OrderParcel> orderParcelList = orderPacelService.findOrderParcelListByTrackCode(trackNo);
                            Boolean flagForUpdateTrackNo = true;//是否可以更新orderTrack下的状态
                            for (OrderParcel orderParcel1 : orderParcelList) {
                                if (orderParcel1.getStatus() != ParcelStatus.ArriveStation) {
                                    flagForUpdateTrackNo = false;//不可更新
                                }
                            }
                            if (flagForUpdateTrackNo) {//可以更新orderTrack下的状态
                                orderTrack.dateUpd = new Date();
                                orderTrack.sendStatus = OrderTrack.SendStatus.ArriveStation;
                                orderTrack.transStatus = TransStatus.YWC;
                                orderTrack.preSchedule = "已送达";
                                orderTrackService.updateOrderTrack(trackNo, orderTrack);
                                incomeService.driverIncome(Numbers.parseInt(orderTrack.driverId, 0), orderTrack.actOrderPrice, orderTrack.trackNo);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * 入库
     *
     * @param request
     * @param mailNum //运单号
     */
    private void doToStore(HttpServletRequest request, String mailNum) {
        User user = adminService.get(UserSession.get(request));//当前登录的用户信息
        Order order = orderService.findOneByMailNum(mailNum);//根据运单号查询
        if (order != null) {
            //入库
            order.setOrderSetStatus(OrderSetStatus.WAITSET);
            orderService.save(order);
        }
    }

    /**
     * 重新计算揽件的数据统计
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateOrderHoldToStoreNumVO", method = RequestMethod.GET)
    public OrderHoldToStoreNumVO updateOrderHoldToStoreNumVO(HttpServletRequest request) {
        return orderService.getOrderHoldToStoreNum(adminService.get(UserSession.get(request)).getSite().getAreaCode());
    }

}
