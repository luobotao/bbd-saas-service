package com.bbd.saas.controllers;

import com.alibaba.dubbo.common.json.JSON;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.*;
import com.bbd.saas.api.mysql.ExpressCompanyService;
import com.bbd.saas.api.mysql.PostDeliveryService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.models.ExpressCompany;
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
                if (new Date().getHours() < 19) {
                    express.setRemark("配送员正在为您重新派件，预计3小时内送达，请注意查收。配送员电话：" + courier.getRealName() + " " + courier.getLoginName());
                } else {
                    express.setRemark("配送员正在为您重新派件，预计明天12:00前送达，请注意查收。配送员电话：" + courier.getRealName() + " " + courier.getLoginName());
                }
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
                    postDeliveryService.updatePostAndStatusAndCompany(mailNum, courier.getPostmanuserId(), courier.getStaffid(), "1", null);
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
                userVO.setLoginName(courier.getLoginName());
                userVO.setRealName(courier.getRealName());
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
                order.setOrderStatus(null);//状态--为空
                order.setUserId("");//未分派
                order.setDateUpd(new Date());//更新时间
                //更新物流信息
                //订单已由【A站点】出库，正在转送到【B站点】进行配送
                String expRemark = "订单已由【" + currUser.getSite().getName() + "】出库，正在转送到【" + order.getAreaName() + "】进行配送。";
                addOrderExpress(ExpressStatus.Packed, order, currUser, expRemark);
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

    /**
     * 增加订单物流信息
     * @param expressStatus 物流状态
     * @param order 订单
     * @param user 当前用户
     * @param remark 物流信息
     */
    private void addOrderExpress(ExpressStatus expressStatus,Order order, User user, String remark){
        //更新物流状态
        order.setExpressStatus(expressStatus);
        //更新物流信息
        List<Express> expressList = order.getExpresses();
        if(expressList == null){
            expressList = new ArrayList<Express>();
        }
        Express express = new Express();
        express.setDateAdd(new Date());
        express.setRemark(remark);
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
                if(rtnReason.equals("4")){//其他
                    expRemark.append(rtnRemark);
                }else {
                    expRemark.append(reason.getMessage());
                    if(StringUtil.isNotEmpty(rtnRemark)){
                        expRemark.append("，");
                        expRemark.append(rtnRemark);
                    }
                }
                expRemark.append("。");
                addOrderExpress(ExpressStatus.APPLY_RETURN, order, currUser, expRemark.toString());
                //更新运单
                Key<Order> r = orderService.save(order);
                if(r != null){
                    map.put("success", true);//成功
                    map.put("msg", "申请退货成功");//0
                    //刷新列表
                    map.put("orderPage", getPageData(currUser.getSite().getAreaCode(), status, pageIndex, arriveBetween));
                }else{
                    map.put("success", false);//失败
                    map.put("msg", "退货失败，请稍候再试");
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


    /**************************  转为其他快递   得到所有的快递公司*************** 开始 *****************************/
    @ResponseBody
    @RequestMapping(value = "/getExpressCompanys", method = RequestMethod.GET)
    public List<ExpressCompany> getExpressCompanys(Model model) {
        List<ExpressCompany> expressCompanys = expressCompanyService.getExpressCompany();
        if (expressCompanys != null && expressCompanys.size() != 0) {
            return expressCompanys;
        } else {
            return null;
        }

    }

    /**
     * @param mailNum 运单号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toOtherExpressCompanys", method = RequestMethod.POST)
    public Order toOtherExpressCompanys(String mailNum, String companyId, String mailNumNew) {

        String companyName = null;
        String companyCode = null;

        if (StringUtils.isNoneBlank(companyId)) {
            ExpressCompany expressCompany = expressCompanyService.getExpressCompanyById(Integer.valueOf(companyId));
            if (null != expressCompany) {
                companyName = expressCompany.getCompanyname();
                companyCode = expressCompany.getCompanycode();
            }
        }
        if (StringUtils.isNotBlank(mailNum)) {

            Order order = orderService.findOneByMailNum("", mailNum);
            if (order != null) {
                List<OtherExpreeVO> otherExpressList = order.getOtherExprees();
                if (otherExpressList == null || otherExpressList.isEmpty()) {
                    otherExpressList = Lists.newArrayList();
                }

                OtherExpreeVO otherExpreeVO = new OtherExpreeVO();

                if (StringUtils.isNotBlank(mailNumNew)) {
                    otherExpreeVO.setMailNum(mailNumNew);
                }
                otherExpreeVO.setCompanyname(companyName);
                otherExpreeVO.setContext("订单超出站点配送 范围，已转发【" + companyName + "快递】，快递单号：" + mailNumNew + "");

                otherExpreeVO.setCompanycode(companyCode);
                otherExpreeVO.setDateUpd(new Date());

                otherExpressList.add(otherExpreeVO);
                order.setOtherExprees(otherExpressList);
                order.setDateUpd(new Date());

                orderService.save(order);
                return order;
            }
        }
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/goTo100Subscribe", method = RequestMethod.POST)
    public ResultResposeDTO goTo100Subscribe(String salt, String resultv2, String companyId, String mailNum, String from, String to, String mailNumNew) {
        String epree100Resultv2 = "";
        String epree100_salt = "";
        String companyName = null;
        String companyCode = null;
        ResultResposeDTO resultResposeDTO = new ResultResposeDTO();
        if (StringUtils.isNoneBlank(resultv2)) {
            epree100Resultv2 = resultv2;
        }


        if (StringUtils.isNoneBlank(epree100_salt)) {
            epree100Resultv2 = salt;
        }
        if (StringUtils.isNoneBlank(companyId)) {
            ExpressCompany expressCompany = expressCompanyService.getExpressCompanyById(Integer.valueOf(companyId));
            if (null != expressCompany) {
                companyName = expressCompany.getCompanyname();
                companyCode = expressCompany.getCompanycode();
            }
        }

        Map<String, String> requestVO = new HashMap<>();
        Expree100BodyVO expree100BodyVO = new Expree100BodyVO();
        Expree100ParametersVO expree100ParametersVO = new Expree100ParametersVO();
        expree100ParametersVO.setCallbackurl(EPREE100_CALLBACK_URL);
        expree100ParametersVO.setSalt(epree100Resultv2);
        expree100ParametersVO.setResultv2(epree100Resultv2);
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
        JSONSerializer jsonSerializer = new JSONSerializer();

        requestVO.put("param", jsonSerializer.serialize(expree100BodyVO));
        requestVO.put("schema", "json/xml");


        RestRequestDTO restRequestDTO = null;
        String restRequestDTOStr = null;

        try {
            restRequestDTOStr = HttpClientUtil.sendHttpPost(EPREE100_URL, requestVO);
            if (StringUtils.isNoneBlank(restRequestDTOStr)) {
                restRequestDTO = JSON.parse(restRequestDTOStr, RestRequestDTO.class);
                if (null != restRequestDTO) {
                    LastResultVO lastResult = restRequestDTO.getLastResult();
                    if (null != lastResult) {
                        List<NewContextVO> data = lastResult.getData();
                        if (data != null && data.size() != 0) {

                            if (StringUtils.isNotBlank(mailNum)) {
                                Order order = orderService.findOneByMailNum("", mailNum);
                                if (order != null) {
                                    List<OtherExpreeVO> otherExpressList = order.getOtherExprees();
                                    if (otherExpressList == null || otherExpressList.isEmpty()) {
                                        otherExpressList = Lists.newArrayList();
                                    }

                                    String resultv21 = expree100ParametersVO.getResultv2();
                                    for (NewContextVO newContext : data) {
                                        if (StringUtils.isNoneBlank(resultv21) && StringUtils.isNotBlank(mailNumNew)) {
                                            OtherExpreeVO otherExpreeVO = new OtherExpreeVO();
                                            otherExpreeVO.setContext(newContext.getContext());
                                            otherExpreeVO.setMailNum(mailNumNew);
                                            otherExpreeVO.setCompanyname(companyName);
                                            otherExpreeVO.setAreaCode(newContext.getAreaCode());
                                            otherExpreeVO.setAreaName(newContext.getAreaName());
                                            otherExpreeVO.setCompanycode(companyCode);
                                            otherExpreeVO.setDateUpd(Dates.parseFullDate(newContext.getFtime()));
                                            otherExpreeVO.setStatus(newContext.getStatus());
                                            otherExpressList.add(otherExpreeVO);
                                        } else {
                                            OtherExpreeVO otherExpreeVO = new OtherExpreeVO();
                                            otherExpreeVO.setContext(newContext.getContext());
                                            otherExpreeVO.setMailNum(mailNumNew);
                                            otherExpreeVO.setCompanyname(companyName);
                                            otherExpreeVO.setCompanycode(companyCode);
                                            otherExpreeVO.setDateUpd(Dates.parseFullDate(newContext.getFtime()));
                                            otherExpressList.add(otherExpreeVO);
                                        }
                                    }


                                    order.setOtherExprees(otherExpressList);

                                    String state = lastResult.getState();
                                    String status = restRequestDTO.getStatus();
                                    String message = restRequestDTO.getMessage();

                                    if (StringUtils.isNotBlank(status) && state != null) {
                                        if ("shutdown".equals(status) && ("3".equals(state) || "4".equals(state))) {
                                            order.setOrderStatus(OrderStatus.SIGNED);
                                        }
                                    }
                                    if (StringUtils.isNotBlank(message) && StringUtils.isNotBlank(status)) {
                                        if (("3天查询无记录".equals(message) && "abort".equals(status))
                                                || ("60天无变化时".equals(message) && "abort".equals(status))) {
                                            order.setOrderStatus(OrderStatus.SIGNED);
                                        }

                                    }

                                    order.setDateUpd(new Date());

                                    orderService.save(order);


                                }


                            }
                        }
                    }

                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            resultResposeDTO.setResult(false);
            resultResposeDTO.setMessage("转运信息失败");

            return resultResposeDTO;
        }

        resultResposeDTO.setResult(true);
        resultResposeDTO.setMessage("转运信息添加成功");

        return resultResposeDTO;
    }


}
