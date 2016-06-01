package com.bbd.saas.controllers.subscribeback;

import com.alibaba.dubbo.common.json.JSON;


import com.alibaba.dubbo.common.json.ParseException;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.enums.OrderStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.vo.*;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by huozhijie on 2016/5/26.
 */

/* 快递100 接口回调*/
@Controller
@RequestMapping("/subscribe")
/*@SessionAttributes("subscribeback")*/
public class SubscribeBackController {

    @Autowired
    OrderService orderService;
    /**
     * @param param json格式的body，快递100 传来的的json 数据
     * @param sign  字符串，签名=MD5(param+salt)
     * @return ResultResposeDTO   封装响应数据的对象VO
     */
    @RequestMapping(value = "/subscribeback", method = RequestMethod.POST/*,headers = { "content-type=application/json;charset=UTF-8" }*/)
    @ResponseBody
    public ResultResposeDTO subscribeback(String param, @RequestParam(value = "sign", required = false) String sign) {

        RestRequestDTO restRequestDTO = null;
        ResultResposeDTO resultResposeDTO = new ResultResposeDTO();

        String state = null;
        String status = null;
        String message = null;
        try {
        if(StringUtils.isNoneBlank(param)){
               /* param = new String(param.getBytes("ISO-8859-1"), "UTF-8");*/
                restRequestDTO = JSON.parse(param, RestRequestDTO.class);
            if(null!=restRequestDTO) {
                LastResultVO lastResult = restRequestDTO.getLastResult();
                if (null != lastResult) {
                    List<HashMap<String,String>> data = lastResult.getData();
                    if (data != null && data.size() != 0) {

                           String mailNum=  lastResult.getNu();

                        if (StringUtils.isNotBlank(mailNum)) {

                            Order order = orderService.findOneByMailNum("", mailNum);
                            if (order != null) {
                                List<Express> expressList = order.getExpresses();
                                if (expressList == null || expressList.isEmpty()) {
                                    expressList = Lists.newArrayList();
                                }


                                for (HashMap<String,String> newContext : data) {
                                             Express   express=new Express();
                                                express.setRemark(newContext.get("context"));
                                                 express.setDateAdd(Dates.parseFullDate(newContext.get("time"))  );
                                                 express.setLat("38.042309700115");
                                                  express.setLon("114.54567066289");
                                      /*  OtherExpreeVO otherExpreeVO = new OtherExpreeVO();
                                        otherExpreeVO.setContext(newContext.get("context"));
                                        otherExpreeVO.setMailNum(mailNum);
                                        //otherExpreeVO.setCompanyname(companyName);
                                        otherExpreeVO.setAreaCode(newContext.get("areaCode"));
                                        otherExpreeVO.setAreaName(newContext.get("areaName"));
                                       // otherExpreeVO.setCompanycode(companyCode);
                                        otherExpreeVO.setDateUpd(Dates.parseFullDate(newContext.get("ftime")));
                                        otherExpreeVO.setStatus(newContext.get("status"));*/
                                    expressList.add(express);

                                }
                                order.setExpresses(expressList);

                                state = lastResult.getState();
                                 status = restRequestDTO.getStatus();
                                 message = restRequestDTO.getMessage();

                                if(StringUtils.isNotBlank(status)&& state!=null) {
                                    if ( "shutdown".equals(status)&& ("3".equals(state)||"4".equals(state))) {
                                        order.setOrderStatus(OrderStatus.SIGNED);
                                    }
                                }
                                if(StringUtils.isNotBlank(message)&&StringUtils.isNotBlank(status) ){
                                    if(("3天查询无记录".equals(message)&& "abort".equals(status))
                                            ||("60天无变化时".equals(message)&& "abort".equals(status))){
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
        }  } catch (Exception e) {
            e.printStackTrace();
        }



    /*Status：当快递单本身为已签收时，status=shutdown，即监控结束，表示此单的生命周期已结束；*/
        if (StringUtils.isNotBlank(status) && state != null) {

            if ("shutdown".equals(status) && "3".equals(state)) {
                resultResposeDTO.setMessage("推送成功");
                resultResposeDTO.setReturnCode(200);
                resultResposeDTO.setResult(true);

                return resultResposeDTO;
            }


        }


  /* 当message为“3天查询无记录”或“60天无变化时”，status= abort，即监控中止。*/
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
