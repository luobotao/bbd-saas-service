package com.bbd.saas.controllers;

import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.OrderQueryVO;
import com.bbd.saas.vo.UserVO;
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

    public static final Logger logger = LoggerFactory.getLogger(HoldToStoreController.class);

    @RequestMapping(value="", method= RequestMethod.GET)
    public String index(Integer pageIndex, Integer status, String todayDate, String embraceId, final HttpServletRequest request, Model model) {
        //当前登录的用户信息
        User user = adminService.get(UserSession.get(request));

        if(user!=null){
            //获取当前登录人的站点编码
            String areaCode = user.getSite().getAreaCode();
            //获取站长下的所有揽件员
            List<User> userList = userService.findUsersBySite(user.getSite(), UserRole.SENDMEM, UserStatus.VALID);//所有小件员
        try {
            //设置默认查询条件
            status = Numbers.defaultIfNull(status, -1);//全部
            //到站时间前天、昨天和今天
            todayDate =   Dates.formatDate2(new Date()) ;
            //查询数据
            PageModel<Order> orderPage = getList(pageIndex,areaCode, status, todayDate, embraceId, request,model);
            logger.info("=====揽件入库页面====" + orderPage);
            model.addAttribute("orderPage", orderPage);
            model.addAttribute("todayDate", todayDate);
        } catch (Exception e) {
            logger.error("===揽件入库页面===出错:" + e.getMessage());
        }
        }
        return "page/packageDispatch";
    }

    //分页Ajax更新
    @ResponseBody
    @RequestMapping(value="/getList", method=RequestMethod.GET)
    public PageModel<Order> getList(Integer pageIndex, String areaCode, Integer status, String todayDate, String embraceId, final HttpServletRequest request, Model model) {
        //查询数据
        PageModel<Order> orderPage = null;
        try {
            //参数为空时，默认值设置
            pageIndex = Numbers.defaultIfNull(pageIndex, 0);
            status = Numbers.defaultIfNull(status, 0);
            //当前登录的用户信息
            User user = adminService.get(UserSession.get(request));
            //设置查询条件
            OrderQueryVO orderQueryVO = new OrderQueryVO();
            orderQueryVO.orderSetStatus=status;
            orderQueryVO.arriveBetween = todayDate;
            orderQueryVO.areaCode=areaCode;
            orderPage = orderService.findPageOrdersForHoldToStore(pageIndex, orderQueryVO);
        } catch (Exception e) {
            logger.error("===分页Ajax揽件入库页面===出错:" + e.getMessage());
        }
        return orderPage;
    }

    /**
     * 格式化orderList给item加入用户信息
     * @param orderList
     * @return
     */
    public List<Order> formatOrder(List<Order> orderList){
        User embrace = null;
        UserVO userVO = null;
        for(Order order1 : orderList){
            embrace = userService.findOne(order1.getUserId());
            if(embrace!=null){
                userVO = new UserVO();
                userVO.setLoginName(embrace.getLoginName());
                userVO.setRealName(embrace.getRealName());
                order1.setUserVO(userVO);
            }
        }
        return orderList;
    }
}
