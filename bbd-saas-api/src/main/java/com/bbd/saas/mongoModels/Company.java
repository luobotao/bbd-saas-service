package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.SiteStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点公司表
 * Created by luobotao on 2016/4/10.
 */
@Entity("site")
@Indexes(
        @Index(value = "username", fields = @Field("username"))
)
public class Company implements Serializable {
    private static final long serialVersionUID = -1853993953815783534L;
    @Id
    private ObjectId id;
    private String name;         //公司名称
    private String flag;         //标识 0注册 1审核中 2审核通过 3驳回
    private Date dateAdd;      //充值时间
    private Date dateUpd;      //更新时间

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
