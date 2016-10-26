package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.SmsInfoService;
import com.bbd.saas.dao.mysql.SmsInfoDao;
import com.bbd.saas.models.SmsInfo;
import com.bbd.saas.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信Service
 */
public class SmsInfoServiceImpl implements SmsInfoService {
    public final Logger logger = LoggerFactory.getLogger(SmsInfoServiceImpl.class);
    @Resource
    SmsInfoDao smsInfoDao;

    @Override
    public String checkToSendsms(String phone, String ip) {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("phone", phone);
        parms.put("ip", ip);
        return smsInfoDao.checkToSendsms(parms);
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @param code 验证码
     * @param type // 1普通短信 2营销短信 3语音短信
     * @return 短信
     */
    @Override
    public void saveVerify(String phone, String code, String type) {
        String args = code;
        String tpl_id = "";
        if (StringUtils.isBlank(type) || "1".equals(type)) {
            args = "#code#=" + code;
            tpl_id = "1510158";
            type = "1";
        } else {
            type = "3";
        }
        saveSmsInfo(args, phone, tpl_id, type);
    }

    public void saveSFBack(String phone, String type) {
        String args = "";
        String tpl_id = "";
        if (StringUtils.isBlank(type) || "1".equals(type)) {
            args = "#name#=棒棒糖";
            tpl_id = "1125699";
            type = "1";
        } else {
            type = "3";
        }
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 已接单短信
     * 您的同城订单#ordernum#快递员已接单，姓名：#name# 电话：#phone#，详情请到微信-我的订单查看
     *
     * @param phone
     * @param ordernum
     * @param postName
     * @param postPhone
     */
    public void sendToCatched(String phone, String ordernum, String postName, String postPhone) {
        String args = "#ordernum#=" + ordernum + "&#name#=" + postName + "&#phone#=" + postPhone;
        String tpl_id = "1208291";
        String type = "1";
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 已揽收短信
     * 您的同城订单#ordernum#快递员已揽收，快递员正在配送，详情请到微信-我的订单查看
     *
     * @param phone
     * @param ordernum
     */
    public void sendToGeted(String phone, String ordernum) {
        String args = "#ordernum#=" + ordernum;
        String tpl_id = "1208293";
        String type = "1";
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 已取消短信
     * 您的同城订单#ordernum#由于长时间无人接单已被取消，详情请到微信-我的订单查看，重新发布订单也许会有人抢哦
     *
     * @param phone
     * @param ordernum
     */
    public void sendToCanceled(String phone, String ordernum) {
        String args = "#ordernum#=" + ordernum;
        String tpl_id = "1208301";
        String type = "1";
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 成功送达短信
     * 您的同城订单#ordernum#快递员已送达，详情请到微信-我的订单查看，如有问题请联系客服：客服电话4009616090
     *
     * @param phone
     * @param ordernum
     */
    public void sendToSuccessed(String phone, String ordernum) {
        String args = "#ordernum#=" + ordernum;
        String tpl_id = "1208329";
        String type = "1";
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 快递员接单后被取消；
     * 您好，同城配送订单：#ordernum#刚刚已被用户取消，该订单已经失效
     *
     * @param phone
     * @param ordernum
     */
    public void sendToPostmanUserCanceled(String phone, String ordernum) {
        String args = "#ordernum#=" + ordernum;
        String tpl_id = "1210165";
        String type = "1";
        saveSmsInfo(args, phone, tpl_id, type);
    }

    /**
     * 已分派揽件员 正在派送短信
     * 【棒棒糖】您的#src#快件#mailnum#正在由快递员#username#派送，请保持电话通畅，如需帮助请致电快递员#phone#或客服#contact#
     * @param src
     * @param mailnum
     * @param username
     * @param phone
     * @param contact
     * @param receiverPhone
     */
    @Override
    public void sendToSending(String src, String mailnum, String username, String phone, String contact,String receiverPhone) {
        String args = "#src#=" + src + "&#mailnum#=" + mailnum + "&#username#=" + username+ "&#phone#=" + phone+ "&#contact#=" + contact;
        String tpl_id = "1462919";
        String type = "1";
        saveSmsInfo(args, receiverPhone, tpl_id, type);
    }




    public void saveSmsInfo(String args, String phone, String tpl_id, String type) {
        logger.info("args is " + args + "; Phone is " + phone + ";tpl_id is " + tpl_id);
        if(StringUtil.checkPhone(phone)){
            SmsInfo smsInfo = new SmsInfo();
            smsInfo.setFlg("0");// 未发送
            smsInfo.setTplId(tpl_id);
            smsInfo.setPhone(phone);
            smsInfo.setArgs(args);
            smsInfo.setTyp(type);// 1普通短信 2营销短信 3语音短信
            smsInfo.setDateNew(new Date());
            smsInfo.setDateUpd(new Date());
            smsInfoDao.insertSmsinfo(smsInfo);
        }
    }

    public SmsInfoDao getSmsInfoDao() {
        return smsInfoDao;
    }

    public void setSmsInfoDao(SmsInfoDao smsInfoDao) {
        this.smsInfoDao = smsInfoDao;
    }
}