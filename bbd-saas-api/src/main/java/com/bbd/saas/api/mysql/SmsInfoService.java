package com.bbd.saas.api.mysql;


/**
 * 短信接口
 * Created by luobotao on 2016/4/11.
 */
public interface SmsInfoService {
    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @param type  // 1普通短信 2营销短信 3语音短信
     * @return 短信
     */
    void saveVerify(String phone, String code, String type);

    /**
     * 已分派揽件员 正在派送短信
     * 【棒棒糖】您的#src#快件#mailnum#正在由快递员#username#派送，请保持电话通畅，如需帮助请致电快递员#phone#或客服#contact#
     *
     * @param src
     * @param mailnum
     * @param username
     * @param phone
     * @param contact
     * @param receiverPhone
     */
    void sendToSending(String src, String mailnum, String username, String phone, String contact, String receiverPhone);
    /**
     * 已分派揽件员 正在派送短信
     * 模板id：1613212
     * 内容：【棒棒达】您的#scr#订单#mailnum#正在由棒棒达快递派送，快递员电话#phone#。查看物流详情：#url#
     * @param src
     * @param mailnum
     * @param phone
     * @param url
     * @param receiverPhone
     */
    void sendToSendingNew(String src, String mailnum, String phone,  String url, String receiverPhone);

    /**
     * 根所手机号与ip检查是否可以发送短信
     *
     * @param phone
     * @param ip
     * @return
     */
    String checkToSendsms(String phone, String ip);

    void saveSmsInfo(String args, String phone, String tpl_id, String type);
}
