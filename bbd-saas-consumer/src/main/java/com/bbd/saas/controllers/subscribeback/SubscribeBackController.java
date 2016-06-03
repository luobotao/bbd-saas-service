package com.bbd.saas.controllers.subscribeback;

import com.alibaba.dubbo.common.json.JSON;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mysql.BalanceService;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.models.Balance;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.vo.Express;
import com.bbd.saas.vo.LastResultVO;
import com.bbd.saas.vo.RestRequestDTO;
import com.bbd.saas.vo.ResultResposeDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * 快递100 接口回调
 * Created by huozhijie on 2016/5/26.
 */

@Controller
@RequestMapping("/subscribe")
public class SubscribeBackController {
    public static final Logger logger = LoggerFactory.getLogger(SubscribeBackController.class);
    @Autowired
    OrderService orderService;
    @Autowired
    BalanceService balanceService;

    /**
     * @param param json格式的body，快递100 传来的的json 数据
     * @param sign  字符串，签名=MD5(param+salt)
     * @return ResultResposeDTO   封装响应数据的对象VO
     */
    @RequestMapping(value = "/subscribeback", method = RequestMethod.POST)
    @ResponseBody
    public ResultResposeDTO subscribeback(String param, @RequestParam(value = "sign", required = false) String sign) {

        logger.info("get form kuaidi 100 is: "+param);
        RestRequestDTO restRequestDTO =null;//封装 param  请求参数对象
        ResultResposeDTO resultResposeDTO = new ResultResposeDTO();//封装返回快递100 信息的对象

        String state = null;//订单状态
        String status = null;//监控状态
        String message = null;//监控状态相关信息
        try {

            if (StringUtils.isNotBlank(param)) {
               /* param = new String(param.getBytes("ISO-8859-1"), "UTF-8");*/
               // 把请求数据转化成restRequestDTO对象
                restRequestDTO = JSON.parse(param, RestRequestDTO.class);

               //从请求参数restRequestDTO 中获取数据
                if (null != restRequestDTO) {
                    LastResultVO lastResult = restRequestDTO.getLastResult();//获取最新内容对象
                    if (null != lastResult) {
                        List<HashMap<String, String>> data = lastResult.getData();//获取最新内容对象的list 对象
                        if (data != null && !data.isEmpty()) {
                            String mailNum = lastResult.getNu();//获取新运单号
                            if (StringUtils.isNotBlank(mailNum)) {
                                Order order = orderService.findOneByNewMailNum(mailNum);//根据其他快递的运单号查询订单
                                if (order != null) {
                                    //如果order 有expressList  ,从order中取出，没有new 一个新的
                                    List<Express> expressList = order.getExpresses();
                                    if (expressList == null || expressList.isEmpty()) {
                                        expressList = Lists.newArrayList();
                                    }
                                   Collections.reverse(data);

                                    List <Express> newExpressList =new ArrayList<Express>();
                                    Map<String,Express> expressMap = new LinkedHashMap<String,Express>();

                                    //从把从快递100返回的结果填充到expressList中
                                    for (HashMap<String, String> newContext : data) {
                                        Express express = new Express();
                                        express.setRemark(newContext.get("context"));
                                       /* express.setDateAdd(new Date());*/
                                        express.setDateAdd(Dates.parseFullDate(newContext.get("time")));
                                        expressList.add(express);
                                           
                                    }

                                    for (Express  express : expressList) {
                                         expressMap.put(express.getRemark(),express);
                                    }

                                    Set<Map.Entry<String, Express>> entryExpressMap = expressMap.entrySet();

                                       for(Map.Entry<String, Express> entryExpress:entryExpressMap ){
                                           Express express = entryExpress.getValue();
                                           newExpressList.add(express);
                                       }

                                    //填充order
                                    order.setExpresses(newExpressList);

                                    //根据lastResult中的，监控状态，订单状态，监控状态信息进行判断
                                    state = lastResult.getState();//订单状态
                                    status = restRequestDTO.getStatus();//监控状态
                                    message = restRequestDTO.getMessage(); //监控状态信息

                                    //Status：当快递单本身为已签收时，status=shutdown，即监控结束，表示此单的生命周期已结束；
                                    if (StringUtils.isNotBlank(status) && state != null) {
                                        /*state 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态，*/
                                        if ("shutdown".equals(status) && ("3".equals(state) || "4".equals(state))) {
                                              //更改订单为签收状态
                                            order.setOrderStatus(OrderStatus.SIGNED);
                                            order.setExpressStatus(ExpressStatus.Success);
                                        }
                                    }
                                    // 当message为“3天查询无记录”或“60天无变化时”，status= abort，即监控中止。
                                    if (StringUtils.isNotBlank(message) && StringUtils.isNotBlank(status)) {
                                        if (("3天查询无记录".equals(message) && "abort".equals(status))
                                                || ("60天无变化时".equals(message) && "abort".equals(status))) {
                                            //更改订单为签收状态
                                            order.setOrderStatus(OrderStatus.SIGNED);
                                            order.setExpressStatus(ExpressStatus.Success);
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
            logger.info("[login error]"+e.getMessage());
        }
        //Status：当快递单本身为已签收时，status=shutdown，即监控结束，表示此单的生命周期已结束；
        if (StringUtils.isNotBlank(status) && state != null) {
            if ("shutdown".equals(status) && "3".equals(state)) {
                resultResposeDTO.setMessage("推送成功");
                resultResposeDTO.setReturnCode(200);
                resultResposeDTO.setResult(true);
                return resultResposeDTO;
            }
        }


        // 当message为“3天查询无记录”或“60天无变化时”，status= abort，即监控中止。
        if (StringUtils.isNotBlank(message) && StringUtils.isNotBlank(status)) {
            if (("3天查询无记录".equals(message) && "abort".equals(status))
                    || ("60天无变化时".equals(message) && "abort".equals(status))) {
                resultResposeDTO.setMessage("推送成功");
                resultResposeDTO.setReturnCode(200);
                resultResposeDTO.setResult(true);
                return resultResposeDTO;
            }

        }
        resultResposeDTO.setMessage("推送失败");
        resultResposeDTO.setReturnCode(500);
        resultResposeDTO.setResult(false);
        return resultResposeDTO;
    }

}
