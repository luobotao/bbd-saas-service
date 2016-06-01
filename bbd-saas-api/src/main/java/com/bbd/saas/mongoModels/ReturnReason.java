package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * Order
 * 订单表
 * Created by luobotao on 2016/4/10.
 */
@Entity("returnReason")
@Indexes(
        @Index(value = "status", fields = @Field("status"))
)
public class ReturnReason implements Serializable {

    @Id
    private ObjectId id;
    private int status;
    private String message;

}
