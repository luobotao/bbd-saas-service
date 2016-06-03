package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * returnReason
 * 退货原因（类型）表
 * Created by liyanlei on 2016/6/1.
 */
@Entity("returnReason")
@Indexes(
        @Index(value = "status", fields = @Field("status"))
)
public class ReturnReason implements Serializable {
    private static final long serialVersionUID = -314284055004023746L;
    @Id
    private ObjectId id;
    private Integer status;
    private String message;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
