package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.ExpressStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * 向外部系统推送物流信息
 */
@Entity("expressExchange")
public class ExpressExchange implements Serializable {

    @Id
    private ObjectId id;

    public String url;                                  //请求url
    public String operator;                             //操作人
    public Order order;                                 //订单
    public String typ;                                  //操作类型
    public String pushInfo;                             //推送内容，json字符串
    public String requestStr;                           //请求串
    public String responseStr;                          //返回结果串
    public String status;                               //操作状态
    public String memo;                                 //备注
    public String pushCount;                            //推送次数
    public String timeInterval;                         //间隔时间
    public String dateAdd;                              //添加时间
    public String dateUpd;                              //更新时间

}