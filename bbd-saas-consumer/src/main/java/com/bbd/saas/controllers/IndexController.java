package com.bbd.saas.controllers;

import com.bbd.poi.api.SiteKeywordApi;
import com.bbd.poi.api.SitePoiApi;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.poi.api.vo.PageList;
import com.bbd.poi.api.vo.SiteKeyword;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.Services.RedisService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.SmsInfoService;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.common.ConcurrentReaderHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Security;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@SessionAttributes("index")
public class IndexController {
    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    AdminService adminService;
    @Autowired
    SiteService siteService;
    @Autowired
    SiteKeywordApi siteKeywordApi;
    @Autowired
    SitePoiApi sitePoiApi;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisService redisService;
    @Autowired
    SmsInfoService smsInfoService;
    /**
     * description: 进入首页
     * 2016年4月1日下午6:22:38
     *
     * @param model
     * @return
     * @author: liyanlei
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request, String typ) {
        User user = adminService.get(UserSession.get(request));
        model.addAttribute("user", user);
        model.addAttribute("typ", typ);
        return "index";
    }

    /**
     * 退出登录
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = adminService.get(UserSession.get(request));
        UserSession.remove(response);//remove from cookies
        adminService.delete(user);//remove adminUser from redis
        return "redirect:/login";
    }

    /**
     * 跳至首页
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request) {
        User user = adminService.get(UserSession.get(request));
        model.addAttribute("user", user);
        try{
            if (user.getRole()== UserRole.SITEMASTER && user.getLoginCount() == 1) {
                //--------panel 1-----------------------
                Site site = siteService.findSite(user.getSite().getId().toString());
                String between = request.getParameter("between");
                String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
                int page = Numbers.parseInt(request.getParameter("page"), 0);
                //导入地址关键词
                //--------panel 3-----------------------
                PageList<SiteKeyword> siteKeywordPage = new PageList<SiteKeyword>();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(between)) {//预计到站时间
                    DateBetween dateBetween = new DateBetween(between);
                    logger.info(dateBetween.getStart() + ":" + dateBetween.getEnd());
                    //导入地址关键词
                    siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", dateBetween.getStart(), dateBetween.getEnd(), page, 10, keyword);
                } else {
                    siteKeywordPage = siteKeywordApi.findSiteKeyword(site.getId() + "", null, null, page, 10, keyword);
                }
                model.addAttribute("site", site);
                model.addAttribute("between", between);
                model.addAttribute("keyword", keyword);
                model.addAttribute("siteKeywordPageList", siteKeywordPage.list);
                model.addAttribute("page", siteKeywordPage.getPage());
                model.addAttribute("pageNum", siteKeywordPage.getPageNum());
                model.addAttribute("pageCount", siteKeywordPage.getCount());

                List<List<MapPoint>> sitePoints = sitePoiApi.getSiteEfence(user.getSite().getId().toString());
                String siteStr = dealSitePoints(sitePoints);
                model.addAttribute("sitePoints", siteStr);
            }else {
                model.addAttribute("site", user.getSite());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }

    @ResponseBody
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.GET)
    public Object sendVerifyCode(@RequestParam(value = "phone", required = true) String phone) {
        logger.info(phone+"====传入的手机号=====");
        phone = SignUtil.Decrypt(phone,"0807060504030201");
        logger.info(phone+"=====解密后的手机号====");
        Map<String, Object> result = new ConcurrentReaderHashMap();
        if (StringUtils.isBlank(phone) || !StringUtil.checkPhone(phone)) {//手机号码不正确
            result.put("status", ErrorCode.getErrorCode("global.phoneError"));
            result.put("msg", ErrorCode.getErrorMsg("global.phoneError"));
            return result;
        }

        String codeTemp = redisService.get(Constants.BBD_SAAS_VERIFY_CODE_TIME + phone);
        if(StringUtils.isNotBlank(codeTemp)){//60秒内刚发过
            result.put("status", ErrorCode.getErrorCode("global.verifyTimeError"));
            result.put("msg", ErrorCode.getErrorMsg("global.verifyTimeError"));
            return result;
        }
        String ip=StringUtil.getIpAddr(request);
        String checkResult = smsInfoService.checkToSendsms(phone,ip);
        logger.info(ip+"========"+checkResult);
        if("1".equals(checkResult)){
            String code = redisService.get(Constants.BBD_SAAS_VERIFY_CODE + phone);
            if (StringUtils.isBlank(code)) {
                code = StringUtil.genRandomCode(4);//生成四位随机数
            }
            redisService.set(Constants.BBD_SAAS_VERIFY_CODE + phone, code, 60 * 30);//写入redis 半小时内有效
            redisService.set(Constants.BBD_SAAS_VERIFY_CODE_TIME + phone, code, 60 );//写入redis 一分钟有效
            smsInfoService.saveVerify(phone, code, "1");//写入短信表 1注册
            result.put("status", "1");
            result.put("msg", "发送成功");
            logger.info("手机号："+ phone + ",验证码 ：" + code);
            return result;
        }else {
            result.put("status", ErrorCode.getErrorCode("global.requestError"));
            result.put("msg", ErrorCode.getErrorMsg("global.requestError"));
            return result;
        }

    }

    private String dealSitePoints(List<List<MapPoint>> sitePoints) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < sitePoints.size(); j++) {
            for (int i = 0; i < sitePoints.get(j).size(); i++) {
                String str = sitePoints.get(j).get(i).getLng() + "_" + sitePoints.get(j).get(i).getLat();
                sb.append(str);
                if (i < sitePoints.get(j).size() - 1) {
                    sb.append(",");
                }
            }
            if (j < sitePoints.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
