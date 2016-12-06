package com.bbd.saas.controllers.service;

import com.bbd.saas.Services.RedisService;
import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.api.mongo.OrderParcelService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.ExpressCompanyService;
import com.bbd.saas.api.mysql.PostDeliverySmsLogService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.api.mysql.SmsInfoService;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.enums.ExpressExchangeStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.models.PostDeliverySmsLog;
import com.bbd.saas.mongoModels.ExpressExchange;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.Reciever;
import com.bbd.saas.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  with redis
 * Created by liyanlei on 2016/8/18.
 */
@Service("commonService")
public class CommonService {
    public static final Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    RedisService redisService;
    @Autowired
    PostmanUserService postmanUserService;
    @Autowired
    OrderParcelService orderParcelService;
    @Autowired
    UserService userService;
    @Autowired
    ExpressExchangeService expressExchangeService;
    @Autowired
    ExpressCompanyService expressCompanyService;
    @Autowired
    SmsInfoService smsInfoService;
    @Autowired
    PostDeliverySmsLogService postDeliverySmsLogService;

    @Value("${EPREE100_KEY}")
    private String EPREE100_KEY ;
    @Value("${EPREE100_CALLBACK_URL}")
    private String EPREE100_CALLBACK_URL ;
    @Value("${EPREE100_URL}")
    private String EPREE100_URL;

    /**
     * 物流同步
     * @param order 订单
     * @param realName 派件员姓名
     * @param phone 派件员电话
     */
    public void saveExpressExChange(Order order, String realName, String phone){
        if (order.getSrc() == Srcs.PINHAOHUO || order.getSrc() == Srcs.DANGDANG || order.getSrc() == Srcs.DDKY) {
            this.doSaveExpressExChange(order, realName, phone);
        }
    }

    /**
     * 物流同步
     * @param order 订单
     * @param realName 派件员姓名
     * @param phone 派件员电话
     */
    public void doSaveExpressExChange(Order order, String realName, String phone){
        ExpressExchange expressExchange = new ExpressExchange();
        expressExchange.setOperator(realName);
        expressExchange.setPhone(phone);
        expressExchange.setOrder(order.coverOrderVo());
        expressExchange.setStatus(ExpressExchangeStatus.waiting);
        expressExchange.setDateAdd(new Date());
        expressExchangeService.save(expressExchange);
    }

    /**
     * 发送短信
     *
     */
    public void sendSmsInfo(Order order, String longUrl_dispatch, String courierPhone){
        if(Srcs.PINHAOHUO != order.getSrc()){
            String mailNumBase64 = Base64.getBase64(order.getMailNum());
            String oldUrl=longUrl_dispatch+ mailNumBase64;
            String shortUrl = ShortUrl.generateShortUrl(oldUrl);
            logger.info("生成的短链："+shortUrl);
            redisService.set(Constants.ORDER_INFO_TIME_LONG + mailNumBase64, mailNumBase64, 60*60*12 );//写入redis 12小时有效 60*60*12

            //写短信发送日志
            PostDeliverySmsLog postDeliverySmsLog = new PostDeliverySmsLog();
            postDeliverySmsLog.setDate_new(new Date());
            postDeliverySmsLog.setPhone(order.getReciever().getPhone());
            postDeliverySmsLog.setMailnum(order.getMailNum());
            postDeliverySmsLog.setBase64num(Base64.getBase64(order.getMailNum()));
            postDeliverySmsLog.setUrl(oldUrl);
            postDeliverySmsLog.setShorturl(shortUrl);
            this.postDeliverySmsLogService.add(postDeliverySmsLog);
            //旧短信内容
            //smsInfoService.sendToSending(order.getSrcMessage(),order.getMailNum(),user.getRealName(),user.getLoginName(),contact,order.getReciever().getPhone());
            //新短信内容
            this.smsInfoService.sendToSendingNew2(order.getSrcMessage(),order.getMailNum(),courierPhone,shortUrl,order.getReciever().getPhone());
        }
    }

    /**
     * 设置派件员快递和电话
     */
    public PageModel<Order> setCourierNameAndPhone(PageModel<Order> orderPage){
        //设置派件员快递和电话
        if(orderPage != null && orderPage.getDatas() != null){
            List<Order> dataList = orderPage.getDatas();
            User courier = null;
            UserVO userVO = null;
            for(Order order : dataList){
                if(StringUtil.isEmpty(order.getPostmanUser())){
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
        }
        return orderPage;
    }

    public  List<String> getRowByOrder(Order order, String siteName){
        List<String> row = new ArrayList<String>();
        row.add(siteName);
        row.add(order.getAreaCode());
        row.add(order.getMailNum());
        if(order.getReciever() == null){
            order.setReciever(new Reciever());
        }
        row.add(StringUtil.initStr(order.getReciever().getName(), ""));
        row.add(StringUtil.initStr(order.getReciever().getPhone(), ""));
        row.add(OrderCommon.getRCVAddress(order.getReciever()));
        row.add(Dates.formatDateTime_New(order.getDateDriverGeted()));
        row.add(Dates.formatDate2(order.getDateMayArrive()));
        row.add(Dates.formatDateTime_New(order.getDateArrived()));
        //签收时间 start
        if(order.getOrderStatus() != null && order.getOrderStatus() == OrderStatus.SIGNED){
            row.add(Dates.formatDateTime_New(order.getDateUpd()));
        }else{
            row.add("");
        }
        //派件员
        if(StringUtil.isNotEmpty(order.getUserId()) && StringUtil.isNotEmpty(order.getPostmanUser())){
            row.add(order.getPostmanUser());
            row.add(order.getPostmanPhone());
        }else if(StringUtil.isNotEmpty(order.getUserId())){
            User courier = userService.findOne(order.getUserId());
            if(courier != null){
                row.add(courier.getRealName());
                row.add(courier.getLoginName());
            }else{
                row.add("");
                row.add("");
            }
        }else{
            row.add("");
            row.add("");
        }
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
        return row;
    }
    public  void exportOneSiteToExcel(List<Order> orderList, String siteName, final HttpServletResponse response){
        List<List<String>> dataList = new ArrayList<List<String>>();
        siteName = StringUtil.initStr(siteName, "");
        if(orderList != null){
            for(Order order : orderList){
                dataList.add(getRowByOrder(order, siteName));
            }
        }
        //表头
        String[] titles = {  "站点名称", "站点编码","运单号", "收货人", "收货人手机" , "收货人地址" , "司机取货时间" , "预计站点入库时间", "站点入库时间", "签收时间", "派送员", "派送员手机", "状态","异常原因" };
        int[] colWidths = {   6000, 3500,5000, 3000, 3500, 12000, 5500, 3500, 5500, 5500, 3000, 3500, 3000,4000};
        ExportUtil.exportExcel(siteName + "_数据导出", dataList, titles, colWidths, response);
    }
    public  void exportSitesToExcel(List<Order> orderList, Map<String, String> siteMap, final HttpServletResponse response){
        List<List<String>> dataList = new ArrayList<List<String>>();
        if(orderList != null){
            for(Order order : orderList){
                dataList.add(getRowByOrder(order, StringUtil.initStr(siteMap.get(order.getAreaCode()), "")));
            }
        }
        //表头
        String[] titles = { "站点名称","站点编码",  "运单号", "收货人", "收货人手机" , "收货人地址" , "司机取货时间" , "预计站点入库时间", "站点入库时间", "签收时间", "派送员", "派送员手机", "状态" ,"异常原因" };
        int[] colWidths = {  6000, 3500, 5000, 3000, 3500, 12000, 5500, 3500, 5500, 5500,  3000,  3500, 3000,4000};
        ExportUtil exportUtil = new ExportUtil();
        exportUtil.exportExcel("运单查询导出", dataList, titles, colWidths, response);
    }
}
