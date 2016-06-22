package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * User
 *
 * @date 16-2-3
 */
@Entity("orderNum")
public class OrderNum implements Serializable {

    @Id
    public ObjectId id;

    //物流单号累计
    public String num;
    //运单号累计
    public String trackNum;
    //支付号累计
    public String tradeNum;
}