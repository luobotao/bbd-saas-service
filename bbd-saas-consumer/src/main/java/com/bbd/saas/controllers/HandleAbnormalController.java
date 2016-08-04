package com.bbd.saas.controllers;

import com.alibaba.dubbo.common.json.JSON;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.*;
import com.bbd.saas.api.mysql.ExpressCompanyService;
import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.api.mysql.SmsInfoService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.models.ExpressCompany;
import com.bbd.saas.models.PostDelivery;
import com.bbd.saas.mongoModels.*;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.*;
import com.google.common.collect.Lists;
import flexjson.JSONSerializer;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

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
    ReturnReasonService returnReasonService;
    @Autowired
    ToOtherSiteLogService toOtherSiteLogService;
    @Autowired
    PostDeliveryService postDeliveryService;

    @Autowired
    ExpressCompanyService expressCompanyService;
    @Autowired
    SmsInfoService smsInfoService;

    @Value("${bbd.contact}")
    private String contact;
    @Value("${EPREE100_KEY}")
    private String EPREE100_KEY ;
    @Value("${EPREE100_CALLBACK_URL}")
    private String EPREE100_CALLBACK_URL ;
    @Value("${EPREE100_URL}")
    private String EPREE100_URL;

    /**
     * description: 跳转到异常件处理页面
     * 2016年4月1日下午6:13:46
     *
     * @param model
     * @return
     * @author: liyanlei
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request, Model model) {
        try {
            //设置默认查询条件
            status = Numbers.defaultIfNull(status, -1);//全部，滞留和拒收
            //到站时间前天、昨天和今天
            arriveBetween = StringUtil.initStr(arriveBetween, Dates.getBetweenTime(new Date(), -10));
            //查询数据
            PageModel<Order> orderPage = getList(pageIndex, status, arriveBetween, request);
            logger.info("=====异常件处理页面列表====" + orderPage);
            model.addAttribute("orderPage", orderPage);
            model.addAttribute("arriveBetween", arriveBetween);
        } catch (Exception e) {
            logger.error("===跳转到异常件处理页面===出错:" + e.getMessage());
        }
        return "page/handleAbnormal";
    }

    //分页Ajax更新
    @ResponseBody
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public PageModel<Order> getList(Integer pageIndex, Integer status, String arriveBetween, final HttpServletRequest request) {
        //查询数据
        PageModel<Order> orderPage = null;
        try {
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
            orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
            //查询派件员姓名电话
            if (orderPage != null && orderPage.getDatas() != null) {
                List<Order> dataList = orderPage.getDatas();
                for (Order order : dataList) {
                    User courier = userService.findOne(order.getUserId());
                    if (courier != null) {
                        UserVO userVO = new UserVO();
                        userVO.setLoginName(courier.getLoginName());
                        userVO.setRealName(courier.getRealName());
                        order.setUserVO(userVO);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("===分页Ajax更新列表数据===出错:" + e.getMessage());
        }
        return orderPage;
    }
    /**************************重新分派***************开始***********************************/
    /**
     * Description: 获取本站点下的所有状态为有效的派件员
     *
     * @param request
     * @return
     * @author: liyanlei
     * 2016年4月15日上午11:06:19
     */
    @ResponseBody
    @RequestMapping(value = "/getAllUserList", method = RequestMethod.GET)
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
     * Description: 运单重新分派--把异常的包裹重新分派给派件员
     *
     * @param mailNum       运单号
     * @param userId        派件员员工Id
     * @param status        更新列表参数--状态
     * @param pageIndex     更新列表参数--页数
     * @param arriveBetween 更新列表参数--到站时间
     * @param request
     * @return
     * @author: liyanlei
     * 2016年4月18日上午11:41:34
     */
    @ResponseBody
    @RequestMapping(value = "/reDispatch", method = RequestMethod.GET)
    public Map<String, Object> reDispatch(String mailNum, String userId, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<String, Object>();
            //当前登录的用户信息
            User currUser = adminService.get(UserSession.get(request));
            //查询运单信息
            Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
            if (order == null) {//运单不存在,与站点无关--正常情况不会执行
                map.put("operFlag", 0);//0:运单号不存在
            } else {//运单存在
                //查询派件员
                User courier = userService.findOne(userId);
                order.setUserId(userId);
                order.setOrderStatus(OrderStatus.DISPATCHED);//更新运单状态--已分派
                //更新物流信息
                order.setExpressStatus(ExpressStatus.Delivering);
                //添加一条物流信息
                List<Express> expressList = order.getExpresses();
                if (expressList == null) {
                    expressList = new ArrayList<Express>();
                }
                Express express = new Express();
                express.setDateAdd(new Date());
                /*if (new Date().getHours() < 19) {
                    express.setRemark("配送员正在为您重新派件，预计3小时内送达，请注意查收。配送员电话：" + courier.getRealName() + " " + courier.getLoginName());
                } else {
                    express.setRemark("配送员正在为您重新派件，预计明天12:00前送达，请注意查收。配送员电话：" + courier.getRealName() + " " + courier.getLoginName());
                }*/
                express.setRemark("配送员正在为您重新派件，配送员电话：" + courier.getRealName() + " " + courier.getLoginName());
                smsInfoService.sendToSending(order.getSrc().getMessage(),order.getMailNum(),courier.getRealName(),courier.getLoginName(),contact,order.getReciever().getPhone());
                express.setLat(currUser.getSite().getLat());//站点经纬度
                express.setLon(currUser.getSite().getLng());
                expressList.add(express);
                order.setExpresses(expressList);
                //更新修改时间
                order.setDateUpd(new Date());
                //更新运单
                Key<Order> r = orderService.save(order);
                if (r != null) {
                    //更新mysql
                    saveOneOrUpdatePost(order,  courier);
                    map.put("operFlag", 1);//1:分派成功
                    //刷新列表
                    map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                } else {
                    map.put("operFlag", 0);//0:分派失败
                }
            }
        } catch (Exception e) {
            logger.error("===运单重新分派===出错:" + e.getMessage());
        }
        return map;
    }

    /**
     * Description: 运单号不存在，则添加一条记录；存在，则更新派件员postManId和staffId
     * @param order
     * @param user
     * @author: liyanlei
     * 2016年4月21日上午10:37:30
     */
    public  void saveOneOrUpdatePost(Order order, User user){
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
            postDelivery.setReceiver_address(order.getReciever().getAddress());
            postDelivery.setReceiver_company_name("");
            postDelivery.setReceiver_name(order.getReciever().getName());
            postDelivery.setReceiver_phone(order.getReciever().getPhone());
            postDelivery.setReceiver_province(order.getReciever().getProvince());
            postDelivery.setSender_company_name(order.getSrc().getMessage());
            Sender sender = order.getSender();
            if(sender!=null){
                postDelivery.setSender_address(order.getSender().getAddress());
                postDelivery.setSender_city(order.getSender().getCity());
                postDelivery.setSender_name(order.getSender().getName());
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
     * Description: 查询列表数据--刷新列表
     *
     * @param areaCode      站点编号
     * @param status        状态
     * @param pageIndex     页码
     * @param arriveBetween 到站时间
     * @return
     * @author: liyanlei
     * 2016年4月18日上午11:58:26
     */
    PageModel<Order> getPageData(String areaCode, Integer status, Integer pageIndex, String arriveBetween) {
        //刷新列表==设置查询条件
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        orderQueryVO.abnormalStatus = Numbers.defaultIfNull(status, -1);
        orderQueryVO.arriveBetween = arriveBetween;
        orderQueryVO.areaCode = areaCode;
        //查询数据
        PageModel<Order> orderPage = orderService.findPageOrders(pageIndex, orderQueryVO);
        //查询派件员姓名电话
        if (orderPage != null && orderPage.getDatas() != null) {
            List<Order> dataList = orderPage.getDatas();
            User courier = null;
            for (Order order : dataList) {
                courier = userService.findOne(order.getUserId());
                UserVO userVO = new UserVO();
                if(courier!=null){
                    userVO.setLoginName(courier.getLoginName());
                    userVO.setRealName(courier.getRealName());
                }

                order.setUserVO(userVO);
            }
        }
        return orderPage;
    }
    /**************************重新分派***************结束***********************************/

    /**************************转其他站点***************开始***********************************/
    /**
     * Description: 查询除本站点外的其他所有站点的VO对象
     *
     * @param request
     * @return
     * @author: liyanlei
     * 2016年4月18日上午10:32:14
     */
    @ResponseBody
    @RequestMapping(value = "/getAllOtherSiteList", method = RequestMethod.GET)
    public List<SiteVO> getAllSiteList(final HttpServletRequest request) {
        try {
            //当前登录的用户信息
            User user = adminService.get(UserSession.get(request));
            return siteService.findAllOtherSiteVOList(user.getSite());
        } catch (Exception e) {
            logger.error("===查询除本站点外的其他所有站点的VO对象===出错:" + e.getMessage());
        }
        return null;
    }

    /**
     * Description: 转其他站点
     *
     * @param mailNum       运单号
     * @param siteId        站点号
     * @param status        更新列表参数--状态
     * @param pageIndex     更新列表参数--页码
     * @param arriveBetween 更新列表参数--到站时间
     * @param request
     * @return
     * @author: liyanlei
     * 2016年4月18日上午11:44:27
     */
    @ResponseBody
    @RequestMapping(value = "/toOtherSite", method = RequestMethod.GET)
    public Map<String, Object> toOtherSite(String mailNum, String siteId, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<String, Object>();
            //当前登录的用户信息
            User currUser = adminService.get(UserSession.get(request));
            //查询运单信息
            Order order = orderService.findOneByMailNum(currUser.getSite().getAreaCode(), mailNum);
            if (order == null) {//运单不存在,与站点无关--正常情况不会执行
                map.put("operFlag", 0);//0:运单号不存在
            } else {//运单存在
                String fromAreaCode = order.getAreaCode();
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
                order.setOrderStatus(OrderStatus.NOTARR);//状态--为未到站
                order.setUserId("");//未分派
                order.setDateUpd(new Date());//更新时间
                //更新物流信息
                //订单已由【A站点】出库，正在转送到【B站点】进行配送
                String expRemark = "订单已由【" + currUser.getSite().getName() + "】出库，正在转送到【" + order.getAreaName() + "】进行配送。";
                OrderCommon.addOrderExpress(ExpressStatus.Packed, order, currUser, expRemark);
                //更新预计到站时间
                order.setDateMayArrive(Dates.addDays(new Date(), 1));
                order.setDateArrived(null);
                //更新运单
                Key<Order> r = orderService.save(order);
                if (r != null) {
                    //记录转站日志
                    ToOtherSiteLog toOtherSiteLog = new ToOtherSiteLog();
                    toOtherSiteLog.setMailNum(order.getMailNum());
                    toOtherSiteLog.setFromAreaCode(fromAreaCode);
                    toOtherSiteLog.setToAreaCode(site.getAreaCode());
                    toOtherSiteLog.setOperTime(new Date());
                    toOtherSiteLogService.save(toOtherSiteLog);
                    //更新到mysql 删除一条记录
                    postDeliveryService.deleteByMailNum(mailNum);
                    map.put("operFlag", 1);//1:成功
                    //刷新列表
                    map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                } else {
                    map.put("operFlag", 0);//0:失败
                }
            }
        } catch (Exception e) {
            logger.error("===转其他站点===出错:" + e.getMessage());
        }
        return map;

    }


    /**************************转其他站点***************结束***********************************/
    /**************************申请退货***************开始***********************************/

    /**
     * 查询所有的退货原因
     * @param request
     * @return
     * @author: liyanlei
     * 2016年4月18日上午10:32:14
     */
    @ResponseBody
    @RequestMapping(value="/getRtnReasonList", method=RequestMethod.GET)
    public List<ReturnReason> getRtnReasonList(final HttpServletRequest request) {
        try {
            //当前登录的用户信息
            User user = adminService.get(UserSession.get(request));
            return returnReasonService.findAll();
        } catch (Exception e) {
            logger.error("===查询所有退货原因===出错:" + e.getMessage());
        }
        return null;
    }

    /**
     * 退货
     * @param mailNum 运单号
     * @param rtnReason 退货原因
     * @param rtnRemark 其他原因--原因详情
     * @param status 状态
     * @param pageIndex 当前页
     * @param arriveBetween 到站时间
     * @param request 请求
     * @return  operFlag=1，orderPage = 当前页的数据； operFlag=0
     */
    @ResponseBody
    @RequestMapping(value="/doReturn", method=RequestMethod.POST)
    public Map<String, Object> dispatch(String mailNum, Integer rtnReason, String rtnRemark, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //查询运单信息
            Order order = orderService.findOneByMailNum("", mailNum);
            if(order != null){
                ReturnReason reason = returnReasonService.findOneByStatus(rtnReason);
                //当前登录的用户信息
                User currUser = adminService.get(UserSession.get(request));
                order.setRtnReason(reason.getMessage());//退货原因
                order.setRtnRemark(rtnRemark);//退货备注
                order.setDateAplyRtn(new Date());//申请退货时间
                order.setOrderStatus(OrderStatus.APPLY_RETURN);//状态
                //更新物流信息
                StringBuffer expRemark = new StringBuffer("订单已申请退货，退货原因：") ;
                if(rtnReason == 4 ){//其他
                    expRemark.append(rtnRemark);
                }else {
                    expRemark.append(reason.getMessage());
                    if(StringUtil.isNotEmpty(rtnRemark)){
                        expRemark.append("，");
                        expRemark.append(rtnRemark);
                    }
                }
                OrderCommon.addOrderExpress(ExpressStatus.APPLY_RETURN, order, currUser, expRemark.toString());
                //更新运单
                Key<Order> r = orderService.save(order);
                if(r != null){
                    map.put("success", true);//成功
                    map.put("msg", "申请退货成功");//0
                    //刷新列表
                    map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                }else{
                    map.put("success", false);//失败
                    map.put("msg", "申请退货失败，请稍候再试");
                }
            }else{
                map.put("success", false);//0:运单号不存在
                map.put("msg", "运单不存在");//0
            }
        } catch (Exception e) {
            logger.error("===退货===出错:" + e.getMessage());
        }
        return map;
    }
    /**************************申请退货***************结束***********************************/


    /**************************
     * 转为其他快递   得到所有的快递公司***************开始
     ***********************************/
    @ResponseBody
    @RequestMapping(value = "/getExpressCompanys", method = RequestMethod.GET)
    public List<ExpressCompany> getExpressCompanys() {
        List<ExpressCompany> expressCompanys = expressCompanyService.getExpressCompany();
        if (expressCompanys != null && expressCompanys.size() != 0) {
            return expressCompanys;
        } else {
            return null;
        }

    }

    /**
     * 转其他快递
     * @param mailNum 原有的运单号
     * @param companyId 要转到的快递公司Id
     * @param mailNumNew 新运单号
     * @param status 分页查询-运单状态
     * @param pageIndex 分页查询-当前页数
     * @param arriveBetween 分页查询-到站时间范围
     * @param request 请求
     * @return success：true|false（成功|失败）; msg: 信息详情
     */
    @ResponseBody
    @RequestMapping(value = "/toOtherExpressCompanys", method = RequestMethod.POST)
    public Map<String, Object> toOtherExpressCompanys(String mailNum, String companyId, String mailNumNew, Integer status, Integer pageIndex, String arriveBetween, final HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //当前登录的用户信息
            User currUser = adminService.get(UserSession.get(request));
            //查询运单信息
            Order order = updExpressForToOtherCmp(companyId, mailNum, mailNumNew, currUser);
            Sender sender = order.getSender();
            Reciever reciever = order.getReciever();
            StringBuffer fromSB = new StringBuffer();
            StringBuffer toSB = new StringBuffer();
            if (sender != null){
                fromSB.append(sender.getProvince());
                fromSB.append(sender.getCity());
                fromSB.append(sender.getArea());
                fromSB.append(sender.getAddress());
            }
            if (reciever != null){
                toSB.append(reciever.getProvince());
                toSB.append(reciever.getCity());
                toSB.append(reciever.getArea());
                toSB.append(reciever.getAddress());
            }
            ResultResposeDTO resposeDTO = goTo100Subscribe("", "", companyId, mailNum,
                    fromSB.toString(), toSB.toString(), mailNumNew);
            if(resposeDTO != null){
                String message=   resposeDTO.getMessage();
                /*if(message.contains("重复订阅")) {//失败
                    map.put("success", false);
                    map.put("msg", "重复订阅");
                    logger.info("= 转其他快递==重复订阅==="  );
                }else{//成功
                    //更新运单
                    Key<Order> r = orderService.save(order);
                    if(r != null){
                        map.put("success", true);//成功
                        map.put("msg", "转运信息添加成功");//0
                        //刷新列表
                        map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                    }else{
                        map.put("success", false);//失败
                        map.put("msg", "转运信息添加失败，请稍候再试");
                    }
                }*/
                //更新运单
                Key<Order> r = orderService.save(order);
                if(r != null){
                    map.put("success", true);//成功
                    map.put("msg", "转运信息添加成功");//0
                    //刷新列表
                    map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                }else{
                    map.put("success", false);//失败
                    map.put("msg", "转运信息添加失败，请稍候再试");
                }
            }else{
                map.put("success", false);//0:运单号不存在
                map.put("msg", "转运信息添加失败，请稍候再试");//0
            }
        } catch (Exception e) {
            logger.error("===转其他快递操作===出错:" + e.getMessage());
        }
        return map;
    }

    /**
     * 转其他快递物流信息
     * @param companyId 要转到的快递公司的Id
     * @param mailNum
     * @param mailNumNew
     * @return
     */
    private Order updExpressForToOtherCmp(String companyId, String mailNum, String mailNumNew, User currUser){
        String companyName = null;
        String companyCode = null;
        Order order = null;
        if (StringUtils.isNoneBlank(companyId)) {
            ExpressCompany expressCompany = expressCompanyService.getExpressCompanyById(Integer.valueOf(companyId));
            if (null != expressCompany) {
                companyName = expressCompany.getCompanyname();
                companyCode = expressCompany.getCompanycode();
            }
        }
        if (StringUtils.isNotBlank(mailNum)) {
            order = orderService.findOneByMailNum("", mailNum);
            if (order != null) {
                //更新状态
                order.setOrderStatus(OrderStatus.TO_OTHER_EXPRESS);
                //更新getOtherExprees字段
                List<OtherExpreeVO> otherExpressList = order.getOtherExprees();
                if (otherExpressList == null || otherExpressList.isEmpty()) {
                    otherExpressList = Lists.newArrayList();
                }
                OtherExpreeVO otherExpreeVO = new OtherExpreeVO();
                if (StringUtils.isNotBlank(mailNumNew)) {
                    otherExpreeVO.setMailNum(mailNumNew);
                }
                otherExpreeVO.setCompanyname(companyName);
                StringBuffer expRemark = new StringBuffer("订单超出站点配送范围，已转发【") ;
                expRemark.append(companyName);
                expRemark.append("】，快递单号：");
                expRemark.append(mailNumNew);
                otherExpreeVO.setContext(expRemark.toString());
                otherExpreeVO.setCompanycode(companyCode);
                otherExpreeVO.setDateUpd(new Date());
                otherExpressList.add(otherExpreeVO);
                order.setOtherExprees(otherExpressList);
                order.setDateUpd(new Date());
                //更新expresses字段
                OrderCommon.addOrderExpress(ExpressStatus.TO_OTHER_EXPRESS, order, currUser, expRemark.toString());
            }
        }
        return order;

    }

    /**
     *    向快递100接口 订阅
     * @param salt 签名用随机字符串（可选）
     * @param resultv2  添加此字段表示开通行政区域解析功能
     * @param companyId 其他快递公司的id
     * @param mailNum  运单号
     * @param from  运单寄出地址
     * @param to      运单签收地址
     * @param mailNumNew 新添的运单号
     * @return
     */
    public ResultResposeDTO goTo100Subscribe(String salt, String resultv2, String companyId, String mailNum,
            String from, String to, String mailNumNew) {

        logger.info("向100快递 ------订阅");
        String epree100Resultv2 = "";
        String epree100_salt = "";
        String companyName = null;//其他快递公司的名字
        String companyCode = null;//其他快递公司的编码

        if (StringUtils.isNoneBlank(resultv2)) {
            epree100Resultv2 = resultv2;
        }

        if (StringUtils.isNoneBlank(epree100_salt)) {
            epree100Resultv2 = salt;
        }
        //根据快递公司的id查询出其他快递公司,为companyNmame,companyCode 赋值
        if (StringUtils.isNoneBlank(companyId)) {
            ExpressCompany expressCompany = expressCompanyService.getExpressCompanyById(Integer.valueOf(companyId));
            if (null != expressCompany) {
                companyName = expressCompany.getCompanyname();
                companyCode = expressCompany.getCompanycode();
            }
        }
           //为快递100 订阅 填充参数数据

        Map<String, String> requestVO = new HashMap<>();//创建requestVo,用于封装订阅 快递100 的单个对象
        Expree100BodyVO expree100BodyVO = new Expree100BodyVO();//用于封装requestVO对象，封装expree100ParametersVO对象
        Expree100ParametersVO expree100ParametersVO = new Expree100ParametersVO();//用于封装parameters 对象

       //填充expree100ParametersVO对象
        expree100ParametersVO.setCallbackurl(EPREE100_CALLBACK_URL);
        expree100ParametersVO.setSalt(epree100Resultv2);
        expree100ParametersVO.setResultv2(epree100Resultv2);

        //填充expree100BodyVO对象
        expree100BodyVO.setCompany(companyCode);

        if (StringUtils.isNotBlank(mailNumNew)) {
            expree100BodyVO.setNumber(mailNumNew);
        }
        if (StringUtils.isNotBlank(from)) {
            expree100BodyVO.setFrom(from);
        }
        if (StringUtils.isNotBlank(to)) {
            expree100BodyVO.setTo(to);
        }
        expree100BodyVO.setKey(EPREE100_KEY);
        expree100BodyVO.setParameters(expree100ParametersVO);

        //填充requestVO对象
        JSONSerializer jsonSerializer = new JSONSerializer();
        requestVO.put("param", jsonSerializer.serialize(expree100BodyVO));
        requestVO.put("schema", "json/xml");


        ResultResposeDTO resultResposeDTO = null;//封装httpClient 的返回结果对象
        String resultResposeDTOstr = null;//封装httpClient 的返回结果字符串

        try {
            //向快递100发送订阅信息
            resultResposeDTOstr = HttpClientUtil.sendHttpPost(EPREE100_URL, requestVO);
            //填充返回结果信息
            if (StringUtils.isNoneBlank(resultResposeDTOstr)) {
                resultResposeDTO = JSON.parse(resultResposeDTOstr, ResultResposeDTO.class);
                if (null != resultResposeDTO) {
                    String message=   resultResposeDTO.getMessage();
                    if(message.contains("重复订阅")){
                        resultResposeDTO.setMessage("重复订阅");
                        resultResposeDTO.setResult(false);
                        resultResposeDTO.setReturnCode(500);
                        logger.info("------------重复订阅--------" );
                    }
                    return resultResposeDTO;

                }
            }
        }catch (Exception e) {

           e.printStackTrace();
            logger.info("[login error]"+e.getMessage());
        }
        return resultResposeDTO;
    }



}
