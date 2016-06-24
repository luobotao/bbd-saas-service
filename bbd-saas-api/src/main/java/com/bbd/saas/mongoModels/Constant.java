package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 常量表
 * @description: Created by liyanlei on 2016/6/23 16:35.
 */
@Entity("constant")
@Indexes(
        @Index(value = "name", fields = @Field("name"))
)
public class Constant  implements Serializable {
    private static final long serialVersionUID = 2802320222840001203L;
    @Id
    private ObjectId id;
    private String name; //名称
    private String value;  //值
    private String module; //模块
    private String type; //类型
    private Date dateAdd; //新增时间
    private Date dateUpd; //修改时间

    public Constant(){

    }
    public Constant(String name, String value, String module, String type, Date dateAdd, Date dateUpd) {
        this.name = name;
        this.value = value;
        this.module = module;
        this.type = type;
        this.dateAdd = dateAdd;
        this.dateUpd = dateUpd;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
