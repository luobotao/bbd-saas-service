package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.Srcs;
import com.bbd.saas.vo.Goods;
import com.bbd.saas.vo.Reciever;
import com.bbd.saas.vo.Sender;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TradePush implements Serializable {

    private Integer time;               //推送次数   默认0
    private Integer flag;               //是否已推送 0 否 1 是
    private Integer postmanId;          //揽件员Id
}
