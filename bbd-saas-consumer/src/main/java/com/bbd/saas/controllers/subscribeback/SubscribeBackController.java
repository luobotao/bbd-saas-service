package com.bbd.saas.controllers.subscribeback;

import com.alibaba.dubbo.common.json.JSON;


import com.bbd.saas.vo.LastResultVO;
import com.bbd.saas.vo.RestRequestDTO;
import com.bbd.saas.vo.ResultResposeDTO;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by huozhijie on 2016/5/26.
 */

/* 快递100 接口回调*/
@Controller
@RequestMapping("/subscribe")
/*@SessionAttributes("subscribeback")*/
public class SubscribeBackController {

    /**
     * @param param json格式的body，快递100 传来的的json 数据
     * @param sign  字符串，签名=MD5(param+salt)
     * @return ResultResposeDTO   封装响应数据的对象VO
     */
    @RequestMapping(value = "/subscribeback", method = RequestMethod.POST/*,headers = { "content-type=application/json;charset=UTF-8" }*/)
    @ResponseBody
    public ResultResposeDTO subscribeback(String param, @RequestParam(value = "sign", required = false) String sign) {

        RestRequestDTO requestDto = null;
        ResultResposeDTO resultResposeDTO = new ResultResposeDTO();

        try {
            if (StringUtils.isNoneBlank(param)) {
                param = new String(param.getBytes("ISO-8859-1"), "UTF-8");
                requestDto = JSON.parse(param, RestRequestDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultResposeDTO.setMessage("推送成功");
            resultResposeDTO.setReturnCode(200);
            resultResposeDTO.setResult(true);
            return resultResposeDTO;
        }

        String state = null;/*快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态*/
        String status = requestDto.getStatus();/*监控状态:polling:监控中，shutdown:结束，abort:中止，updateall： 重新推送。其中当快递单为已签收时status=shutdown，当message为“3天查询无记录”或“60天无变化时”status= abort*/

        LastResultVO lastResult = requestDto.getLastResult();

        if (lastResult != null) {
            state = lastResult.getState();
        }


        String message = requestDto.getMessage();

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
