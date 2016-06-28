package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.OrderHoldToStoreNumVO;
import com.bbd.saas.vo.OrderHoldToStoreVo;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 揽件入库
 * Created by huozhijie on 2016/6/25.
 */

/**
 * 跳转到揽件入库页面
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


    public static final Logger logger = LoggerFactory.getLogger(HoldToStoreController.class);

    @RequestMapping(value="", method= RequestMethod.GET)
    public String index(Integer pageIndex, Integer status , String embraceId, final HttpServletRequest request, Model model) {
        //当前登录的用户信息
        User user = adminService.get(UserSession.get(request));
        if(user!=null){
            //获取当前登录人的站点编码
            String areaCode = user.getSite().getAreaCode();

            OrderHoldToStoreNumVO orderHoldToStoreNum = orderService.getOrderHoldToStoreNum(areaCode);
            long historyToStoreNum = orderHoldToStoreNum.getHistoryToStoreNum();
            long todayNoToStoreNum = orderHoldToStoreNum.getTodayNoToStoreNum();
            long successOrderNum = orderHoldToStoreNum.getSuccessOrderNum();
            long  todayToStoreNum=orderHoldToStoreNum.getTodayToStoreNum();

              model.addAttribute("historyToStoreNum",historyToStoreNum);
              model.addAttribute("todayNoToStoreNum",todayNoToStoreNum);
              model.addAttribute("successOrderNum",successOrderNum);
               model.addAttribute("todayToStoreNum",todayToStoreNum);


            //获取站长下的所有揽件员
            List<User> userList = userService.findUsersBySite(user.getSite(), UserRole.SENDMEM, UserStatus.VALID);//所有小件员
        try {
            //设置默认查询条件
            status = Numbers.defaultIfNull(status, -1);//全部

            //查询数据
            PageModel<OrderHoldToStoreVo> orderHoldPageModel= getList(pageIndex, status,  embraceId, request,model);
            logger.info("=====揽件入库页面====" + orderHoldPageModel);
            model.addAttribute("orderHoldPageModel", orderHoldPageModel);
            model.addAttribute("userList",userList);
        } catch (Exception e) {
            logger.error("===揽件入库页面===出错:" + e.getMessage());
        }
        }
        return "page/holdToStore";
    }

    //分页Ajax更新
    @ResponseBody
    @RequestMapping(value="/getList", method=RequestMethod.GET)
    public   PageModel<OrderHoldToStoreVo>  getList(Integer pageIndex , Integer orderSetStatus , String embraceId, final HttpServletRequest request, Model model) {
        //查询数据
        PageModel<OrderHoldToStoreVo> orderHoldPageModel=null;
            //今天到站时间
         /* String todayDate =   Dates.formatSimpleDate(new Date()) ;*/
            //参数为空时，默认值设置
            pageIndex = Numbers.defaultIfNull(pageIndex, 0);
            orderSetStatus = Numbers.defaultIfNull(orderSetStatus, -1);
            //当前登录的用户信息
            User user = adminService.get(UserSession.get(request));
        String areaCode= user.getSite().getAreaCode();
            //设置查询条件
            OrderQueryVO orderQueryVO = new OrderQueryVO();
              /*  orderQueryVO.dateArrived = todayDate;*/
                orderQueryVO.orderSetStatus=orderSetStatus;
        List<String> tradeNoList=null;
            if(StringUtils.isNotBlank(embraceId)&& !("0".equals(embraceId))){
                List<Trade> tradeList = tradeService.findTradesByEmbraceId(embraceId);
                  tradeNoList=new ArrayList<>();
                for(Trade trade:tradeList){
                    tradeNoList.add(trade.getTradeNo());
                }
            }

            orderQueryVO.areaCode=areaCode;



            orderHoldPageModel = orderService.findPageOrdersForHoldToStore(pageIndex,tradeNoList,orderQueryVO);

        return orderHoldPageModel;
    }


}
