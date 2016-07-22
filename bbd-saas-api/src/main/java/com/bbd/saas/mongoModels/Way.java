package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 站点表
 * Created by luobotao on 2016/2/23.
 */
@Entity("way")
@Indexes(
        @Index(value = "name", fields = @Field("name"))
)
/**
 * 线路
 * Created by luobotao on 2016/7/13.
 */
public class Way implements Serializable {
    private static final long serialVersionUID = 7742560057056014887L;
    @Id
    public ObjectId id;
    public String name;         //线路名称
    public String province;     //省
    public String city;         //市
    public String area;         //区
    public String uid;   //司机ID
    public List<SiteInfo> sites;         //站点的ID列表
    public Date dateAdd;      //录入时间
    public Date dateUpd;      //更新时间

    public static class SiteInfo implements Serializable {
        public String siteId;
        public String siteName;
        public String wayUserName;//接货人
        public String wayPhone;//接货人电话
        public String arriveTime;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<SiteInfo> getSites() {
        return sites;
    }

    public void setSites(List<SiteInfo> sites) {
        this.sites = sites;
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

