package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商户揽件员关联表
 * Created by liyanlei on 2016/10/14.
 */
@Entity("relation")
@Indexes(
        @Index(value = "mechId", fields = @Field("mechId"))
)
public class Relation implements Serializable {
    private static final long serialVersionUID = 7742560057056014887L;
    @Id
    private ObjectId id;
    private String mechId;         //商户Id
    private List<Integer> embraceIdList;     //揽件员Id
    private Date dateAdd;      //录入时间
    private Date dateUpd;      //更新时间

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMechId() {
        return mechId;
    }

    public void setMechId(String mechId) {
        this.mechId = mechId;
    }

    public List<Integer> getEmbraceIdList() {
        return embraceIdList;
    }

    public void setEmbraceIdList(List<Integer> embraceIdList) {
        this.embraceIdList = embraceIdList;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }
}

